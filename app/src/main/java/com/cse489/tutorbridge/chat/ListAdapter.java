package com.cse489.tutorbridge.chat;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.cse489.tutorbridge.R;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.cse489.tutorbridge.modal.OrderModal;
import com.google.firebase.database.annotations.Nullable;

import java.util.List;

public class ListAdapter extends ArrayAdapter {

    private Activity mContext;
    List<OrderModal> userList;
    TextView tvName;

    public ListAdapter(Activity mContext, List<OrderModal> userList){
        super(mContext,R.layout.user_item,userList);
        this.mContext = mContext;
        this.userList = userList;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = mContext.getLayoutInflater();
        convertView = inflater.inflate(R.layout.user_item,null,false);

        tvName = convertView.findViewById(R.id.username);


        OrderModal orderModal = userList.get(position);

        tvName.setText(orderModal.getOrderId());

        return convertView;
    }
}
