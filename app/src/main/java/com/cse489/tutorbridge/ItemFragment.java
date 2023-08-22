package com.cse489.tutorbridge;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cse489.tutorbridge.placeholder.OrderModal;

import java.util.ArrayList;

public class ItemFragment extends Fragment {
    private MyItemRecyclerViewAdapter adapter;
    private ArrayList<OrderModal> hList;

    public ItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        RecyclerView rView = view.findViewById(R.id.historyRview);
        rView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Create your list of items
        // store data
        hList = new ArrayList<>();


        adapter = new MyItemRecyclerViewAdapter(hList);
        rView.setAdapter(adapter);

        return view;
    }
}