package com.administration.bureau.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.administration.bureau.App;
import com.administration.bureau.R;
import com.administration.bureau.activity.ArticleDetialActivity;
import com.administration.bureau.activity.CertificateActivity;
import com.administration.bureau.activity.LawsActivity;
import com.administration.bureau.activity.MainActivity;
import com.administration.bureau.activity.MoreActivity;
import com.administration.bureau.activity.RegisterActivity;
import com.administration.bureau.activity.RegisterUserActivity;
import com.administration.bureau.activity.PublicNotiveActivity;
import com.administration.bureau.activity.ReminderActivity;
import com.administration.bureau.activity.ServiceInfoActivity;
import com.administration.bureau.activity.TravelActivity;
import com.administration.bureau.entity.ArticleEntity;
import com.administration.bureau.entity.BannerEntity;
import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by omyrobin on 2017/3/30.
 */

public class HomePageAdapter extends RecyclerView.Adapter {

    private static final int BANNER_ITEM = 0;

    private static final int TRAVEL = 1;

    private static final int NEWS = 2;

    private static final int LAST = 3;

    private Context context;

    private LayoutInflater inflater;

    private ArrayList<BannerEntity> bannerEntities;

    private ViewPager bannerVp;

    private ArticleEntity articleEntity;

    private BannerAdapter bannerAdaper;

    private ViewPagerHandler handler;

    private volatile int currentPos;

    private ArrayList<ArticleEntity.DataBean> news = new ArrayList<>();

    private ArrayList<ArticleEntity.DataBean> travels = new ArrayList<>();

    private ArticleEntity newsData;

    private ArticleEntity travelData;


    public HomePageAdapter(Context context, ArrayList<BannerEntity> bannerEntities) {
        this.context = context;
        this.bannerEntities = bannerEntities;
        inflater = LayoutInflater.from(context);
        handler = new ViewPagerHandler((MainActivity)context);
    }

    public void setNewsData(ArticleEntity newsData) {
        this.newsData = newsData;
        news = newsData.getData();
        notifyDataSetChanged();
    }

