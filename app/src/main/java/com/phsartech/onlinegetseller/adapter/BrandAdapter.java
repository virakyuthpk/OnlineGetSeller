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
import com.phsartech.onlinegetseller.callback.CallBackFunctionOnButtonBrandDialogClick;
import com.phsartech.onlinegetseller.model.BrandModel;

import java.util.List;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.MyViewHolder> {

    private List<BrandModel.DataEntity> list;
    private CallBackFunctionOnButtonBrandDialogClick callBackFunctionOnButtonBrandDialogClick;
    private LayoutInflater inflater;

    public BrandAdapter(Activity activity, List<BrandModel.DataEntity> list, CallBackFunctionOnButtonBrandDialogClick callBackFunctionOnButtonBrandDialogClick) {
        this.callBackFunctionOnButtonBrandDialogClick = callBackFunctionOnButtonBrandDialogClick;
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
        final BrandModel.DataEntity item = list.get(position);

        holder.materialButton.setText(item.getNameEn());

        holder.materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackFunctionOnButtonBrandDialogClick.onButtonBrandClick(item);
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
