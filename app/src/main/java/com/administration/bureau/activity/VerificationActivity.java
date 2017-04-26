package com.administration.bureau.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.TextView;

import com.administration.bureau.App;
import com.administration.bureau.BaseActivity;
import com.administration.bureau.R;
import com.administration.bureau.entity.ArticleEntity;
import com.administration.bureau.entity.BaseResponse;
import com.administration.bureau.entity.UserRegisterInfoEntity;
import com.administration.bureau.entity.eventbus.CancelEvent;
import com.administration.bureau.entity.eventbus.UserLogoutEvent;
import com.administration.bureau.http.ProgressSubscriber;
import com.administration.bureau.http.RetrofitClient;
import com.administration.bureau.http.RetrofitManager;
import com.administration.bureau.model.DeleteService;
import com.administration.bureau.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;
import retrofit2.Response;
import rx.Observable;

/**
 * Created by omyrobin on 2017/4/19.
 */

public class VerificationActivity extends BaseActivity{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title_tv)
    TextView titleTv;
    @BindView(R.id.toolbar_action_tv)
    TextView actionTv;
    @BindView(R.id.leave_date_et)
    EditText leaveDateEt;
    @BindView(R.id.leave_reason_et)
    EditText leaveReasonEt;
    @BindView(R.id.destination_et)
    EditText destinationEt;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_verification;
    }

    @Override
    protected void initializeToolbar() {
        titleTv.setText(R.string.write_off_info);
        actionTv.setText(R.string.submit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @OnClick(R.id.toolbar_action_tv)
    public void actionTo(){
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle(getString(R.string.hint)).setMessage(getString(R.string.write_off_message))
                .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteInfo();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.cancle),null).create();
        dialog.show();
    }

    @Override
    protected void initializeActivity(Bundle savedInstanceState) {

    }

    @OnTouch(R.id.leave_date_et)
    protected boolean selectDate(TextView textView, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP)
            super.selectDate(textView);
        return true;
    }

    private void deleteInfo(){
        if(App.getInstance().getUserEntity() == null)
            return;
        HashMap<String,Object> params = new HashMap<>();
        params.put("leave_date",leaveDateEt.getText().toString());
        params.put("leave_reason",leaveReasonEt.getText().toString());
        params.put("destination",destinationEt.getText().toString());
        int user_id = App.getInstance().getUserEntity().getUser().getId();
        int id = infoEntity.getId();
        String token = "Bearer "+ App.getInstance().getUserEntity().getToken();

        DeleteService deleteService = RetrofitManager.getRetrofit().create(DeleteService.class);
        Observable<Response<BaseResponse<Boolean>>> observable = deleteService.deleteInfo(user_id,id,params,token);
        RetrofitClient.client().request(observable, new ProgressSubscriber<Boolean>(this) {
            @Override
            protected void onSuccess(Boolean aBoolean) {
                if(aBoolean){
                    ToastUtil.showShort(getString(R.string.information_off));
                    //销毁用户提交资料
                    App.getInstance().setInfoEntity(new UserRegisterInfoEntity());
                    infoEntity = App.getInstance().getInfoEntity();
                    //TODO 刷新信息
                    EventBus.getDefault().post(new CancelEvent());
                    finish();
                }
            }

            @Override
            protected void onFailure(String message) {

            }
        });

    }
}
