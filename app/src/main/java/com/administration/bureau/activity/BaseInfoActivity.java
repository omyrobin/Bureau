package com.administration.bureau.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.administration.bureau.utils.ToastUtil;
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
 * Created by omyrobin on 2017/4/5.
 */

public class BaseInfoActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title_tv)
    TextView titleTv;
    @BindView(R.id.toolbar_action_tv)
    TextView actionTv;
    //护照信息页照片
    @BindView(R.id.avatar_img)
    ImageView avatarImg;
    //护照信息页照片
    @BindView(R.id.passport_info_img)
    ImageView passportInfoImg;
    //国家
    @BindView(R.id.country_et)
    EditText countryEt;
    //证件类型
    @BindView(R.id.credential_type_et)
    EditText credentialTypeEt;
    //证件号码
    @BindView(R.id.credential_et)
    EditText credentialEt;
    //证件有效期
    @BindView(R.id.credential_expired_date_et)
    EditText credentialExpiredDateEt;
    //人员类型
//    @BindView(R.id.person_type_et)
//    EditText personTypeEt;
    //人员地域类型
//    @BindView(R.id.person_area_type_et)
//    EditText personAreaTypeEt;
    //英文姓
    @BindView(R.id.firstname_et)
    EditText firstnameEt;
    //英文名
    @BindView(R.id.lastname_et)
    EditText lastnameEt;
    //中文名
    @BindView(R.id.chinese_name_et)
    EditText chineseNameEt;
    //性别
    @BindView(R.id.gender_et)
    EditText genderEt;
    //出生日期
    @BindView(R.id.birthday_et)
    EditText birthdayEt;
    //出生地
    @BindView(R.id.birthplace_et)
    EditText birthplaceEt;
    //职业
    @BindView(R.id.occupation_et)
    EditText occupationEt;
    //工作机构
    @BindView(R.id.working_organization_et)
    EditText workingOrganizationEt;
    //本人联系电话
    @BindView(R.id.phone_et)
    EditText phoneEt;
    //紧急联系人
    @BindView(R.id.emergency_contact_et)
    EditText emergencyContactEt;
    //紧急联系人电话
    @BindView(R.id.emergency_phone_et)
    EditText emergencyPhoneEt;

    private int selectImg;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_baseinfo;
    }

    @Override
    protected void initializeToolbar() {
        titleTv.setText(R.string.base_info);
        actionTv.setText(R.string.next_table);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @OnClick(R.id.toolbar_action_tv)
    public void actionTo(){
        setBaseInfoParams();
        if (isBaseInfoCompleted()){
            Intent intent = new Intent(this, EntryVisaActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void initializeActivity(Bundle savedInstanceState) {

    }

    @OnTouch({R.id.credential_expired_date_et, R.id.birthday_et})
    protected boolean selectDate(TextView textView, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP)
            super.selectDate(textView);
        return true;
    }

    @OnTouch({R.id.country_et,R.id.credential_type_et,R.id.gender_et,R.id.occupation_et})
    protected boolean selectPosition(TextView editView, MotionEvent event){
        DataAdapter adapter;
        ListAlertDialog dialog = null;
        if(event.getAction() == MotionEvent.ACTION_UP){
            switch (editView.getId()){
                case R.id.country_et:
                    adapter = new DataAdapter(this,transformToListAZ(App.getInstance().getCountry()));
                    dialog = new ListAlertDialog(this, adapter, new IItemClickPosition() {
                        @Override
                        public void itemClickPosition(DataEntity dataEntity) {
                            countryEt.setText(dataEntity.getValue());
                            infoEntity.setCountry(dataEntity.getKey());
                        }
                    });
                    break;

                case R.id.credential_type_et:
                    adapter = new DataAdapter(this,transformToList(App.getInstance().getCredential_type()));
                    dialog = new ListAlertDialog(this, adapter, new IItemClickPosition() {
                        @Override
                        public void itemClickPosition(DataEntity dataEntity) {
                            credentialTypeEt.setText(dataEntity.getValue());
                            infoEntity.setCredential_type(dataEntity.getKey());
                        }
                    });
                    break;

//                case R.id.person_type_et:
//                    adapter = new DataAdapter(this,transformToList(App.getInstance().getPerson_type()));
//                    dialog = new ListAlertDialog(this, adapter, new IItemClickPosition() {
//                        @Override
//                        public void itemClickPosition(DataEntity dataEntity) {
//                            personTypeEt.setText(dataEntity.getValue());
//                            infoEntity.setPerson_type(dataEntity.getKey());
//                        }
//                    });
//
//                    break;

//                case R.id.person_area_type_et:
//                    adapter = new DataAdapter(this,transformToList(App.getInstance().getPerson_area_type()));
//                    dialog = new ListAlertDialog(this, adapter, new IItemClickPosition() {
//                        @Override
//                        public void itemClickPosition(DataEntity dataEntity) {
//                            personAreaTypeEt.setText(dataEntity.getValue());
//                            infoEntity.setPerson_area_type(dataEntity.getKey());
//                        }
//                    });
//                    break;

                case R.id.gender_et:
                    adapter = new DataAdapter(this,transformToList(App.getInstance().getGender()));
                    dialog = new ListAlertDialog(this, adapter, new IItemClickPosition() {
                        @Override
                        public void itemClickPosition(DataEntity dataEntity) {
                            genderEt.setText(dataEntity.getValue());
                            infoEntity.setGender(dataEntity.getKey());
                        }
                    });
                    break;

                case R.id.occupation_et:
                    adapter =  new DataAdapter(this,transformToList(App.getInstance().getOccupation()));
                    dialog = new ListAlertDialog(this, adapter, new IItemClickPosition() {
                        @Override
                        public void itemClickPosition(DataEntity dataEntity) {
                            occupationEt.setText(dataEntity.getValue());
                            infoEntity.setOccupation(dataEntity.getKey());
                        }
                    });
                    break;
            }
            if(dialog!=null)
                dialog.show();
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(infoEntity.getAvatar())){
            Glide.with(this).load(infoEntity.getAvatar()).into(avatarImg);
        }
        if(!TextUtils.isEmpty(infoEntity.getPassport_image())){
            Glide.with(this).load(infoEntity.getPassport_image()).into(passportInfoImg);
        }
        if(!TextUtils.isEmpty(infoEntity.getCountry())){
            countryEt.setText(App.getInstance().getCountry().get(infoEntity.getCountry()+""));
        }
        if(!TextUtils.isEmpty(infoEntity.getCredential_type())){
            credentialTypeEt.setText(App.getInstance().getCredential_type().get(infoEntity.getCredential_type()));
        }
//        if(!TextUtils.isEmpty(infoEntity.getPerson_type())){
//            personTypeEt.setText(App.getInstance().getPerson_type().get(infoEntity.getPerson_type()));
//        }
//        if(!TextUtils.isEmpty(infoEntity.getPerson_area_type())){
//            personAreaTypeEt.setText(App.getInstance().getPerson_area_type().get(infoEntity.getPerson_area_type()));
//        }
        if(!TextUtils.isEmpty(infoEntity.getGender())){
            genderEt.setText(App.getInstance().getGender().get(infoEntity.getGender()));
        }
        if(!TextUtils.isEmpty(infoEntity.getOccupation())){
            occupationEt.setText(App.getInstance().getOccupation().get(infoEntity.getOccupation()));
        }
        if(!TextUtils.isEmpty(infoEntity.getCredential())){
            credentialEt.setText(infoEntity.getCredential());
        }
        if(!TextUtils.isEmpty(infoEntity.getCredential_expired_date())){
            credentialExpiredDateEt.setText(infoEntity.getCredential_expired_date());
        }
        if(!TextUtils.isEmpty(infoEntity.getFirstname())){
            firstnameEt.setText(infoEntity.getFirstname());
        }
        if(!TextUtils.isEmpty(infoEntity.getLastname())){
            lastnameEt.setText(infoEntity.getLastname());
        }
        if(!TextUtils.isEmpty(infoEntity.getChinese_name())){
            chineseNameEt.setText(infoEntity.getChinese_name());
        }
        if(!TextUtils.isEmpty(infoEntity.getBirthday())){
            birthdayEt.setText(infoEntity.getBirthday());
        }
        if(!TextUtils.isEmpty(infoEntity.getCredential())){
            chineseNameEt.setText(infoEntity.getCredential());
        }
        if(!TextUtils.isEmpty(infoEntity.getBirthplace())){
            birthplaceEt.setText(infoEntity.getBirthplace());
        }
        if(!TextUtils.isEmpty(infoEntity.getWorking_organization())){
            workingOrganizationEt.setText(infoEntity.getWorking_organization());
        }
        if(!TextUtils.isEmpty(infoEntity.getPhone())){
            phoneEt.setText(infoEntity.getPhone());
        }
        if(!TextUtils.isEmpty(infoEntity.getEmergency_contact())){
            emergencyContactEt.setText(infoEntity.getEmergency_contact());
        }
        if(!TextUtils.isEmpty(infoEntity.getEmergency_phone())){
            emergencyPhoneEt.setText(infoEntity.getEmergency_phone());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        setBaseInfoParams();
    }

    private void setBaseInfoParams(){
        //证件号码
        String credential_num = credentialEt.getText().toString();
        infoEntity.setCredential(credential_num);
        //证件有效期
        String credentialExpiredDate = credentialExpiredDateEt.getText().toString();
        infoEntity.setCredential_expired_date(credentialExpiredDate);
        //英文姓
        String firstname= firstnameEt.getText().toString();
        infoEntity.setFirstname(firstname);
        //英文名
        String lastname = lastnameEt.getText().toString();
        infoEntity.setLastname(lastname);
        //中文名
        String chinestname = chineseNameEt.getText().toString();
        infoEntity.setChinese_name(chinestname);
        //出生日期
        String birthday= birthdayEt.getText().toString();
        infoEntity.setBirthday(birthday);
        //出生地
        String birthplace = birthplaceEt.getText().toString();
        infoEntity.setBirthplace(birthplace);
        //工作机构
        String workingOrganization= workingOrganizationEt.getText().toString();
        infoEntity.setWorking_organization(workingOrganization);
        //本人联系电话
        String phone= phoneEt.getText().toString();
        infoEntity.setPhone(phone);
        //紧急联系人
        String emergencyContact = emergencyContactEt.getText().toString();
        infoEntity.setEmergency_contact(emergencyContact);
        //紧急联系人电话
        String emergencyPhone = emergencyPhoneEt.getText().toString();
        infoEntity.setEmergency_phone(emergencyPhone);
    }

    @OnClick({R.id.passport_info_layout, R.id.user_shelf_layout})
    public void selectPicFrom(ViewGroup viewGroup){
        switch (viewGroup.getId()){
            case R.id.user_shelf_layout:
                selectImg = 1;
                break;

            case R.id.passport_info_layout:
                selectImg = 2;
                break;
        }
        showSelectPicDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            getUntreatedFile(requestCode, data);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inJustDecodeBounds = false;
            Log.i("TAG",  "URL -------------------:   "+untreatedFile);
            Bitmap bitmap = BitmapFactory.decodeFile(untreatedFile, options);
            if(selectImg == 1){
                avatarImg.setImageBitmap(bitmap);
            }else{
                passportInfoImg.setImageBitmap(bitmap);
            }
            if(untreatedFile!=null){
                upLoadImage(BitmapUtil.compressImage(untreatedFile));
            }
        }
    }

    private void upLoadImage(String untreatedFile) {
        File file = new File(untreatedFile);

        // 创建 RequestBody，用于封装构建RequestBody
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        // MultipartBody.Part  和后端约定好Key，这里的partName是用file
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        PostService postService = RetrofitManager.getRetrofit().create(PostService.class);
        Observable<Response<BaseResponse<UploadEntity>>> ob = postService.uploadFile("upload",body,"Bearer "+ App.getInstance().getUserEntity().getToken());
        RetrofitClient.client().request(ob, new ProgressSubscriber<UploadEntity>(this,true) {
            @Override
            protected void onSuccess(UploadEntity uploadEntity) {
                if(selectImg == 1){
                    infoEntity.setAvatar(uploadEntity.getUrl());//本人照片
                }else{
                    infoEntity.setPassport_image(uploadEntity.getUrl());//护照信息页照片
                }
            }

            @Override
            protected void onFailure(String message) {

            }
        });
    }
}
