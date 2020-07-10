package com.example.moneyless.ui.add;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.moneyless.data.Entity.User;
import com.google.gson.Gson;
import com.example.moneyless.R;
import com.example.moneyless.data.Entity.Category;
import com.example.moneyless.data.Entity.MoneyLess;
import com.example.moneyless.databinding.FragmentAddBinding;
import com.example.moneyless.ui.category.CategoryActivity;
import com.example.moneyless.ui.detail.EditFragment;
import com.wihaohao.PageGridView;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "AddFragment_number";
    private AddViewModel addViewModel;
    private FragmentAddBinding binding;
    private List<MyIconModel> mList, mList2;
    private LiveData<List<Category>> allCategoriesLive;
    private LiveData<List<User>> allUsersLive;
    private List<Category> allCategories;
    private List<User> allUsers;
    private PageGridView<MyIconModel> mPageGridView;
    private MoneyLess moneyLess;
    private DecimalFormat mAmountFormat = new DecimalFormat("0.00");
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");

    public static AddFragment newInstance(int index) {
        AddFragment fragment = new AddFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addViewModel = ViewModelProviders.of(this).get(AddViewModel.class);
//        int index = 1;
//        if (getArguments() != null) {
//            index = getArguments().getInt(ARG_SECTION_NUMBER);
//        }
//        addViewModel.setIndex(index);

        allCategoriesLive = addViewModel.getAllCategoriesLive();
        allCategoriesLive.observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                allCategories = categories;
                if (categories != null && !categories.isEmpty()) {
                    initData();
                    int index = 1;
                    if (getArguments() != null) {
                        index = getArguments().getInt(ARG_SECTION_NUMBER);
                    }
                    addViewModel.setIndex(index);
//                for(Category c:allCategories)
//                    Log.e("xxxxAddFragment",c.getName()+" "+c.getIcon());
                }
            }
        });
        allUsersLive = addViewModel.getAllUsersLive();
        allUsersLive.observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                allUsers = users;
            }
        });

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Gson gson = new Gson();
        Intent intent = getActivity().getIntent();
        String wasteBookJson = intent.getStringExtra(EditFragment.WASTEBOOK_EDIT);
//        String wasteBookJson = getArguments().getString(EditFragment.WASTEBOOK_EDIT);
        if (wasteBookJson != null) {
            moneyLess = gson.fromJson(wasteBookJson, MoneyLess.class);
            Log.e("xxxx", wasteBookJson);
            addViewModel.setWasteBook(moneyLess);
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add, container, false);
        binding.setVm(addViewModel);
        binding.setActivity(getActivity());
        binding.setLifecycleOwner(getViewLifecycleOwner());

        initData();
        // initData(addViewModel.getAllCategories());
        addViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Log.e("AddFragment", s);
                if (s.contains("1")) {
                    mPageGridView = binding.getRoot().findViewById(R.id.vp_grid_view);
                    mPageGridView.setData(mList);
                    mPageGridView.setOnItemClickListener(new PageGridView.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            if (position == mList.size() - 1) {
                                startActivity(new Intent(requireActivity(), CategoryActivity.class));
                            } else {
                                if (addViewModel.getWasteBookEdit() == null) {
                                    addViewModel.setmAmountTextClear();
                                }
                                addViewModel.setcategoryid(mList.get(position).getId());
                                addViewModel.setuid(mList.get(position).getuId());
                                addViewModel.setType(mList.get(position).getName());
                                addViewModel.setIconId(mList.get(position).getIconName());
                                //mList.get(position).getId();

                                ImageView imageView = binding.getRoot().findViewById(R.id.imageView_AddFragment_category);
                                imageView.setImageDrawable(requireContext().getDrawable(mList.get(position).getIconId()));
                            }
                        }
                    });

                }
                if (s.contains("2")) {
                    mPageGridView = binding.getRoot().findViewById(R.id.vp_grid_view);
                    mPageGridView.setData(mList2);
                    mPageGridView.setOnItemClickListener(new PageGridView.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            if (position == mList2.size() - 1) {
                                //跳转
                                startActivity(new Intent(requireActivity(), CategoryActivity.class));
                            } else {
                                if (addViewModel.getWasteBookEdit() == null) {
                                    addViewModel.setmAmountTextClear();
                                }
                                addViewModel.setType(mList2.get(position).getName());
                                addViewModel.setIconId(mList2.get(position).getIconName());
                                ImageView imageView = binding.getRoot().findViewById(R.id.imageView_AddFragment_category);
                                imageView.setImageDrawable(requireContext().getDrawable(mList2.get(position).getIconId()));
                            }
                        }
                    });
                }
            }
        });
        return binding.getRoot();
    }


    private void initData() {
        mList = new ArrayList<>();
        mList2 = new ArrayList<>();
        if (allCategories != null&& !allCategories.isEmpty())
            for (Category c : allCategories) {
                if (c.isType())
                    mList.add(new MyIconModel(c.getName(), c.getIcon(), getDrawableId(c.getIcon()), c.getId(), allUsers.get(0).getId()));
                if (!c.isType())
                    mList2.add(new MyIconModel(c.getName(), c.getIcon(), getDrawableId(c.getIcon()), c.getId(), allUsers.get(0).getId()));
            }
        mList.add(new MyIconModel("设置", getDrawableId("ic_category_setting")));
        mList2.add(new MyIconModel("设置", getDrawableId("ic_category_setting")));
    }

    private int getDrawableId(String iconName) {
        Field field = null;
        int res_ID;
        try {
            field = R.drawable.class.getField(iconName);
            res_ID = field.getInt(field.getName());
        } catch (NoSuchFieldException e) {
            res_ID = R.drawable.ic_category_out_1;
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            res_ID = R.drawable.ic_category_out_1;
            e.printStackTrace();
        }
        return res_ID;
    }

}