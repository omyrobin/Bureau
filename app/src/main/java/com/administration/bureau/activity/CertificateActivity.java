package com.administration.bureau.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.administration.bureau.App;
import com.administration.bureau.BaseActivity;
import com.administration.bureau.R;
import com.administration.bureau.constant.Url;
import com.administration.bureau.interfaces.ImageDownLoadCallBack;
import com.administration.bureau.utils.DownLoadImageService;
import com.bumptech.glide.Glide;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

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
    @BindView(R.id.ceriflcate_change_tv)
    TextView ceriflcateChangeTv;
    boolean isBack = false;
    private ProgressDialog myDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_certificate;
    }

    @Override
    protected void initializeToolbar() {
        titleTv.setText(R.string.electronic_certificate);
        actionTv.setText(R.string.download);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @OnClick(R.id.toolbar_action_tv)
    public void actionTo(){
        circle();
        onDownLoad(infoEntity.getCertificate_image());
        onDownLoad(Url.GENERATE);
    }

    @Override
    protected void initializeActivity(Bundle savedInstanceState) {
        ceriflcateChangeTv.setText(R.string.show_back);
        previewCertificater();
    }

    private void previewCertificater(){
        Glide.with(this).load(infoEntity.getCertificate_image()).into(certificateImg);
    }

    @OnClick(R.id.ceriflcate_change_tv)
    public void previewBackCertificater(){
        isBack = !isBack;
        if(isBack){
            ceriflcateChangeTv.setText(R.string.show_front);
            Glide.with(this).load(Url.GENERATE).into(certificateImg);
        }else{
            ceriflcateChangeTv.setText(R.string.show_back);
            previewCertificater();
        }

    }

    /**
     * 启动图片下载线程
     */
    private void onDownLoad(final String url) {
        DownLoadImageService service = new DownLoadImageService(getApplicationContext(), url, new ImageDownLoadCallBack() {
            @Override
            public void onDownLoadSuccess(File file) {

            }

            @Override
            public void onDownLoadSuccess(Bitmap bitmap) {
                if(url.equals(Url.GENERATE))
                    myDialog.dismiss();
            }

            @Override
            public void onDownLoadFailed() {

            }
        });
        //启动图片下载线程
        new Thread(service).start();
    }

    /**
     * 圆形进度条测试..
     */
    private void circle() {
        myDialog = new ProgressDialog(this); // 获取对象
        myDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // 设置样式为圆形样式
        myDialog.setMessage(getString(R.string.downloading)); // 设置进度条的提示信息
        myDialog.setIndeterminate(false); // 设置进度条是否为不明确
        myDialog.setCancelable(true); // 设置进度条是否按返回键取消

        myDialog.show(); // 显示进度条
    }
}
