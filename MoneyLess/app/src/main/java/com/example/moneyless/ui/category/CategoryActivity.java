package com.example.moneyless.ui.category;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import com.example.moneyless.data.Entity.User;
import com.google.android.material.tabs.TabLayout;
import com.example.moneyless.R;

import java.util.List;


public class CategoryActivity extends AppCompatActivity {

    private List<User> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        PagerAdapter sectionsPagerAdapter = new PagerAdapter(this,getSupportFragmentManager());
        ViewPager viewPager1 = findViewById(R.id.view_pager_category);
        viewPager1.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs_category);
        tabs.setupWithViewPager(viewPager1);

    }

}
