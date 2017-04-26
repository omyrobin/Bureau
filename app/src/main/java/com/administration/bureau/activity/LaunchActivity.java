package com.administration.bureau.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.administration.bureau.App;
import com.administration.bureau.BaseActivity;
import com.administration.bureau.R;

/**
 * Created by omyrobin on 2017/4/26.
 */

public class LaunchActivity extends BaseActivity {

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

    }

    @Override
    protected void initializeActivity(Bundle savedInstanceState) {

        new Handler().postDelayed(new Runnable() {
            Intent intent;
            @Override
            public void run() {
                if(App.locale != -1){
                    intent = new Intent(LaunchActivity.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    intent = new Intent(LaunchActivity.this,LanguageSelectActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        },3000);
    }
}
