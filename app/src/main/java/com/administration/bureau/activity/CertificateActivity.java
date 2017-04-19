package com.administration.bureau.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.administration.bureau.App;
import com.administration.bureau.BaseActivity;
import com.administration.bureau.R;
import com.bumptech.glide.Glide;

import butterknife.BindView;

/**
 * Created by omyrobin on 2017/4/19.
 */

public class CertificateActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title_tv)
    TextView titleTv;
    @BindView(R.id.toolbar_action_tv)
    TextView actionTv;
    @BindView(R.id.certificate_img)
    ImageView certificateImg;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_certificate;
    }

    @Override
    protected void initializeToolbar() {
        titleTv.setText("预览电子证书");
        actionTv.setText("下载");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void initializeActivity(Bundle savedInstanceState) {
        previewCertificater();
    }

    private void previewCertificater(){
        Glide.with(this).load(App.getInstance().certificate_image).into(certificateImg);
    }
}
