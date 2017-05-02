package com.administration.bureau.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
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

import java.util.ArrayList;
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
    @BindView(R.id.phone_number_et)
    EditText phoneNumberEt;
    @BindView(R.id.auth_code_et)
    TextView authCodeEt;

    private static final int MSG_SET_ALIAS = 1001;

    private String code;

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
        String phoneNumber = phoneNumberEt.getText().toString();
        switch (view.getId()){
            case R.id.register_user_tv:
                String authCode = authCodeEt.getText().toString();
                registerUser(phoneNumber, authCode);
                break;

            case R.id.auth_code_tv:

                if(isMobile(phoneNumber)){
                    getCode(phoneNumber);
                }else{
                    ToastUtil.showShort(getString(R.string.correct_phone_number));
                }
                break;
        }
    }

    private void getCode(String phoneNumber){
        PostService postService = RetrofitManager.getRetrofit().create(PostService.class);
        Observable<Response<BaseResponse<ArrayList<String>>>> observable = postService.getCode(phoneNumber);
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

//    private void showCodeDialog(){
//        AlertDialog dialog = new AlertDialog.Builder(this)
//                .setTitle(getString(R.string.have_not_yet_opened))
//                .setMessage(getString(R.string.auto_input))
//                .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        authCodeEt.setText("123123");
//                        dialog.dismiss();
//                    }
//                }).create();
//        dialog.show();
//    }

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

    private void setAlias() {
        // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, App.getInstance().getUserEntity().getUser().getPhone()));
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    if(App.getInstance().getUserEntity()!=null)
                        SharedPreferencesUtil.setParam(RegisterUserActivity.this,App.getInstance().getUserEntity().getUser().getPhone(),true);
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
            }
        }
    };

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    break;
                default:
                    break;
            }
        }
    };
}
