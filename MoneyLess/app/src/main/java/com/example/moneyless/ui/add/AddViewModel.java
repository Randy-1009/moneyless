package com.example.moneyless.ui.add;

import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.arch.core.util.Function;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.moneyless.MainActivity;
import com.example.moneyless.R;
import com.example.moneyless.data.CategoryRepository;
import com.example.moneyless.data.Entity.Category;
import com.example.moneyless.data.Entity.MoneyLess;
import com.example.moneyless.data.Entity.User;
import com.example.moneyless.data.UserRepository;
import com.example.moneyless.data.WasteBookRepository;
import com.example.moneyless.utils.CommonUtils;
import com.example.moneyless.utils.DatePickUtils;
import com.example.moneyless.utils.DateToLongUtils;
import com.example.moneyless.utils.ResUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddViewModel extends AndroidViewModel {
    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private SimpleDateFormat mDateFormat;
    private DecimalFormat mAmountFormat = new DecimalFormat("0.00");
    private ObservableField<String> mDateText = new ObservableField<>();
    private ObservableField<String> mAmountText = new ObservableField<>();
    private ObservableField<String> mDesc = new ObservableField<>();
    private ObservableField<String> mType = new ObservableField<>();
    private ObservableField<String> umId = new ObservableField<>();
    private ObservableField<Long> mId= new ObservableField<>();
    private String iconId;
    private long mDate;
    private double mAmount;
    private WasteBookRepository wasteBookRepository;
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;
    private MoneyLess moneyLessEdit;
    private List<User> userList;
    public AddViewModel(@NonNull Application application) {
        super(application);
        mDateFormat = new SimpleDateFormat(ResUtils.getString(
                application, R.string.date_format_y_m_d) + " HH:mm", Locale.getDefault());
        mDate = System.currentTimeMillis();
        mDateText.set(mDateFormat.format(new Date(mDate)));
        wasteBookRepository = new WasteBookRepository(application);
        categoryRepository = new CategoryRepository(application);
        userRepository = new UserRepository(application);
    }


    /**
     * 消费说明点击
     */
    public void onDescClick(Activity activity) {

        EditText input = new EditText(activity);
        input.setText(getDesc().get());
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("添加备注").setView(input)
                .setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String tmp;
                tmp = input.getText().toString().trim();
                if (!tmp.isEmpty()) {
                    setmDesc(tmp);
                }
            }
        }).show();
    }

    /**
     * 消费记录时间点击
     */
    public void onDateClick(Activity activity) {
        DatePickUtils.showDatePickDialog(activity,
                mDate,
                new DatePickUtils.OnDatePickListener() {
                    @Override
                    public void onDatePick(DialogInterface dialog,
                                           int year,
                                           int month,
                                           int dayOfMonth) {
                    }

                    @Override
                    public void onConfirmClick(DialogInterface dialog, long timeInMills) {
                        mDate = timeInMills;
                        mDateText.set(mDateFormat.format(new Date(mDate)));
                    }
                });
    }

    /**
     * 键盘数字点击
     */
    public void onNumberClick(String number) {
        String amount = mAmountText.get();
        amount = TextUtils.isEmpty(amount) ? "" : amount;
        if ("0".equals(amount)) {
            amount = "";
        }
        amount += number;
        mAmountText.set(amount);
        mAmount = CommonUtils.string2float(amount, 0);
    }

    /**
     * 键盘删除点击
     */
    public void onDeleteClick() {
        String amount = mAmountText.get();
        amount = TextUtils.isEmpty(amount) ? "" : amount;
        if (!TextUtils.isEmpty(amount)) {
            amount = amount.substring(0, amount.length() - 1);
        }
        if (TextUtils.isEmpty(amount)) {
            amount = "0";
        }
        mAmountText.set(amount);
        mAmount = CommonUtils.string2float(amount, 0);
    }

    /**
     * 键盘清除点击
     */
    public void onClearClick() {
        mAmountText.set("0");
        mAmount = 0;
    }

    /**
     * 键盘 . 点击
     */
    public void onDotClick() {
        String amount = mAmountText.get();
        amount = TextUtils.isEmpty(amount) ? "" : amount;
        if (!amount.contains(".")) {
            amount = amount + ".";
        }
        mAmountText.set(amount);
        mAmount = CommonUtils.string2float(amount, 0);
    }

    /**
     * 确定点击
     */
    public void onEnterClick(Activity activity) {
        if (getType().get() == null || getAmountText().get() == null || getAmountText().get().isEmpty() || iconId == null) {
            Toast.makeText(activity, "请输入完整的信息", Toast.LENGTH_SHORT).show();
        } else {
            Boolean wasteBookType = true;
            if (getText().getValue().contains("2")) wasteBookType = false;
            Log.e("getDesc", getDesc().get());
            if (moneyLessEdit != null) {
                moneyLessEdit.setAmount(Double.parseDouble(getAmountText().get()));
                moneyLessEdit.setCategory_id(mId.get());
                moneyLessEdit.setIcon(iconId);
                moneyLessEdit.setCategory(getType().get());
                moneyLessEdit.setNote(getDesc().get());
                moneyLessEdit.setTime(mDate);
                moneyLessEdit.setType(wasteBookType);
                updateDate(moneyLessEdit);
                Toast.makeText(activity, "修改成功!", Toast.LENGTH_SHORT).show();
            } else {
                MoneyLess moneyLess = new MoneyLess(wasteBookType, false ,Double.parseDouble(getAmountText().get()), getType().get(), iconId, mDate, getDesc().get());
                moneyLess.setCategory_id(mId.get());
                moneyLess.setAccountId(Long.valueOf(umId.get()));
                saveData(moneyLess);
            }
            //activity.finish();
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
        }
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    private void updateDate(MoneyLess moneyLess) {
        Log.e("xxxxxx", "update");
        wasteBookRepository.updateWasteBook(moneyLess);
    }

    private void saveData(MoneyLess moneyLess) {
        Log.e("xxxxxx", "insert");
        wasteBookRepository.insertWasteBook(moneyLess);
    }

    public LiveData<List<Category>> getAllCategoriesLive() {
        return categoryRepository.getAllCategoriesLive();
    }

    public LiveData<List<User>> getAllUsersLive() {
        return userRepository.getAllUsersLive();
    }
    public ObservableField<String> getDateText() {
        return mDateText;
    }

    public ObservableField<String> getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType.set(mType);
    }

    public void setcategoryid(long id) {
        this.mId.set(id);
    }
    public void setuid(String uid) {
        this.umId.set(uid);
    }
    public ObservableField<String> getAmountText() {
        return mAmountText;
    }

    public void setmAmountTextClear() {
        this.mAmountText.set("");
    }

    public ObservableField<String> getDesc() {
        if (mDesc.get() == null)
            mDesc.set("");
        return mDesc;
    }

    //pager data
    private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            return "index:" + input;
        }
    });

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setmDateText(String mDateText) {
        this.mDateText.set(mDateText);
    }

    public void setmDesc(String mDesc) {
        this.mDesc.set(mDesc);
    }

    public void setmAmountText(String mAmountText) {
        this.mAmountText.set(mAmountText);
    }

    public MoneyLess getWasteBookEdit() {
        return moneyLessEdit;
    }

    public void setWasteBook(MoneyLess moneyLess) {
        this.moneyLessEdit = moneyLess;
        this.setType(moneyLess.getCategory());
        this.setmAmountText(mAmountFormat.format(moneyLess.getAmount()));
        this.setmDesc(moneyLess.getNote());
        this.setmDateText(DateToLongUtils.longToDateAdd(moneyLess.getTime()));
        this.setIconId(moneyLess.getIcon());
    }
}