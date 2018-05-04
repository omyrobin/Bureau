package com.administration.bureau.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.administration.bureau.App;
import com.administration.bureau.BaseActivity;
import com.administration.bureau.R;
import com.administration.bureau.adapter.DataAdapter;
import com.administration.bureau.constant.Constant;
import com.administration.bureau.entity.BaseResponse;
import com.administration.bureau.entity.DataEntity;
import com.administration.bureau.entity.StatusChangeEvent;
import com.administration.bureau.entity.UserEntity;
import com.administration.bureau.entity.eventbus.UserLoginEvent;
import com.administration.bureau.http.ProgressSubscriber;
import com.administration.bureau.http.RetrofitClient;
import com.administration.bureau.http.RetrofitManager;
import com.administration.bureau.interfaces.IItemClickPosition;
import com.administration.bureau.model.PostService;
import com.administration.bureau.utils.SharedPreferencesUtil;
import com.administration.bureau.utils.ToastUtil;
import com.administration.bureau.widget.ListAlertDialog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import retrofit2.Response;
import rx.Observable;

/**
 * Created by omyrobin on 2017/4/16.
 */

public class RegisterUserActivity extends BaseActivity{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title_tv)
    TextView titleTv;
    @BindView(R.id.country_number_tv)
    TextView countryNumberTv;
    @BindView(R.id.phone_number_et)
    EditText phoneNumberEt;
    @BindView(R.id.auth_code_et)
    TextView authCodeEt;
    @BindView(R.id.age_of_16_tv)
    TextView ageOf16Tv;

    private boolean ageof16 = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_user;
    }

    @Override
    protected void initializeToolbar() {
        titleTv.setText(R.string.register);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //暂时隐藏新功能
//        ageOf16Tv.setVisibility(View.GONE);
    }

    @Override
    protected void initializeActivity(Bundle savedInstanceState) {

    }

    @OnClick({R.id.country_number_tv,R.id.register_user_tv, R.id.auth_code_tv, R.id.age_of_16_tv})
    public void actionBtn(TextView view){
        String phoneNumber = phoneNumberEt.getText().toString();
        String country_code = countryNumberTv.getText().toString().replace("+","");
        String authCode = authCodeEt.getText().toString();
        switch (view.getId()){
            case R.id.register_user_tv:
                registerUser(phoneNumber, authCode);
                break;

            case R.id.auth_code_tv:

                if(isMobile(phoneNumber)){
                    getCode("86",phoneNumber);
                }else{
                    ToastUtil.showShort(getString(R.string.correct_phone_number));
                }
                break;

            case R.id.age_of_16_tv:
                ageof16 = true;
                Intent intent = new Intent(RegisterUserActivity.this, RegisterUserAgeOf16Activity.class);
                startActivity(intent);
                finish();
                break;

            default:
                DataAdapter adapter;
                ListAlertDialog dialog = null;
                String country = App.locale == 0 ? Constant.CN_COUNTRY_PHONE: Constant.EN_COUNTRY_PHONE;
                ArrayList<DataEntity> dataEntities = new ArrayList<>();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(country);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(jsonObject!=null){
                    Iterator keys = jsonObject.keys();
                    while (keys.hasNext()){
                        String key = (String) keys.next();
                        String value = jsonObject.optString(key);
                        DataEntity dataEntity = new DataEntity(value, key);
                        dataEntities.add(dataEntity);
                    }
                    Collections.sort(dataEntities);
                }
                adapter = new DataAdapter(this,dataEntities);
                dialog = new ListAlertDialog(this, adapter, new IItemClickPosition() {
                    @Override
                    public void itemClickPosition(DataEntity dataEntity) {
                        countryNumberTv.setText("+" + dataEntity.getKey());
                    }
                });
                if(dialog!=null)
                    dialog.show();
                break;
        }
    }

    private void getCode(String code,String phoneNumber){
        PostService postService = RetrofitManager.getRetrofit().create(PostService.class);
        Observable<Response<BaseResponse<ArrayList<String>>>> observable = postService.getCode(code,phoneNumber);
        RetrofitClient.client().request(observable, new ProgressSubscriber<ArrayList<String>>(this) {
            @Override
            protected void onSuccess(ArrayList<String> s) {
                ToastUtil.showShort(getString(R.string.code_success));
            }

            @Override
            protected void onFailure(String message) {
                ToastUtil.showShort(getString(R.string.code_failure));
            }
        });
    }


    private void registerUser(String phoneNumber, String authCode){
        Observable<Response<BaseResponse<UserEntity>>> observable = RetrofitManager.getRetrofit().create(PostService.class).registerUser("login", phoneNumber, authCode);
        RetrofitClient.client().request(observable, new ProgressSubscriber<UserEntity>(this) {
            @Override
            protected void onSuccess(UserEntity userEntity) {
                App.getInstance().setUserEntity(userEntity);
                //TODO 通知Homepage刷新
                EventBus.getDefault().post(new UserLoginEvent());
                EventBus.getDefault().post(new StatusChangeEvent());
                if(!(boolean)SharedPreferencesUtil.getParam(RegisterUserActivity.this,App.getInstance().getUserEntity().getUser().getPhone(),false)){
                    setAlias();
                }
                finish();
            }

            @Override
            protected void onFailure(String message) {
                ToastUtil.showShort(getString(R.string.code_error));
            }
        });
    }

    public static boolean isMobile(final String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }
}
