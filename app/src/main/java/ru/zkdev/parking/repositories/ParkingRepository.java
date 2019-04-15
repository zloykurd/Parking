package ru.zkdev.parking.repositories;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import io.realm.Realm;
import io.realm.RealmResults;
import ru.zkdev.parking.R;
import ru.zkdev.parking.models.Parking;

public class ParkingRepository {
  private static final String TAG = "ParkingRepository";

  private static ParkingRepository repository;


  public static ParkingRepository getInstance() {
    if (repository == null)
      repository = new ParkingRepository();

    return repository;
  }

  public MutableLiveData<List<Parking>> getParkingPlaces() {
    MutableLiveData<List<Parking>> data = new MutableLiveData<>();

    Realm realm = Realm.getDefaultInstance();
    RealmResults<Parking> realmResults = realm.where(Parking.class).findAll();
    data.setValue(realmResults);
    realm.close();
    return data;
  }


  public Parking getParkingById(int id) {
    Realm realm = Realm.getDefaultInstance();
    Parking parking = null;
    try {
      parking = realm.where(Parking.class).equalTo("id", id).findFirst();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (!realm.isClosed())
        realm.close();
    }
    return parking;
  }

  public void add(Parking model) {
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

  public void setMoqData() {

    Realm realm = Realm.getDefaultInstance();
    RealmResults<Parking> realmResults = realm.where(Parking.class).findAll();
    if (realmResults == null || realmResults.size() == 0) {
      //region Simple data
      add(new Parking(1, "Parking " + 1, R.drawable.ic_local_parking_white_24dp, "https://images.unsplash.com/photo-1526626607369-f89fe1ed77a9?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1350&q=80",
          new LatLng[]{
              new LatLng(42.842218, 74.620396),
              new LatLng(42.840542, 74.6203),
              new LatLng(42.840464, 74.622328),
              new LatLng(42.842084, 74.622403)
          }, "Parking Description", true, 10, 100));

      add(new Parking(2, "Parking " + 2, R.drawable.ic_local_parking_white_24dp, "https://images.unsplash.com/photo-1506521781263-d8422e82f27a?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80",
          new LatLng[]{
              new LatLng(42.875709, 74.609335),
              new LatLng(42.874604, 74.60933),
              new LatLng(42.874526, 74.611438),
              new LatLng(42.875564, 74.611459)
          }, "Parking Description", false, 20, 100));

      add(new Parking(3, "Parking " + 3, R.drawable.ic_local_parking_white_24dp, "https://images.unsplash.com/photo-1525879127219-ab8b81b86a15?ixlib=rb-1.2.1&auto=format&fit=crop&w=1351&q=80",
          new LatLng[]{
              new LatLng(42.880588, 74.603332),
              new LatLng(42.87953, 74.603295),
              new LatLng(42.879534, 74.604679),
              new LatLng(42.880548, 74.604802)
          }, "Parking Description", true, 100, 100));

      add(new Parking(4, "Parking " + 4, R.drawable.ic_local_parking_white_24dp, "https://images.unsplash.com/photo-1517008914955-8ccab1c6459b?ixlib=rb-1.2.1&auto=format&fit=crop&w=1360&q=80",
          new LatLng[]{
              new LatLng(42.883099, 74.609952),
              new LatLng(42.882093, 74.609936),
              new LatLng(42.882231, 74.611609),
              new LatLng(42.883194, 74.61162)
          }, "Parking Description", false, 40, 100));


      add(new Parking(5, "Parking " + 5, R.drawable.ic_local_parking_white_24dp, "https://images.unsplash.com/photo-1517871524228-a9c52d679b64?ixlib=rb-1.2.1&auto=format&fit=crop&w=1268&q=80",
          new LatLng[]{
              new LatLng(42.88282, 74.589015),
              new LatLng(42.881751, 74.588923),
              new LatLng(42.881598, 74.592072),
              new LatLng(42.882683, 74.592163)
          }, "Parking Description", false, 50, 100));

      add(new Parking(6, "Parking " + 6, R.drawable.ic_local_parking_white_24dp, "https://images.unsplash.com/photo-1517871524228-a9c52d679b64?ixlib=rb-1.2.1&auto=format&fit=crop&w=1268&q=80",
          new LatLng[]{
              new LatLng(42.840306, 74.621324),
              new LatLng(42.839146, 74.62067),
              new LatLng(42.839453, 74.624452),
              new LatLng(42.840467, 74.624506)
          }, "Parking Description", false, 60, 100));
      //endregion
    }

  }
}
