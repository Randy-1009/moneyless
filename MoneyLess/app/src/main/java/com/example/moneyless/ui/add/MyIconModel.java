package com.example.moneyless.ui.add;

import android.view.View;
import android.widget.ImageView;

import com.wihaohao.PageGridView;

public class MyIconModel implements PageGridView.ItemModel {
    private String name;
    private String iconName;
    private int iconId;
    private long id;
    private String uid;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getuId() {
        return uid;
    }

    public void setuId(String uid) {
        this.uid = uid;
    }



/*
    public MyIconModel(String name, String iconName, int iconId) {
        this.name = name;
        this.iconName = iconName;
        this.iconId = iconId;

    }
*/
    public MyIconModel(String name, String iconName, int iconId, long id, String uid) {
        this.name = name;
        this.iconName = iconName;
        this.iconId = iconId;
        this.id = id;
        this.uid = uid;
    }


    public MyIconModel(String name, int iconId) {
        this.name = name;
        this.iconId = iconId;
    }

    public String getIconName() {
        return iconName;
    }

    @Override
    public String getItemName() {
        return name;
    }

    @Override
    public void setIcon(ImageView imageView) {
        imageView.setImageResource(iconId);
    }

    @Override
    public void setItemView(View itemView) {

    }
}