    public void setTravelData(ArticleEntity travelData) {
        this.travelData = travelData;
        travels = travelData.getData();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(travels.isEmpty())
            return news !=null ? news.size() + 2: 1;
        else
            return news !=null ? news.size() + 3: 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return BANNER_ITEM;
        }else if(position > 0 && position <= news.size()){
            return NEWS;
        }else if(!travels.isEmpty() && position == news.size() + 1 ){
            return TRAVEL;
        }else{
            return LAST;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == BANNER_ITEM) {
            View bannerView = inflater.inflate(R.layout.item_homepage_banner, null);
            return new BannerViewHolder(bannerView);
        }else if(viewType == NEWS){
            View articleView = inflater.inflate(R.layout.item_homepage_article, null);
            return new NewsViewHolder(articleView);
        }else if(viewType == TRAVEL){
            View travelView = inflater.inflate(R.layout.item_homepage_travel, null);
            return new TravelViewHolder(travelView);
        }else if (viewType == LAST){
            View lastView = inflater.inflate(R.layout.item_homepage_other, null);
            return new LastViewHolder(lastView);
        }else{
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof BannerViewHolder){
            if(bannerEntities != null && !bannerEntities.isEmpty()){
                bannerAdaper = new BannerAdapter();
                bannerVp = ((BannerViewHolder) holder).bannerVp;
                bannerVp.setAdapter(bannerAdaper);
                bannerVp.addOnPageChangeListener(bannerAdaper);
                bannerVp.setCurrentItem(1,false);
                bannerVp.setOffscreenPageLimit(5);
                if(App.getInstance().status == 0){
                    ((BannerViewHolder) holder).registerTv.setText(R.string.check_state_wait);
                }else if(App.getInstance().status == 1){
                    ((BannerViewHolder) holder).registerTv.setText(R.string.check_state_reject);
                }else if(App.getInstance().status == 3){
                    ((BannerViewHolder) holder).registerTv.setText(R.string.electronic_certificate_download);
                }else{
                    ((BannerViewHolder) holder).registerTv.setText(R.string.registeration);
                }
                ((BannerViewHolder) holder).lawTv.setText(R.string.foreign_laws);
                ((BannerViewHolder) holder).messageBoardTv.setText(R.string.leave_message);
            }
        }else if(holder instanceof NewsViewHolder){
            if(position != 1){
                ((NewsViewHolder) holder).newsBigTitleTv.setVisibility(View.GONE);
            }else{
                ((NewsViewHolder) holder).newsBigTitleTv.setVisibility(View.VISIBLE);
                ((NewsViewHolder) holder).newsBigTitleTv.setText(R.string.fangshan_news);
            }
            Glide.with(context).load(news.get(position-1).getCover()).into(((NewsViewHolder) holder).newsPicImg);
            ((NewsViewHolder) holder).newsTitleTv.setText(news.get(position - 1).getTitle());
            ((NewsViewHolder) holder).newsDateTv.setText(news.get(position - 1).getCreated_at());
            ((NewsViewHolder) holder).newsLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArticleDetialActivity.newInstance(context,news.get(position - 1).getId());
                }
            });
        }else if(holder instanceof TravelViewHolder){
            ((TravelViewHolder) holder).travelBigTitleLayout.setVisibility(View.VISIBLE);
            ((TravelViewHolder) holder).travelBigTitleTv.setText(R.string.fangshang_tourist_culture);
            ((TravelViewHolder) holder).travelBigMoreTv.setText(R.string.more);
            ((TravelViewHolder) holder).travelrv.setLayoutManager(new GridLayoutManager(context,3));
            ((TravelViewHolder) holder).travelrv.setAdapter(new TravelAdapter());
            ((TravelViewHolder) holder).travelBigMoreTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, TravelActivity.class);
                    context.startActivity(intent);
                }
            });
        }else if(holder instanceof LastViewHolder){
            ((LastViewHolder) holder).publicNotiveTv.setText(R.string.public_notice);
            ((LastViewHolder) holder).serviceInfoTv.setText(R.string.service_information);
            ((LastViewHolder) holder).reminderTv.setText(R.string.reminder);
            ((LastViewHolder) holder).moreTv.setText(R.string.more);
        }
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
        @BindView(R.id.news_date_tv)
        TextView newsDateTv;

        public NewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class LastViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.public_notive_tv)
        TextView publicNotiveTv;
        @BindView(R.id.service_info_tv)
        TextView serviceInfoTv;
        @BindView(R.id.reminder_tv)
        TextView reminderTv;
        @BindView(R.id.more_tv)
        TextView moreTv;

        public LastViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.public_notive_tv, R.id.service_info_tv, R.id.reminder_tv, R.id.more_tv})
        public void actionTo(TextView textView){
            Intent intent = null;
            switch (textView.getId()){
                case R.id.public_notive_tv:
                    intent = new Intent(context, PublicNotiveActivity.class);
                    break;

                case R.id.service_info_tv:
                    intent = new Intent(context, ServiceInfoActivity.class);
                    break;

                case R.id.reminder_tv:
                    intent = new Intent(context, ReminderActivity.class);
                    break;

                case R.id.more_tv:
                    intent = new Intent(context, MoreActivity.class);
                    break;
            }
            context.startActivity(intent);
        }
    }

    class TravelViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.travel_bigtitle_layout)
        ViewGroup travelBigTitleLayout;
        @BindView(R.id.travel_bigtitle_tv)
        TextView travelBigTitleTv;
        @BindView(R.id.travel_more_tv)
        TextView travelBigMoreTv;
        @BindView(R.id.travel_rv)
        RecyclerView travelrv;

        public TravelViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class TravelAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = inflater.inflate(R.layout.item_homepage_travel_rv_item, null);
            return new TravelItemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            TextView textView = ((TravelItemViewHolder)holder).tv;
            ViewGroup.LayoutParams params_tv = textView.getLayoutParams();
            params_tv.width = (int) (App.width*0.3);
            textView.setLayoutParams(params_tv);
            textView.setText(travels.get(position).getTitle());

            ImageView imageView = ((TravelItemViewHolder)holder).image;
            ViewGroup.LayoutParams params_img = imageView.getLayoutParams();
            params_img.width = (int) (App.width*0.3);
            params_img.height = (int)(App.width*0.4);
            imageView.setLayoutParams(params_img);
            Glide.with(context).load(travels.get(position).getCover()).into(imageView);

            ((TravelItemViewHolder)holder).travelLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArticleDetialActivity.newInstance(context,travels.get(position).getId());
                }
            });
        }

        @Override
        public int getItemCount() {
            int size;
            if(travels.size()>6){
                size = 6;
            }else {
                size = travels.size();
            }
            return travels!=null ? size : 0;
        }

        class TravelItemViewHolder extends RecyclerView.ViewHolder{

            @BindView(R.id.travel_layout)
            ViewGroup travelLayout;
            @BindView(R.id.travel_item_pic_image)
            ImageView image;
            @BindView(R.id.travel_item_title_tv)
            TextView tv;

            public TravelItemViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }
    }

    class BannerViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.banner_vp)
        ViewPager bannerVp;
        @BindView(R.id.register_tv)
        TextView registerTv;
        @BindView(R.id.law_tv)
        TextView lawTv;
        @BindView(R.id.message_board_tv)
        TextView messageBoardTv;

        public BannerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.register_tv, R.id.law_tv, R.id.message_board_tv})
        public void actionTo(TextView button){
            Intent intent = null;
            switch (button.getId()){
                case R.id.register_tv:
                    if(App.getInstance().status == -1){
                        if(App.getInstance().getUserEntity() == null){
                            intent = new Intent(context, RegisterUserActivity.class);
                        }else{
                            intent = new Intent(context, RegisterActivity.class);
                        }
                        context.startActivity(intent);
                    }else if(App.getInstance().status == 1){
                        intent = new Intent(context, RegisterActivity.class);
                        context.startActivity(intent);
                    }else if(App.getInstance().status == 3) {
                        intent = new Intent(context, CertificateActivity.class);
                        context.startActivity(intent);
                    }
                    break;

                case R.id.law_tv:
                    intent = new Intent(context, LawsActivity.class);
                    context.startActivity(intent);
                    break;

                case R.id.message_board_tv:

                    break;
            }
        }
    }

    public class BannerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

        @Override
        public int getCount() {
            return bannerEntities.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = new ImageView(context);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(context).load(bannerEntities.get(position).getImage()).into(view);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            object = null;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currentPos = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {

                if (currentPos == getCount() - 1) {
                    bannerVp.setCurrentItem(1, false);
                } else if (currentPos == 0) {
                    bannerVp.setCurrentItem(getCount() - 2, false);
                }
            }
        }

    }

    public static class ViewPagerHandler extends Handler {

        WeakReference<MainActivity> weakReferenceActivity;

        MainActivity activity;

        public ViewPagerHandler(MainActivity activity) {
            weakReferenceActivity = new WeakReference<>(activity);
            this.activity = weakReferenceActivity.get();
        }

        @Override
        public void handleMessage(Message msg) {
            if(activity!=null){
//                currentPos += 1;
//                if (currentPos == ids.size() - 1) {
//                    adapter.getBannerVp().setCurrentItem(1, true);
//                }
//                else if (adapter.getCurrentPosition() == 0) {
//                    adapter.getBannerVp().setCurrentItem(ids.size() - 2, true);
//                }
//                else{
//                    adapter.getBannerVp().setCurrentItem(currentPos);
//                }
//                sendEmptyMessageDelayed(0, 4000);
            }
        }
    }

}
