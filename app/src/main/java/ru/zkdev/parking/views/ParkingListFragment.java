package ru.zkdev.parking.views;


import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.zkdev.parking.R;
import ru.zkdev.parking.databinding.FragmentParkingListBinding;
import ru.zkdev.parking.models.Parking;
import ru.zkdev.parking.services.LocationService;
import ru.zkdev.parking.ui.PolygonListAdapter;
import ru.zkdev.parking.utils.BaseFragment;
import ru.zkdev.parking.viewModels.PolygonVM;

import static ru.zkdev.parking.configs.Constants.MAIN_CONTAINER;

public class ParkingListFragment extends BaseFragment {
  private static final String TAG = "ParkingListFragment";
  private static final int LAYOUT = R.layout.fragment_parking_list;
  private FragmentParkingListBinding binding;
  private Context context;
  private PolygonVM vm;
  private PolygonListAdapter adapter;
  private LocationService locationService;
  private boolean isSort = false;

  public ParkingListFragment() {
    // Required empty public constructor
  }

  public static ParkingListFragment getInstance(Context context, boolean isSort) {
    ParkingListFragment fragment = new ParkingListFragment();
    fragment.setContext(context);
    fragment.setIsSort(isSort);
    return fragment;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    vm = ViewModelProviders.of(getActivity()).get(PolygonVM.class);
    vm.init();
    locationService = new LocationService(getActivity());
    initRecyclerView();

    vm.getParkingPlaces().observe(getActivity(), parkings -> {
      Log.d(TAG, "onChanged: ");
      adapter.notifyDataSetChanged();
    });
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    binding = DataBindingUtil.inflate(inflater, LAYOUT, container, false);
    return binding.getRoot();
  }


  @Override
  public void onResume() {
    super.onResume();
    Log.d(TAG, "onResume: ");
    getActivity().setTitle(getString(isSort ? R.string.title_toolbar_search : R.string.title_toolbar_all));
  }

  private void initRecyclerView() {
    Log.d(TAG, "initRecyclerView: ");

    ;

    adapter = new PolygonListAdapter(getContext(), sortByDistance(vm.getParkingPlaces().getValue()));
    binding.recyclerViewOnParkingList.setLayoutManager(new LinearLayoutManager(getContext()));
    binding.recyclerViewOnParkingList.setHasFixedSize(true);
    binding.recyclerViewOnParkingList.setAdapter(adapter);
    adapter.setOnItemClickListener((position, item) -> {
      Log.d(TAG, "setOnItemClickListener: ");
      doFragmentTransaction(MapFragment.getInstance(getContext(), calculateCenter(item.getPolygon())), "MapFragment", true, MAIN_CONTAINER);
    });

  }

  @Nullable
  @Override
  public Context getContext() {
    return context;
  }

  public void setContext(Context context) {
    this.context = context;
  }

  public void setIsSort(boolean sort) {
    isSort = sort;
  }

  private List<Parking> sortByDistance(List<Parking> parkings) {
    double distance = 0;
    List<RangeHelper> rangeHelpers = new ArrayList<>();
    for (Parking polygon : parkings) {
      if (polygon.getMaxQuantity() == polygon.getQuantity() && isSort) {
        continue;
      }
      Location location = new Location(locationService.getProvider());
      LatLng markerCenter = calculateCenter(polygon.getPolygon());
      location.setLongitude(markerCenter.longitude);
      location.setLatitude(markerCenter.latitude);

      distance = locationService.getDistance(location);
      rangeHelpers.add(new RangeHelper(distance, polygon));
    }

    Collections.sort(rangeHelpers, (z1, z2) -> {
      if (z1.getDistance() > z2.getDistance())
        return 1;
      if (z1.getDistance() < z2.getDistance())
        return -1;
      return 0;
    });

    parkings = new ArrayList<>();
    for (RangeHelper helper : rangeHelpers) parkings.add(helper.getParking());

    return parkings;
  }

  public class RangeHelper implements Comparator<RangeHelper> {
    private double distance;
    private Parking parking;

    public RangeHelper(double distance, Parking parking) {
      this.distance = distance;
      this.parking = parking;
    }

    public RangeHelper() {
    }

    public double getDistance() {
      return distance;
    }

    public void setDistance(double distance) {
      this.distance = distance;
    }

    public Parking getParking() {
      return parking;
    }

    public void setParking(Parking parking) {
      this.parking = parking;
    }

    @Override
    public int compare(RangeHelper o1, RangeHelper o2) {
      return 0;
    }
  }

}
