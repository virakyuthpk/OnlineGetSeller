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
import com.phsartech.onlinegetseller.callback.CallBackFucntionOnItemOnSaleClick;
import com.phsartech.onlinegetseller.model.ProductModelOnSale;

import java.util.List;

public class OnsaleProductAdapter extends RecyclerView.Adapter<OnsaleProductAdapter.MyViewHolder> {

    private List<ProductModelOnSale.Data> dataProductList;
    private LayoutInflater inflater;
    private CallBackFucntionOnItemOnSaleClick callBackFucntionOnItemOnSaleClick;

    public OnsaleProductAdapter(Context context, List<ProductModelOnSale.Data> listClear, CallBackFucntionOnItemOnSaleClick callBackFucntionOnItemOnSaleClick) {
        inflater = LayoutInflater.from(context);
        this.dataProductList = listClear;
        this.callBackFucntionOnItemOnSaleClick = callBackFucntionOnItemOnSaleClick;
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
        final ProductModelOnSale.Data item = dataProductList.get(position);

        if (item.getImage() != null) {
            Glide.with(holder.itemView.getContext())
                    .load(item.getImage())
                    .placeholder(R.drawable.noimg)
                    .into(holder.imageView_thumbnail);
        }
        holder.textView_title.setText(item.getName_en());
        holder.textView_count.setText(item.getQty() + " total");
        holder.textView_time.setText(item.getDes_en());

        holder.materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackFucntionOnItemOnSaleClick.OnClickItem(item);
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
            imageView_thumbnail = itemView.findViewById(R.id.img_thumbnail_product);
            textView_title = itemView.findViewById(R.id.text_title_product);
            textView_count = itemView.findViewById(R.id.text_item_product);
            textView_time = itemView.findViewById(R.id.text_time_product);
            materialCardView = itemView.findViewById(R.id.card_product);
            textView_time.setMaxLines(2);
        }
    }
}
