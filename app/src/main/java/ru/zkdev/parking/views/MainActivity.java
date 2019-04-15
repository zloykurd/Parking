package ru.zkdev.parking.views;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import ru.zkdev.parking.R;
import ru.zkdev.parking.databinding.ActivityMainBinding;
import ru.zkdev.parking.utils.BaseActivity;

import static ru.zkdev.parking.configs.Constants.MAIN_CONTAINER;

public class MainActivity extends BaseActivity  {
  private static final String TAG = "MainActivity";
  private static final int LAYOUT = R.layout.activity_main;
  private ActivityMainBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getLayoutId());
    binding = DataBindingUtil.setContentView(this, getLayoutId());
    showDefaultFragment();
  }

  private void showDefaultFragment() {
    Log.d(TAG, "showDefaultFragment: ");
    doFragmentTransaction(MapFragment.getInstance(this,null), "MapFragment", false, MAIN_CONTAINER);
  }

  @Override
  protected int getLayoutId() {
    return LAYOUT;
  }

}
