package com.administration.bureau.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.administration.bureau.App;
import com.administration.bureau.BaseActivity;
import com.administration.bureau.R;
import com.administration.bureau.adapter.DataAdapter;
import com.administration.bureau.constant.Constant;
import com.administration.bureau.entity.BaseResponse;
import com.administration.bureau.entity.DataEntity;
import com.administration.bureau.entity.UploadEntity;
import com.administration.bureau.http.ProgressSubscriber;
import com.administration.bureau.http.RetrofitClient;
import com.administration.bureau.http.RetrofitManager;
import com.administration.bureau.interfaces.IItemClickPosition;
import com.administration.bureau.model.PostService;
import com.administration.bureau.utils.BitmapUtil;
import com.administration.bureau.widget.ListAlertDialog;
import com.bumptech.glide.Glide;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import rx.Observable;

/**
 * Created by omyrobin on 2017/4/6.
 */

public class EntryVisaActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title_tv)
    TextView titleTv;
    @BindView(R.id.toolbar_action_tv)
    TextView actionTv;
    //入境页照片layout
    @BindView(R.id.entry_page_layout)
    ViewGroup entryPageLayout;
    //签证页照片layout
    @BindView(R.id.visa_page_layout)
    ViewGroup visaPageLayout;
    //入境页照片
    @BindView(R.id.entry_page_img)
    ImageView entryPageImg;
    //签证页照片
    @BindView(R.id.visa_page_img)
    ImageView visaPageImg;
    //签证（注）种类layout
    @BindView(R.id.visa_type_layout)
    ViewGroup visaTypelayout;
    //签证（注）有效期layout
    @BindView(R.id.visa_expired_date_layout)
    ViewGroup visaExpiredDateLayout;
    //签证（注）种类
    @BindView(R.id.visa_type_et)
    EditText visaTypeEt;
    //签证（注）有效期
    @BindView(R.id.visa_expired_date_et)
    EditText visaExpiredDateEt;
    //入境日期
//    @BindView(R.id.entry_date_et)
//    EditText entryDateEt;
    //入境口岸
//    @BindView(R.id.entry_port_et)
//    EditText entryPortEt;
    //停留事由
    @BindView(R.id.stay_reason_et)
    EditText stayReasonEt;
    //停留有效期
