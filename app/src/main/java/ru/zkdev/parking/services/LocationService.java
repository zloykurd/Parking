package ru.zkdev.parking.services;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import java.util.Timer;

import androidx.annotation.Nullable;
import ru.zkdev.parking.R;

import static ru.zkdev.parking.configs.Constants.LOCATION_UPDATE;

public class LocationService extends Service implements LocationListener {
  private static final String TAG = "LocationService";
  private Context mContext;
  private String PROVIDER = "";
  private int mStartMode = START_REDELIVER_INTENT;
  // flag for GPS status
  boolean isGPSEnabled = false;

  // flag for network status
  boolean isNetworkEnabled = false;

  // flag for GPS status
  boolean canGetLocation = false;

  Location location; // location
  double latitude; // latitude
  double longitude; // longitude

  // The minimum distance to change Updates in meters
  private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

  // The minimum time between updates in milliseconds
  private static final long MIN_TIME_BW_UPDATES = 1000;// * 60 * 1; // 1 minute

  // Declaring a Location Manager
  protected LocationManager locationManager;

  public LocationService() {
  }


  private void startService() {
    Log.d(TAG, "startService: ");
    getLocation();
  }

  @Override
  public void onCreate() {
    Log.d(TAG, "onCreate: ");
    // The service is being created
    startService();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Log.d(TAG, "onStartCommand: ");
    return mStartMode;
  }

  public LocationService(Context context) {
    this.mContext = context;
    getLocation();
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return new Binder();
  }

  @Override
  public void onLocationChanged(Location location) {
    Log.d(TAG, "onLocationChanged: ");
    Log.d(TAG, "onLocationChanged: " + location.getLatitude());
    Log.d(TAG, "onLocationChanged: " + location.getLongitude());
    Intent intent = new Intent(LOCATION_UPDATE);
    intent.putExtra("coordinates", location.getLongitude() + " " + location.getLatitude());
    mContext.sendBroadcast(intent);
  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {
    Log.d(TAG, "onStatusChanged: ");
    Log.d(TAG, "onStatusChanged: " + provider);
    setProvider(provider);
    Log.d(TAG, "onStatusChanged: " + status);
    Log.d(TAG, "onStatusChanged: " + extras.toString());

  }

  @Override
  public void onProviderEnabled(String provider) {
    Log.d(TAG, "onProviderEnabled: ");

  }

  @Override
  public void onProviderDisabled(String provider) {
    Log.d(TAG, "onProviderDisabled: ");

    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);

  }

  public String getProvider() {
    return PROVIDER;
  }

  public void setProvider(String provider) {
    this.PROVIDER = provider;
  }


  public double getDistance(Location point) {
    return location.distanceTo(point);
  }

  @SuppressLint("MissingPermission")
  public Location getLocation() {
    try {
      locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

      // getting GPS status
      isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

      // getting network status
      isNetworkEnabled = locationManager
          .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

      if (!isGPSEnabled && !isNetworkEnabled) {
        // no network provider is enabled
      } else {
        this.canGetLocation = true;
        // First get location from Network Provider
        if (isNetworkEnabled) {
          locationManager.requestLocationUpdates(
              LocationManager.NETWORK_PROVIDER,
              MIN_TIME_BW_UPDATES,
              MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

          Log.d("Network", "Network");
          if (locationManager != null) {
            location = locationManager
                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {
              latitude = location.getLatitude();
              longitude = location.getLongitude();
            }
          }
        }
        // if GPS Enabled get lat/long using GPS Services
        if (isGPSEnabled) {
          if (location == null) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

            Log.d("GPS Enabled", "GPS Enabled");
            if (locationManager != null) {
              location = locationManager
                  .getLastKnownLocation(LocationManager.GPS_PROVIDER);

              if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
              }
            }
          }
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return location;
  }

  /**
   * Stop using GPS listener
   * Calling this function will stop using GPS in your app
   */

  public void stopUsingGPS() {
    if (locationManager != null) {
      locationManager.removeUpdates(LocationService.this);
    }
  }

  /**
   * Function to get latitude
   */

  public double getLatitude() {
    if (location != null) {
      latitude = location.getLatitude();
    }

    // return latitude
    return latitude;
  }

  /**
   * Function to get longitude
   */

  public double getLongitude() {
    if (location != null) {
      longitude = location.getLongitude();
    }

    // return longitude
    return longitude;
  }

  /**
   * Function to check GPS/wifi enabled
   *
   * @return boolean
   */

  public boolean canGetLocation() {
    return this.canGetLocation;
  }

  /**
   * Function to show settings alert dialog
   * On pressing Settings button will lauch Settings Options
   */

  public void showSettingsAlert() {
    AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
    alertDialog.setTitle(getString(R.string.title_geo_setting));
    alertDialog.setMessage(getString(R.string.message_geo_setting));
    alertDialog.setPositiveButton(getString(R.string.agree), (dialog, which) -> {
      Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
      mContext.startActivity(intent);
    });
    alertDialog.setNegativeButton(getString(R.string.disagree), (dialog, which) -> dialog.cancel());
    alertDialog.show();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (locationManager != null) {
      locationManager.removeUpdates(this);
    }
  }
}
