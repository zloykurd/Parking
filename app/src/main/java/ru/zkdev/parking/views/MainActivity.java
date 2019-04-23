package ru.zkdev.parking.views;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.navigation.NavigationView;

import ru.zkdev.parking.R;
import ru.zkdev.parking.databinding.ActivityMainBinding;
import ru.zkdev.parking.utils.BaseActivity;

import static ru.zkdev.parking.configs.Constants.MAIN_CONTAINER;

public class MainActivity extends BaseActivity
    implements NavigationView.OnNavigationItemSelectedListener {
  private static final String TAG = "MainActivity";
  private static final int LAYOUT = R.layout.activity_main;
  private ActivityMainBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getLayoutId());
    binding = DataBindingUtil.setContentView(this, getLayoutId());
    initNavigationView();
    showDefaultFragment();
  }

  private void showDefaultFragment() {
    Log.d(TAG, "showDefaultFragment: ");
    binding.navigation.setCheckedItem(binding.navigation.getMenu().getItem(0));
    binding.navigation.getMenu().performIdentifierAction(R.id.nav_map, 0);
  }

  @Override
  protected int getLayoutId() {
    return LAYOUT;
  }

  private void initNavigationView() {
    Log.d(TAG, "initNavigationView: ");
    setSupportActionBar(binding.toolbarOnMain);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, binding.mainDrawer, binding.toolbarOnMain,
        R.string.navigation_drawer_open,
        R.string.navigation_drawer_close);
    binding.mainDrawer.addDrawerListener(toggle);
    toggle.syncState();
    binding.navigation.setNavigationItemSelectedListener(this);
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
    Log.d(TAG, "onNavigationItemSelected: ");
    switch (menuItem.getItemId()) {
      case R.id.nav_map:

        doFragmentTransaction(MapFragment.getInstance(this, null),
            "MapFragment", false, MAIN_CONTAINER);
        break;
      case R.id.nav_all_parking:
        doFragmentTransaction(ParkingListFragment.getInstance(this, false),
            "ParkingListFragment", true, MAIN_CONTAINER);
        break;
      case R.id.nav_free_parking:
        doFragmentTransaction(ParkingListFragment.getInstance(this, true),
            "ParkingListFragment", true, MAIN_CONTAINER);
        break;
      case R.id.nav_history:
        doFragmentTransaction(HistoryFragment.getInstance(this), "HistoryFragment",
            true, MAIN_CONTAINER);
        break;
      case R.id.nav_about:
        doFragmentTransaction(AboutFragment.getInstance(this),
            "AboutFragment", true, MAIN_CONTAINER);
        break;
    }
    binding.mainDrawer.closeDrawer(GravityCompat.START);
    return true;
  }

  @Override
  public void onBackPressed() {
    Log.d(TAG, "onBackPressed: ");
    if (binding.mainDrawer.isDrawerOpen(GravityCompat.START)) {
      binding.mainDrawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }
}
