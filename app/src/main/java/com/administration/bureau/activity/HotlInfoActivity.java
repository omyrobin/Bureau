package com.administration.bureau.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.administration.bureau.App;
import com.administration.bureau.BaseActivity;
import com.administration.bureau.R;
import com.administration.bureau.adapter.DataAdapter;
import com.administration.bureau.adapter.PhotosAdapter;
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
import com.administration.bureau.utils.FileUtil;
import com.administration.bureau.utils.ToastUtil;
import com.administration.bureau.widget.ListAlertDialog;
import com.bumptech.glide.Glide;
import com.yanzhenjie.album.Album;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

public class HotlInfoActivity extends BaseActivity implements PhotosAdapter.OnRvItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title_tv)
    TextView titleTv;
    @BindView(R.id.toolbar_action_tv)
    TextView actionTv;
    //入住日期
//    @BindView(R.id.check_in_date_et)
//    EditText checkInDateEt;
    //我有房主身份证和房屋租赁合同
    @BindView(R.id.contract_of_tenancy_sw)
    SwitchCompat contractOfTenancySw;
    //拟定离开日期
//    @BindView(R.id.check_out_date_et)
//    EditText checkOutDateEt;
    //有租赁合同的Layout
    @BindView(R.id.house_have_layout)
    View houseHaveLayout;
    //房主身份证照片
    @BindView(R.id.landlord_identity_img)
    ImageView landlordIdentityImg;
    //房屋租赁合同照片数量
    @BindView(R.id.photos_number_tv)
    TextView photosNumberTv;
    //房屋租赁合同照片
    @BindView(R.id.contract_of_tenancy_rv)
    RecyclerView contractOfTenancyRv;
    //没有有租赁合同的Layout
    @BindView(R.id.house_nothave_layout)
    View houseNotHaveLayout;
    //详细地址
    @BindView(R.id.house_address_et)
    EditText houseAddressEt;
    //所属派出所
//    @BindView(R.id.police_station_et)
//    EditText policeStationEt;
    //所属社区
//    @BindView(R.id.community_et)
//    EditText communityEt;
    //住房种类
