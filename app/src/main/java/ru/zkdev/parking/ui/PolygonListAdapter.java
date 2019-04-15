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
import ru.zkdev.parking.databinding.ItemForParkingListBinding;
import ru.zkdev.parking.models.Parking;
import ru.zkdev.parking.viewModels.ParkingVM;

public class PolygonListAdapter extends RecyclerView.Adapter<PolygonListAdapter.ParkingViewHolder> {
  private static final String TAG = "PolygonListAdapter";
  private List<Parking> parkingList = new ArrayList<>();
  private Context context;
  private OnItemClickListener<Parking> onItemClickListener;

  public PolygonListAdapter(Context context, List<Parking> parkingList) {
    this.parkingList = parkingList;
    this.context = context;
  }

  @NonNull
  @Override
  public ParkingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemForParkingListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
        R.layout.item_for_parking_list, parent, false);
    return new ParkingViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull ParkingViewHolder holder, int position) {
    ItemForParkingListBinding binding = holder.binding;
    holder.getBinding().getRoot().setOnClickListener(v -> {
      if (onItemClickListener != null)
        onItemClickListener.onItemClick(position, parkingList.get(position));
    });

    binding.setItem(new ParkingVM(parkingList.get(position)));
  }

  @Override
  public int getItemCount() {
    return parkingList.size();
  }

  public void setOnItemClickListener(OnItemClickListener<Parking> onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }

  public class ParkingViewHolder extends RecyclerView.ViewHolder {
    ItemForParkingListBinding binding;

    public ParkingViewHolder(ItemForParkingListBinding binding) {
      super(binding.pictureItem);
      this.binding = binding;
    }

    public ItemForParkingListBinding getBinding() {
      return binding;
    }

  }

}
