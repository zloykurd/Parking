package ru.zkdev.parking.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.zkdev.parking.configs.Constants.LOCATION_UPDATE;

public class MapFragmentTest {

  @Mock
  BroadcastReceiver mReceiver;
  @Mock
  Context mContext;

  @Test
  public void resiveData(){
    Intent intent = new Intent(Intent.ACTION_NEW_OUTGOING_CALL);
    intent.putExtra(Intent.EXTRA_PHONE_NUMBER, "45.55555");
    mReceiver.onReceive(mContext, intent);
    assertNull(mReceiver.getResultData());

    // what did receiver do?
    ArgumentCaptor<Intent> argument =
        ArgumentCaptor.forClass(Intent.class);
    verify(mContext, times(1)).startActivity(argument.capture());
    Intent receivedIntent = argument.getValue();
    assertNull(receivedIntent.getAction());
    assertEquals("45.55555", receivedIntent.getStringExtra(LOCATION_UPDATE));
    assertTrue((receivedIntent.getFlags() &
        Intent.FLAG_ACTIVITY_NEW_TASK) != 0);
  }

}