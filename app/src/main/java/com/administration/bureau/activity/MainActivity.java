package com.administration.bureau.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.administration.bureau.App;
import com.administration.bureau.BaseActivity;
import com.administration.bureau.R;
import com.administration.bureau.constant.Constant;
import com.administration.bureau.entity.BaseResponse;
import com.administration.bureau.entity.SpinnerData;
import com.administration.bureau.entity.UserRegisterInfoEntity;
import com.administration.bureau.entity.eventbus.UserLoginEvent;
import com.administration.bureau.entity.eventbus.UserLogoutEvent;
import com.administration.bureau.entity.eventbus.UserRegisterEvent;
import com.administration.bureau.fragment.HomePageFragment;
import com.administration.bureau.fragment.MessageFragment;
import com.administration.bureau.fragment.MineFragment;
import com.administration.bureau.http.ProgressSubscriber;
import com.administration.bureau.http.RetrofitClient;
import com.administration.bureau.http.RetrofitManager;
import com.administration.bureau.model.GetService;
import com.administration.bureau.utils.SharedPreferencesUtil;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;
import rx.Observable;

public class MainActivity extends BaseActivity {

    private static final String CURRENT_FRAGMENT_POS = "CURRENT_FRAGMENT_POS";
    @BindView(R.id.homepage_tv)
    TextView homepageTv;
    @BindView(R.id.message_tv)
    TextView messageTv;
    @BindView(R.id.mine_tv)
    TextView mineTv;

    private HomePageFragment homePageFragment;

    private MessageFragment messageFragment;

    private MineFragment mineFragment;

    private ArrayList<Fragment> fragmentArrayList;

    private int currPos;

    private int prePos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.mAppStatus = 0;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void protectApp() {}

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initializeToolbar() {

    }

    @Override
    protected void initializeActivity(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        requestStatus();
        reqeustSpinnerData();
        if (savedInstanceState != null) {
            Log.i("TAG","MainActivity is to savedInstanceState");
            currPos = savedInstanceState.getInt(CURRENT_FRAGMENT_POS,0);
            homePageFragment = (HomePageFragment) getSupportFragmentManager().findFragmentByTag(0+"");
            messageFragment = (MessageFragment) getSupportFragmentManager().findFragmentByTag(1+"");
            mineFragment = (MineFragment) getSupportFragmentManager().findFragmentByTag(2+"");
        }
        addToFragmentList();
        addFragmentToActivity(currPos);
    }

    private void hideAllFragment() {
        for (int i=0;i<fragmentArrayList.size();i++){
            if(fragmentArrayList.get(i).isAdded()){
                getSupportFragmentManager().beginTransaction().hide(fragmentArrayList.get(i)).commit();
            }
        }
    }

    private void initFragment(){
        if(homePageFragment == null)
            homePageFragment = new HomePageFragment();
        if(messageFragment == null)
            messageFragment = new MessageFragment();
        if(mineFragment == null)
            mineFragment = new MineFragment();
    }

    private void addToFragmentList(){
        initFragment();
        fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(homePageFragment);
        fragmentArrayList.add(messageFragment);
        fragmentArrayList.add(mineFragment);
        hideAllFragment();
    }

    private void addFragmentToActivity(int currPos){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(!fragmentArrayList.get(currPos).isAdded()){
            ft.add(R.id.fragement_layout, fragmentArrayList.get(currPos),currPos+"")
                    .hide(fragmentArrayList.get(prePos))
                    .show(fragmentArrayList.get(currPos)).commit();
        }else{
            ft.hide(fragmentArrayList.get(prePos)).show(fragmentArrayList.get(currPos)).commit();
        }
        prePos = currPos;
    }

    @Subscribe
    public void onMessageEvent(UserLoginEvent event){
        requestStatus();
    }

    @Subscribe
    public void onMessageEvent(UserLogoutEvent event){
        if(homePageFragment.getAdapter() != null)
            homePageFragment.getAdapter().notifyDataSetChanged();
    }

    @Subscribe
    public void onMessageEvent(UserRegisterEvent event){
        requestStatus();
    }

    private void requestStatus(){
        if(App.getInstance().getUserEntity() == null)
            return;
        GetService getService = RetrofitManager.getRetrofit().create(GetService.class);
        Observable<Response<BaseResponse<UserRegisterInfoEntity>>> observable = getService.getStatus(App.getInstance().getUserEntity().getUser().getId(),"Bearer "+ App.getInstance().getUserEntity().getToken());
        RetrofitClient.client().request(observable, new ProgressSubscriber<UserRegisterInfoEntity>(this) {
            @Override
            protected void onSuccess(UserRegisterInfoEntity userRegisterInfoEntity) {
                App.getInstance().status = userRegisterInfoEntity.getStatus();
                App.getInstance().certificate_image = userRegisterInfoEntity.getCertificate_image();
                App.getInstance().chinese_name = userRegisterInfoEntity.getChinese_name();
                App.getInstance().reject_reason = userRegisterInfoEntity.getReject_reason();
                App.getInstance().id = userRegisterInfoEntity.getId();

                if(homePageFragment.getAdapter() != null)
                    homePageFragment.getAdapter().notifyDataSetChanged();
            }

            @Override
            protected void onFailure(String message) {

            }
        });
    }

    private void reqeustSpinnerData(){
        Observable<Response<BaseResponse<SpinnerData>>> observable = RetrofitManager.getRetrofit().create(GetService.class).getSpinnerData("config");
        RetrofitClient.client().request(observable, new ProgressSubscriber<SpinnerData>(this) {
            @Override
            protected void onSuccess(SpinnerData spinnerData) {
                ///国家
                App.getInstance().setCountry(spinnerData.getCountry());
                //证件类型
                App.getInstance().setCredential_type(spinnerData.getCredential_type());
                //人员地域类型
                App.getInstance().setPerson_area_type(spinnerData.getPerson_area_type());
                //人员类型
                App.getInstance().setPerson_type(spinnerData.getPerson_type());
                //职业
                App.getInstance().setOccupation(spinnerData.getOccupation());
                //入境口岸u
                App.getInstance().setEntry_port(spinnerData.getEntry_port());
                //所属派出所
                App.getInstance().setPolice_station(spinnerData.getPolice_station());
                //停留事由
                App.getInstance().setStay_reason(spinnerData.getStay_reason());
                //住房种类
                App.getInstance().setHouse_type(spinnerData.getHouse_type());
                //证件类型
                App.getInstance().setVisa_type(spinnerData.getVisa_type());
                //所属派出所对应社区
                App.getInstance().setAllCommunity(spinnerData.getCommunity());
            }

            @Override
            protected void onFailure(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick({R.id.homepage_tv, R.id.message_tv, R.id.mine_tv})
    public void clickTab(TextView buttonId){
        switch (buttonId.getId()){
            case R.id.homepage_tv:
                currPos = 0;
                break;

            case R.id.message_tv:
                currPos = 1;
                break;

            case R.id.mine_tv:
                currPos = 2;
                break;
        }
        if(prePos != currPos)
            addFragmentToActivity(currPos);
    }


    //TODO 应用被强杀,重走登陆流程
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String action = intent.getStringExtra("action");
        if("force_kill".equals(action)){
            protectApp();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_FRAGMENT_POS,currPos);
        Log.i("TAG", currPos + "");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        Gson gson = new Gson();
        SharedPreferencesUtil.setParam(this, Constant.SAVE_USER_REGISTER_INFO, gson.toJson(infoEntity));
    }
}