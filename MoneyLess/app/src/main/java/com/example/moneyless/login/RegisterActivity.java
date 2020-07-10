package com.example.moneyless.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.moneyless.servlet.HttpLogin;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.moneyless.R;
import com.example.moneyless.data.Entity.User;

import java.util.List;


public class RegisterActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private Button btGo;
    private CardView cvAdd;
    private EditText etUsername;
    private EditText etPassword, etSecondPassword;
    private int ResultCode = 2;
    private final static int REGISTER_JUDGE = 2;
    private LoginViewModel loginViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ShowEnterAnimation();

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                switch (msg.what){
                    case REGISTER_JUDGE:{
                        Bundle bundle = new Bundle();
                        bundle = msg.getData();
                        String result = bundle.getString("result");
                        //Toast.makeText(MainActivity.this,result,Toast.LENGTH_SHORT).show();
                        try {
                            if (result.equals("success")) {
                                Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                                animateRevealClose();
                                return;
                            } else if(result.equals("")){
                                Toast.makeText(RegisterActivity.this, "无法连接服务器", Toast.LENGTH_SHORT).show();
                                animateRevealClose();
                                return;
                            } else {
                                Toast.makeText(RegisterActivity.this, "注册失败，可能用户已存在", Toast.LENGTH_SHORT).show();
                                animateRevealClose();
                                return;
                            }
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }
                    }
                    break;
                }
            }
        };


        initView();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateRevealClose();
            }
        });
        btGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String secondPassword = etSecondPassword.getText().toString().trim();
                Log.e("RegisterActivity", userName + " " + password + " " + secondPassword);
                if (userName.isEmpty() || password.isEmpty() || secondPassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "请输入完整的注册信息！", Toast.LENGTH_SHORT).show();
                } else {
                    if (!password.equals(secondPassword)) {
                        Toast.makeText(RegisterActivity.this, "重复密码错误！", Toast.LENGTH_SHORT).show();
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String result = HttpLogin.RegisterByPost(userName, password);
                                Bundle bundle = new Bundle();
                                bundle.putString("result",result);
                                Message msg = new Message();
                                msg.what = REGISTER_JUDGE;
                                msg.setData(bundle);
                                handler.sendMessage(msg);
                            }
                        }).start();
                    }
                }
            }
        });
    }

    private void initView() {
        fab = findViewById(R.id.fab);
        cvAdd = findViewById(R.id.cv_add);
        btGo = findViewById(R.id.bt_go);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etSecondPassword = findViewById(R.id.et_repeatpassword);
    }

    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                cvAdd.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }


        });
    }

    public void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, fab.getWidth() / 2, cvAdd.getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                cvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    public void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, cvAdd.getHeight(), fab.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                fab.setImageResource(R.drawable.plus);
                RegisterActivity.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    @Override
    public void onBackPressed() {
        animateRevealClose();
    }
}
