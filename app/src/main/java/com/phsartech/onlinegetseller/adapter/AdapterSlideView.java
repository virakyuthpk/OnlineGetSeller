package com.phsartech.onlinegetseller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.phsartech.onlinegetseller.R;
import com.phsartech.onlinegetseller.model.GalleryModel;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class AdapterSlideView extends SliderViewAdapter<AdapterSlideView.SlideViewHolder> {


    private List<GalleryModel.DataEntity> slides;
    private Context context;

    public AdapterSlideView(List<GalleryModel.DataEntity> slides, Context context) {
        this.context = context;
        this.slides = slides;
    }

    @Override
    public SlideViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_item, null);
        return new SlideViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(SlideViewHolder viewHolder, int position) {
        GalleryModel.DataEntity item = slides.get(position);
        Glide.with(viewHolder.itemView)
                .load(item.getPath())
                .placeholder(R.drawable.noimg)
                .into(viewHolder.imageView);
    }

    @Override
    public int getCount() {
        return slides.size();
    }

    class SlideViewHolder extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageView;

        public SlideViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_slider);
            this.itemView = itemView;
        }
    }
}