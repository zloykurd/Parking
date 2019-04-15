package ru.zkdev.parking.configs;

public interface OnItemClickListener<T> {
  void onItemClick(int position, T item);
}
