package ru.zkdev.parking;

import android.content.pm.PackageInfo;
import android.test.ApplicationTestCase;

import org.junit.Before;
import org.junit.Test;

import ru.zkdev.parking.utils.ParkingApp;

import static junit.framework.TestCase.assertNotNull;

public class ParkingAppTest  {
  private ParkingApp application;

  @Before
  protected void setUp() {
    application = new ParkingApp();
  }

  @Test
  public void testCorrectVersion() throws Exception {
    PackageInfo info = application.getPackageManager().getPackageInfo(application.getPackageName(), 0);
    assertNotNull(info);
    MoreAsserts.assertMatchesRegex("\\d\\.\\d", info.versionName);
  }
}
