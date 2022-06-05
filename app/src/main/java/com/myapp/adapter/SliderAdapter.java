package com.myapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.transition.Hold;
import com.myapp.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.Holder> {
    int [] sliderImages;
    public SliderAdapter(int [] sliderImages){
        this.sliderImages = sliderImages;
    }
    @Override
    public Holder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder viewHolder, int position) {
viewHolder.imageView.setImageResource(sliderImages[position]);
    }

    @Override
    public int getCount() {
        return sliderImages.length;
    }

    public class Holder extends SliderViewAdapter.ViewHolder {

        ImageView imageView;

        public Holder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_for_slider);
        }
    }
}
