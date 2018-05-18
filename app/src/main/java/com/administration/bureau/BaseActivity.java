package com.administration.bureau;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.TextView;

import com.administration.bureau.activity.BaseInfoActivity;
import com.administration.bureau.activity.MainActivity;
import com.administration.bureau.activity.RegisterUserActivity;
import com.administration.bureau.activity.SamplePhotoActivity;
import com.administration.bureau.constant.Constant;
import com.administration.bureau.entity.BaseResponse;
import com.administration.bureau.entity.DataEntity;
import com.administration.bureau.entity.UserRegisterInfoEntity;
import com.administration.bureau.entity.eventbus.UserRegisterEvent;
import com.administration.bureau.http.ProgressSubscriber;
import com.administration.bureau.http.RetrofitClient;
import com.administration.bureau.http.RetrofitManager;
import com.administration.bureau.model.PostService;
import com.administration.bureau.model.PutService;
import com.administration.bureau.utils.FileUtil;
import com.administration.bureau.utils.KitKatUri;
import com.administration.bureau.utils.ProviderUtil;
import com.administration.bureau.utils.SharedPreferencesUtil;
import com.administration.bureau.utils.ToastUtil;
import com.administration.bureau.widget.pic.ISelectPic;
import com.administration.bureau.widget.pic.SelectPicDialog;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import retrofit2.Response;
import rx.Observable;

/**
 * Created by omyrobin on 2017/4/5.
 */

public abstract class BaseActivity extends AppCompatActivity implements ISelectPic{

    protected SelectPicDialog mDialog;

    protected String untreatedFile;

    protected UserRegisterInfoEntity infoEntity;

    private AlertDialog premissionsDialog;

