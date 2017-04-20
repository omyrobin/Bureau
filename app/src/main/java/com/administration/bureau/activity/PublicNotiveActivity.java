package com.administration.bureau.activity;

import android.os.Bundle;
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

public class PublicNotiveActivity extends BaseActivity{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title_tv)
    TextView titleTv;
    @BindView(R.id.laws_rv)
    RecyclerView lawsRv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_laws;
    }

    @Override
    protected void initializeToolbar() {
        titleTv.setText("公示公告");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }


    @Override
    protected void initializeActivity(Bundle savedInstanceState) {
        initLayoutManager();
        requestLawsData();
    }

    private void initLayoutManager(){
        LinearLayoutManager manager = new LinearLayoutManager(this);
        lawsRv.setLayoutManager(manager);
    }

    private void requestLawsData(){
        GetService getService = RetrofitManager.getRetrofit().create(GetService.class);
        Observable<Response<BaseResponse<ArticleEntity>>> observable = getService.getLawsArticle("article",4, Constant.languages[App.locale]);
        RetrofitClient.client().request(observable, new ProgressSubscriber<ArticleEntity>(this) {
            @Override
            protected void onSuccess(ArticleEntity articleEntity) {
                lawsRv.setAdapter(new ArticleAdapter(PublicNotiveActivity.this, articleEntity.getData()));
            }

            @Override
            protected void onFailure(String message) {

            }
        });
    }

}
