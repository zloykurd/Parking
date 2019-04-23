package ru.zkdev.parking;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;


import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.internal.RealmCore;
import io.realm.log.RealmLog;
import ru.zkdev.parking.models.Parking;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doCallRealMethod;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(RobolectricTestRunner.class)
//@Config(constants = BuildConfig.class, sdk = 21)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@SuppressStaticInitializationFor("io.realm.internal.Util")
@PrepareForTest({Realm.class, RealmConfiguration.class, RealmQuery.class, RealmResults.class, RealmCore.class, RealmLog.class})
public class RealmTest {
  @Rule
  public PowerMockRule rule = new PowerMockRule();
  private Realm mockRealm;
  private RealmResults<Parking> parking;

  @Before
  public void setup() throws Exception {
    mockStatic(RealmCore.class);
    mockStatic(RealmLog.class);
    mockStatic(Realm.class);
    mockStatic(RealmConfiguration.class);
    Realm.init(RuntimeEnvironment.systemContext);
    final Realm mockRealm = mock(Realm.class);
    final RealmConfiguration mockRealmConfig = mock(RealmConfiguration.class);

    doNothing().when(RealmCore.class);
    RealmCore.loadLibrary(any(Context.class));
    whenNew(RealmConfiguration.class).withAnyArguments().thenReturn(mockRealmConfig);
    when(Realm.getDefaultInstance()).thenReturn(mockRealm);
    when(mockRealm.createObject(Parking.class)).thenReturn(new Parking());

    Parking p1 = new Parking(4, "Parking " + 4, R.drawable.ic_local_parking_white_24dp, "https://images.unsplash.com/photo-1517008914955-8ccab1c6459b?ixlib=rb-1.2.1&auto=format&fit=crop&w=1360&q=80",
        new LatLng[]{
            new LatLng(42.883099, 74.609952),
            new LatLng(42.882093, 74.609936),
            new LatLng(42.882231, 74.611609),
            new LatLng(42.883194, 74.61162)
        }, "Parking Description", false, 40, 100);


    Parking p2 = new Parking(1, "Parking " + 1, R.drawable.ic_local_parking_white_24dp, "https://images.unsplash.com/photo-1526626607369-f89fe1ed77a9?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1350&q=80",
        new LatLng[]{
            new LatLng(42.842218, 74.620396),
            new LatLng(42.840542, 74.6203),
            new LatLng(42.840464, 74.622328),
            new LatLng(42.842084, 74.622403)
        }, "Parking Description", true, 10, 100);


    Parking p3 = new Parking(2, "Parking " + 2, R.drawable.ic_local_parking_white_24dp, "https://images.unsplash.com/photo-1506521781263-d8422e82f27a?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80",
        new LatLng[]{
            new LatLng(42.875709, 74.609335),
            new LatLng(42.874604, 74.60933),
            new LatLng(42.874526, 74.611438),
            new LatLng(42.875564, 74.611459)
        }, "Parking Description", false, 20, 100);


    Parking p4 = new Parking(3, "Parking " + 3, R.drawable.ic_local_parking_white_24dp, "https://images.unsplash.com/photo-1525879127219-ab8b81b86a15?ixlib=rb-1.2.1&auto=format&fit=crop&w=1351&q=80",
        new LatLng[]{
            new LatLng(42.880588, 74.603332),
            new LatLng(42.87953, 74.603295),
            new LatLng(42.879534, 74.604679),
            new LatLng(42.880548, 74.604802)
        }, "Parking Description", true, 100, 100);

    List<Parking> personList = Arrays.asList(p1, p2, p3, p4);
    RealmQuery<Parking> personQuery = mockRealmQuery();
    when(personQuery.findFirst()).thenReturn(personList.get(0));
    when(mockRealm.where(Parking.class)).thenReturn(personQuery);
    when(personQuery.equalTo(anyString(), anyInt())).thenReturn(personQuery);
    mockStatic(RealmResults.class);
    RealmResults<Parking> parkings = mockRealmResults();
    when(mockRealm.where(Parking.class).findAll()).thenReturn(parkings);

    when(personQuery.between(anyString(), anyInt(), anyInt())).thenReturn(personQuery);
    when(personQuery.beginsWith(anyString(), anyString())).thenReturn(personQuery);
    when(personQuery.findAll()).thenReturn(parking);

    when(parking.iterator()).thenReturn(personList.iterator());
    when(parking.size()).thenReturn(personList.size());
    this.mockRealm = mockRealm;
    this.parking = parking;
  }

  @Test
  public void shouldBeAbleToAccessActivityAndVerifyRealmInteractions() {
    doCallRealMethod().when(mockRealm).executeTransaction(Mockito.any(Realm.Transaction.class));
    verifyStatic(times(2));
    Realm.getDefaultInstance();
    verify(mockRealm, times(5)).where(Parking.class);
    verify(mockRealm, times(2)).delete(Parking.class);
    verify(mockRealm, times(2)).close();
  }

  @Test
  public void shouldBeAbleToVerifyTransactionCalls() {
    verifyStatic(times(2));
    Realm.getDefaultInstance();
    verify(mockRealm, times(4)).executeTransaction(Mockito.any(Realm.Transaction.class));
    verify(mockRealm, times(5)).executeTransaction(Mockito.any(Realm.Transaction.class));
    verify(mockRealm, times(2)).close();
  }

  @SuppressWarnings("unchecked")
  private <T extends RealmObject> RealmQuery<T> mockRealmQuery() {
    return mock(RealmQuery.class);
  }

  @SuppressWarnings("unchecked")
  private <T extends RealmObject> RealmResults<T> mockRealmResults() {
    return mock(RealmResults.class);
  }
}
