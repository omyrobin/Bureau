package com.administration.bureau.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.administration.bureau.App;
import com.administration.bureau.BaseActivity;
import com.administration.bureau.R;
import com.administration.bureau.constant.Constant;
import com.administration.bureau.utils.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by omyrobin on 2017/4/23.
 */

public class LanguageSelectActivity extends BaseActivity {

    @BindView(R.id.language_select_layout)
    ViewGroup languageSelectLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_language_select;
    }

    @Override
    protected void initializeToolbar() {

    }

    @Override
    protected void initializeActivity(Bundle savedInstanceState) {
        if(App.locale != -1){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            showLanguageSelectBg();
        }
    }

    private void showLanguageSelectBg(){
        languageSelectLayout.setVisibility(View.VISIBLE);
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
        languageSelectLayout.setVisibility(View.GONE);
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
