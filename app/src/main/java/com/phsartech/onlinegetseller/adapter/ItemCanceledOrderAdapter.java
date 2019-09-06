package com.phsartech.onlinegetseller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.phsartech.onlinegetseller.MyViewHolder;
import com.phsartech.onlinegetseller.R;
import com.phsartech.onlinegetseller.model.OrderModel;

import java.util.List;

public class ItemCanceledOrderAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private final LayoutInflater inflater;
    private final List<OrderModel.Data> dataProductList;
    private View view;
    private ImageView imageView;
    private TextView textView_name, textView_qty, textView_time;
    private MaterialButton materialButton_positive, materialButton_negative;

    public ItemCanceledOrderAdapter(Context context, List<OrderModel.Data> list) {
        this.inflater = LayoutInflater.from(context);
        this.dataProductList = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.item_order_product, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        registerComponent(view);
        return holder;
    }

    private void registerComponent(View view) {
        imageView = view.findViewById(R.id.image_item);
        textView_name = view.findViewById(R.id.text_item_order_product_name);
        textView_qty = view.findViewById(R.id.text_item_order_product_qty);
        textView_time = view.findViewById(R.id.text_item_order_product_time);
        materialButton_positive = view.findViewById(R.id.button_positive);
        materialButton_negative = view.findViewById(R.id.button_negative);
        materialButton_positive.setVisibility(View.GONE);
        materialButton_negative.setVisibility(View.GONE);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OrderModel.Data item = dataProductList.get(position);

        textView_name.setText(item.getPname());
        textView_qty.setText("Order qty : " + item.getQty() + "");
        textView_time.setText(item.getCreated_at());

        if (item.getPimage() != null) {
            Glide.with(holder.itemView.getContext())
                    .load(item.getPimage())
                    .into(imageView);
        }
    }

    @Override
    public int getItemCount() {
        return dataProductList.size();
    }
}
