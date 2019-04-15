package ru.zkdev.parking.utils;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static ru.zkdev.parking.configs.Constants.WEITING_SERVICE;

public class ParkingApp extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    createCallService();
    initRealm();
  }

  private void initRealm() {
    Realm.init(this);
    RealmConfiguration configuration = new RealmConfiguration.Builder().name("parking.db")
        .deleteRealmIfMigrationNeeded()
        .build();
    Realm.setDefaultConfiguration(configuration);
  }

  private void createCallService() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel serviceId = new NotificationChannel(
          WEITING_SERVICE, WEITING_SERVICE,
          NotificationManager.IMPORTANCE_DEFAULT);
      NotificationManager notificationManager = getSystemService(NotificationManager.class);
      notificationManager.createNotificationChannel(serviceId);
    }
  }
}