    private static final int MSG_SET_ALIAS = 1001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if(App.mAppStatus == -1){
            protectApp();
        }
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        infoEntity = App.getInstance().getInfoEntity();
        initializeToolbar();
        initializeActivity(savedInstanceState);
    }

    protected abstract int getLayoutId();

    protected abstract void initializeToolbar();

    protected abstract void initializeActivity(Bundle savedInstanceState);

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void protectApp() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("action", "force_kill");
        startActivity(intent);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(defInAnim(), R.anim.slide_no_anim);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(defInAnim(), R.anim.slide_no_anim);
    }

    @Override
    public void finish() {
        super.finish();
        if(defOutAnim() == -1)
            return;
        overridePendingTransition(R.anim.slide_no_anim, defOutAnim());
    }

    protected int defInAnim() {
        return R.anim.slide_right_in;
    }

    protected int defOutAnim() {
        return R.anim.slide_right_out;
    }

    protected void showSelectPicDialog(boolean isSample){
        if(mDialog==null){
            mDialog = new SelectPicDialog(this, this);
        }
        mDialog.setIsSample(isSample);
        mDialog.show();
    }

    /**
     * 拍照
     */
    @Override
    public void selectOneItem() {
        mDialog.dismiss();
        if(checkSelfPermissionCamera() && checkSelfPermissionWrite()){
            File file = new File(FileUtil.getCamoraFile(),System.currentTimeMillis() + ".jpg");
            untreatedFile  = file.getAbsolutePath();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            Uri uri;
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
                uri = Uri.fromFile(file);
            }else{
                /**
                 * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
                 * 并且这样可以解决MIUI系统上拍照返回size为0的情况
                 */
                uri = FileProvider.getUriForFile(this, ProviderUtil.getFileProviderName(this), file);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, Constant.SELECT_CAMERA);
        }
    }

    /**
     * 从相册选择
     */
    @Override
    public void selectTwoItem() {
        mDialog.dismiss();
        if(checkSelfPermissionWrite()){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, Constant.SELECT_CHOOSER);
        }
    }

    @Override
    public void selectSampleItem() {
        mDialog.dismiss();
        Intent intent = new Intent(this,SamplePhotoActivity.class);
        startActivity(intent);
    }

    /**
     * 获取未处理的图片路径
     * @param requestCode 相册还是拍照
     * @param data 数据
     * @return
     */
    protected String getUntreatedFile(int requestCode,Intent data){
        if(requestCode == Constant.SELECT_CHOOSER){//是相册
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,  filePathColumn, null, null, null);
            if(cursor!=null){
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                untreatedFile = cursor.getString(columnIndex);
                cursor.close();
            }else{
                /***4.4得到的uri,需要以下方法来获取文件的路径***/
                untreatedFile = KitKatUri.getPath(this, selectedImage);
            }
        }
        return untreatedFile;
    }

    /***申请----CAMERA----权限***/
    protected boolean checkSelfPermissionCamera(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //权限还没有授予，需要在这里写申请权限的代码
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, Constant.MY_PERMISSIONS_REQUEST_CALL_CAMERA);
            return false;
        }else {
            //权限已经被授予，在这里直接写要执行的相应方法即可
            return true;
        }
    }

    /***申请----WRITE_EXTERNAL_STORAGE----权限***/
    protected boolean checkSelfPermissionWrite(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //权限还没有授予，需要在这里写申请权限的代码
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.MY_PERMISSIONS_REQUEST_CALL_CHOOSER);
            return false;
        }else {
            //权限已经被授予，在这里直接写要执行的相应方法即可
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constant.MY_PERMISSIONS_REQUEST_CALL_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                selectOneItem();
            } else {
                // Permission Denied
//                ToastUtil.showShort(R.string.permission_denied_camera);
                showPremissionsDialog(getString(R.string.permission_denied_camera));
            }
        }


        if (requestCode == Constant.MY_PERMISSIONS_REQUEST_CALL_CHOOSER) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                selectTwoItem();
            } else {
                // Permission Denied
//                ToastUtil.showShort(R.string.permission_denied_write);
                showPremissionsDialog(getString(R.string.permission_denied_write));
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showPremissionsDialog(String message){
        premissionsDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.hint))
                .setMessage(message)
                .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE) {
                            // 进入设置系统应用权限界面
                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
                            startActivity(intent);
                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {// 运行系统在5.x环境使用
                            // 进入设置系统应用权限界面
                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
                            startActivity(intent);
                        }
                        premissionsDialog.dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.cancle),null)
                .create();
        premissionsDialog.show();
    }

    protected void selectDate(final TextView textView){
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String currMotnth;
                String currDay;

                int thisMotnth = month+1;
                currMotnth = thisMotnth+"";
                if(thisMotnth+1<10){
                    currMotnth = "0"+thisMotnth;
                }
                currDay = day+"";
                if(day<10){
                    currDay = "0"+day;
                }
                textView.setText(year+"-"+currMotnth+"-"+currDay);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) ).show();
    }

    protected ArrayList<DataEntity> transformToList(HashMap<String, String> map){
        ArrayList<DataEntity> dataEntities = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()){
            DataEntity dataEntity = new DataEntity(entry.getKey(),entry.getValue());
            dataEntities.add(dataEntity);
        }
        return dataEntities;
    }

    protected ArrayList<DataEntity> transformToListAZ(HashMap<String, String> map){
        ArrayList<DataEntity> dataEntities = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()){
            DataEntity dataEntity = new DataEntity(entry.getKey(),entry.getValue());
            dataEntities.add(dataEntity);
        }
        Collections.sort(dataEntities);
        return dataEntities;
    }

    protected void registrationInfo(){
        PostService postService = RetrofitManager.getRetrofit().create(PostService.class);
        int user_id = App.getInstance().getUserEntity().getUser().getId();
        String token = "Bearer "+ App.getInstance().getUserEntity().getToken();
        Observable<Response<BaseResponse<UserRegisterInfoEntity>>> observable =  postService.registerInfo(user_id, getRequestParams(), token);
        RetrofitClient.client().request(observable, new ProgressSubscriber<UserRegisterInfoEntity>(this) {
            @Override
            protected void onSuccess(UserRegisterInfoEntity userRegisterInfoEntity) {
                ToastUtil.showShort(getString(R.string.info_succeed));
                //TODO 刷新HomePageAdapter
                EventBus.getDefault().post(new UserRegisterEvent());
                finish();
            }

            @Override
            protected void onFailure(String message) {
                ToastUtil.showShort(getString(R.string.info_falid));
            }
        });
    }

    protected void registrationInfoAgain(){
        PutService putService = RetrofitManager.getRetrofit().create(PutService.class);
        int user_id = App.getInstance().getUserEntity().getUser().getId();
        int id = App.getInstance().getInfoEntity().getId();
        String token = "Bearer "+ App.getInstance().getUserEntity().getToken();
        Observable<Response<BaseResponse<Boolean>>> observable =  putService.registerInfoAgain(user_id, id,getRequestParams(), token);
        RetrofitClient.client().request(observable, new ProgressSubscriber<Boolean>(this) {
            @Override
            protected void onSuccess(Boolean data) {
                ToastUtil.showShort(getString(R.string.info_succeed));
                //TODO 刷新HomePageAdapter
                EventBus.getDefault().post(new UserRegisterEvent());
                finish();
            }

            @Override
            protected void onFailure(String message) {
                ToastUtil.showShort(getString(R.string.info_falid));
            }
        });
    }

    protected HashMap<String,Object> getRequestParams(){
        HashMap<String, Object> params = new HashMap<>();
        params.put("avatar",infoEntity.getAvatar());
        params.put("passport_image",infoEntity.getPassport_image());
        params.put("country",infoEntity.getCountry());
        params.put("credential",infoEntity.getCredential());
        params.put("credential_type",infoEntity.getCredential_type());
//        params.put("credential_expired_date",infoEntity.getCredential_expired_date());
//        params.put("person_type",infoEntity.getPerson_type());
//        params.put("person_area_type",infoEntity.getPerson_area_type());
        params.put("firstname",infoEntity.getFirstname());
        params.put("lastname",infoEntity.getLastname());
//        params.put("chinese_name",infoEntity.getChinese_name());
        params.put("gender",infoEntity.getGender());
//        params.put("birthday",infoEntity.getBirthday());
        params.put("birthplace",infoEntity.getBirthplace());
        if(!TextUtils.isEmpty(infoEntity.getOccupation()))
            params.put("occupation",infoEntity.getOccupation());
        if(!TextUtils.isEmpty(infoEntity.getWorking_organization()))
            params.put("working_organization",infoEntity.getWorking_organization());
//        params.put("phone",infoEntity.getPhone());
        params.put("emergency_contact",infoEntity.getEmergency_contact());
        params.put("emergency_phone",infoEntity.getEmergency_phone());

        if(!"1".equals(infoEntity.getCredential_type()) && !"7".equals(infoEntity.getCredential_type()) && !"11".equals(infoEntity.getCredential_type())){
            params.put("enter_image",infoEntity.getEnter_image());
            params.put("visa_image",infoEntity.getVisa_image());
//            params.put("visa_type",infoEntity.getVisa_type());
//            params.put("visa_expired_date",infoEntity.getVisa_expired_date());
        }
//        params.put("entry_date",infoEntity.getEntry_date());
//        params.put("entry_port",infoEntity.getEntry_port());
//        params.put("stay_reason",infoEntity.getStay_reason());
//        params.put("stay_expired_date",infoEntity.getStay_expired_date());
//        params.put("checkout_date",infoEntity.getCheckout_date());
        params.put("house_address",infoEntity.getHouse_address());
//        params.put("house_type",infoEntity.getHouse_type());
        if(App.getInstance().isHave()){
            Gson gson = new Gson();
            params.put("landlord_identity_image",infoEntity.getLandlord_identity_image());
            Log.i("TAG", gson.toJson(infoEntity.getHouse_contract_image()).toString());
            for(int i=0; i<infoEntity.getHouse_contract_image().length; i++){
                params.put("house_contract_image"+"["+i+"]",infoEntity.getHouse_contract_image()[i]);
            }
        }else{
//            params.put("checkin_date",infoEntity.getCheckin_date());
//            params.put("police_station",infoEntity.getPolice_station());
//            params.put("community",infoEntity.getCommunity());
            params.put("landlord_country",infoEntity.getLandlord_country());
            params.put("landlord_identity",infoEntity.getLandlord_identity());
            params.put("landlord_name",infoEntity.getLandlord_name());
            params.put("landlord_gender",infoEntity.getLandlord_gender());
            params.put("landlord_phone",infoEntity.getLandlord_phone());
            //提交坐标
            params.put("location",infoEntity.getLocation());
            params.put("location_address",infoEntity.getLocation_address());
        }


        Log.i("TAG",infoEntity.toString());
        return params;
    }

    protected boolean isBaseInfoCompleted(){
        if(infoEntity == null){
            return false;
        }
        if(TextUtils.isEmpty(infoEntity.getAvatar())){
            ToastUtil.showShort(getString(R.string.my_photo_null));
            return false;
        }
        if(TextUtils.isEmpty(infoEntity.getPassport_image())){
            ToastUtil.showShort(getString(R.string.passport_image_null));
            return false;
        }
        if(TextUtils.isEmpty(infoEntity.getCountry())){
            ToastUtil.showShort(getString(R.string.country_null));
            return false;
        }
        if(TextUtils.isEmpty(infoEntity.getCredential_type())){
            ToastUtil.showShort(getString(R.string.credential_type_null));
            return false;
        }
        if(TextUtils.isEmpty(infoEntity.getCredential())){
            ToastUtil.showShort(getString(R.string.credential_null));
            return false;
        }
//        if(TextUtils.isEmpty(infoEntity.getCredential_expired_date())){
//            ToastUtil.showShort(getString(R.string.credential_expired_null));
//            return false;
//        }
//        if(TextUtils.isEmpty(infoEntity.getPerson_type())){
//            ToastUtil.showShort("人员类型不能为空");
//            return false;
//        }
//        if(TextUtils.isEmpty(infoEntity.getPerson_area_type())){
//            ToastUtil.showShort("人员地域类型不能为空");
//            return false;
//        }
        if(TextUtils.isEmpty(infoEntity.getLastname())){
            ToastUtil.showShort(getString(R.string.last_name_null));
            return false;
        }
        if(TextUtils.isEmpty(infoEntity.getGender())){
            ToastUtil.showShort(getString(R.string.gender_null));
            return false;
        }
//        if(!TextUtils.isEmpty(infoEntity.getFirstname())){
//            return false;
//        }
//        if(!TextUtils.isEmpty(infoEntity.getChinese_name())){
//            return false;
//        }
//        if(TextUtils.isEmpty(infoEntity.getBirthday())){
//            ToastUtil.showShort(getString(R.string.birthday_null));
//            return false;
//        }
        if(TextUtils.isEmpty(infoEntity.getBirthplace())){
            ToastUtil.showShort(getString(R.string.birthplace_null));
            return false;
        }
//        if(TextUtils.isEmpty(infoEntity.getOccupation())){
//            ToastUtil.showShort(getString(R.string.occupation_null));
//            return false;
//        }
//        if(TextUtils.isEmpty(infoEntity.getWorking_organization())){
//            ToastUtil.showShort(getString(R.string.working_organization_null));
//            return false;
//        }
//        if(TextUtils.isEmpty(infoEntity.getPhone())){
//            ToastUtil.showShort(getString(R.string.phone_null));
//            return false;
//        }
//        if(TextUtils.isEmpty(infoEntity.getEmergency_contact())){
//            ToastUtil.showShort(getString(R.string.emergency_contact_null));
//            return false;
//        }
//        if(TextUtils.isEmpty(infoEntity.getEmergency_phone())){
//            ToastUtil.showShort(getString(R.string.emergency_phone_null));
//            return false;
//        }
        return true;
    }

    protected boolean isEntryVisaCompleted() {
        if(infoEntity == null){
            return false;
        }
        if(!"1".equals(infoEntity.getCredential_type()) && !"7".equals(infoEntity.getCredential_type()) && !"11".equals(infoEntity.getCredential_type())){
            if(TextUtils.isEmpty(infoEntity.getEnter_image())){
                ToastUtil.showShort(getString(R.string.enter_image_null));
                return false;
            }
            if(TextUtils.isEmpty(infoEntity.getVisa_image())){
                ToastUtil.showShort(getString(R.string.visa_image_null));
                return false;
            }
//            if(TextUtils.isEmpty(infoEntity.getVisa_type())){
//                ToastUtil.showShort(getString(R.string.visa_type_null));
//                return false;
//            }
//            if(TextUtils.isEmpty(infoEntity.getVisa_expired_date())){
//                ToastUtil.showShort(getString(R.string.visa_expired_null));
//                return false;
//            }
        }
//        if(TextUtils.isEmpty(infoEntity.getEntry_date())){
//            ToastUtil.showShort(getString(R.string.entry_date_null));
//            return false;
//        }
//        if(TextUtils.isEmpty(infoEntity.getEntry_port())){
//            ToastUtil.showShort(getString(R.string.entry_port_null));
//            return false;
//        }
//        if(TextUtils.isEmpty(infoEntity.getStay_reason())){
//            ToastUtil.showShort(getString(R.string.stay_reason_null));
//            return false;
//        }
        return true;
    }

    protected boolean isHotlInfoCompleted(){
        if(infoEntity == null){
            return false;
        }
//        if(TextUtils.isEmpty(infoEntity.getCheckin_date())){
//            ToastUtil.showShort(getString(R.string.check_in_date_null));
//            return false;
//        }
//        if(TextUtils.isEmpty(infoEntity.getCheckout_date())){
//            ToastUtil.showShort(getString(R.string.check_out_date_null));
//            return false;
//        }
//        if(TextUtils.isEmpty(infoEntity.getPolice_station())){
//            ToastUtil.showShort("所属派出所不能为空");
//            return false;
//        }
//        if(TextUtils.isEmpty(infoEntity.getCommunity())){
//            return false;
//        }
        if(TextUtils.isEmpty(infoEntity.getHouse_address())){
            ToastUtil.showShort(getString(R.string.address_null));
            return false;
        }
//        if(TextUtils.isEmpty(infoEntity.getHouse_type())){
//            ToastUtil.showShort(getString(R.string.house_typr_null));
//            return false;
//        }
        if(App.getInstance().isHave()){
            if(TextUtils.isEmpty(infoEntity.getLandlord_identity_image())){
                ToastUtil.showShort(getString(R.string.landlord_identity_pic_null));
                return false;
            }
            if(infoEntity.getHouse_contract_image() == null || infoEntity.getHouse_contract_image().length == 0){
                ToastUtil.showShort(getString(R.string.contract_of_tenancy_photo_null));
                return false;
            }
        }else{
            if(TextUtils.isEmpty(infoEntity.getLandlord_country())){
                ToastUtil.showShort(getString(R.string.landlord_country_null));
                return false;
            }
            if(TextUtils.isEmpty(infoEntity.getLandlord_gender())){
                ToastUtil.showShort(getString(R.string.landlord_gender_null));
                return false;
            }
            if(TextUtils.isEmpty(infoEntity.getLandlord_identity())){
                ToastUtil.showShort(getString(R.string.landlord_identity_null));
                return false;
            }
            if(TextUtils.isEmpty(infoEntity.getLandlord_name())){
                ToastUtil.showShort(getString(R.string.landlord_name_null));
                return false;
            }
            if(TextUtils.isEmpty(infoEntity.getLandlord_phone())){
                ToastUtil.showShort(getString(R.string.landlord_phone_null));
                return false;
            }
        }
        return true;
    }

    protected void setAlias() {
        // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, App.getInstance().getUserEntity().getUser().getPhone()));
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    if(App.getInstance().getUserEntity()!=null)
                        SharedPreferencesUtil.setParam(BaseActivity.this,App.getInstance().getUserEntity().getUser().getPhone(),true);
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
            }
        }
    };

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    break;
                default:
                    break;
            }
        }
    };

}
