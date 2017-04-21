package com.administration.bureau.activity;

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
import com.administration.bureau.constant.Constant;
import com.administration.bureau.entity.ArticleEntity;
import com.administration.bureau.entity.BaseResponse;
import com.administration.bureau.http.ProgressSubscriber;
import com.administration.bureau.http.RetrofitClient;
import com.administration.bureau.http.RetrofitManager;
import com.administration.bureau.model.GetService;

import butterknife.BindView;
import retrofit2.Response;
import rx.Observable;

/**
 * Created by omyrobin on 2017/4/19.
 */

public class ReminderActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title_tv)
    TextView titleTv;
    @BindView(R.id.laws_rv)
    RecyclerView lawsRv;
    @BindView(R.id.laws_re_layout)
    SwipeRefreshLayout lawsReLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_laws;
    }

    @Override
    protected void initializeToolbar() {
        titleTv.setText(R.string.reminder);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }


    @Override
    protected void initializeActivity(Bundle savedInstanceState) {
        initRefreshLayout();
        initLayoutManager();
        autoRefresh();
    }

    private void initRefreshLayout(){
        lawsReLayout.setColorSchemeResources(R.color.colorPrimary,R.color.colorPrimary,R.color.colorPrimary);
        lawsReLayout.setOnRefreshListener(this);
    }

    private void initLayoutManager(){
        LinearLayoutManager manager = new LinearLayoutManager(this);
        lawsRv.setLayoutManager(manager);
    }

    private void autoRefresh(){
        lawsReLayout.post(new Runnable() {
            @Override
            public void run() {
                lawsReLayout.setRefreshing(true);
                requestLawsData();
            }
        });
    }

    private void requestLawsData(){
        GetService getService = RetrofitManager.getRetrofit().create(GetService.class);
        Observable<Response<BaseResponse<ArticleEntity>>> observable = getService.getLawsArticle("article",6, Constant.languages[0]);
        RetrofitClient.client().request(observable, new ProgressSubscriber<ArticleEntity>(this) {
            @Override
            protected void onSuccess(ArticleEntity articleEntity) {
                lawsRv.setAdapter(new ArticleAdapter(ReminderActivity.this, articleEntity.getData(),true));
                lawsReLayout.setRefreshing(false);
            }

            @Override
            protected void onFailure(String message) {
                lawsReLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        lawsReLayout.setRefreshing(true);
        requestLawsData();
    }
}
