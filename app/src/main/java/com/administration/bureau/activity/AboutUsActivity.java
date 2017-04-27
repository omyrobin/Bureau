package com.administration.bureau.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.administration.bureau.App;
import com.administration.bureau.BaseActivity;
import com.administration.bureau.R;

import butterknife.BindView;

/**
 * Created by omyrobin on 2017/4/27.
 */

public class AboutUsActivity extends BaseActivity{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title_tv)
    TextView titleTv;
    @BindView(R.id.about_us_tv)
    TextView aboutUsTv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_aboutus;
    }

    @Override
    protected void initializeToolbar() {
        titleTv.setText(R.string.about_us);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void initializeActivity(Bundle savedInstanceState) {
        String versionName = getString(R.string.for_android, App.getInstance().getVersionName());
        aboutUsTv.setText(versionName);
    }
}
