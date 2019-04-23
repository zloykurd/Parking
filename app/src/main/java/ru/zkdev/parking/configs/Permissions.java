package ru.zkdev.parking.configs;

import android.Manifest;

public class Permissions {

  public static final String[] PERMISSIONS = {
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION
  };
  public static final String[] PERMISSIONS_PHONE = {
      Manifest.permission.CALL_PHONE
  };
}
