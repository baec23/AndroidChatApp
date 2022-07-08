package com.gausslab.websockettest;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketEcho extends WebSocketListener {

    private final String webSocketUrl = "ws://10.0.2.2:8080/name";

    public void run() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build();

        Request request = new Request.Builder()
                .url(webSocketUrl)
                .build();

        client.newWebSocket(request, this);
        client.dispatcher().executorService().shutdown();

    }

    @Override
    public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
        Log.d("DEBUG", "onOpen");
        webSocket.send("Hello...");
        webSocket.send("...World!");
    }

    @Override
    public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
        Log.d("DEBUG", "MESSAGE" + text);
    }

    @Override
    public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {
        Log.d("DEBUG", "MESSAGE" + bytes.hex());
    }

    @Override
    public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
        webSocket.close(1000, null);
        Log.d("DEBUG", "CLOSE: " + code + " " + reason);
    }

    @Override
    public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
        t.printStackTrace();
    }
}
