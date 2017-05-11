package com.administration.bureau.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by omyrobin on 2017/5/11.
 */

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotosViewHolder> {

    private Context context;

    private ArrayList<String> photos;

    public PhotosAdapter(Context context, ArrayList<String> photos) {
        this.context = context;
        this.photos = photos;
    }

    @Override
    public PhotosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView itemView = new ImageView(context);
        return new PhotosViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PhotosViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return itemCount();
    }

    private int itemCount(){
        if(photos.size() > 9){
            photos.remove(9);
        }
        return photos.size();
    }

    class PhotosViewHolder extends RecyclerView.ViewHolder{

        public PhotosViewHolder(View itemView) {
            super(itemView);
        }
    }
}
