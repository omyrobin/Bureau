package com.administration.bureau.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class MessageBoardActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title_tv)
    TextView titleTv;
    @BindView(R.id.toolbar_action_tv)
    TextView actionTv;
    @BindView(R.id.send_message_et)
    EditText sendMessageEt;
    private int selectIndex;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_send_message;
    }

    @Override
    protected void initializeToolbar() {
        titleTv.setText(R.string.leave_message);
        actionTv.setText(R.string.send);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @OnClick(R.id.toolbar_action_tv)
    public void actionTo(){
        if(App.getInstance().getUserEntity()==null){
            Intent intent = new Intent(this,RegisterUserActivity.class);
            startActivity(intent);
            return;
        }
        showMessageObject();
    }

    @Override
    protected void initializeActivity(Bundle savedInstanceState) {

    }

    private void showMessageObject(){
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_message_object,null);
        RadioGroup radioGroup = view.findViewById(R.id.rg_message_object);
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setOnClickListener(this);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

            }
        });
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setTitle("选择留言对象")
                .setNegativeButton(R.string.cancle,null)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        senMessage();
                    }
                }).create();
        dialog.show();
    }

    private void senMessage(){
        PostService postService = RetrofitManager.getRetrofit().create(PostService.class);
        int user_id = App.getInstance().getUserEntity().getUser().getId();
        String content = sendMessageEt.getText().toString();
        String token = "Bearer "+ App.getInstance().getUserEntity().getToken();
        Observable<Response<BaseResponse<ContentEntity>>> observable = postService.sendMessage(user_id, content, token);
        RetrofitClient.client().request(observable, new ProgressSubscriber<ContentEntity>(this,true) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rb_bureau:
                selectIndex = 0;
                break;

            case R.id.rb_community_police:
                selectIndex = 1;
                break;

            default:
                selectIndex = 2;
                break;
        }
    }
}
