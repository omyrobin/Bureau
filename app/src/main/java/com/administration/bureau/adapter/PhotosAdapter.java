package com.administration.bureau.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.administration.bureau.R;
import com.administration.bureau.constant.Constant;
import com.bumptech.glide.Glide;
import com.yanzhenjie.album.Album;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by omyrobin on 2017/5/11.
 */

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotosViewHolder> {

    private Context context;

    private List<String> photos;

    private volatile int canAddphotosLength = 9;

    private OnRvItemClickListener listener;

    public PhotosAdapter(Context context, List<String> photos, OnRvItemClickListener listener) {
        this.context = context;
        this.photos = photos;
        this.listener = listener;
    }

    public void addPhotoPath(List<String> paths){
        if(paths.size() < 9){
            canAddphotosLength = canAddphotosLength - paths.size();
        }else{
            canAddphotosLength = 0;
        }
        photos.addAll(0,paths);
        notifyDataSetChanged();
    }

    public int getPhotoCount(){
        return 9 - canAddphotosLength;
    }

    public synchronized void setPhotoCount(){
        if(canAddphotosLength == 0){
            if(!photos.contains("Add")){
                photos.add("Add");
            }
        }
        canAddphotosLength += 1;
    }

    @Override
    public PhotosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View itemView = LayoutInflater.from(context).inflate(R.layout.item_photos,null);
        return new PhotosViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PhotosViewHolder holder, final int position) {
        if(photos.get(position).equals("Add")) {
            holder.imageView.setImageDrawable(ContextCompat.getDrawable(context, android.R.drawable.ic_menu_add));
        }else {
            Glide.with(context).load(photos.get(position)).into(holder.imageView);
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(photos.get(position).equals("Add")){
                    Album.startAlbum((Activity)context,
                            Constant.SELECT_CONTRACT_OF_TENANCY, canAddphotosLength,
                            ContextCompat.getColor(context, R.color.colorPrimary),
                            ContextCompat.getColor(context, R.color.colorPrimaryDark));
                } else{
                    listener.onClick(position);
                    notifyDataSetChanged();

                }
            }
        });
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

        @BindView(R.id.photos_item_img)
        ImageView imageView;

        public PhotosViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnRvItemClickListener{
        void onClick(int position);
    }
}
