package com.phsartech.onlinegetseller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.phsartech.onlinegetseller.R;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

    private LayoutInflater layoutInflater;
    private Context context;
    private ArrayList<String> img_modelArrayList;

    public ImageAdapter(Context context, ArrayList<String> img_modelArrayList) {
        this.context = context;
        this.img_modelArrayList = img_modelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.img_item, null);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.MyViewHolder myViewHolder, final int position) {

        Glide.with(context)
                .asBitmap()
                .load(img_modelArrayList.get(position))
                .into(myViewHolder.imageView);

        myViewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_modelArrayList.remove(position);
                notifyItemRemoved(position);
//                notifyItemChanged(position, img_modelArrayList.size());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return img_modelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        MaterialButton imageButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_product);
            imageButton = itemView.findViewById(R.id.button_close);
        }
    }
}
