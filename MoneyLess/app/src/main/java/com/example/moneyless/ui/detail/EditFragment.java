package com.example.moneyless.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.example.moneyless.R;
import com.example.moneyless.adapters.MoneyLessAdapter;
import com.example.moneyless.data.Entity.MoneyLess;
import com.example.moneyless.ui.add.AddActivity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditFragment extends Fragment {
    public static final String WASTEBOOK_EDIT = "wasteBook_edit";
    private DetailViewModel detailViewModel;
    private MoneyLess moneyLess;
    private TextView type, amount, info, date, category;
    private ImageView icon;
    private Button bt_edit, bt_delete;
    private DecimalFormat mAmountFormat = new DecimalFormat("0.00");
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm E");

    public EditFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit, container, false);
        getActivity().findViewById(R.id.nav_view).setVisibility(View.GONE);
        type = root.findViewById(R.id.textView_edit_type);
        amount = root.findViewById(R.id.textView_edit_amount);
        date = root.findViewById(R.id.textView_edit_date);
        info = root.findViewById(R.id.textView_edit_info);
        icon = root.findViewById(R.id.imageView_edit_icon);
        category = root.findViewById(R.id.textView_edit_category);
        bt_edit = root.findViewById(R.id.button_edit_edit);
        bt_delete = root.findViewById(R.id.button_edit_delete);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        detailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        Gson gson = new Gson();
        String wasteBookJson = getArguments().getString(WASTEBOOK_EDIT);
        if (wasteBookJson != null) {
            moneyLess = gson.fromJson(wasteBookJson, MoneyLess.class);
            if (moneyLess.isType()) {
                type.setText("支出");
                amount.setText("-" + mAmountFormat.format(moneyLess.getAmount()));
            } else {
                type.setText("收入");
                amount.setText("+" + mAmountFormat.format(moneyLess.getAmount()));
            }
            date.setText(sdf.format(new Date(moneyLess.getTime())));
            category.setText(moneyLess.getCategory());
            info.setText(moneyLess.getNote());
            icon.setImageDrawable(getContext().getDrawable(MoneyLessAdapter.getDrawableId(moneyLess.getIcon())));
        }

        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moneyLess != null) {
                    Intent intent = new Intent(getActivity(), AddActivity.class);
                    intent.putExtra(WASTEBOOK_EDIT, wasteBookJson);
                    startActivity(intent);
//                    Bundle bundle = new Bundle();
//                    bundle.putString(WASTEBOOK_EDIT,wasteBookJson);
//                    Navigation.findNavController(v).navigate(R.id.action_editFragment_to_navigation_add,bundle);
                }
            }
        });
        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moneyLess != null)
                    detailViewModel.deleteWasteBook(moneyLess);
                Navigation.findNavController(getActivity().findViewById(R.id.nav_host_fragment)).navigateUp();
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
    }
}
