package com.administration.bureau.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.administration.bureau.App;
import com.administration.bureau.BaseActivity;
import com.administration.bureau.R;
import com.administration.bureau.entity.BaseResponse;
import com.administration.bureau.entity.StatusChangeEvent;
import com.administration.bureau.entity.UserEntity;
import com.administration.bureau.entity.eventbus.UserLoginEvent;
import com.administration.bureau.http.ProgressSubscriber;
import com.administration.bureau.http.RetrofitClient;
import com.administration.bureau.http.RetrofitManager;
import com.administration.bureau.model.PostService;
import com.administration.bureau.utils.SharedPreferencesUtil;
import com.administration.bureau.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;
import rx.Observable;

/**
 * Created by omyrobin on 2017/12/20.
 */

public class RegisterUserAgeOf16Activity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title_tv)
    TextView titleTv;
    @BindView(R.id.passport_number_et)
    EditText passporeNumberEt;
    @BindView(R.id.register_user_age_of_16_tv)
    TextView registerUserAgeOf16Tv;

    @Override
    protected int getLayoutId() {
        return R.layout.avtivity_register_user_age_of_16;
    }

    @Override
    protected void initializeToolbar() {
        titleTv.setText(R.string.register);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void initializeActivity(Bundle savedInstanceState) {

    }

    @OnClick(R.id.register_user_age_of_16_tv)
    public void click(View view){
        String passproeNum = passporeNumberEt.getText().toString();
        switch (view.getId()){
            case R.id.register_user_age_of_16_tv:
                registerUserAgeOf16(passproeNum);
                break;
        }
    }

    private void registerUserAgeOf16(String passport){
        Observable<Response<BaseResponse<UserEntity>>> observable = RetrofitManager.getRetrofit().create(PostService.class).registerUserAgeOf16(passport);
        RetrofitClient.client().request(observable, new ProgressSubscriber<UserEntity>(this) {
            @Override
            protected void onSuccess(UserEntity userEntity) {
                App.getInstance().setUserEntity(userEntity);
                //TODO 通知Homepage刷新
                EventBus.getDefault().post(new UserLoginEvent());
                EventBus.getDefault().post(new StatusChangeEvent());
                if(!(boolean) SharedPreferencesUtil.getParam(RegisterUserAgeOf16Activity.this,App.getInstance().getUserEntity().getUser().getPhone(),false)){
                    setAlias();
                }
                finish();
            }

            @Override
            protected void onFailure(String message) {
//                ToastUtil.showShort(message);
            }
        });
    }

}
