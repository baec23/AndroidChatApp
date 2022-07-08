package com.gausslab.websockettest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gausslab.websockettest.databinding.FragmentGlobalchatBinding;

public class GlobalChatFragment extends Fragment {

    private FragmentGlobalchatBinding binding;
    private ChatMessageRecyclerViewAdapter adapter;
    private GlobalChatViewModel globalChatViewModel;

    private RecyclerView rv_chat;
    private EditText et_inputText;
    private Button bt_submit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalChatViewModel = new ViewModelProvider(requireActivity()).get(GlobalChatViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGlobalchatBinding.inflate(inflater, container, false);
        rv_chat = binding.globalChatRvChat;
        et_inputText = binding.globalChatEtMessageInput;
        bt_submit = binding.globalChatBtSubmitMessage;

        rv_chat.setLayoutManager(new LinearLayoutManager(rv_chat.getContext()));
        adapter = new ChatMessageRecyclerViewAdapter(globalChatViewModel.getMessages());
        rv_chat.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        globalChatViewModel.isMessagesUpdated().observe(getViewLifecycleOwner(), isChanged -> {
            if(isChanged) {
                adapter.notifyDataSetChanged();
                rv_chat.scrollToPosition(globalChatViewModel.getMessages().size()-1);
            }
        });
        bt_submit.setOnClickListener(v -> {
            globalChatViewModel.sendMessage("Anonymous", et_inputText.getText().toString());
            et_inputText.setText("");
        });
    }
}