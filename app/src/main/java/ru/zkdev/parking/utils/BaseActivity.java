package ru.zkdev.parking.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public abstract class BaseActivity extends AppCompatActivity {
  private static final String TAG = "BaseActivity";

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getLayoutId());
  }

  protected abstract int getLayoutId();



  public void showMessageToast(String message) {
    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
  }

  public static boolean isNetworkAvaible(Context context) {
    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
  }

  public void doFragmentTransaction(Fragment fragment, String tag,
                                    boolean addBackStack, int container) {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(container, fragment, tag);

    if (addBackStack) {
      transaction.addToBackStack(tag);
    }

    transaction.commit();
  }
}
