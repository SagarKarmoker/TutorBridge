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
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            viewHolder = new ViewHolder();

            if (message.isSentByCurrentUser()) {
                convertView = inflater.inflate(R.layout.item_message_sent, parent, false);
                viewHolder.messageView = convertView.findViewById(R.id.item_message_sent_view);
            } else {
                convertView = inflater.inflate(R.layout.item_message_received, parent, false);
                viewHolder.messageView = convertView.findViewById(R.id.item_message_rcv_view);
            }

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Bind and display the message content
        viewHolder.messageView.setText(message.getContent());

        return convertView;
    }

    // ViewHolder pattern for better performance
    private static class ViewHolder {
        TextView messageView;
    }
}
