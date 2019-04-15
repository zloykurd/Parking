package ru.zkdev.parking.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import ru.zkdev.parking.R;
import ru.zkdev.parking.configs.OnItemClickListener;
import ru.zkdev.parking.databinding.ItemForHistoryListBinding;
import ru.zkdev.parking.models.ParkingHistory;
import ru.zkdev.parking.viewModels.HistoryVM;

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.ParkingViewHolder> {
  private static final String TAG = "PolygonListAdapter";
  private List<ParkingHistory> parkingList = new ArrayList<>();
  private Context context;
  private OnItemClickListener<ParkingHistory> onItemClickListener;

  public HistoryListAdapter(Context context, List<ParkingHistory> parkingList) {
    this.parkingList = parkingList;
    this.context = context;
  }

  @NonNull
  @Override
  public ParkingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemForHistoryListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
        R.layout.item_for_history_list, parent, false);
    return new ParkingViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull ParkingViewHolder holder, int position) {
    ItemForHistoryListBinding binding = holder.binding;
    holder.getBinding().getRoot().setOnClickListener(v -> {
      if (onItemClickListener != null)
        onItemClickListener.onItemClick(position, parkingList.get(position));
    });
    HistoryVM vm = new HistoryVM();
    vm.setHistory(parkingList.get(position));
    binding.setItem(vm);
  }

  @Override
  public int getItemCount() {
    return parkingList.size();
  }

  public void setOnItemClickListener(OnItemClickListener<ParkingHistory> onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }

  public class ParkingViewHolder extends RecyclerView.ViewHolder {
    ItemForHistoryListBinding binding;

    public ParkingViewHolder(ItemForHistoryListBinding binding) {
      super(binding.pictureItem);
      this.binding = binding;
    }

    public ItemForHistoryListBinding getBinding() {
      return binding;
    }

  }

}
