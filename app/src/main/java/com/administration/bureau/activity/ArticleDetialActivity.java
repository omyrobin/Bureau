package com.administration.bureau.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.widget.TextView;

import com.administration.bureau.App;
import com.administration.bureau.BaseActivity;
import com.administration.bureau.R;
import com.administration.bureau.constant.Constant;
import com.administration.bureau.entity.ArticleDetialEntity;
import com.administration.bureau.entity.BaseResponse;
import com.administration.bureau.http.ProgressSubscriber;
import com.administration.bureau.http.RetrofitClient;
import com.administration.bureau.http.RetrofitManager;
import com.administration.bureau.model.GetService;

import butterknife.BindView;
import retrofit2.Response;
import rx.Observable;

/**
 * Created by omyrobin on 2017/4/20.
 */

public class ArticleDetialActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title_tv)
    TextView titleTv;
    @BindView(R.id.article_detial_title_tv)
    TextView articleDetialTitleTv;
    @BindView(R.id.article_detial_date_tv)
    TextView articleDetialDateTv;
    @BindView(R.id.article_detial_content_wb)
    WebView articleDetialContentWb;
    private int article_id;

    public static void newInstance(Context context, int article_id){
        Intent intent = new Intent(context, ArticleDetialActivity.class);
        intent.putExtra("article_id", article_id);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_article_detial;
    }

    @Override
    protected void initializeToolbar() {
        titleTv.setText(R.string.article_detials);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void initializeActivity(Bundle savedInstanceState) {
        article_id = getIntent().getIntExtra("article_id",0);
        requestArticleDetial();
    }

    private void requestArticleDetial(){
        GetService getService = RetrofitManager.getRetrofit().create(GetService.class);
        Observable<Response<BaseResponse<ArticleDetialEntity>>> observable = getService.getArticleDetial(article_id, Constant.languages[App.locale]);
        RetrofitClient.client().request(observable, new ProgressSubscriber<ArticleDetialEntity>(this) {
            @Override
            protected void onSuccess(ArticleDetialEntity articleDetialEntity) {
                initContentView(articleDetialEntity);
            }

            @Override
            protected void onFailure(String message) {

            }
        });
    }

    private void initContentView(ArticleDetialEntity articleDetialEntity){
        articleDetialTitleTv.setText(articleDetialEntity.getTitle());
        articleDetialDateTv.setText(articleDetialEntity.getCreated_at());
        articleDetialContentWb.loadDataWithBaseURL(null, articleDetialEntity.getContent(), "text/html", "utf-8", null);
        articleDetialContentWb.getSettings().setJavaScriptEnabled(true); //设置支持Javascript
    }
}
