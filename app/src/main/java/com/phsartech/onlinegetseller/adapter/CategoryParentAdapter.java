package com.phsartech.onlinegetseller.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.phsartech.onlinegetseller.R;
import com.phsartech.onlinegetseller.callback.CallBackFunctionOnButtonCategoryParentDialogClick;
import com.phsartech.onlinegetseller.model.CategoryModel;

import java.util.List;

public class CategoryParentAdapter extends RecyclerView.Adapter<CategoryParentAdapter.MyViewHolder> {

    private List<CategoryModel.DataEntity> list;
    private CallBackFunctionOnButtonCategoryParentDialogClick callBackFunctionOnButtonCategoryParentDialogClick;
    private LayoutInflater inflater;

    public CategoryParentAdapter(Activity activity, List<CategoryModel.DataEntity> list, CallBackFunctionOnButtonCategoryParentDialogClick callBackFunctionOnButtonCategoryParentDialogClick) {
        this.callBackFunctionOnButtonCategoryParentDialogClick = callBackFunctionOnButtonCategoryParentDialogClick;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_category, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final CategoryModel.DataEntity item = list.get(position);

        holder.materialButton.setText(item.getTitleEn());

        holder.materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackFunctionOnButtonCategoryParentDialogClick.onButtonCategoryParentClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private MaterialButton materialButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            materialButton = itemView.findViewById(R.id.button_category_parent);
        }
    }
}