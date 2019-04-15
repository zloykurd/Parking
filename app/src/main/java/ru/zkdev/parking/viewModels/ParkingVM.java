package ru.zkdev.parking.viewModels;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import androidx.databinding.BaseObservable;
import androidx.databinding.BindingAdapter;
import ru.zkdev.parking.R;
import ru.zkdev.parking.models.Parking;

public class ParkingVM extends BaseObservable {
  private Parking parking;
  private String pictureUrl;

  public ParkingVM(Parking parking) {
    this.parking = parking;
  }

  @BindingAdapter("imageUrl")
  public static void setImageUrl(ImageView imageView, String pictureUrl) {
    Picasso.with(imageView.getContext())
        .load(pictureUrl)
        .error(R.drawable.ic_local_parking_blue_24dp)
        // .placeholder(R.mipmap.ic_launcher_foreground)
        .into(imageView);
  }

  public Parking getParking() {
    return parking;
  }

  public void setParking(Parking parking) {
    this.parking = parking;
  }

  public String getPictureUrl() {
    pictureUrl = parking.getUrlImage();
    return pictureUrl;
  }

}
