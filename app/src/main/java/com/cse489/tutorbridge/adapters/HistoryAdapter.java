package com.cse489.tutorbridge.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.cse489.tutorbridge.PaymentActivity;
import com.cse489.tutorbridge.R;
import com.cse489.tutorbridge.modal.HistoryClass;

import java.io.Serializable;
import java.util.ArrayList;

public class HistoryAdapter extends ArrayAdapter<HistoryClass> {
    private final Context context;
    private final ArrayList<HistoryClass> histories;

    public HistoryAdapter(@NonNull Context context, ArrayList<HistoryClass> histories) {
        super(context, -1, histories);
        this.context = context;
        this.histories = histories;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { // it will be called for each row (0 to n-1)

        // inflater is renderer
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.history_row, parent, false); // works as a view

        ImageView historyMoreBtn = rowView.findViewById(R.id.historyMore);
        TextView orderId = rowView.findViewById(R.id.orderId);
        TextView orderDate = rowView.findViewById(R.id.orderDate);
        TextView orderStatus = rowView.findViewById(R.id.orderStatus);
        TextView paymentMethod = rowView.findViewById(R.id.paymentMethod);
        TextView orderCategory = rowView.findViewById(R.id.orderCategory);

        HistoryClass history = histories.get(position);
        //Log.d("inAdapter", profile.toString());
        orderId.setText("Order#: "+ history.getOrderId());
        orderDate.setText("On: "+history.getOrderDate());
        orderStatus.setText("Status: "+history.getOrderStatus());
        paymentMethod.setText("Payment Method: "+history.getPaymentMethod());
        orderCategory.setText("(Category: "+ history.getOrderCategory() + ")");

        //TODO send user and mentor data also

        historyMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PaymentActivity.class);
                i.putExtra("order", (Serializable) history);
                context.startActivity(i);
            }
        });


        return rowView;
    }
}
