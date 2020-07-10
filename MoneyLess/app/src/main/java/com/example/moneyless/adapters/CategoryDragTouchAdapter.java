package com.example.moneyless.adapters;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneyless.R;
import com.example.moneyless.data.Entity.Category;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.lang.reflect.Field;
import java.util.List;

/**
 * <p>
 * 触摸即开始拖拽。
 * </p>
 */
public class CategoryDragTouchAdapter extends CategoryBaseAdapter<CategoryDragTouchAdapter.ViewHolder> {

    private SwipeRecyclerView mMenuRecyclerView;
    private List<String> mDataList;
    private List<Category> categories;
    private Context context;

    public CategoryDragTouchAdapter(Context context, SwipeRecyclerView menuRecyclerView) {
        super(context);
        this.context = context;
        this.mMenuRecyclerView = menuRecyclerView;
    }

    public void setAllCategory(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public void notifyDataSetChanged(List<String> dataList) {
        this.mDataList = dataList;
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return categories == null ? 0 : categories.size();
        //return mDataList == null ? 0 : mDataList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(getInflater().inflate(R.layout.category_item, parent, false));
        viewHolder.mMenuRecyclerView = mMenuRecyclerView;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //holder.setData(mDataList.get(position));
        holder.setData(context, categories.get(position));
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {

        TextView tvTitle;
        ImageView imageView;
        SwipeRecyclerView mMenuRecyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            imageView = itemView.findViewById(R.id.iv_icon);
            itemView.findViewById(R.id.iv_touch).setOnTouchListener(this);
        }

        public void setData(Context context, Category category) {
            Field field = null;
            int res_ID;
            try {
                field = R.drawable.class.getField(category.getIcon());
                res_ID = field.getInt(field.getName());
            } catch (NoSuchFieldException e) {
                res_ID = R.drawable.ic_category_out_1;
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                res_ID = R.drawable.ic_category_out_1;
                e.printStackTrace();
            }
            this.tvTitle.setText(category.getName());
            this.imageView.setImageDrawable(context.getDrawable(res_ID));
        }

//        public void setData(String title) {
//            this.tvTitle.setText(title);
//        }


        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN: {
                    mMenuRecyclerView.startDrag(this);
                    break;
                }
            }
            return false;
        }


    }
}