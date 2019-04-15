package ru.zkdev.parking.views;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import ru.zkdev.parking.R;
import ru.zkdev.parking.databinding.FragmentHistoryBinding;
import ru.zkdev.parking.services.LocationService;
import ru.zkdev.parking.ui.HistoryListAdapter;
import ru.zkdev.parking.utils.BaseFragment;
import ru.zkdev.parking.viewModels.HistoryVM;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends BaseFragment {
  private static final String TAG = "ParkingListFragment";
  private static final int LAYOUT = R.layout.fragment_history;
  private FragmentHistoryBinding binding;
  private Context context;
  private HistoryVM vm;
  private HistoryListAdapter adapter;
  private LocationService locationService;

  public HistoryFragment() {
    // Required empty public constructor
  }

  public static HistoryFragment getInstance(Context context) {
    HistoryFragment fragment = new HistoryFragment();
    fragment.setContext(context);
    return fragment;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    vm = ViewModelProviders.of(getActivity()).get(HistoryVM.class);
    vm.init();
    locationService = new LocationService(getActivity());
    initToolbar();
    initRecyclerView();
    vm.getPlaces().observe(getActivity(), parkingHistories -> {
      Log.d(TAG, "onChanged: ");
      adapter.notifyDataSetChanged();
    });
  }

  private void initToolbar() {
    Log.d(TAG, "initToolbar: ");
    ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbarOnHistoryList);
    ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    binding.toolbarOnHistoryList.setTitle(getString(R.string.title_toolbar_history));
    binding.toolbarOnHistoryList.setNavigationOnClickListener(v -> getActivity().onBackPressed());
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    binding = DataBindingUtil.inflate(inflater, LAYOUT, container, false);
    return binding.getRoot();
  }

  private void initRecyclerView() {
    Log.d(TAG, "initRecyclerView: ");

    ;

    adapter = new HistoryListAdapter(getContext(), vm.getPlaces().getValue());
    binding.recyclerViewOnHistoryList.setLayoutManager(new LinearLayoutManager(getContext()));
    binding.recyclerViewOnHistoryList.setHasFixedSize(true);
    binding.recyclerViewOnHistoryList.setAdapter(adapter);
    adapter.setOnItemClickListener((position, item) -> {

      AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
      dialog.setTitle(getString(R.string.title_delete));
      dialog.setMessage(getString(R.string.message_delete));
      dialog.setPositiveButton("Да", (dialog1, which) -> {
        vm.geleteById(item.getId());
        adapter.notifyDataSetChanged();
        dialog1.dismiss();
      });
      dialog.show();
    });

  }

  @Nullable
  @Override
  public Context getContext() {
    return context;
  }

  public void setContext(Context context) {
    this.context = context;
  }

}
