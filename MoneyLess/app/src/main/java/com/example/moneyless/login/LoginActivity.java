package com.example.moneyless.login;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.transition.Explode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.moneyless.data.Entity.Category;
import com.example.moneyless.servlet.HttpGet;
import com.example.moneyless.servlet.HttpLogin;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.moneyless.MainActivity;
import com.example.moneyless.R;
import com.example.moneyless.data.Entity.User;
import com.example.moneyless.data.Entity.MoneyLess;
import java.util.List;


public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btGo;
    private CardView cv;
    private FloatingActionButton fab;
    private final static int LOGIN_JUDGE = 1;
    private LoginViewModel loginViewModel;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.getAllUserLive().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                if (users != null) {
                    userList = users;
                }
            }
        });

        initView();
        setListener();
    }
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOGIN_JUDGE: {
                    Bundle bundle = new Bundle();
                    bundle = msg.getData();
                    String result = bundle.getString("result");
                    String wastebookdata =  bundle.getString("wastebookdata");
                    String categorydata =  bundle.getString("categorydata");
                    if(result.equals("")) {
                        Toast.makeText(getApplicationContext(), "无法连接服务器!", Toast.LENGTH_SHORT).show();
                        return ;
                    }
                    String[] splited = result.split(" ");
                    result = splited[0];
                    String id = splited[1];

                    //Toast.makeText(MainActivity.this,result,Toast.LENGTH_SHORT).show();
                    try {
                        if (result.equals("success")) {
                            Explode explode = new Explode();
                            explode.setDuration(500);
                            getWindow().setExitTransition(explode);
                            getWindow().setEnterTransition(explode);
                            User user = new User(id, etPassword.getText().toString().trim(), etUsername.getText().toString().trim());
                            loginViewModel.insertUser(user);
                            if (!wastebookdata.equals("false")&&!wastebookdata.equals("")){
                                String[] wastebooks = wastebookdata.split("&&&");
                                for (String s : wastebooks)
                                {
                                    String[] wastes = s.split(" ");
                                    MoneyLess moneyLess = new MoneyLess();
                                    moneyLess.setId(Integer.valueOf(wastes[0]));
                                    moneyLess.setAccountId(Long.valueOf(wastes[1]));
                                    moneyLess.setCategory_id(Long.valueOf(wastes[2]));
                                    moneyLess.setType(Boolean.valueOf(wastes[3]));
                                    moneyLess.setIcon(wastes[4]);
                                    moneyLess.setTime(Long.valueOf(wastes[5]));
                                    moneyLess.setNote(wastes[6]);
                                    moneyLess.setAmount(Double.valueOf(wastes[7]));
                                    moneyLess.setCategory(wastes[8]);
                                    moneyLess.setUpload(true);
                                    loginViewModel.insertWasteBook(moneyLess);
                                }
                            }
                            if (!categorydata.equals("false")&&!categorydata.equals("")){
                                String[] categories = categorydata.split("&&&");
                                for (String c : categories)
                                {
                                    String[] wastes = c.split(" ");
                                    Category category = new Category();
                                    category.setId(Integer.valueOf(wastes[0]));
                                    category.setAccountId(Long.valueOf(wastes[1]));
                                    category.setName(wastes[2]);
                                    category.setIcon(wastes[3]);
                                    category.setOrder(Integer.valueOf(wastes[4]));
                                    category.setType(Boolean.valueOf(wastes[5]));
                                    category.setUpload(true);
                                    loginViewModel.insertCategory(category);
                                }
                            }
                            ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivity.this);
                            Intent i2 = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i2, oc2.toBundle());


                            Toast.makeText(getApplicationContext(), "登录成功!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(getApplicationContext(), "登录失败，账号或密码错误!", Toast.LENGTH_SHORT).show();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
    };
    private void initView() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btGo = findViewById(R.id.bt_go);
        cv = findViewById(R.id.cv);
        fab = findViewById(R.id.fab);
    }

    private void setListener() {
        btGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etPassword.getText().toString().trim().isEmpty() || etUsername.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "请输入完整的信息!", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //使用下面类里的函数，连接servlet，返回一个result，使用handler处理这个result
                            String result = HttpLogin.LoginByPost(etUsername.getText().toString().trim(),etPassword.getText().toString().trim());
                            if(result.equals("")) {
                                Toast.makeText(getApplicationContext(), "无法连接服务器!", Toast.LENGTH_SHORT).show();
                                return ;
                            }
                            String[] splited = result.split(" ");
                            String id = splited[1];
                            String wastebookdata = "";
                            String categorydata = "";
                            Bundle bundle = new Bundle();
                            if (!id.equals(-1)){
                                wastebookdata = HttpGet.getwastebook(id);
                                categorydata = HttpGet.getcategory(id);
                            }
                            bundle.putString("result",result);
                            bundle.putString("wastebookdata",wastebookdata);
                            bundle.putString("categorydata",categorydata);
                            Message message = new Message();
                            message.setData(bundle);
                            message.what = LOGIN_JUDGE;
                            handler.sendMessage(message);
                        }
                    }).start();
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, fab, fab.getTransitionName());
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class), options.toBundle());
            }
        });
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onRestart() {
        super.onRestart();
        fab.setVisibility(View.GONE);
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onResume() {
        super.onResume();
        fab.setVisibility(View.VISIBLE);
    }
}
