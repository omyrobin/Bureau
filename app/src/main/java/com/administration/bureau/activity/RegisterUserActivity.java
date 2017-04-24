package com.administration.bureau.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.administration.bureau.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        titleTv.setText(R.string.register);
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
                if(isMobile(phoneNumberEt.getText().toString())){
                    showCodeDialog();
                }else{
                    ToastUtil.showShort(getString(R.string.correct_phone_number));
                }
                break;
        }
    }

    private void showCodeDialog(){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.have_not_yet_opened))
                .setMessage(getString(R.string.auto_input))
                .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        authCodeEt.setText("123123");
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
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
