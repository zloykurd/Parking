package ru.zkdev.parking.viewModels;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ru.zkdev.parking.models.Parking;
import ru.zkdev.parking.repositories.ParkingRepository;

public class PolygonVM extends ViewModel {
  private ParkingRepository repository;
  private MutableLiveData<List<Parking>> mParkingPlaces;

  public LiveData<List<Parking>> getParkingPlaces() {
    return mParkingPlaces;
  }

  public Parking getParkingBuId(int id) {
    return repository.getParkingById(id);
  }

  public void init() {
    if (mParkingPlaces != null) return;
    repository = ParkingRepository.getInstance();
    mParkingPlaces = repository.getParkingPlaces();
  }

}
