package ru.zkdev.parking.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ParkingHistory extends RealmObject {
  @PrimaryKey
  private int id;
  private String placeName;
  private int placeId;
  private int duration;
  private String date;

  public ParkingHistory() {
  }

  public ParkingHistory(int id, String placeName, int placeId, int duration, String date) {
    this.id = id;
    this.placeName = placeName;
    this.placeId = placeId;
    this.duration = duration;
    this.date = date;
  }

  public ParkingHistory(String placeName, int placeId, int duration, String date) {
    this.placeName = placeName;
    this.placeId = placeId;
    this.duration = duration;
    this.date = date;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getPlaceName() {
    return placeName;
  }

  public void setPlaceName(String placeName) {
    this.placeName = placeName;
  }

  public int getPlaceId() {
    return placeId;
  }

  public void setPlaceId(int placeId) {
    this.placeId = placeId;
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }
}
