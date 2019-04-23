package ru.zkdev.parking.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import ru.zkdev.parking.R;
import ru.zkdev.parking.databinding.ActivitySplashBinding;
import ru.zkdev.parking.repositories.ParkingRepository;

public class SplashActivity extends AppCompatActivity {
  private static final String TAG = "SplashActivity";
  public static final int LAYOUT = R.layout.activity_splash;
  private Animation animation;
  private ActivitySplashBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(LAYOUT);
    binding = DataBindingUtil.setContentView(this, LAYOUT);
    ParkingRepository repository = ParkingRepository.getInstance();
    repository.setMoqData();
  }

  @Override
  protected void onResume() {
    super.onResume();
    showAnimation();
    onNextPage();
  }

  private void onNextPage() {
    Log.d(TAG, "onNextPage: ");
    new Handler().postDelayed(() -> {
          startActivity(
              new Intent(this, MainActivity.class));
          finish();
        },
        3000);
  }

  private void showAnimation() {
    Log.d(TAG, "showAnimation: ");
    animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_zoom_out);
    binding.ivOnSplashActivity.startAnimation(animation);
  }

}
