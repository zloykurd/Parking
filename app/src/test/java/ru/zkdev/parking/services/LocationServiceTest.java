package ru.zkdev.parking.services;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;

import androidx.test.InstrumentationRegistry;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.common.collect.Lists;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LocationServiceTest {

  @Mock
  Context context;
  @Mock
  LocationManager mockedLocationManager;
  @InjectMocks
  LocationService locationService;

  @Before
  public void onCreate() {
    locationService = new LocationService(context);
    locationService.setLocationManager(mockedLocationManager);
    assertNotNull(locationService);
  }

  @Test
  public void onConnectionToProviderTest() {
    // given
    given(mockedLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        .willReturn(false);
    given(mockedLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        .willReturn(false);
  }

  @Test
  public void getLocationGPSProviderTest() {
    // given
    Location expected = mock(Location.class);

    List<String> providers =
        Lists.newArrayList(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER);
    when(mockedLocationManager.getProviders(true))
        .thenReturn(providers);
    when(mockedLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        .thenReturn(true);
    when(mockedLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        .thenReturn(false);
    when(mockedLocationManager.getLastKnownLocation(anyString()))
        .thenReturn(expected);
    Location result = mockedLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    assertEquals(expected, result);
  }

  @Test
  public void getLocationNetworkProviderTest() {
    // given
    List<String> providers =
        Lists.newArrayList(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER);
    given(mockedLocationManager.getProviders(true))
        .willReturn(providers);
    given(mockedLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        .willReturn(false);
    given(mockedLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        .willReturn(true);

    // when
    Location result = mockedLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    // then
    assertNull(result);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testGetLocalityFromLatLng() throws Exception {
    LatLng latLng = new LatLng(locationService.latitude, locationService.longitude);
    when(latLng).thenReturn(new LatLng(locationService.latitude, locationService.longitude));
    assertThat(latLng, is(notNullValue()));
    verify(times(1));
  }
 @Test
  public void LatLngTest(LatLng a, LatLng b) {
    Assert.assertEquals(a.latitude, b.latitude);
    Assert.assertEquals(a.longitude, b.longitude);
  }
}