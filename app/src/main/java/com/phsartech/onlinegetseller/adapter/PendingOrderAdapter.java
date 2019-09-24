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
import com.google.android.material.card.MaterialCardView;
import com.phsartech.onlinegetseller.R;
import com.phsartech.onlinegetseller.callback.CallBackFunctionOnItemClickPending;
import com.phsartech.onlinegetseller.model.OrderModel;

import java.util.List;

public class PendingOrderAdapter extends RecyclerView.Adapter<PendingOrderAdapter.MyViewHolder> {

    private List<OrderModel.Data> dataProductList;
    private LayoutInflater inflater;
    private CallBackFunctionOnItemClickPending callBackFunctionOnItemClickPending;

    public PendingOrderAdapter(Context context, List<OrderModel.Data> listClear, CallBackFunctionOnItemClickPending callBackFunctionOnItemClickPending) {
        inflater = LayoutInflater.from(context);
        this.dataProductList = listClear;
        this.callBackFunctionOnItemClickPending = callBackFunctionOnItemClickPending;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_order, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final OrderModel.Data item = dataProductList.get(position);

        holder.textView_title.setText(item.getUser_name() + "");
        if (item.getCount() > 1) {
            holder.textView_count.setText(item.getCount() + " orders");
        } else {
            holder.textView_count.setText(item.getCount() + " order");
        }
        if (item.getUser_image() != null) {
            Glide.with(holder.itemView.getContext())
                    .load(item.getUser_image())
                    .placeholder(R.drawable.noimg)
                    .into(holder.imageView_thumbnail);
        }
        holder.textView_time.setText(item.getCreated_at() + "");
        holder.materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackFunctionOnItemClickPending.onItemClickPending(item.getShop_id(), item.getUser_id(), item.getUser_image(), item.getUser_name(), item.getEmail());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataProductList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView_thumbnail;
        private TextView textView_title, textView_count, textView_time;
        private MaterialCardView materialCardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView_thumbnail = itemView.findViewById(R.id.img_thumbnail_order);
            textView_title = itemView.findViewById(R.id.text_title_order);
            textView_count = itemView.findViewById(R.id.text_item_order);
            textView_time = itemView.findViewById(R.id.text_time_order);
            materialCardView = itemView.findViewById(R.id.card_order);
        }
    }
}
