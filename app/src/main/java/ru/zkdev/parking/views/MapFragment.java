package ru.zkdev.parking.views;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.BaseObservable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

import ru.zkdev.parking.R;
import ru.zkdev.parking.databinding.BottomDialogShowInfoBinding;
import ru.zkdev.parking.databinding.FragmentMapBinding;
import ru.zkdev.parking.models.Parking;
import ru.zkdev.parking.services.LocationService;
import ru.zkdev.parking.services.WaitingService;
import ru.zkdev.parking.utils.BaseActivity;
import ru.zkdev.parking.utils.BaseFragment;
import ru.zkdev.parking.viewModels.ParkingVM;
import ru.zkdev.parking.viewModels.PolygonVM;

import static androidx.core.content.PermissionChecker.checkSelfPermission;
import static ru.zkdev.parking.configs.Constants.LOCATION_UPDATE;
import static ru.zkdev.parking.configs.Constants.VERIFY_PERMISSIONS_REQUEST;
import static ru.zkdev.parking.configs.Permissions.PERMISSIONS;

public class MapFragment extends BaseFragment implements OnMapReadyCallback,
    GoogleMap.OnMapClickListener, GoogleMap.OnInfoWindowClickListener {
  private static final String TAG = "MapFragment";
  private final int LAYOUT = R.layout.fragment_map;
  private Context context;
  private FragmentMapBinding binding;
  private PolygonVM vm;
  private SupportMapFragment mapFragment;
  private GoogleMap mMap;
  private LocationService locationService;
  private BroadcastReceiver receiver;
  private LatLng selectedLatLng = null;
  private List<Polygon> polygonList = new ArrayList<>();
  private BottomSheetDialog dialog = null;
  private BottomDialogShowInfoBinding infoBinding;
  private int index = 0;
  private Marker marker;

  public MapFragment() {
  }

  /**
   * @param context
   * @param latLng
   * @return
   */
  public static MapFragment getInstance(Context context, LatLng latLng) {
    MapFragment fragment = new MapFragment();
    fragment.setContext(context);
    if (latLng != null) fragment.setSelectedLatLng(latLng);
    return fragment;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    vm = ViewModelProviders.of(getActivity()).get(PolygonVM.class);
    vm.init();
    locationService = new LocationService(getActivity());

  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    binding = DataBindingUtil.inflate(inflater, LAYOUT, container, false);
    mapFragment = (SupportMapFragment) getChildFragmentManager()
        .findFragmentById(R.id.map_view);
    binding.setHandler(new Handler());
    mapFragment.getMapAsync(this);
    initBottomMenu();

    final Intent intent = new Intent(this.getActivity(), LocationService.class);
    getActivity().startService(intent);
    return binding.getRoot();
  }

  @Override
  public void onResume() {
    super.onResume();
    Log.d(TAG, "onResume: ");
    getActivity().setTitle(getString(R.string.title_toolbar_map));
    receiveData();
  }

  private void receiveData() {
    if (receiver == null) {
      receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
          updateLocation();
        }
      };
    }
    getActivity().registerReceiver(receiver, new IntentFilter(LOCATION_UPDATE));
  }

  private void updateLocation() {
    Log.d(TAG, "updateLocation: ");
    LatLng latLng = new LatLng(locationService.getLocation().getLatitude(),
        locationService.getLocation().getLongitude());
    if (marker != null) marker.remove();
    marker = mMap.addMarker(new MarkerOptions().position(latLng));
    updateCamera(marker.getPosition());
  }

  @Override
  public void onStop() {
    locationService.stopUsingGPS();
    super.onStop();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (receiver != null)
      getActivity().unregisterReceiver(receiver);
  }

  @Nullable
  @Override
  public Context getContext() {
    return context;
  }

  private void setContext(Context context) {
    this.context = context;
  }

  public void setSelectedLatLng(LatLng selectedLatLng) {
    this.selectedLatLng = selectedLatLng;
  }

  private void initBottomMenu() {
    Log.d(TAG, "initBottomMenu: ");
    infoBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
        R.layout.bottom_dialog_show_info, null, false);
    dialog = new BottomSheetDialog(getActivity());
    dialog.setContentView(infoBinding.getRoot());
  }

  @Override
  public void onMapClick(LatLng latLng) {
    Log.d(TAG, "onMapClick: ");
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    Log.d(TAG, "onMapReady: ");
    if (mMap != null) mMap.clear();
    mMap = googleMap;
    mMap.setOnPolygonClickListener(this::setSelected);
    mMap.setOnInfoWindowClickListener(this);
    //region addPolygon onMap
    polygonList = new ArrayList<>();
    if (vm.getParkingPlaces().getValue() != null)
      for (Parking parking : vm.getParkingPlaces().getValue()) {

        PolygonOptions polygonOptions = new PolygonOptions();
        for (LatLng latLng : parking.getPolygon()) {
          polygonOptions.add(latLng);
        }
        LatLng markerCenter = calculateCenter(parking.getPolygon());
        Log.d(TAG, "onMapReady: " + markerCenter.longitude);
        Log.d(TAG, "onMapReady: " + markerCenter.latitude);


        mMap.addMarker(new MarkerOptions().position(markerCenter).title(parking.getName())
            .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_local_parking_white_24dp)));

        polygonOptions
            .strokeColor(R.color.line_not_place)
            .strokeWidth(10)
            .clickable(true)
            .fillColor(R.color.bg_default);

        polygonList.add(mMap.addPolygon(polygonOptions));
      }
    //endregion

    //region check PERMISSION
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
          checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        if (!checkPermissionsArray(PERMISSIONS)) {
          verifyPermissions(PERMISSIONS);
        }
        return;
      }
    } else {
      if (!checkPermissionsArray(PERMISSIONS)) {
        verifyPermissions(PERMISSIONS);
      }
    }
    //endregion
    getLocation();
  }

  public void showDirection(String[] directionList) {
    Log.d(TAG, "showDirection: ");
    if (directionList.length == 0) return;
    for (String item : directionList) {
      PolylineOptions options = new PolylineOptions()
          .color(R.color.line_green)
          .width(10)
          .addAll(PolyUtil.decode(item));
      mMap.addPolyline(options);
    }
  }

  private void setSelected(Polygon polygon) {
    Log.d(TAG, "setSelected: ");
    index = 0;
    for (Polygon poly : polygonList) {
      if (poly.getId().equals(polygon.getId())) {
        Parking parking = vm.getParkingPlaces().getValue().get(index);
        showPolygon(parking.getPolygon()[0]);
        infoBinding.setItem(new ParkingVM(parking));
        dialog.show();
      } else {
        polygon.setStrokeColor(R.color.bg_default);
      }
      index++;
    }
  }

  private void getLocation() {
    Log.d(TAG, "getLocation: ");
    if (locationService.canGetLocation()) {
      Log.d(TAG, "getLocation:selectedLatLng " + selectedLatLng);
      if (selectedLatLng != null) {
        showPolygon(selectedLatLng);
        return;
      }

      showLocation(locationService.getLocation());
      checkPolygon(locationService.getLocation());
    } else {
      locationService.showSettingsAlert();
    }
  }

  private void showPolygon(LatLng latLng) {
    Log.d(TAG, "showPolygon: ");
    checkPolygon(locationService.getLocation());
    animateCamera(latLng);
  }

  private void showLocation(Location location) {
    Log.d(TAG, "showLocarion: ");
    if (location == null && !BaseActivity.isNetworkAvaible(getActivity())) {
      locationService.showSettingsAlert();
      return;
    } else if (location == null) return;
    if (marker != null) marker.remove();
    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    marker = mMap.addMarker(new MarkerOptions().position(latLng));
    animateCamera(latLng);
  }

  private void animateCamera(LatLng latLng) {
    Log.d(TAG, "animateCamera: ");
    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
    mMap.animateCamera(CameraUpdateFactory.zoomIn());
    mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

    CameraPosition cameraPosition = new CameraPosition.Builder()
        .target(latLng)
        .zoom(16)
        // .bearing(90)
        //.tilt(30)
        .build();

    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
  }

  private void updateCamera(LatLng latLng) {
    Log.d(TAG, "animateCamera: ");

    CameraPosition cameraPosition = new CameraPosition.Builder()
        .target(latLng)
        .zoom(16)
        .build();

    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
  }

  private void checkPolygon(Location location) {
    Log.d(TAG, "checkPolygon: ");
    if (location == null) return;

    boolean contain = false;
    index = 0;
    for (Polygon poinPolygon : polygonList) {
      Parking parking = vm.getParkingPlaces().getValue().get(index);

      if (selectedLatLng != null) {
        LatLng latLng = calculateCenter(parking.getPolygon());

        if (selectedLatLng.latitude == latLng.latitude &&
            selectedLatLng.longitude == latLng.longitude) {
          infoBinding.setItem(new ParkingVM(parking));
          dialog.show();
          selectedLatLng = null;
          return;
        }
      }

      if (contain)
        continue;

      contain = PolyUtil.containsLocation(
          new LatLng(location.getLatitude(), location.getLongitude()), poinPolygon.getPoints(),
          true);

      if (contain && selectedLatLng == null) {
        getActivity().startService(new Intent(getActivity(), WaitingService.class).putExtra("id", parking.getId()));
        infoBinding.setItem(new ParkingVM(parking));
        dialog.show();
      }
      index++;
    }
  }

  //region PERMISSION
  public void verifyPermissions(String[] permissions) {
    Log.e(TAG, "verifyPermissions: ");
    MapFragment.this.requestPermissions(permissions, VERIFY_PERMISSIONS_REQUEST);
  }

  public boolean checkPermissionsArray(String[] permissions) {
    Log.e(TAG, "checkPermissionsArray: ");
    for (int i = 0; i < permissions.length; i++) {
      String check = permissions[i];
      if (!checkPermissions(check)) {
        return false;
      }
    }
    return true;
  }

  public boolean checkPermissions(String permission) {
    Log.e(TAG, "checkPermissions: ");
    int permissionRequest =
        ActivityCompat.checkSelfPermission(getActivity(), permission);

    if (permissionRequest != PackageManager.PERMISSION_GRANTED) {
      Log.e(TAG, "checkPermissions: \n Permission war not granted for  " + permission);
      return false;
    } else {
      Log.e(TAG, "checkPermissions: \n Permission war granted for  " + permission);
      return true;
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode,
                                         String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (ActivityCompat.checkSelfPermission(getActivity(),
        permissions[0]) == PackageManager.PERMISSION_GRANTED) {
      getLocation();
    }
  }
  //endregion

  private BitmapDescriptor bitmapDescriptorFromVector(Context context,
                                                      @DrawableRes int vectorDrawableResourceId) {
    Drawable background = ContextCompat.getDrawable(context, vectorDrawableResourceId);
    background.setBounds(0, 0, background.getIntrinsicWidth(),
        background.getIntrinsicHeight());
    Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
    vectorDrawable.setBounds(60, 40, vectorDrawable.getIntrinsicWidth() + 60,
        vectorDrawable.getIntrinsicHeight() + 40);
    Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(),
        background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    background.draw(canvas);
    vectorDrawable.draw(canvas);
    return BitmapDescriptorFactory.fromBitmap(bitmap);
  }
  @Override
  public void onInfoWindowClick(Marker marker) {
    Log.d(TAG, "onInfoWindowClick: ");
    Location location = new Location(locationService.getProvider());
    location.setLatitude(marker.getPosition().latitude);
    location.setLongitude(marker.getPosition().longitude);
    checkPolygon(location);
  }

  public class Handler extends BaseObservable {

    public void onMyLocationClick(@NonNull View view) {
      Log.d(TAG, "geMyLocationClick: ");
      getLocation();
    }

  }
}
