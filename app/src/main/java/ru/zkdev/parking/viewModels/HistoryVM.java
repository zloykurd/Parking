package ru.zkdev.parking.viewModels;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ru.zkdev.parking.models.ParkingHistory;
import ru.zkdev.parking.repositories.HistoryRepository;

public class HistoryVM extends ViewModel {
  private ParkingHistory history;
  private HistoryRepository repository;
  private MutableLiveData<List<ParkingHistory>> mPlaces;

  public LiveData<List<ParkingHistory>> getPlaces() {
    return mPlaces;
  }

  public void geleteById(int id) {
    repository.deleteById(id);
  }


  public void init() {
    if (mPlaces != null) return;
    repository = HistoryRepository.getInstance();
    mPlaces = repository.getAllHistory();
  }

  public ParkingHistory getHistory() {
    return history;
  }

  public void setHistory(ParkingHistory history) {
    this.history = history;
  }
}
