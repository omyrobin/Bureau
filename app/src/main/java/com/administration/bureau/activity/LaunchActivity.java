package com.administration.bureau.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.administration.bureau.App;
import com.administration.bureau.BaseActivity;
import com.administration.bureau.R;
import com.administration.bureau.constant.Constant;
import com.administration.bureau.download.DownService;
import com.administration.bureau.utils.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by omyrobin on 2017/4/26.
 */

public class LaunchActivity extends BaseActivity {

    @BindView(R.id.language_select_layout)
    ViewGroup languageSelectLayout;

    Intent intent = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        App.mAppStatus = 0;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initializeToolbar() {
        if(App.locale != -1) {
            languageSelectLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initializeActivity(Bundle savedInstanceState) {
        intent = new Intent(this, DownService.class);
        startService(intent);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if(App.locale != -1){
                    intent = new Intent(LaunchActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },3000);
    }

    @OnClick({R.id.chinese_tv, R.id.english_tv})
    public void selectLanguage(TextView buttonId){
        switch (buttonId.getId()){
            case R.id.chinese_tv:
                App.locale = 0;
                break;

            case R.id.english_tv:
                App.locale = 1;
                break;
        }
        App.getInstance().initLocale();
        SharedPreferencesUtil.setParam(this, Constant.LOCALE,App.locale);
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
