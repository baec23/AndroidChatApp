package com.gausslab.websockettest.ui;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gausslab.websockettest.databinding.ObjectChatmessageBinding;
import com.gausslab.websockettest.model.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ChatMessageRecyclerViewAdapter extends RecyclerView.Adapter<ChatMessageRecyclerViewAdapter.ViewHolder> {

    private final List<ChatMessage> messages;

    public ChatMessageRecyclerViewAdapter(List<ChatMessage> items) {
        messages = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(ObjectChatmessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.chatMessage = messages.get(position);
        holder.tv_senderName.setText(holder.chatMessage.getSenderName());
        String pattern = "K:mm:ss a";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        String dateString = simpleDateFormat.format(new Date(holder.chatMessage.getSendTimestamp()));
        holder.tv_sendDate.setText(dateString);
        holder.tv_messageContent.setText(holder.chatMessage.getMessageContent());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView tv_senderName;
        public final TextView tv_sendDate;
        public final TextView tv_messageContent;
        public ChatMessage chatMessage;

        public ViewHolder(ObjectChatmessageBinding binding) {
            super(binding.getRoot());
            tv_senderName = binding.chatMessageTvSenderName;
            tv_sendDate = binding.chatMessageTvSendDate;
            tv_messageContent = binding.chatMessageTvMessageContent;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tv_messageContent.getText() + "'";
        }
    }
}