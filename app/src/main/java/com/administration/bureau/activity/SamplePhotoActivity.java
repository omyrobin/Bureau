package com.administration.bureau.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.administration.bureau.App;
import com.administration.bureau.BaseActivity;
import com.administration.bureau.R;
import com.administration.bureau.constant.Constant;
import com.bumptech.glide.Glide;

import butterknife.BindView;

/**
 * Created by omyrobin on 2017/4/27.
 */

public class SamplePhotoActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title_tv)
    TextView titleTv;
    @BindView(R.id.sample_img)
    ImageView sampleImg;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_samplephoto;
    }

    @Override
    protected void initializeToolbar() {
        titleTv.setText(R.string.select_example_photo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void initializeActivity(Bundle savedInstanceState) {
        switch (App.getInstance().samplePhotoIndex){
            case Constant.PASSPORT_INFO:
                Glide.with(this).load(R.drawable.pass_port_img).into(sampleImg);
                break;

            case Constant.ENTRY_PAGE:
                Glide.with(this).load(R.drawable.entry_port_img).into(sampleImg);
                break;

            case Constant.VISA_PAGE:
                Glide.with(this).load(R.drawable.visa_page_img).into(sampleImg);
                break;
        }

    }
}
