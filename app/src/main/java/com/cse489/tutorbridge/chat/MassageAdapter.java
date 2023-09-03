package com.cse489.tutorbridge.chat;

import android.app.Activity;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cse489.tutorbridge.R;

import java.util.List;

public class MassageAdapter extends BaseAdapter {
    private Context context;
    private List<Message> messagesList;
    TextView item_message_sent_view_id;
    TextView item_message_received_id;

    public MassageAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messagesList = messages;
    }

    @Override
    public int getCount() {
        return messagesList.size();
    }

    @Override
    public Message getItem(int position) {
        return messagesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = messagesList.get(position);
        LayoutInflater inflater = LayoutInflater.from(context);

        if (message.isSentByCurrentUser()) {
            convertView = inflater.inflate(R.layout.item_message_sent, parent, false);
            // Bind and display sent message
            item_message_sent_view_id = convertView.findViewById(R.id.item_message_sent_view);


            Message msg = messagesList.get(position);


            item_message_sent_view_id.setText(msg.getContent());

        }
        else {
            convertView = inflater.inflate(R.layout.item_message_received, parent, false);
            // Bind and display received message
            item_message_received_id = convertView.findViewById(R.id.item_message_rcv_view);


            Message msg = messagesList.get(position);


            item_message_received_id.setText(msg.getContent());

        }

        return convertView;
    }
}
