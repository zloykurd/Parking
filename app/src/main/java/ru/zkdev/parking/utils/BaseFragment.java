package ru.zkdev.parking.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.model.LatLng;

public class BaseFragment extends Fragment {
  private static final String TAG = "BaseFragment";

  public void doFragmentTransaction(Fragment fragment, String tag,
                                    boolean addBackStack, int container) {
    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
    transaction.replace(container, fragment, tag);

    if (addBackStack) {
      transaction.addToBackStack(tag);
    }

    transaction.commit();
  }

  public static LatLng calculateCenter(LatLng[] points) {
    double[] centroid = {0.0, 0.0};

    for (int i = 0; i < points.length; i++) {
      centroid[0] += points[i].latitude;
      centroid[1] += points[i].longitude;
    }

    int totalPoints = points.length;
    centroid[0] = centroid[0] / totalPoints;
    centroid[1] = centroid[1] / totalPoints;

    return new LatLng(centroid[0], centroid[1]);
  }

}
