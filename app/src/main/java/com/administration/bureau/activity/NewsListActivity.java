package com.administration.bureau.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.administration.bureau.App;
import com.administration.bureau.BaseActivity;
import com.administration.bureau.R;
import com.administration.bureau.adapter.ArticleAdapter;
import com.administration.bureau.adapter.NewsAdapter;
import com.administration.bureau.constant.Constant;
import com.administration.bureau.entity.ArticleEntity;
import com.administration.bureau.entity.BaseResponse;
import com.administration.bureau.http.ProgressSubscriber;
import com.administration.bureau.http.RetrofitClient;
import com.administration.bureau.http.RetrofitManager;
import com.administration.bureau.model.GetService;

import java.util.ArrayList;

import butterknife.BindView;
import retrofit2.Response;
import rx.Observable;

/**
 * Created by omyrobin on 2018/2/27.
 */

public class NewsListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title_tv)
    TextView titleTv;
    @BindView(R.id.news_rv)
    RecyclerView newsRv;
    @BindView(R.id.news_re_layout)
    SwipeRefreshLayout newsReLayout;

    private ArticleEntity data;

    private ArrayList<ArticleEntity.DataBean> news = new ArrayList<>();

    public static void newInstance(Context context){
        Intent intent = new Intent(context, NewsListActivity.class);
//        intent.putExtra("news_data", data);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news;
    }

    @Override
    protected void initializeToolbar() {
        titleTv.setText(R.string.fangshan_news);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }


    @Override
    protected void initializeActivity(Bundle savedInstanceState) {
//        getNewDataFromIntent();
        initRefreshLayout();
        initLayoutManager();
//        initAdapter();
        autoRefresh();
    }

//    private void getNewDataFromIntent(){
//        data = (ArticleEntity) getIntent().getSerializableExtra("news_data");
//        for (int i=0 ;i< data.getData().size();i++){
//            if(data.getData().get(i).getType() == 2){
//                news.add(data.getData().get(i));
//            }
//        }
//    }

    private void initRefreshLayout(){
        newsReLayout.setColorSchemeResources(R.color.colorPrimary,R.color.colorPrimary,R.color.colorPrimary);
        newsReLayout.setOnRefreshListener(this);
    }

    private void initLayoutManager(){
        LinearLayoutManager manager = new LinearLayoutManager(this);
        newsRv.setLayoutManager(manager);
    }

    private void initAdapter(){
        NewsAdapter adapter = new NewsAdapter(this,news);
        newsRv.setAdapter(adapter);
    }

    private void autoRefresh(){
        newsReLayout.post(new Runnable() {
            @Override
            public void run() {
                newsReLayout.setRefreshing(true);
                requestNewsData();
            }
        });
    }

    private void requestNewsData(){
        GetService getService = RetrofitManager.getRetrofit().create(GetService.class);
        Observable<Response<BaseResponse<ArticleEntity>>> ob_news = getService.getArticle("article",2, Constant.languages[0]);
        RetrofitClient.client().request(ob_news, new ProgressSubscriber<ArticleEntity>(this) {
            @Override
            protected void onSuccess(ArticleEntity articleEntity) {
                newsRv.setAdapter(new NewsAdapter(NewsListActivity.this, articleEntity.getData()));
                newsReLayout.setRefreshing(false);
            }

            @Override
            protected void onFailure(String message) {
                newsReLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        newsReLayout.setRefreshing(true);
        requestNewsData();
    }
}
