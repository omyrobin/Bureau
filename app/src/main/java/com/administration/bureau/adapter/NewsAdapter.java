package com.administration.bureau.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.administration.bureau.R;
import com.administration.bureau.activity.ArticleDetialActivity;
import com.administration.bureau.entity.ArticleEntity;
import com.administration.bureau.entity.BannerEntity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by omyrobin on 2018/2/27.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder>{

    private Context context;

    private ArrayList<ArticleEntity.DataBean> news;

    private LayoutInflater inflater;

    public NewsAdapter(Context context, ArrayList<ArticleEntity.DataBean> news) {
        this.context = context;
        this.news = news;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View articleView = inflater.inflate(R.layout.item_homepage_article, null);
        return new NewsViewHolder(articleView);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, final int position) {
        holder.newsBigTitleTv.setVisibility(View.GONE);
        holder.newsMoreTv.setVisibility(View.GONE);
        if(!TextUtils.isEmpty(news.get(position).getCover())){
            holder.newsPicImg.setVisibility(View.VISIBLE);
            Glide.with(context).load(news.get(position).getCover()).into(holder.newsPicImg);
        }else{
            holder.newsPicImg.setVisibility(View.GONE);
        }

        holder.newsTitleTv.setText(news.get(position).getTitle());
        holder.newsDateTv.setText(news.get(position).getCreated_at());
        holder.newsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArticleDetialActivity.newInstance(context,news.get(position).getId(),true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return news != null ? news.size() : 0;
    }

    class NewsViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.news_layout)
        ViewGroup newsLayout;
        @BindView(R.id.news_bigtitle_tv)
        TextView newsBigTitleTv;
        @BindView(R.id.news_pic_img)
        ImageView newsPicImg;
        @BindView(R.id.news_title_tv)
        TextView newsTitleTv;
        @BindView(R.id.news_more_tv)
        TextView newsMoreTv;
        @BindView(R.id.news_date_tv)
        TextView newsDateTv;

        public NewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
