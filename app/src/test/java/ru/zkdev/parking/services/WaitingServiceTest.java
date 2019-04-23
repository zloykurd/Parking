package ru.zkdev.parking.services;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import android.test.*;
import static junit.framework.TestCase.assertTrue;
import static ru.zkdev.parking.configs.Constants.LOCATION_UPDATE;

@RunWith(AndroidJUnit4.class)
public class WaitingServiceTest {

  @Mock
  Context context;
  @Mock
  LocationService locationService;


  @Rule
  public final WaitingService mServiceRule = new WaitingService();

  @Before
  private void onCreate() {
    locationService = new LocationService(context);
  }

  // test for a service which is started with startService
  @Test
  public void testWithStartedService() {
    mServiceRule.startService(new Intent(InstrumentationRegistry.getTargetContext(),
        WaitingService.class));
    // test code
  }

  @Test
  // test for a service which is started with bindService
  public void testWithBoundService() {
    assertTrue(LOCATION_UPDATE, mServiceRule.checkPolygon(locationService.getLocation()));
  }
}