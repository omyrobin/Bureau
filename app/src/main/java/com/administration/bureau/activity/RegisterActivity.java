package com.administration.bureau.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.administration.bureau.App;
import com.administration.bureau.BaseActivity;
import com.administration.bureau.R;
import com.administration.bureau.entity.BaseResponse;
import com.administration.bureau.entity.UserRegisterInfoEntity;
import com.administration.bureau.http.ProgressSubscriber;
import com.administration.bureau.http.RetrofitClient;
import com.administration.bureau.http.RetrofitManager;
import com.administration.bureau.model.PostService;
import com.administration.bureau.utils.ToastUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by omyrobin on 2017/4/5.
 */

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title_tv)
    TextView titleTv;
    //拒绝原因
    @BindView(R.id.reject_reason_tv)
    TextView rejectReasonTv;
    //基本信息
    @BindView(R.id.base_info_tv)
    TextView baseInfoTv;
    @BindView(R.id.base_info_completed_tv)
    TextView baseInfoCompletedTv;
    //入住及签证（注）信息
    @BindView(R.id.entry_visa_tv)
    TextView entryVisaTv;
    @BindView(R.id.entry_visa_completed_tv)
    TextView entryVisaCompletedTv;
    //住宿信息
    @BindView(R.id.hotel_info_tv)
    TextView hotlInfoTv;
    @BindView(R.id.hotel_info_completed_tv)
    TextView hotlInfoCompletedTv;
    @BindView(R.id.registration_info_tv)
    TextView registrationInfoTv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initializeToolbar() {
        titleTv.setText(R.string.registeration);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void initializeActivity(Bundle savedInstanceState) {
        if(infoEntity.getStatus() == 1 && !TextUtils.isEmpty(infoEntity.getReject_reason())){
            rejectReasonTv.setVisibility(View.VISIBLE);
            rejectReasonTv.setText(infoEntity.getReject_reason());
        }
        baseInfoTv.setText(R.string.base_info);
        entryVisaTv.setText(R.string.visa_note_info);
        hotlInfoTv.setText(R.string.hotel_information);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ToastUtil.isShow = false;
        if(isBaseInfoCompleted()){
            baseInfoCompletedTv.setText(R.string.completed);
            baseInfoCompletedTv.setTextColor(Color.GREEN);
        }else{
            baseInfoCompletedTv.setText(R.string.not_yet_completed);
            baseInfoCompletedTv.setTextColor(Color.RED);
        }

        if(isEntryVisaCompleted()){
            entryVisaCompletedTv.setText(R.string.completed);
            entryVisaCompletedTv.setTextColor(Color.GREEN);
        }else{
            entryVisaCompletedTv.setText(R.string.not_yet_completed);
            entryVisaCompletedTv.setTextColor(Color.RED);
        }

        if(isHotlInfoCompleted()){
            hotlInfoCompletedTv.setText(R.string.completed);
            hotlInfoCompletedTv.setTextColor(Color.GREEN);
        }else{
            hotlInfoCompletedTv.setText(R.string.not_yet_completed);
            hotlInfoCompletedTv.setTextColor(Color.RED);
        }

        if(isBaseInfoCompleted() && isEntryVisaCompleted() && isHotlInfoCompleted()){
            registrationInfoTv.setEnabled(true);
        }else{
            registrationInfoTv.setEnabled(false);
        }
        ToastUtil.isShow = true;
    }

    @OnClick({R.id.base_info_layout, R.id.entry_visa_layout, R.id.hotel_info_layout,R.id.registration_info_tv})
    public void actionTo(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.base_info_layout:
                intent = new Intent(this,BaseInfoActivity.class);
                startActivityForResult(intent,0);
                break;

            case R.id.entry_visa_layout:
                intent = new Intent(this,EntryVisaActivity.class);
                startActivityForResult(intent,1);
                break;

            case R.id.hotel_info_layout:
                intent = new Intent(this,HotlInfoActivity.class);
                startActivityForResult(intent,2);
                break;

            case R.id.registration_info_tv:
                if(App.getInstance().getInfoEntity().getStatus() == 1){
                    registrationInfoAgain();
                }else{
                    registrationInfo();
                }
                break;
        }
    }

}
