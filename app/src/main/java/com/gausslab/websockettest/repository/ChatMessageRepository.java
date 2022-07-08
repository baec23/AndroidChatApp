package com.gausslab.websockettest.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gausslab.websockettest.service.WebSocketService;
import com.gausslab.websockettest.model.ChatMessage;
import com.gausslab.websockettest.retrofit.ChatMessageApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatMessageRepository {

    private static ChatMessageRepository instance = new ChatMessageRepository();

    private MutableLiveData<Boolean> doneLoading = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> chatMessagesUpdated = new MutableLiveData<>(false);

    private Executor executor = Executors.newSingleThreadExecutor();
    private List<ChatMessage> chatMessages;
    private ChatMessageApi chatMessageApi;
    private WebSocketService webSocketService;
    private Gson gson;

    private ChatMessageRepository() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        initRetrofit();
        initWebSocket();
        loadRecentMessages();
    }

    public static ChatMessageRepository getInstance() {
        return instance;
    }

    public void sendMessage(ChatMessage toSend) {
        webSocketService.getWebSocket().send(toSend.toJson());
    }

    public LiveData<Boolean> isChatMessagesUpdated() {
        return chatMessagesUpdated;
    }

    public List<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    private void loadRecentMessages() {
        executor.execute(() -> {
            chatMessageApi.getRecentChatMessages().enqueue(new Callback<List<ChatMessage>>() {
                @Override
                public void onResponse(Call<List<ChatMessage>> call, retrofit2.Response<List<ChatMessage>> response) {
                    chatMessages = response.body();
                    doneLoading.postValue(true);
                }

                @Override
                public void onFailure(Call<List<ChatMessage>> call, Throwable t) {
                    chatMessages = new ArrayList<>();
                    doneLoading.postValue(true);
                }
            });
        });
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        chatMessageApi = retrofit.create(ChatMessageApi.class);
    }

    private void initWebSocket() {
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
                ChatMessage receivedMessage = gson.fromJson(text, ChatMessage.class);
                chatMessages.add(receivedMessage);
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

    public void destroy() {
        webSocketService.getWebSocket().close(500, "Closed");
    }

    public LiveData<Boolean> isDoneLoading() {
        return doneLoading;
    }
}
