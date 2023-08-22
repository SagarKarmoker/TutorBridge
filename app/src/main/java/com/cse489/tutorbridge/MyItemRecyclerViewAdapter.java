package com.cse489.tutorbridge;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cse489.tutorbridge.databinding.FragmentItemBinding;
import com.cse489.tutorbridge.placeholder.OrderModal;

import java.util.ArrayList;
import java.util.List;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private ArrayList<OrderModal> historyList;

    public MyItemRecyclerViewAdapter(ArrayList<OrderModal> histories) {
        this.historyList = histories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FragmentItemBinding binding = FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        OrderModal order = historyList.get(position);

        // Bind your data to the views using data binding
//        holder.binding.setOrder(order);
//        holder.binding.executePendingBindings(); // This is important to ensure immediate binding
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private FragmentItemBinding binding; // Use your data binding class here

        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        // No need for toString() here
    }
}