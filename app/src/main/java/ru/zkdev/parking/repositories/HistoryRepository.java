package ru.zkdev.parking.repositories;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import io.realm.Realm;
import io.realm.RealmResults;
import ru.zkdev.parking.models.Parking;
import ru.zkdev.parking.models.ParkingHistory;

public class HistoryRepository {

  private static HistoryRepository repository;

  public static HistoryRepository getInstance() {
    if (repository == null)
      repository = new HistoryRepository();

    return repository;
  }

  public MutableLiveData<List<ParkingHistory>> getAllHistory() {
    MutableLiveData<List<ParkingHistory>> data = new MutableLiveData<>();
    Realm realm = Realm.getDefaultInstance();
    RealmResults<ParkingHistory> realmResults = realm.where(ParkingHistory.class).findAll();
    data.setValue(realmResults);
    realm.close();
    return data;
  }


  public void deleteById(int id) {
    Realm realm = Realm.getDefaultInstance();
    ParkingHistory parkingHistoryking = null;
    try {
      realm.beginTransaction();
      parkingHistoryking = realm.where(ParkingHistory.class)
          .equalTo("id", id).findFirst();
      if (parkingHistoryking != null)
        parkingHistoryking.deleteFromRealm();
      realm.commitTransaction();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (!realm.isClosed())
        realm.close();
    }
  }

  public void add(ParkingHistory model) {
    Realm realm = Realm.getDefaultInstance();
    try {
      realm.beginTransaction();
      realm.insertOrUpdate(model);
      realm.commitTransaction();
    } catch (Exception e) {
      e.printStackTrace();
      realm.cancelTransaction();
    } finally {
      if (!realm.isClosed())
        realm.close();
    }
  }
}
