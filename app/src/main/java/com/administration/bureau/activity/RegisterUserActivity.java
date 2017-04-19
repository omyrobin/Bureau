package com.administration.bureau.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;

import com.administration.bureau.App;
import com.administration.bureau.BaseActivity;
import com.administration.bureau.R;
import com.administration.bureau.entity.BaseResponse;
import com.administration.bureau.entity.UserEntity;
import com.administration.bureau.entity.eventbus.UserLoginEvent;
import com.administration.bureau.http.ProgressSubscriber;
import com.administration.bureau.http.RetrofitClient;
import com.administration.bureau.http.RetrofitManager;
import com.administration.bureau.model.PostService;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
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
    @BindView(R.id.phone_number_et)
    EditText phoneNumberEt;
    @BindView(R.id.auth_code_et)
    TextView authCodeEt;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_user;
    }

    @Override
    protected void initializeToolbar() {
        titleTv.setText("注册");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void initializeActivity(Bundle savedInstanceState) {

    }

    @OnClick({R.id.register_user_tv, R.id.auth_code_tv})
    public void actionBtn(TextView view){
        switch (view.getId()){
            case R.id.register_user_tv:
                String phoneNumber = phoneNumberEt.getText().toString();
                String authCode = authCodeEt.getText().toString();
                registerUser(phoneNumber, authCode);
                break;

            case R.id.auth_code_tv:

                break;
        }
    }

    private void registerUser(String phoneNumber, String authCode){
        Observable<Response<BaseResponse<UserEntity>>> observable = RetrofitManager.getRetrofit().create(PostService.class).registerUser("login", phoneNumber, authCode);
        RetrofitClient.client().request(observable, new ProgressSubscriber<UserEntity>(this) {
            @Override
            protected void onSuccess(UserEntity userEntity) {
                App.getInstance().setUserEntity(userEntity);
                //TODO 通知Homepage刷新
                EventBus.getDefault().post(new UserLoginEvent());
                finish();
            }

            @Override
            protected void onFailure(String message) {

            }
        });
    }
}
