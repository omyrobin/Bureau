package com.administration.bureau.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.administration.bureau.entity.eventbus.CancelEvent;
import com.administration.bureau.entity.eventbus.LanguageEvent;
import com.administration.bureau.entity.eventbus.UserLoginEvent;
import com.administration.bureau.entity.eventbus.UserLogoutEvent;
import com.administration.bureau.entity.eventbus.UserRegisterEvent;
import com.administration.bureau.http.ProgressSubscriber;
import com.administration.bureau.http.RetrofitClient;
import com.administration.bureau.http.RetrofitManager;
import com.administration.bureau.model.GetService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func3;
import rx.schedulers.Schedulers;

/**
 * Created by omyrobin on 2017/3/30.
 */

public class HomePageFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.toolbar_title_tv)
    public TextView titleTv;
    @BindView(R.id.homepage_re_layout)
    SwipeRefreshLayout homepageReLayout;
    @BindView(R.id.homepage_rv)
    public RecyclerView homepageRv;

    private HomePageAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_homepage;
    }

    @Override
    protected void initializeToolbar() {
        setLanguageText();
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public void setLanguageText() {
        titleTv.setText(R.string.entry_and_exit_administration_bureau);
    }

    @Subscribe
    public void onMessageEvent(LanguageEvent event){
        setLanguageText();
        requestBannerData();
    }

//    @Subscribe
//    public void onMessageEvent(UserLoginEvent event){
//        adapter.notifyDataSetChanged();
//    }

    @Subscribe
    public void onMessageEvent(UserLogoutEvent event){
        adapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onMessageEvent(CancelEvent event){
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void initContent(@Nullable Bundle savedInstanceState) {
        initRefreshLayout();
        initLayoutManager();
        autoRefresh();
    }

    private void initRefreshLayout(){
        homepageReLayout.setColorSchemeResources(R.color.colorPrimary,R.color.colorPrimary,R.color.colorPrimary);
        homepageReLayout.setOnRefreshListener(this);
    }

    private void initLayoutManager(){
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        homepageRv.setLayoutManager(manager);
    }

    private void autoRefresh(){
        homepageReLayout.post(new Runnable() {
            @Override
            public void run() {
                homepageReLayout.setRefreshing(true);
                requestBannerData();
            }
        });
    }

    public void reuestHomePageData(){
        GetService getService = RetrofitManager.getRetrofit().create(GetService.class);
        Observable<Response<BaseResponse<ArticleEntity>>> ob_news = getService.getArticle("article",2, Constant.languages[0]);
        Observable<Response<BaseResponse<ArticleEntity>>> ob_travel = getService.getArticle("article",3, Constant.languages[App.locale]);
        Observable<Response<BaseResponse<ArticleEntity>>> ob_date = Observable.mergeDelayError(ob_news, ob_travel);
        RetrofitClient.client().request(ob_date, new ProgressSubscriber<ArticleEntity>(getActivity()) {
            @Override
            protected void onSuccess(ArticleEntity articleEntity) {
                adapter.setArticleData(articleEntity);
                homepageReLayout.setRefreshing(false);
            }

            @Override
            protected void onFailure(String message) {
                homepageReLayout.setRefreshing(false);
            }
        });
    }

    public void requestBannerData(){
        Observable<Response<BaseResponse<ArrayList<BannerEntity>>>> observable = RetrofitManager.getRetrofit().create(GetService.class).getBanner("banner", Constant.languages[App.locale]);
        RetrofitClient.client().request(observable, new ProgressSubscriber<ArrayList<BannerEntity>>(getActivity()) {
            @Override
            protected void onSuccess(ArrayList<BannerEntity> bannerEntities) {
                setAdapterToMessageRv(initBannerData(bannerEntities));
                reuestHomePageData();
            }

            @Override
            protected void onFailure(String message) {
                homepageReLayout.setRefreshing(false);
            }
        });
    }

    private ArrayList<BannerEntity> initBannerData(ArrayList<BannerEntity> bannerEntities){
        //TODO 请求数据
//        if(bannerEntities!=null && !bannerEntities.isEmpty()){
//            bannerEntities.add(0,bannerEntities.get(bannerEntities.size()-1));
//            bannerEntities.add(bannerEntities.get(0));
//        }
        return bannerEntities;
    }

    private void setAdapterToMessageRv( ArrayList<BannerEntity> bannerEntities){
        adapter = new HomePageAdapter(getActivity(),bannerEntities);
        homepageRv.setAdapter(adapter);
    }

    public HomePageAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void onRefresh() {
        EventBus.getDefault().post(new UserRegisterEvent());
        requestBannerData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

}
