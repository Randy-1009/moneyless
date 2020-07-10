package com.example.moneyless.ui.category;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneyless.data.Entity.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.example.moneyless.R;
import com.example.moneyless.adapters.CategoryDragTouchAdapter;
import com.example.moneyless.data.Entity.Category;
import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yanzhenjie.recyclerview.touch.OnItemMoveListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


public class CategoryFragment extends Fragment {


    private static final String ARG_SECTION_NUMBER = "Category_number";

    private CategoryViewModel categoryViewModel;
    private LiveData<List<User>> allUsersLive;
    private LiveData<List<Category>> allCategoriesLive;
    private List<User> userList;
    private List<Category> allCategories, INCategory, OUTCategory;
    private SwipeRecyclerView mRecyclerView, mRecyclerView2;
    private CategoryDragTouchAdapter adapter, adapter2;
    //    private static boolean isInitCategory=false;
    private FloatingActionButton floatingActionButton;


    public static CategoryFragment newInstance(int index) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        categoryViewModel.setIndex(index);
        allCategoriesLive = categoryViewModel.getAllCategoriesLive();
        allCategoriesLive.observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                allCategories = categories;
                //if(categories.size()==0&&!isInitCategory)initCategory();
            }
        });
        allUsersLive = categoryViewModel.getAllUserLive();
        allUsersLive.observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                if (users != null) {
                    userList = users;
                }
            }
        });
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_category, container, false);

        categoryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@NonNull String s) {
                Log.e("CategoryFragment", s);
                if (s.contains("1")) {
                    mRecyclerView = root.findViewById(R.id.recyclerView_category);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                    mRecyclerView.setLongPressDragEnabled(true); // 长按拖拽，默认关闭。
                    mRecyclerView.setItemViewSwipeEnabled(true); // 滑动删除，默认关闭。
//                    mRecyclerView.setLongClickable(true);    //长按删除
                    adapter = new CategoryDragTouchAdapter(requireContext(), mRecyclerView);
                    mRecyclerView.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int adapterPosition) {
                            String temp = OUTCategory.get(adapterPosition).getName();
                            if (temp != null && !temp.isEmpty()) {
                                Log.e("xxxx", temp);
                                Intent intent = new Intent(getContext(), CategoryWasteBookFragment.class);
                                intent.putExtra(CategoryWasteBookFragment.ARG_PARAM1, temp);
                                startActivity(intent);
                                //Navigation.findNavController(get).navigate(R.id.action_categoryFragment_to_categoryWasteBookFragment, bundle);
//                                getFragmentManager()
//                                        .beginTransaction()
//                                        //.addToBackStack(null)  //将当前fragment加入到返回栈中
//                                        .replace(R.id.fragment_wb,  CategoryWasteBookFragment.newInstance(temp)).commit();
                            }
                        }
                    });

                    mRecyclerView.setAdapter(adapter);

                    //感知数据更新
                    allCategoriesLive.observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
                        @Override
                        public void onChanged(List<Category> categories) {
                            OUTCategory = new ArrayList<>();
                            for (Category c : categories) {
                                if (c.isType()) {
                                    OUTCategory.add(c);
                                }
                            }
                            if (adapter.getItemCount() != OUTCategory.size()) {
                                adapter.setAllCategory(OUTCategory);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });


                    //移动排序
                    mRecyclerView.setOnItemMoveListener(new OnItemMoveListener() {

                        @Override
                        public boolean onItemMove(RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {
                            if (srcHolder.getItemViewType() != targetHolder.getItemViewType())
                                return false;
                            int fromPosition = srcHolder.getAdapterPosition();
                            int toPosition = targetHolder.getAdapterPosition();
                            Collections.swap(OUTCategory, fromPosition, toPosition);
                            adapter.notifyItemMoved(fromPosition, toPosition);

//                            for (Category c:OUTCategory)
//                                Log.e("xxxxposition",c.getName()+" "+c.getOrder());
                            for (int i = 0; i < OUTCategory.size(); i++) {
                                Category category = OUTCategory.get(i);
                                category.setOrder(i);
                                categoryViewModel.updateCategory(category);
                            }
                            return true;// 返回true表示处理了并可以换位置，返回false表示你没有处理并不能换位置。
                        }

                        @Override
                        public void onItemDismiss(RecyclerView.ViewHolder srcHolder) {
                            int position = srcHolder.getAdapterPosition();
                            Category toDelete = OUTCategory.get(position);
                            OUTCategory.remove(position);
                            adapter.notifyItemRemoved(position);

                            categoryViewModel.deleteCategory(toDelete);
                            //Toast.makeText(CategoryActivity.this, "现在的第" + position + "条被删除。", Toast.LENGTH_SHORT).show();
                            Snackbar.make(root.findViewById(R.id.fragment_category), "已删除一个分类", Snackbar.LENGTH_SHORT).setAction("撤销", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    categoryViewModel.insertCategory(toDelete);
                                }
                            }).show();
                        }
                    });

                    floatingActionButton = root.findViewById(R.id.floatingActionButton_category);

                    floatingActionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditText input = new EditText(getContext());
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("添加新类型").setView(input)
                                    .setNegativeButton(R.string.cancel, null);
                            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String tmp;
                                    tmp = input.getText().toString().trim();
                                    if (!tmp.isEmpty()) {
                                        Category category = new Category();
                                        category.setOrder(allCategories.size() + 1);
                                        category.setName(tmp);
                                        category.setType(true);
                                        category.setIcon("ic_category_out_16");
                                        category.setUpload(false);
                                        category.setAccountId(Long.valueOf(userList.get(0).getId()));
                                        try {
                                            categoryViewModel.insertCategory(category);
                                        } catch (Exception e) {
                                            Toast.makeText(getContext(), "添加失败", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }
                            }).show();
                        }
                    });
                }
                if (s.contains("2")) {

                    mRecyclerView2 = root.findViewById(R.id.recyclerView_category);
                    mRecyclerView2.setLayoutManager(new LinearLayoutManager(requireContext()));
                    mRecyclerView2.setLongPressDragEnabled(true); // 长按拖拽，默认关闭。
                    mRecyclerView2.setItemViewSwipeEnabled(true); // 滑动删除，默认关闭。
                    adapter2 = new CategoryDragTouchAdapter(requireContext(), mRecyclerView2);
                    mRecyclerView2.setAdapter(adapter2);

                    //读取数据库数据
                    //感知数据更新
                    allCategoriesLive.observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
                        @Override
                        public void onChanged(List<Category> categories) {
                            INCategory = new ArrayList<>();
                            for (Category c : categories) {
                                if (!c.isType()) {
                                    INCategory.add(c);
                                }
                            }
                            if (adapter2.getItemCount() != INCategory.size()) {
                                adapter2.setAllCategory(INCategory);
                                adapter2.notifyDataSetChanged();
                            }
                        }
                    });

