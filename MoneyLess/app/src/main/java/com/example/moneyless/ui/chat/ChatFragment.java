package com.example.moneyless.ui.chat;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.moneyless.R;
import com.example.moneyless.data.Entity.MoneyLess;
import com.example.moneyless.ui.add.AddActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatFragment extends Fragment {
    private ChatViewModel chatViewModel;
    private List<ChatMessage> list;
    private LiveData<List<MoneyLess>> moneyless;
    private ListView chat_listview;
    private EditText chat_input;
    private Button chat_send;
    private ChatMessageAdapter chatAdapter;
    private ChatMessage chatMessage = null;
    private View root;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // initView(root);
        chatViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
        if (root != null) {
            ViewGroup parent = (ViewGroup) root.getParent();
            if (parent != null) {
                parent.removeView(root);
            }
        } else {
            root = inflater.inflate(R.layout.fragment_chat, container, false);
            initView(root);// 控件初始化
            final FloatingActionButton floatingActionButton = root.findViewById(R.id.floatingActionButton_detail);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), AddActivity.class);
                    startActivity(intent);
                }
            });
        }
        return root;
    }



    private void initView(View v){
        // 1.初始视图
        chat_listview = (ListView) v.findViewById(R.id.chat_listview);
        chat_input = (EditText) v.findViewById(R.id.chat_input_message);
        chat_send = (Button) v.findViewById(R.id.chat_send);

        // 2.设置监听事件
        chat_send.setOnClickListener(onClickListener);

        // 3.初始化数据
        list = new ArrayList<ChatMessage>();
        list.add(new ChatMessage("呐呐呐，私の名前は 少少", ChatMessage.Type.INCOUNT, new Date()));
        chatAdapter = new ChatMessageAdapter(list);
        chat_listview.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();

        //chat_input.setText("你好呀");
        //chat();
        //chat_input.setText("");

        moneyless = chatViewModel.getAllWasteBookLive();
        moneyless.observe(getViewLifecycleOwner(), new Observer<List<MoneyLess>>() {
            @Override
            public void onChanged(List<MoneyLess> moneyless) {
                for (MoneyLess w : moneyless) {
                    setData(w.getCategory());
                    String price = String.valueOf(w.getAmount());
                    setData("在".concat(w.getCategory()).concat("上用了").concat(price).concat("元"));
                    chat();
                    setData("");
                    break;
                }
            }
        });


    }

    // 4.发送消息聊天
    private void chat() {
        // 1.判断是否输入内容
        final String send_message = chat_input.getText().toString().trim();
        if (TextUtils.isEmpty(send_message)) {
            Toast.makeText(getContext(), "对不起，您还未编辑任何消息",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // 2.自己输入的内容也是一条记录，记录刷新
        ChatMessage sendChatMessage = new ChatMessage();
        sendChatMessage.setMessage(send_message);
        sendChatMessage.setData(new Date());
        sendChatMessage.setType(ChatMessage.Type.OUTCOUNT);
        list.add(sendChatMessage);
        chatAdapter.notifyDataSetChanged();
        chat_input.setText("");

        // 3.发送你的消息，去服务器端，返回数据
        new Thread() {
            public void run() {
                ChatMessage chat = HttpUtils.sendMessage(send_message);
                Message message = new Message();
                message.what = 0x1;
                message.obj = chat;
                handler.sendMessage(message);
            };
        }.start();

    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0x1) {
                if (msg.obj != null) {
                    chatMessage = (ChatMessage) msg.obj;
                }
                // 添加数据到list中，更新数据
                list.add(chatMessage);
                //setData(chatMessage.getMessage());
                chatAdapter.notifyDataSetChanged();
            }
        };
    };

    public void setData(String string) {
        chat_input.setText(string);
    }

    // 点击事件监听
    OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.chat_send:
                    chat();
                    break;
            }
        }
    };



}
