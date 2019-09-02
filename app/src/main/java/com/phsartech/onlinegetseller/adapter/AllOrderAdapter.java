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
import com.phsartech.onlinegetseller.MyViewHolder;
import com.phsartech.onlinegetseller.R;
import com.phsartech.onlinegetseller.model.OrderModel;

import java.util.List;

public class AllOrderAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private List<OrderModel.Data> dataProductList;
    private LayoutInflater inflater;
    private View view;
    private ImageView imageView_thumbnail;
    private TextView textView_title, textView_count, textView_time;

    public AllOrderAdapter(Context context, List<OrderModel.Data> listClear) {
        inflater = LayoutInflater.from(context);
        this.dataProductList = listClear;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.item_order, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        registerComponent(view);
        return holder;
    }

    private void registerComponent(View view) {
        imageView_thumbnail = view.findViewById(R.id.img_thumbnail_order);
        textView_title = view.findViewById(R.id.text_title_order);
        textView_count = view.findViewById(R.id.text_item_order);
        textView_time = view.findViewById(R.id.text_time_order);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OrderModel.Data item = dataProductList.get(position);

        textView_title.setText(item.getUser_name() + "");
        if (item.getCount() > 1) {
            textView_count.setText(item.getCount() + " orders");
        } else {
            textView_count.setText(item.getCount() + " order");
        }
        if (item.getUser_image() != null) {
            Glide.with(view.getContext())
                    .load(item.getUser_image())
                    .into(imageView_thumbnail);
        }
        textView_time.setText(item.getCreated_at() + "");
    }

    @Override
    public int getItemCount() {
        return dataProductList.size();
    }
}