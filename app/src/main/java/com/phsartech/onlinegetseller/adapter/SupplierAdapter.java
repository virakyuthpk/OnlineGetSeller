package com.phsartech.onlinegetseller.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.phsartech.onlinegetseller.MyViewHolder;
import com.phsartech.onlinegetseller.R;
import com.phsartech.onlinegetseller.callback.CallBackFunctionOnButtonSupplierDialogClick;
import com.phsartech.onlinegetseller.model.SupplierModel;

import java.util.List;

public class SupplierAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private List<SupplierModel.DataEntity> list;
    private CallBackFunctionOnButtonSupplierDialogClick callBackFunctionOnButtonSupplierDialogClick;
    private LayoutInflater inflater;
    private View view;
    private MaterialButton materialButton;

    public SupplierAdapter(Activity activity, List<SupplierModel.DataEntity> list, CallBackFunctionOnButtonSupplierDialogClick callBackFunctionOnButtonBrandDialogClick) {
        this.callBackFunctionOnButtonSupplierDialogClick = callBackFunctionOnButtonBrandDialogClick;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.item_category, parent, false);
        materialButton = view.findViewById(R.id.button_category_parent);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final SupplierModel.DataEntity item = list.get(position);

        materialButton.setText(item.getNameEn());

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackFunctionOnButtonSupplierDialogClick.onButtonSupplierClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
