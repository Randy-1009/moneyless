package com.example.moneyless.data.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "MoneyLess")
public class MoneyLess {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    /** 用户 ID */
    @ColumnInfo(name = "user_id")
    private long accountId;

    @ColumnInfo(name = "amount")
    private double amount;
    //false:支出、true:收入
    @ColumnInfo(name = "type")
    private boolean type;
    //具体类型
    @ColumnInfo(name = "category_id")
    private long category_id;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "icon")
    private String icon;
    //时间
    @ColumnInfo(name = "create_datetime")
    private long time;
    //备注
    @ColumnInfo(name = "note")
    private String note;
    //同步标志位
    @ColumnInfo(name = "upload")
    private boolean upload;

    public MoneyLess( boolean type, boolean upload , double amount,String category,String icon, long time, String note) {
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.icon = icon;
        this.time = time;
        this.note = note;
        this.upload = upload;
    }
    @Ignore
    public MoneyLess(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(long category_id) {
        this.category_id = category_id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isUpload() { return upload; }

    public void setUpload(boolean upload) { this.upload = upload; }

}
