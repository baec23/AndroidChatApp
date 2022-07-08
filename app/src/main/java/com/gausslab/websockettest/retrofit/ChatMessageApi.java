package com.gausslab.websockettest.retrofit;

import com.gausslab.websockettest.model.ChatMessage;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ChatMessageApi {

    @GET("/api/recentMessages")
    Call<List<ChatMessage>> getRecentChatMessages();
}
