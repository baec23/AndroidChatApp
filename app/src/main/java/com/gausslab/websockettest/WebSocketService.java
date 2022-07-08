package com.gausslab.websockettest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketService extends WebSocketListener {
    private final String webSocketUrl;
    private final List<WebSocketListener> listeners = new ArrayList<>();
    private WebSocket webSocket;

    public WebSocketService(String webSocketUrl) {
        this.webSocketUrl = webSocketUrl;
        init();
    }

    public void registerListener(WebSocketListener listener) {
        listeners.add(listener);
    }

    public void deregisterListener(WebSocketListener listener) {
        listeners.remove(listener);
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    @Override
    public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
        for (WebSocketListener listener : listeners) {
            listener.onOpen(webSocket, response);
        }
    }

    @Override
    public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
        for (WebSocketListener listener : listeners) {
            listener.onMessage(webSocket, text);
        }
    }

    @Override
    public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {
        for (WebSocketListener listener : listeners) {
            listener.onMessage(webSocket, bytes);
        }
    }

    @Override
    public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
        webSocket.close(1000, null);
        for (WebSocketListener listener : listeners) {
            listener.onClosing(webSocket, code, reason);
        }
    }

    @Override
    public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
        t.printStackTrace();
    }

    private void init() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build();

        Request request = new Request.Builder()
                .url(webSocketUrl)
                .build();

        webSocket = client.newWebSocket(request, this);
        client.dispatcher().executorService().shutdown();
    }
}
