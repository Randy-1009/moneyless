package com.example.moneyless.ui.person;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import java.text.SimpleDateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.moneyless.MainActivity;
import com.example.moneyless.R;
import com.example.moneyless.data.Entity.Category;
import com.example.moneyless.data.Entity.MoneyLess;
import com.example.moneyless.login.LoginActivity;
import com.example.moneyless.servlet.HttpLogin;
import com.example.moneyless.servlet.HttpUpload;
import com.example.moneyless.utils.DateToLongUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class PersonFragment extends Fragment {

    private final static String YEAR_BUDGET = "year_budget_total", MONTH_BUDGET = "month_budget_total";
    private PersonViewModel personViewModel;
    private List<Category> categoryList;
    private SharedPreferences shp;
    private TextView tv_total_wastebook, tv_year_budget_total, tv_month_budget_total, tv_year_budget_left, tv_month_budget_left, tv_logout,synch_db;
    private TextView et_year_budget_total, et_month_budget_total;
    private ImageView imageView_AddTestSet;
    private String thisYear, thisMonth;
    private double yearTotal = 0.0, monthTotal = 0.0;
    private int y_b_shp, m_b_shp;
    private static boolean yShow = true, mShow = true;
    private final static int UPLOAD_JUDGE = 1;
    private List<MoneyLess> moneylessList;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        personViewModel =
                ViewModelProviders.of(this).get(PersonViewModel.class);

        thisYear = DateToLongUtils.getSysYear();
        thisMonth = DateToLongUtils.getSysMonth();

        View root = inflater.inflate(R.layout.fragment_person, container, false);
        tv_total_wastebook = root.findViewById(R.id.tv_total_wastebook);

        tv_year_budget_total = root.findViewById(R.id.textView_year_budget);
        tv_year_budget_total.setText("设置" + thisYear + "年预算");
        tv_month_budget_total = root.findViewById(R.id.textView_month_budget);
        tv_month_budget_total.setText("设置" + thisMonth + "月预算");
        et_year_budget_total = root.findViewById(R.id.et_year_budget_total);
        et_month_budget_total = root.findViewById(R.id.et_month_budget_total);
        tv_year_budget_left = root.findViewById(R.id.tv_year_budget_left);
        tv_month_budget_left = root.findViewById(R.id.tv_month_budget_left);

        imageView_AddTestSet = root.findViewById(R.id.imageView_AddTestSet);
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case UPLOAD_JUDGE: {
                        Bundle bundle = new Bundle();
                        bundle = msg.getData();
                        String result = bundle.getString("result");
                        //Toast.makeText(MainActivity.this,result,Toast.LENGTH_SHORT).show();
                        try {
                            if(result.equals("success")){                            Toast toastsyn = Toast.makeText(getContext(), "同步成功", Toast.LENGTH_LONG);
                                toastsyn.setGravity(Gravity.CENTER, 0, 0);
                                toastsyn.show();
                                return;
                            }else{
                                //Toast.makeText(getContext(),"同步失败", Toast.LENGTH_SHORT).show();
                                Toast toastsyn = Toast.makeText(getContext(), "同步失败", Toast.LENGTH_LONG);
                                toastsyn.setGravity(Gravity.CENTER, 0, 0);
                                toastsyn.show();
                                return;
                            }

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
            }
        };
        synch_db = root.findViewById(R.id.textView_sync);
        synch_db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean success = true;
                        String result = "fail";
                        for (Category c : categoryList){
                            if (!c.isUpload() && success){
                                String resulti = HttpUpload.UploadCategoryByPost(String.valueOf(c.getId()),String.valueOf(c.getAccountId()),String.valueOf(c.getName()),String.valueOf(c.getIcon()),String.valueOf(c.getOrder()),String.valueOf(c.isType()));
                                if (!resulti.equals("success")) {
                                    success = false;
                                }else {
                                    c.setUpload(true);
                                    personViewModel.updateCategory(c);
                                }
                            }
                        }
                        for (MoneyLess w : moneylessList) {
                            if (!w.isUpload() && success) {
                                String resulti = HttpUpload.UploadWasteBookByPost(String.valueOf(w.getId()), String.valueOf(w.getAccountId()), String.valueOf(w.getCategory_id()), String.valueOf(w.isType()), w.getIcon(), String.valueOf(w.getTime()), w.getNote(), String.valueOf(w.getAmount()), w.getCategory());
                                if (!resulti.equals("success")) {
                                    success = false;
                                }else {
                                    w.setUpload(true);
                                    personViewModel.updateWasteBook(w);
                                }
                            }
                        }
                        if(success)
                            result  = "success";
                        Bundle bundle = new Bundle();
                        bundle.putString("result",result);
                        Message message = new Message();
                        message.setData(bundle);
                        message.what = UPLOAD_JUDGE;
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });
        tv_logout = root.findViewById(R.id.textView_logout);
        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personViewModel.deleteUser();
                personViewModel.deleteAllWasteBooks();
                for(Category c:categoryList)
                {
                    if(c.getAccountId() != 0)
                        personViewModel.deleteCategoty(c);
                }
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        return root;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        shp = requireActivity().getSharedPreferences("budget", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();
        if (shp.getAll() != null) {
            y_b_shp = shp.getInt(YEAR_BUDGET, 0);
            if (y_b_shp != 0) {
                et_year_budget_total.setText(y_b_shp + "元");
            }
            m_b_shp = shp.getInt(MONTH_BUDGET, 0);
            if (m_b_shp != 0) {
                et_month_budget_total.setText(m_b_shp + "元");
            }
        }

        personViewModel.getAllWasteBookLive().observe(getViewLifecycleOwner(), new Observer<List<MoneyLess>>() {
            @Override
            public void onChanged(List<MoneyLess> moneyless) {
                tv_total_wastebook.setText(moneyless.size() + "条记录");
                if (moneyless != null) {
                    yearTotal = 0.0;
                    monthTotal = 0.0;
                    for (MoneyLess w : moneyless) {
                        String yearStart = thisYear + "-01-01 00:00:00";
                        String monthStart = thisYear + "-" + thisMonth + "-1 00:00:00";
                        if (w.getTime() >= DateToLongUtils.dateToLong(yearStart)) {
                            if (w.isType())
                                yearTotal += w.getAmount();
                            if (w.getTime() >= DateToLongUtils.dateToLong(monthStart)) {
                                if (w.isType())
                                    monthTotal += w.getAmount();
                            }
                        }
                    }
                    if (y_b_shp != 0 ) {
                        tv_year_budget_left.setText((y_b_shp - yearTotal) + "元");
                    }
                    if (m_b_shp != 0) {
                        tv_month_budget_left.setText((m_b_shp - monthTotal) + "元");
                    }
                    if(y_b_shp != 0) {
                        if ((yearTotal / y_b_shp >= 0.8) && (yearTotal / y_b_shp <= 1)) {
                            Toast.makeText(getContext(), "年预算使用已超80%", Toast.LENGTH_SHORT).show();
                        }
                        if ((yearTotal / y_b_shp >= 1) && yShow) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("警告")
                                    .setMessage("已超出年预算，请计划经济")
                                    .setNegativeButton(R.string.cancel, null);
                            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    yShow = false;
                                }
                            }).show();
                        }
                    }
                    if (m_b_shp != 0) {
                        if ((monthTotal / m_b_shp >= 0.8) && (monthTotal / m_b_shp <= 1)) {
                            Toast.makeText(getContext(), "月预算使用已超80%", Toast.LENGTH_SHORT).show();
                        }
                        if ((monthTotal / m_b_shp >= 1 && mShow)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("警告")
                                    .setMessage("已超出月预算，请注意节俭")
                                    .setNegativeButton(R.string.cancel, null);
                            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    mShow = false;
                                }
                            }).show();
                        }
                    }

                }
            }
        });

        personViewModel.getAllCategoriesLive().observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                categoryList = categories;
            }
        });
        personViewModel.getAllWasteBookUpload().observe(getViewLifecycleOwner(), new Observer<List<MoneyLess>>() {
            @Override
            public void onChanged(List<MoneyLess> moneyLesses) {
                moneylessList = moneyLesses;
            }
        });

        et_year_budget_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input = new EditText(getActivity());
                if(!et_year_budget_total.getText().toString().isEmpty())
                    input.setText(et_year_budget_total.getText().toString().substring(0, et_year_budget_total.getText().toString().length() - 1));
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("添加年预算").setView(input)
                        .setNegativeButton(R.string.cancel, null);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String tmp;
                        tmp = input.getText().toString().trim();
                        try {

                            if (!tmp.isEmpty()) {
                                et_year_budget_total.setText(Integer.parseInt(input.getText().toString()) + "元");
                                editor.putInt(YEAR_BUDGET, Integer.parseInt(input.getText().toString()));
                                editor.commit();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "请输入合法信息", Toast.LENGTH_SHORT);
                        }

                    }
                }).show();
            }
        });

        et_month_budget_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input = new EditText(getActivity());
                if(!et_month_budget_total.getText().toString().isEmpty())
                    input.setText(et_month_budget_total.getText().toString().substring(0, et_month_budget_total.getText().toString().length() - 1));
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("添加月预算").setView(input)
                        .setNegativeButton(R.string.cancel, null);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String tmp;
                        tmp = input.getText().toString().trim();
                        try {

                            if (!tmp.isEmpty()) {
                                et_month_budget_total.setText(Integer.parseInt(input.getText().toString()) + "元");
                                editor.putInt(MONTH_BUDGET, Integer.parseInt(input.getText().toString()));
                                editor.commit();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "请输入合法信息", Toast.LENGTH_SHORT);
                        }
                    }
                }).show();


            }
        });


        imageView_AddTestSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("是否添加测试数据")
                        .setNegativeButton(R.string.cancel, null);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        addTestSet();
                    }
                }).show();

            }
        });
        imageView_AddTestSet.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("是否清空数据")
                        .setNegativeButton(R.string.cancel, null);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        personViewModel.deleteAllWasteBooks();
                    }
                }).show();
                return true;
            }
        });
    }
    private void addTestSet() {
        int mount = 5;
        String note = "Just For Test";
        Random random = new Random();
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);

        for (int i = 0; i < mount * 2; i++) {
            cal.add(Calendar.DAY_OF_MONTH, -i);
            int a = random.nextInt(1000);
            MoneyLess moneyless = new MoneyLess();
            moneyless.setType(a % 2 != 0);
            setWasteBookIconName(moneyless);
            moneyless.setTime(cal.getTimeInMillis());
            moneyless.setNote(note);
            if (a % 2 != 0) a = a * 2;
            moneyless.setAmount(a + 600);
            personViewModel.insertWasteBook(moneyless);
            cal.setTime(date);
        }
        //month
        for (int i = 0; i < mount; i++) {
            cal.add(Calendar.MONTH, -i);
            int a = random.nextInt(1000);
            MoneyLess moneyless = new MoneyLess();
            moneyless.setType(a % 5 != 0);
            setWasteBookIconName(moneyless);
            moneyless.setTime(cal.getTimeInMillis());
            moneyless.setNote(note);
            if (a % 2 != 0) a = a * 2;
            moneyless.setAmount(a + 600);
            personViewModel.insertWasteBook(moneyless);
            cal.setTime(date);
        }

        for (int i = 0; i < mount / 2; i++) {
            cal.add(Calendar.YEAR, -i);
            int a = random.nextInt(1000);
            MoneyLess moneyless = new MoneyLess();
            moneyless.setType(a % 5 != 0);
            setWasteBookIconName(moneyless);
            moneyless.setTime(cal.getTimeInMillis());
            moneyless.setNote(note);
            if (a % 2 != 0) a = a * 2;
            moneyless.setAmount(a + 600);
            personViewModel.insertWasteBook(moneyless);
            cal.setTime(date);
        }

    }

    private void setWasteBookIconName(MoneyLess w) {
        if (categoryList != null) {
            Random random = new Random();
            int a = random.nextInt(categoryList.size() - 1);
            int temp = a;
            w.setIcon(categoryList.get(a).getIcon());
            w.setCategory(categoryList.get(a).getName());
            while (categoryList.get(temp).isType() != w.isType()) {
                w.setIcon(categoryList.get(a).getIcon());
                w.setCategory(categoryList.get(a).getName());
                temp = a;
                a = random.nextInt(categoryList.size() - 1);
            }
        }
    }

}