package ru.zkdev.parking.viewModels;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.BaseObservable;

import ru.zkdev.parking.R;

public class AboutVM extends BaseObservable {
  private static final String TAG = "AboutVM";
  private Context context;
  private OnCallClickListener onCallClickListener;

  public AboutVM(Context context) {
    this.context = context;
  }

  public void onCallClick(@NonNull View view) {
    Log.d(TAG, "onCallClick: ");
    getCallAction();
  }

  public void onSendClick(@NonNull View view) {
    Log.d(TAG, "onSendClick: ");
    shareAction();
  }

  private void getCallAction() {
    Log.d(TAG, "getCallAction: ");
    onCallClickListener.onCall();
  }

  private void shareAction() {
    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
    sharingIntent.setType("text/plain");
    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, context.getString(R.string.info_name));
    context.startActivity(Intent.createChooser(sharingIntent, context.getString(R.string.info_email)));
  }

  public void setOnCallClickListener(OnCallClickListener onCallClickListener) {
    this.onCallClickListener = onCallClickListener;
  }

  public interface OnCallClickListener{
    void onCall();
  }
}
