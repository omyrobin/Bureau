package com.administration.bureau.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.administration.bureau.App;
import com.administration.bureau.BaseFragment;
import com.administration.bureau.R;
import com.administration.bureau.adapter.HomePageAdapter;
import com.administration.bureau.constant.Constant;
import com.administration.bureau.entity.ArticleEntity;
import com.administration.bureau.entity.BannerEntity;
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
 * Created by omyrobin on 2017/3/30.
 */

public class HomePageFragment extends BaseFragment{

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.toolbar_title_tv)
    public TextView titleTv;
    @BindView(R.id.homepage_rv)
    public RecyclerView homepageRv;

    private HomePageAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_homepage;
    }

    @Override
    protected void initializeToolbar() {
        titleTv.setText("房山出入境");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void initContent(@Nullable Bundle savedInstanceState) {
        initLayoutManager();
        requestBannerData();
    }

    private void initLayoutManager(){
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        homepageRv.setLayoutManager(manager);
    }

    private void requestBannerData(){
        Observable<Response<BaseResponse<ArrayList<BannerEntity>>>> observable = RetrofitManager.getRetrofit().create(GetService.class).getBanner("banner", Constant.languages[App.locale]);
        RetrofitClient.client().request(observable, new ProgressSubscriber<ArrayList<BannerEntity>>(getActivity()) {
            @Override
            protected void onSuccess(ArrayList<BannerEntity> bannerEntities) {
                setAdapterToMessageRv(initBannerData(bannerEntities));
                requestNewsData();
            }

            @Override
            protected void onFailure(String message) {

            }
        });
    }

    private void requestNewsData(){
        GetService getService = RetrofitManager.getRetrofit().create(GetService.class);
        Observable<Response<BaseResponse<ArticleEntity>>> observable = getService.getArticle("article",2, Constant.languages[App.locale]);
        RetrofitClient.client().request(observable, new ProgressSubscriber<ArticleEntity>(getActivity()) {
            @Override
            protected void onSuccess(ArticleEntity articleEntity) {
                adapter.setNewsData(articleEntity);
                requestTravelData();
            }

            @Override
            protected void onFailure(String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestTravelData(){
        GetService getService = RetrofitManager.getRetrofit().create(GetService.class);
        Observable<Response<BaseResponse<ArticleEntity>>> observable = getService.getArticle("article",3, Constant.languages[App.locale]);
        RetrofitClient.client().request(observable, new ProgressSubscriber<ArticleEntity>(getActivity()) {
            @Override
            protected void onSuccess(ArticleEntity articleEntity) {
                adapter.setTravelData(articleEntity);
            }

            @Override
            protected void onFailure(String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayList<BannerEntity> initBannerData(ArrayList<BannerEntity> bannerEntities){
        //TODO 请求数据
        if(bannerEntities!=null && !bannerEntities.isEmpty()){
            bannerEntities.add(0,bannerEntities.get(bannerEntities.size()-1));
            bannerEntities.add(bannerEntities.get(0));
        }
        return bannerEntities;
    }

    private void setAdapterToMessageRv( ArrayList<BannerEntity> bannerEntities){
        adapter = new HomePageAdapter(getActivity(),bannerEntities);
        homepageRv.setAdapter(adapter);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.i("TAG", "MessagetFragment id onHiddenChanged");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("TAG", "MessagetFragment id onDestroyView");
    }


    public HomePageAdapter getAdapter() {
        return adapter;
    }
}
