package com.phsartech.onlinegetseller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phsartech.onlinegetseller.MyViewHolder;
import com.phsartech.onlinegetseller.R;
import com.phsartech.onlinegetseller.model.ProductModelOnSale;
import com.phsartech.onlinegetseller.model.ProductModelSold;

import java.util.List;

public class OnsaleProductAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private List<ProductModelOnSale.Data> dataProductList;
    private LayoutInflater inflater;
    private ImageView imageView_thumbnail;
    private ImageButton imageButton_option;
    private TextView textView_title, textView_count, textView_time;
    private View view;

    public OnsaleProductAdapter(Context context, List<ProductModelOnSale.Data> listClear) {
        inflater = LayoutInflater.from(context);
        this.dataProductList = listClear;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.item_product, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        registerComponent(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProductModelOnSale.Data item = dataProductList.get(position);

        textView_title.setText(item.getName_en());
    }

    private void registerComponent(View view) {
        imageView_thumbnail = view.findViewById(R.id.img_thumbnail_product);
        textView_title = view.findViewById(R.id.text_title_product);
        textView_count = view.findViewById(R.id.text_item_product);
        textView_time = view.findViewById(R.id.text_time_product);
        imageButton_option = view.findViewById(R.id.more_option);
    }

    @Override
    public int getItemCount() {
        return dataProductList.size();
    }
}
