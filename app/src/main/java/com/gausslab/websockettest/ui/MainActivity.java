package com.gausslab.websockettest.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.gausslab.websockettest.R;
import com.gausslab.websockettest.repository.ChatMessageRepository;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChatMessageRepository.getInstance().destroy();
    }
}