//                    mRecyclerView2.setOnItemClickListener(new OnItemClickListener() {
//                        @Override
//                        public void onItemClick(View view, int adapterPosition) {
//                            Log.e("xxxx",INCategory.get(adapterPosition).getName());
//                        }
//                    });

                    mRecyclerView2.setOnItemMoveListener(new OnItemMoveListener() {
                        @Override
                        public boolean onItemMove(RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {
                            if (srcHolder.getItemViewType() != targetHolder.getItemViewType())
                                return false;
                            int fromPosition = srcHolder.getAdapterPosition();
                            int toPosition = targetHolder.getAdapterPosition();
                            Collections.swap(INCategory, fromPosition, toPosition);
                            adapter2.notifyItemMoved(fromPosition, toPosition);

                            //更新数据中的排序
                            for (int i = 0; i < INCategory.size(); i++) {
                                Category category = INCategory.get(i);
                                category.setOrder(i);
                                categoryViewModel.updateCategory(category);
                            }

                            return true;// 返回true表示处理了并可以换位置，返回false表示你没有处理并不能换位置。
                        }

                        @Override
                        public void onItemDismiss(RecyclerView.ViewHolder srcHolder) {
                            int position = srcHolder.getAdapterPosition();
                            Category toDelete = INCategory.get(position);
                            INCategory.remove(position);
                            adapter2.notifyItemRemoved(position);
                            categoryViewModel.deleteCategory(toDelete);
//                            final AlertDialog.Builder normalDialog =
//                                    new AlertDialog.Builder(requireActivity());
//                            normalDialog.setTitle("删除标签");
//                            normalDialog.setMessage("您确定要删除次标签?");
//                            normalDialog.setPositiveButton("确定",
//                                    new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            Toast.makeText(requireContext(),"已删除该标签",Toast.LENGTH_SHORT).show();
//                                            categoryViewModel.deleteCategory(toDelete);
//                                        }
//                                    });
//                            normalDialog.setNegativeButton("取消",
//                                    new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            Toast.makeText(requireContext(),"已取消删除",Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                            normalDialog.show();
                            //Toast.makeText(CategoryActivity.this, "现在的第" + position + "条被删除。", Toast.LENGTH_SHORT).show();
                            Snackbar.make(root.findViewById(R.id.fragment_category), "已删除一个分类", Snackbar.LENGTH_SHORT).setAction("撤销", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    categoryViewModel.insertCategory(toDelete);
                                }
                            }).show();
                        }
                    });

                    floatingActionButton = root.findViewById(R.id.floatingActionButton_category);

                    floatingActionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditText input = new EditText(getContext());
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("添加新类型").setView(input)
                                    .setNegativeButton(R.string.cancel, null);
                            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String tmp;
                                    tmp = input.getText().toString().trim();
                                    if (!tmp.isEmpty()) {
                                        Category category = new Category();
                                        category.setOrder(allCategories.size() + 1);
                                        category.setName(tmp);
                                        category.setType(false);
                                        category.setIcon("ic_category_in_10");
                                        category.setUpload(false);
                                        try {
                                            categoryViewModel.insertCategory(category);
                                        } catch (Exception e) {
                                            Toast.makeText(getContext(), "添加失败", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }
                            }).show();
                        }
                    });

                }
            }
        });


        return root;
    }

    //初始化category数据库
//    private void initCategory() {
//        isInitCategory=true;
//        String[] categoryINName={"搬砖","工资","奖金","卖房","其它"};
//        String[] categoryOUTName={"餐饮","购物","服饰","健身","交通","捐赠","社交","通信","房租","教育","医疗","生活","零食","旅行","水果","其它"};
//        //支出
//        for(int i=0;i<categoryOUTName.length;i++) {
//            Category category = new Category();
//            category.setIcon("ic_category_out_"+(i+1));
//            category.setType(true);
//            category.setName(categoryOUTName[i]);
//            category.setOrder(i);
//            categoryViewModel.insertCategory(category);
//        }
//        //收入
//        for(int i=0;i<categoryINName.length;i++) {
//            Category category = new Category();
//            category.setIcon("ic_category_in_"+(i+1));
//            category.setType(false);
//            category.setName(categoryINName[i]);
//            category.setOrder(i);
//            categoryViewModel.insertCategory(category);
//        }
//    }

}
