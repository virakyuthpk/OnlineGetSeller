package com.phsartech.onlinegetseller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.phsartech.onlinegetseller.MyViewHolder;
import com.phsartech.onlinegetseller.R;
import com.phsartech.onlinegetseller.callback.CallBackFunctionOnItemClickAll;
import com.phsartech.onlinegetseller.model.OrderModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllOrderAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private List<OrderModel.Data> dataProductList;
    private MaterialCardView materialCardView;
    private LayoutInflater inflater;
    private View view;
    private CircleImageView imageView_thumbnail;
    private TextView textView_title, textView_count, textView_time;
    private CallBackFunctionOnItemClickAll callBackFunctionOnItemClickAll;

    public AllOrderAdapter(Context context, List<OrderModel.Data> listClear, CallBackFunctionOnItemClickAll callBackFunctionOnItemClickAll) {
        this.inflater = LayoutInflater.from(context);
        this.dataProductList = listClear;
        this.callBackFunctionOnItemClickAll = callBackFunctionOnItemClickAll;
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
        materialCardView = view.findViewById(R.id.card_order);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final OrderModel.Data item = dataProductList.get(position);

        textView_title.setText(item.getUser_name() + "");
        if (item.getCount() > 1) {
            textView_count.setText(item.getCount() + " orders");
        } else {
            textView_count.setText(item.getCount() + " order");
        }
        if (item.getUser_image() != null) {
            Glide.with(view.getContext())
                    .load(item.getUser_image())
                    .placeholder(R.drawable.noimg)
                    .into(imageView_thumbnail);
        }

        materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackFunctionOnItemClickAll.onItemClickAll(item.getShop_id(), item.getUser_id(), item.getUser_image(), item.getUser_name(), item.getEmail());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataProductList.size();
    }
}