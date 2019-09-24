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
import com.phsartech.onlinegetseller.callback.CallBackFucntionOnItemSoldClick;
import com.phsartech.onlinegetseller.model.ProductModelSold;

import java.util.List;

public class SoldProductAdapter extends RecyclerView.Adapter<SoldProductAdapter.MyViewHolder> {

    private List<ProductModelSold.Data> dataProductList;
    private LayoutInflater inflater;
    private CallBackFucntionOnItemSoldClick callBackFucntionOnItemSoldClick;

    public SoldProductAdapter(Context context, List<ProductModelSold.Data> listClear, CallBackFucntionOnItemSoldClick callBackFucntionOnItemSoldClick) {
        inflater = LayoutInflater.from(context);
        this.dataProductList = listClear;
        this.callBackFucntionOnItemSoldClick = callBackFucntionOnItemSoldClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_product, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ProductModelSold.Data item = dataProductList.get(position);

        holder.textView_title.setText(item.getProduct_name() + "");
        holder.textView_count.setText(item.getCount() + " sold");
        if (item.getProduct_image() != null) {
            Glide.with(holder.itemView.getContext())
                    .load(item.getProduct_image())
                    .placeholder(R.drawable.noimg)
                    .into(holder.imageView_thumbnail);
        }
        holder.textView_time.setText(item.getCreated_at() + "");

        holder.materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackFucntionOnItemSoldClick.OnClickItem(item);
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
            materialCardView = itemView.findViewById(R.id.card_product);
            imageView_thumbnail = itemView.findViewById(R.id.img_thumbnail_product);
            textView_title = itemView.findViewById(R.id.text_title_product);
            textView_count = itemView.findViewById(R.id.text_item_product);
            textView_time = itemView.findViewById(R.id.text_time_product);
        }
    }
}
