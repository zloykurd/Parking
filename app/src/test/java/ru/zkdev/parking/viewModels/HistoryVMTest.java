package ru.zkdev.parking.viewModels;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.realm.RealmObject;
import ru.zkdev.parking.models.ParkingHistory;
import ru.zkdev.parking.repositories.HistoryRepository;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HistoryVMTest {
  @Mock
  private ParkingHistory history;
  @Mock
  private HistoryRepository repository;
  @Mock
  private MutableLiveData<List<ParkingHistory>> mPlaces;

  @Test
  public <T extends RealmObject> void getPlacesTest(final LiveData<T> liveData) {
    final Object[] data = new Object[1];
    final CountDownLatch latch = new CountDownLatch(1);
    Observer<T> observer = new Observer<T>() {
      @Override
      public void onChanged(@Nullable T o) {
        data[0] = o;
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
  }

  @Test
  public void geleteByIdTest(int id) {
    when(id).thenReturn(0);

  }

  @Test
  public void initTest() {

  }

  @Test
  public void getHistoryTest() {

  }

  @Test
  public void setHistoryTest() {
  }
}