//    @BindView(R.id.house_type_et)
//    EditText houseTypeEt;
    //房主国家
    @BindView(R.id.landlord_country_et)
    EditText landlordCountryEt;
    //房主身份证号
    @BindView(R.id.landlord_identity_et)
    EditText landlordIdentityEt;
    //房主中文姓名
    @BindView(R.id.landlord_name_et)
    EditText landlordNameEt;
    //房主性别
    @BindView(R.id.landlord_gender_et)
    EditText landlordGenderEt;
    //房主联系电话
    @BindView(R.id.landlord_phone_et)
    EditText landlordPhoneEt;

    private List<String> photos;

    private List<String> net_photos;

    private PhotosAdapter adapter;

    private int selectImg;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_hotlinfo;
    }

    @Override
    protected void initializeToolbar() {
        titleTv.setText(R.string.hotel_information);
        actionTv.setText(R.string.submit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @OnClick(R.id.toolbar_action_tv)
    public void actionTo(){
        setHotlinfoParams();
        if(isBaseInfoCompleted() && isEntryVisaCompleted() && isHotlInfoCompleted()){
            if(App.getInstance().getInfoEntity().getStatus() == 1){
                registrationInfoAgain();
            }else{
                registrationInfo();
            }
        }
    }

//    @OnTouch({R.id.check_out_date_et})
//    protected boolean selectDate(TextView textView, MotionEvent event) {
//        if(event.getAction() == MotionEvent.ACTION_UP)
//            super.selectDate(textView);
//        return true;
//    }

    @Override
    protected void initializeActivity(Bundle savedInstanceState) {
        setOnCheckedChangeListener();
        initRecyclerView();
    }

    private void initRecyclerView(){
        net_photos = new ArrayList<>();
        photos = new ArrayList<>();
        photos.add("Add");

        LinearLayoutManager layout = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        contractOfTenancyRv.setLayoutManager(layout);
        adapter = new PhotosAdapter(this,photos,this);
        contractOfTenancyRv.setAdapter(adapter);

        if(infoEntity.getHouse_contract_image() != null && infoEntity.getHouse_contract_image().length>0){
            List<String> list = Arrays.asList(infoEntity.getHouse_contract_image());
            net_photos.addAll(list);
            adapter.addPhotoPath(list);
        }
        photosNumberTv.setText(getString(R.string.photo_number,adapter.getPhotoCount()));
    }

    private void setOnCheckedChangeListener(){
        //吐槽下ButterKnife不能直接设置include的id来找整个view
        contractOfTenancySw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    houseHaveLayout.setVisibility(View.VISIBLE);
                    houseNotHaveLayout.setVisibility(View.GONE);
                }else{
                    houseHaveLayout.setVisibility(View.GONE);
                    houseNotHaveLayout.setVisibility(View.VISIBLE);
                }
                App.getInstance().setHave(isChecked);
            }
        });
    }

    @OnTouch({R.id.house_address_et, R.id.landlord_country_et,R.id.landlord_gender_et})
    protected boolean selectPosition(TextView editView, MotionEvent event){
        DataAdapter adapter;
        ListAlertDialog dialog = null;
        if(event.getAction() == MotionEvent.ACTION_UP){
            switch (editView.getId()){
                case R.id.house_address_et:
                    Intent intent = new Intent(this, MapActivity.class);
                    startActivity(intent);
//                    if(FileUtil.hasSoFile(this)){
//                        Intent intent = new Intent(this, MapActivity.class);
//                        startActivity(intent);
//                    }else {
//                        ToastUtil.showShort("地图相关组件正在加载中,请稍后重试");
//                    }
                    break;

//                case R.id.police_station_et:
//                    adapter = new DataAdapter(this, transformToList(App.getInstance().getPolice_station()));
//                    dialog = new ListAlertDialog(this, adapter, new IItemClickPosition() {
//                        @Override
//                        public void itemClickPosition(DataEntity dataEntity) {
//                            policeStationEt.setText(dataEntity.getValue());
//                            infoEntity.setPolice_station(dataEntity.getKey());
//                            communityEt.setText("");
//                            infoEntity.setCommunity("");
//                        }
//                    });
//                    break;

//                case R.id.community_et:
//                    DataAdapter dataAdapter = new DataAdapter(this, transformToList(App.getInstance().getAllCommunity().get(infoEntity.getPolice_station())));
//                    dialog = new ListAlertDialog(this, dataAdapter, new IItemClickPosition() {
//                        @Override
//                        public void itemClickPosition(DataEntity dataEntity) {
//                            communityEt.setText(dataEntity.getValue());
//                            infoEntity.setCommunity(dataEntity.getKey());
//                        }
//                    });
//                    break;

//                case R.id.house_type_et:
//                    adapter = new DataAdapter(this, transformToList(App.getInstance().getHouse_type()));
//                    dialog = new ListAlertDialog(this, adapter, new IItemClickPosition() {
//                        @Override
//                        public void itemClickPosition(DataEntity dataEntity) {
//                            houseTypeEt.setText(dataEntity.getValue());
//                            infoEntity.setHouse_type(dataEntity.getKey());
//                        }
//                    });
//                    break;

                case R.id.landlord_country_et:
                    adapter = new DataAdapter(this, transformToListAZ(App.getInstance().getCountry()));
                    dialog = new ListAlertDialog(this, adapter, new IItemClickPosition() {
                        @Override
                        public void itemClickPosition(DataEntity dataEntity) {
                            landlordCountryEt.setText(dataEntity.getValue());
                            infoEntity.setLandlord_country(dataEntity.getKey());
                        }
                    });
                    break;

                case R.id.landlord_gender_et:
                    adapter = new DataAdapter(this, transformToList(App.getInstance().getGender()));
                    dialog = new ListAlertDialog(this, adapter, new IItemClickPosition() {
                        @Override
                        public void itemClickPosition(DataEntity dataEntity) {
                            landlordGenderEt.setText(dataEntity.getValue());
                            infoEntity.setLandlord_gender(dataEntity.getKey());
                        }
                    });
                    break;
            }
        }
        if(dialog!=null)
            dialog.show();
        return true;
    }

    @OnClick({R.id.landlord_identity_pic_layout})
    public void selectPicFrom(ViewGroup layout){
        showSelectPicDialog(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if(requestCode == Constant.SELECT_CONTRACT_OF_TENANCY){
                selectImg = 1;
                List<String> pathList = Album.parseResult(data);
                Log.i("NET_PHOTOS_SIZE", pathList.size() + "");
                adapter.addPhotoPath(pathList);
                for(int i=0; i < pathList.size(); i++){
//                    getUntreatedFile(requestCode, data);
                    if(pathList!=null){
                        upLoadImage(BitmapUtil.compressImage(pathList.get(i)));
                    }
                }
                photosNumberTv.setText(getString(R.string.photo_number,adapter.getPhotoCount()));
            }else{
                selectImg = 0;
                getUntreatedFile(requestCode, data);
                Bitmap bitmap = BitmapUtil.commpressBitmap(untreatedFile);
                landlordIdentityImg.setImageBitmap(bitmap);
                if(untreatedFile!=null){
                    upLoadImage(BitmapUtil.compressImage(untreatedFile));
                }
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
                if(selectImg == 0){
                    infoEntity.setLandlord_identity_image(uploadEntity.getUrl());//房主身份证照片
                    Glide.with(HotlInfoActivity.this).load(uploadEntity.getUrl()).into(landlordIdentityImg);
                }else{
                    if(!net_photos.contains(uploadEntity.getUrl()))
                        net_photos.add(uploadEntity.getUrl());
                    for(int i=0; i<net_photos.size(); i++){
                        Log.i("NET_PHOTOS_URL", net_photos.get(i));
                    }
                    infoEntity.setHouse_contract_image(net_photos.toArray(new String[net_photos.size()]));//房屋租赁合同
                }
            }

            @Override
            protected void onFailure(String message) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if(!TextUtils.isEmpty(infoEntity.getCheckin_date())){
//            checkInDateEt.setText(infoEntity.getCheckin_date());
//        }
//        if(!TextUtils.isEmpty(infoEntity.getCheckout_date())){
//            checkOutDateEt.setText(infoEntity.getCheckout_date());
//        }
        if(!TextUtils.isEmpty(infoEntity.getHouse_address())){
            houseAddressEt.setText(infoEntity.getHouse_address());
        }
        if(!TextUtils.isEmpty(App.getInstance().getLocation())){

        }
//        if(!TextUtils.isEmpty(infoEntity.getPolice_station())){
//            policeStationEt.setText(App.getInstance().getPolice_station().get(infoEntity.getPolice_station()));
//        }
//        if(!TextUtils.isEmpty(infoEntity.getCommunity())){
//            communityEt.setText(App.getInstance().getAllCommunity().get(infoEntity.getPolice_station()).get(infoEntity.getCommunity()));
//        }
//        if(!TextUtils.isEmpty(infoEntity.getHouse_type())){
//            houseTypeEt.setText(App.getInstance().getHouse_type().get(infoEntity.getHouse_type()));
//        }
        if(!TextUtils.isEmpty(infoEntity.getLandlord_country())){
            landlordCountryEt.setText(App.getInstance().getCountry().get(infoEntity.getLandlord_country()));
        }
        if(!TextUtils.isEmpty(infoEntity.getLandlord_identity())){
            landlordIdentityEt.setText(infoEntity.getLandlord_identity());
        }
        if(!TextUtils.isEmpty(infoEntity.getLandlord_name())){
            landlordNameEt.setText(infoEntity.getLandlord_name());
        }
        if(!TextUtils.isEmpty(infoEntity.getLandlord_gender())){
            landlordGenderEt.setText(App.getInstance().getGender().get(infoEntity.getLandlord_gender()));
        }
        if(!TextUtils.isEmpty(infoEntity.getLandlord_phone())){
            landlordPhoneEt.setText(infoEntity.getLandlord_phone());
        }
        if(!TextUtils.isEmpty(infoEntity.getLandlord_identity_image())){
            Glide.with(this).load(infoEntity.getLandlord_identity_image()).into(landlordIdentityImg);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        setHotlinfoParams();
    }

    private void setHotlinfoParams(){
        //入住日期
//        String checkInDate = checkInDateEt.getText().toString();
//        infoEntity.setCheckin_date(checkInDate);
        //拟定离开日期
//        String checkOutDate = checkOutDateEt.getText().toString();
//        infoEntity.setCheckout_date(checkOutDate);
        //详细地址
        String houseAddress = houseAddressEt.getText().toString();
        infoEntity.setHouse_address(houseAddress);
        if(!App.getInstance().isHave()){
            //房主身份证号
            String landlordIdentity = landlordIdentityEt.getText().toString();
            infoEntity.setLandlord_identity(landlordIdentity);
            //房主中文姓名
            String landlordName = landlordNameEt.getText().toString();
            infoEntity.setLandlord_name(landlordName);
            //房主联系电话
            String landlordPhone = landlordPhoneEt.getText().toString();
            infoEntity.setLandlord_phone(landlordPhone);
        }
    }

    @Override
    public void onClick(final int position) {
        new AlertDialog.Builder(this).setTitle(getString(R.string.hint))
                .setMessage(R.string.delete_this_photo)
                .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        net_photos.remove(position);
                        photos.remove(position);
                        adapter.setPhotoCount();
                        photosNumberTv.setText(getString(R.string.photo_number,adapter.getPhotoCount()));
                        infoEntity.setHouse_contract_image(net_photos.toArray(new String[net_photos.size()]));//房屋租赁合同
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(getString(R.string.cancle),null)
                .create()
                .show();
    }
}
