package com.gausslab.websockettest;

import android.util.JsonReader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class ChatMessageRepository {

    private static ChatMessageRepository instance = new ChatMessageRepository();
    private MutableLiveData<Boolean> chatMessagesUpdated = new MutableLiveData<>(false);
    private List<ChatMessage> chatMessages = new ArrayList<ChatMessage>();
    private WebSocketService webSocketService;

    private ChatMessageRepository() {
        init();
    }

    public static ChatMessageRepository getInstance() {
        return instance;
    }

    public void sendMessage(ChatMessage toSend)
    {
        webSocketService.getWebSocket().send(toSend.getMessageContent());
    }

    public LiveData<Boolean> isChatMessagesUpdated() {
        return chatMessagesUpdated;
    }

    public List<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    private void init()
    {
        webSocketService = new WebSocketService("ws://10.0.2.2:8080/name");
        webSocketService.registerListener(new WebSocketListener() {
            @Override
            public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                super.onClosed(webSocket, code, reason);
            }

            @Override
            public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                super.onClosing(webSocket, code, reason);
            }

            @Override
            public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
                super.onFailure(webSocket, t, response);
            }

            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
                chatMessages.add(new ChatMessage("Anonymous", text));
                chatMessagesUpdated.postValue(true);
            }

            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {
                super.onMessage(webSocket, bytes);
            }

            @Override
            public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
                super.onOpen(webSocket, response);
            }
        });
    }

    public void destroy()
    {
        webSocketService.getWebSocket().close(500, "Closed");
    }
}
