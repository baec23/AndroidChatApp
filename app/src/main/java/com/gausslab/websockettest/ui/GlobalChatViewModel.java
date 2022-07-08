package com.gausslab.websockettest.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.gausslab.websockettest.model.ChatMessage;
import com.gausslab.websockettest.repository.ChatMessageRepository;

import java.util.List;

public class GlobalChatViewModel extends ViewModel {
    ChatMessageRepository chatMessageRepository = ChatMessageRepository.getInstance();

    public void sendMessage(String senderName, String message)
    {
        chatMessageRepository.sendMessage(new ChatMessage(senderName, message));
    }

    public List<ChatMessage> getMessages()
    {
        return chatMessageRepository.getChatMessages();
    }

    public LiveData<Boolean> isDoneLoading()
    {
        return chatMessageRepository.isDoneLoading();
    }

    public LiveData<Boolean> isMessagesUpdated()
    {
        return chatMessageRepository.isChatMessagesUpdated();
    }
}
