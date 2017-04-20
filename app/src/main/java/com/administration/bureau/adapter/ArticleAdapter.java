package com.administration.bureau.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.administration.bureau.R;
import com.administration.bureau.activity.ArticleDetialActivity;
import com.administration.bureau.entity.ArticleEntity;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by omyrobin on 2017/4/19.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private Context context;

    private List<ArticleEntity.DataBean> datas;

    public ArticleAdapter(Context context, List<ArticleEntity.DataBean> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View articleView = LayoutInflater.from(context).inflate(R.layout.item_article, null);
        return new ArticleViewHolder(articleView);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, final int position) {
        Glide.with(context).load(datas.get(position).getCover()).into((holder.atricelPicImg));
        holder.atricelTitleTv.setText(datas.get(position).getTitle());
        holder.atricelDateTv.setText(datas.get(position ).getCreated_at());
        holder.articleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArticleDetialActivity.newInstance(context,datas.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas != null ? datas.size() : 0;
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.article_layout)
        ViewGroup articleLayout;
        @BindView(R.id.article_pic_img)
        ImageView atricelPicImg;
        @BindView(R.id.article_title_tv)
        TextView atricelTitleTv;
        @BindView(R.id.article_date_tv)
        TextView atricelDateTv;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
