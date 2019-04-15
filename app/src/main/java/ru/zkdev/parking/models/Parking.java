package ru.zkdev.parking.models;

import com.google.android.gms.maps.model.LatLng;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Parking extends RealmObject {
  @PrimaryKey
  private int id;
  private String name;
  private int pointImg;
  private String urlImage;
  private RealmList<CustomLatLng> polygon;
  private String description;
  private boolean isAllDay;
  private int quantity;
  private int maxQuantity;

  public Parking() {
  }

  public Parking(int id, String name, int pointImg, String urlImage, LatLng[] polygon,
                 String description, boolean isAllDay, int quantity, int MaxQuantity) {
    this.id = id;
    this.name = name;
    this.pointImg = pointImg;
    this.urlImage = urlImage;
    setPolygon(polygon);
    this.description = description;
    this.isAllDay = isAllDay;
    this.quantity = quantity;
    this.maxQuantity = MaxQuantity;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getPointImg() {
    return pointImg;
  }

  public void setPointImg(int pointImg) {
    this.pointImg = pointImg;
  }

  public LatLng[] getPolygon() {
    LatLng[] poly = new LatLng[4];
    for (int i = 0; i < polygon.size(); i++) {
      poly[i] = new LatLng(polygon.get(i).getLat(), polygon.get(i).getLng());
    }
    return poly;
  }

  public void setPolygon(LatLng[] poly) {
    polygon = new RealmList<>();
    for (LatLng latLng : poly) {
      polygon.add(new CustomLatLng(latLng.latitude, latLng.longitude));
    }
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isAllDay() {
    return isAllDay;
  }

  public void setAllDay(boolean allDay) {
    isAllDay = allDay;
  }

  public String getUrlImage() {
    return urlImage;
  }

  public void setUrlImage(String urlImage) {
    this.urlImage = urlImage;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public int getMaxQuantity() {
    return maxQuantity;
  }

  public void setMaxQuantity(int maxQuantity) {
    this.maxQuantity = maxQuantity;
  }
}