//    @BindView(R.id.stay_expired_date_et)
//    EditText stayExpiredDateEt;
    private int selectImg;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_entryvisa;
    }

    @Override
    protected void initializeToolbar() {
        titleTv.setText(R.string.visa_note_info);
        actionTv.setText(R.string.next_table);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @OnClick(R.id.toolbar_action_tv)
    public void actionTo(){
        setEntryVisaParams();
        if(isEntryVisaCompleted()){
            Intent intent = new Intent(this, HotlInfoActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void initializeActivity(Bundle savedInstanceState) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        if("1".equals(infoEntity.getCredential_type())|| "7".equals(infoEntity.getCredential_type())|| "11".equals(infoEntity.getCredential_type())){
            entryPageLayout.setVisibility(View.GONE);
            visaPageLayout.setVisibility(View.GONE);
            visaTypelayout.setVisibility(View.GONE);
            visaExpiredDateLayout.setVisibility(View.GONE);
        }else{
            entryPageLayout.setVisibility(View.VISIBLE);
            visaPageLayout.setVisibility(View.VISIBLE);
            visaTypelayout.setVisibility(View.VISIBLE);
            visaExpiredDateLayout.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(infoEntity.getEnter_image())){
                Glide.with(this).load(infoEntity.getEnter_image()).into(entryPageImg);
            }
            if(!TextUtils.isEmpty(infoEntity.getVisa_image())){
                Glide.with(this).load(infoEntity.getVisa_image()).into(visaPageImg);
            }
            if(!TextUtils.isEmpty(infoEntity.getVisa_type())){
                visaTypeEt.setText(App.getInstance().getVisa_type().get(infoEntity.getVisa_type()));
            }
            if(!TextUtils.isEmpty(infoEntity.getVisa_expired_date())){
                visaExpiredDateEt.setText(infoEntity.getVisa_expired_date());
            }
        }
//        if(!TextUtils.isEmpty(infoEntity.getEntry_port())){
//            entryPortEt.setText(App.getInstance().getEntry_port().get(infoEntity.getEntry_port()));
//        }
        if(!TextUtils.isEmpty(infoEntity.getStay_reason())){
            stayReasonEt.setText(App.getInstance().getStay_reason().get(infoEntity.getStay_reason()));
        }
//        if(!TextUtils.isEmpty(infoEntity.getEntry_date())){
//            entryDateEt.setText(infoEntity.getEntry_date());
//        }
//        if(!TextUtils.isEmpty(infoEntity.getStay_expired_date())){
//            stayExpiredDateEt.setText(infoEntity.getStay_expired_date());
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        setEntryVisaParams();
    }

    private void setEntryVisaParams(){
        //签证（注）有效期
        String visaExpiredDate = visaExpiredDateEt.getText().toString();
        infoEntity.setVisa_expired_date(visaExpiredDate);
        //入境日期
//        String entryDate = entryDateEt.getText().toString();
//        infoEntity.setEntry_date(entryDate);
        //停留有效期
//        String stayExpiredDate = stayExpiredDateEt.getText().toString();
//        infoEntity.setStay_expired_date(stayExpiredDate);
    }

    @OnClick({R.id.entry_page_layout, R.id.visa_page_layout})
    public void selectPicFrom(ViewGroup layout){
        switch (layout.getId()){
            case R.id.entry_page_layout:
                selectImg = 1;
                App.getInstance().samplePhotoIndex = Constant.ENTRY_PAGE;
                break;

            case R.id.visa_page_layout:
                selectImg = 2;
                App.getInstance().samplePhotoIndex = Constant.VISA_PAGE;
                break;
        }
        showSelectPicDialog(true);
    }

    @OnTouch({R.id.visa_expired_date_et})
    protected boolean selectDate(TextView textView, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP)
            super.selectDate(textView);
        return true;
    }

    @OnTouch({R.id.visa_type_et,R.id.stay_reason_et})
    protected boolean selectPosition(TextView editView, MotionEvent event){
        DataAdapter adapter;
        ListAlertDialog dialog = null;
        if(event.getAction() == MotionEvent.ACTION_UP){
            switch (editView.getId()){
                case R.id.visa_type_et:
                    adapter = new DataAdapter(this, transformToList(App.getInstance().getVisa_type()));
                    dialog = new ListAlertDialog(this, adapter, new IItemClickPosition() {
                        @Override
                        public void itemClickPosition(DataEntity dataEntity) {
                            visaTypeEt.setText(dataEntity.getValue());
                            infoEntity.setVisa_type(dataEntity.getKey());
                        }
                    });
                    break;

//                case R.id.entry_port_et:
//                    adapter = new DataAdapter(this, transformToListAZ(App.getInstance().getEntry_port()));
//                    dialog = new ListAlertDialog(this, adapter, new IItemClickPosition() {
//                        @Override
//                        public void itemClickPosition(DataEntity dataEntity) {
//                            entryPortEt.setText(dataEntity.getValue());
//                            infoEntity.setEntry_port(dataEntity.getKey());
//                        }
//                    });
//                    break;

                case R.id.stay_reason_et:
                    adapter = new DataAdapter(this, transformToList(App.getInstance().getStay_reason()));
                    dialog = new ListAlertDialog(this, adapter, new IItemClickPosition() {
                        @Override
                        public void itemClickPosition(DataEntity dataEntity) {
                            stayReasonEt.setText(dataEntity.getValue());
                            infoEntity.setStay_reason(dataEntity.getKey());
                        }
                    });
                    break;
            }
        }
        if(dialog!=null)
            dialog.show();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            getUntreatedFile(requestCode, data);
            Bitmap bitmap = BitmapUtil.commpressBitmap(untreatedFile);
            if(selectImg == 1){
                entryPageImg.setImageBitmap(bitmap);
            }else{
                visaPageImg.setImageBitmap(bitmap);
            }
            if(untreatedFile!=null){
                upLoadImage(BitmapUtil.compressImage(untreatedFile));
            }
        }
    }

    private void upLoadImage(String filePath) {
        File file = new File(filePath);
        // 创建 RequestBody，用于封装构建RequestBody
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/from-data"), file);
        // MultipartBody.Part  和后端约定好Key，这里的partName是用file
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        PostService postService = RetrofitManager.getRetrofit().create(PostService.class);
        Observable<Response<BaseResponse<UploadEntity>>> ob = postService.uploadFile("upload",body,"Bearer "+ App.getInstance().getUserEntity().getToken());
        RetrofitClient.client().request(ob, new ProgressSubscriber<UploadEntity>(this,true) {
            @Override
            protected void onSuccess(UploadEntity uploadEntity) {
                if(selectImg == 1){
                    infoEntity.setEnter_image(uploadEntity.getUrl());//入境页照片
                    Glide.with(EntryVisaActivity.this).load(uploadEntity.getUrl()).into(entryPageImg);
                }else{
                    infoEntity.setVisa_image(uploadEntity.getUrl());//签证页照片
                    Glide.with(EntryVisaActivity.this).load(uploadEntity.getUrl()).into(visaPageImg);
                }
            }

            @Override
            protected void onFailure(String message) {

            }
        });
    }
}
