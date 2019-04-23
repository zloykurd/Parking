package ru.zkdev.parking.views;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import ru.zkdev.parking.R;
import ru.zkdev.parking.databinding.FragmentAboutBinding;
import ru.zkdev.parking.utils.BaseFragment;
import ru.zkdev.parking.viewModels.AboutVM;

import static ru.zkdev.parking.configs.Constants.VERIFY_PERMISSIONS_REQUEST;
import static ru.zkdev.parking.configs.Permissions.PERMISSIONS_PHONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends BaseFragment {
  private static final String TAG = "AboutFragment";
  private static final int LAYOUT = R.layout.fragment_about;
  private Context context;
  private FragmentAboutBinding binding;

  public AboutFragment() {
    // Required empty public constructor
  }

  public static AboutFragment getInstance(Context context) {
    AboutFragment fragment = new AboutFragment();
    fragment.setContext(context);
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    binding = DataBindingUtil.inflate(inflater, LAYOUT, container, false);
    AboutVM aboutVM = new AboutVM(getActivity());
    aboutVM.setOnCallClickListener(() -> onCallAction());
    binding.setItem(aboutVM);

    return binding.getRoot();
  }

  @Nullable
  @Override
  public Context getContext() {
    return context;
  }

  public void setContext(Context context) {
    this.context = context;
  }

  @Override
  public void onResume() {
    super.onResume();
    getActivity().setTitle(getString(R.string.title_toolbar_about));
  }

  private void onCallAction() {
    //region check PERMISSION
    if (checkPermissionsArray(PERMISSIONS_PHONE)) {

    } else {
      verifyPermissions(PERMISSIONS_PHONE);
    }
    //endregion
    Intent callIntent = new Intent(Intent.ACTION_CALL);
    callIntent.setData(Uri.parse("tel:" + getActivity().getString(R.string.info_phone)));
    if (ActivityCompat.checkSelfPermission(getActivity(),
        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
      Log.e(TAG, "callAction: != PackageManager.PERMISSION_GRANTED");
      return;
    }
    getActivity().startActivity(callIntent);
  }
  //region PERMISSION
  public void verifyPermissions(String[] permissions) {
    Log.e(TAG, "verifyPermissions: ");

    ActivityCompat.requestPermissions(
        getActivity(),
        permissions,
        VERIFY_PERMISSIONS_REQUEST
    );
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

    if (ActivityCompat.checkSelfPermission(getActivity(),
        permissions[0]) == PackageManager.PERMISSION_GRANTED) {
      switch (requestCode) {
        case 4:
          Log.e(TAG, "onRequestPermissionsResult: ");
          break;
        case 2:
          Log.e(TAG, "onRequestPermissionsResult: ");
          break;

      }
    }

  }
  //endregion
}
