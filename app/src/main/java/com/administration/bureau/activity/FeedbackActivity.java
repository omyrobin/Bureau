package com.administration.bureau.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;

import com.administration.bureau.App;
import com.administration.bureau.BaseActivity;
import com.administration.bureau.R;
import com.administration.bureau.entity.BaseResponse;
import com.administration.bureau.entity.ContentEntity;
import com.administration.bureau.http.ProgressSubscriber;
import com.administration.bureau.http.RetrofitClient;
import com.administration.bureau.http.RetrofitManager;
import com.administration.bureau.model.PostService;
import com.administration.bureau.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;
import rx.Observable;

/**
 * Created by omyrobin on 2017/4/21.
 */

public class FeedbackActivity extends BaseActivity{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title_tv)
    TextView titleTv;
    @BindView(R.id.toolbar_action_tv)
    TextView actionTv;
    @BindView(R.id.send_message_et)
    EditText sendMessageEt;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_send_message;
    }

    @Override
    protected void initializeToolbar() {
        titleTv.setText(R.string.feedback);
        actionTv.setText(R.string.send);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @OnClick(R.id.toolbar_action_tv)
    public void actionTo(){
        senMessage();
    }

    @Override
    protected void initializeActivity(Bundle savedInstanceState) {

    }

    private void senMessage(){
        PostService postService = RetrofitManager.getRetrofit().create(PostService.class);
        int user_id = App.getInstance().getUserEntity().getUser().getId();
        String content = sendMessageEt.getText().toString();
        String token = "Bearer "+ App.getInstance().getUserEntity().getToken();
        Observable<Response<BaseResponse<ContentEntity>>> observable = postService.sendMessage(user_id, content, token);
        RetrofitClient.client().request(observable, new ProgressSubscriber<ContentEntity>(this) {
            @Override
            protected void onSuccess(ContentEntity contentEntity) {
                ToastUtil.showShort(getString(R.string.send_success));
                finish();
            }

            @Override
            protected void onFailure(String message) {
                ToastUtil.showShort(getString(R.string.send_failure));
            }
        });
    }
}
