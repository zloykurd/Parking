package ru.zkdev.parking.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import androidx.core.app.NotificationCompat;
import ru.zkdev.parking.R;
import ru.zkdev.parking.models.Parking;
import ru.zkdev.parking.models.ParkingHistory;
import ru.zkdev.parking.repositories.HistoryRepository;
import ru.zkdev.parking.repositories.ParkingRepository;
import ru.zkdev.parking.views.MainActivity;

import static ru.zkdev.parking.configs.Constants.DEFAULT_PAY;
import static ru.zkdev.parking.configs.Constants.DEFAULT_WAIT;
import static ru.zkdev.parking.configs.Constants.WEITING_SERVICE;

public class WaitingService extends Service {
  private static final String TAG = "WaitingService";
  private static Timer timer = new Timer();
  private Context ctx;
  private int mStartMode = START_REDELIVER_INTENT;       // indicates how to behave if the service is killed
  private IBinder mBinder;      // interface for clients that bind
  private boolean mAllowRebind; // indicates whether onRebind should be used
  private int total = 0, defaultWeitTile = 10, ID = 0;
  private LocationService locationService;
  private Parking poinPolygon;
  private int random;

  private ParkingRepository repository;
  private HistoryRepository history;


  @Override
  public void onCreate() {
    Log.d(TAG, "onCreate: ");
    // The service is being created
    total = 0;
    ctx = this;
    locationService = new LocationService(ctx);
    repository = ParkingRepository.getInstance();
    history = HistoryRepository.getInstance();
    startService();

  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Log.d(TAG, "onStartCommand: ");
    // The service is starting, due to a call to startService()
    if (intent != null) {
      random = new Random().nextInt(999999);
      ID = intent.getIntExtra("id", ID);
      poinPolygon = repository.getParkingById(ID);
      showDefaultNotification();

    }
    return mStartMode;
  }

  private void showPayNotification() {
    Log.d(TAG, "showPayNotification: ");
    Intent notificationIntent = new Intent(this, MainActivity.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, ID, notificationIntent, 0);
    Notification notification = new NotificationCompat.Builder(this, WEITING_SERVICE)
        .setContentTitle(getString(R.string.app_name))
        .setContentText(poinPolygon.getName() + " Время парковки " + total + " минут")
        .setAutoCancel(false)
        .setSmallIcon(R.drawable.ic_local_parking_blue_24dp).setContentIntent(pendingIntent).build();
    startForeground(ID, notification);
  }

  private void showDefaultNotification() {
    Intent notificationIntent = new Intent(this, MainActivity.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, ID, notificationIntent, 0);
    Notification notification = new NotificationCompat.Builder(this, WEITING_SERVICE)
        .setContentTitle(getString(R.string.app_name))
        .setContentText(poinPolygon.getName() + " Через " + defaultWeitTile + " минут начнется оплата")
        .setAutoCancel(false)
        .setSmallIcon(R.drawable.ic_local_parking_blue_24dp).setContentIntent(pendingIntent).build();
    startForeground(ID, notification);
  }

  private void showComplitedNotification() {
    Log.d(TAG, "showPayNotification: ");
    Intent notificationIntent = new Intent(this, MainActivity.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, ID, notificationIntent, 0);
    Notification notification = new NotificationCompat.Builder(this, WEITING_SERVICE)
        .setContentTitle(getString(R.string.app_name))
        .setContentText("Парковка завершена " + " Время парковки " + total + " минут")
        .setAutoCancel(true)
        .setSmallIcon(R.drawable.ic_local_parking_blue_24dp).setContentIntent(pendingIntent).build();
    startForeground(ID, notification);
    history.add(new ParkingHistory(random, poinPolygon.getName(), poinPolygon.getId(), total, getDate()));
  }

  @Override
  public IBinder onBind(Intent intent) {
    Log.d(TAG, "onBind: ");
    // A client is binding to the service with bindService()
    return mBinder;
  }

  @Override
  public boolean onUnbind(Intent intent) {
    Log.d(TAG, "onUnbind: ");
    // All clients have unbound with unbindService()
    return mAllowRebind;
  }

  @Override
  public void onRebind(Intent intent) {
    Log.d(TAG, "onRebind: ");
    // A client is binding to the service with bindService(),
    // after onUnbind() has already been called
  }

  @Override
  public void onDestroy() {
    Log.d(TAG, "onDestroy: ");
    // The service is no longer used and is being destroyed
  }

  private void startService() {
    Log.d(TAG, "startService: ");
    timer.scheduleAtFixedRate(defaultWeitTile == 0 ? new mainTask() : new defaultTask(), 0, 1000);
  }

  private class mainTask extends TimerTask {
    public void run() {
      total++;
      toastHandler.sendEmptyMessage(total);
    }
  }

  private class defaultTask extends TimerTask {
    public void run() {
      Log.d(TAG, "run: " + defaultWeitTile);
      toastHandler.sendEmptyMessage(defaultWeitTile);
      if (defaultWeitTile != 0) defaultWeitTile--;
    }
  }

  @SuppressLint("HandlerLeak")
  private final Handler toastHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      Log.d(TAG, "handleMessage: ");
      if (checkPolygon(locationService.getLocation())) {
        if (defaultWeitTile == 0) {
          showPayNotification();
          history.add(new ParkingHistory(random, poinPolygon.getName(), poinPolygon.getId(), total, getDate()));
          total++;
        } else {
          showDefaultNotification();
        }
        timer.scheduleAtFixedRate(new mainTask(), DEFAULT_WAIT, DEFAULT_PAY);
      } else {

        showComplitedNotification();
      }
    }
  };


  private String getDate() {
    String pattern = "dd-MM-yyyy HH:mm";
    Calendar c = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat(pattern);
    String formatedDate = df.format(c.getTime());
    return formatedDate;
  }

  public boolean checkPolygon(Location location) {
    Log.d(TAG, "checkPolygon: ");
    if (location == null || poinPolygon == null) return false;

    boolean isInPolygon = PolyUtil.containsLocation(
        new LatLng(location.getLatitude(), location.getLongitude()), Arrays.asList(poinPolygon.getPolygon()),
        true);
    Log.d(TAG, "checkPolygon: isInPolygon " + isInPolygon);
    return isInPolygon;
  }
}
