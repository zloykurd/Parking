package ru.zkdev.parking.viewModels;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import ru.zkdev.parking.models.ParkingHistory;
import ru.zkdev.parking.repositories.HistoryRepository;
import ru.zkdev.parking.repositories.ParkingRepository;

import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class HistoryVMTest {

  private ParkingHistory history;
  @Mock
   Context context;

  @Mock
   Realm realm;
  private HistoryRepository repository;

  private MutableLiveData<List<ParkingHistory>> mPlaces;

  @Before
  public void onCreate() {
    initRealm();
    ParkingRepository repo = ParkingRepository.getInstance();
    repo.setMoqData();
    MockitoAnnotations.initMocks(this);
    repository = HistoryRepository.getInstance();
    history = new ParkingHistory();
  }

  @After
  public void closeRealm() {
    realm.close();
  }

  private void initRealm() {
    Realm.init(context);
    RealmConfiguration testConfig =
        new RealmConfiguration.Builder().
            inMemory().
            name("test-realm").build();

     realm = Realm.getInstance(testConfig);

  }

/*  @Test
  public void getPlacesTest() {
     MutableLiveData<List<ParkingHistory>> liveData = repository.getAllHistory();
     CountDownLatch latch = new CountDownLatch(1);
    Observer<List<ParkingHistory>> observer = new Observer<List<ParkingHistory>>() {
      @Override
      public void onChanged(@Nullable List<ParkingHistory> list) {
        latch.countDown();
        liveData.removeObserver(this);
      }
    };

    liveData.observeForever(observer);
    try {
      latch.await(2, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

  }*/

  @Test
  public void geleteByIdTest() {
    //when(32).thenReturn(32);

  }
/*
  @Test
  public void initTest() {

  }

  @Test
  public void getHistoryTest() {

  }

  @Test
  public void setHistoryTest() {
  }*/
}