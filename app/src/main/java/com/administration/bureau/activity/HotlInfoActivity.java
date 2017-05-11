package com.administration.bureau.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.administration.bureau.App;
import com.administration.bureau.BaseActivity;
import com.administration.bureau.R;
import com.administration.bureau.adapter.DataAdapter;
import com.administration.bureau.adapter.PhotosAdapter;
import com.administration.bureau.entity.DataEntity;
import com.administration.bureau.interfaces.IItemClickPosition;
import com.administration.bureau.utils.ToastUtil;
import com.administration.bureau.widget.ListAlertDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * Created by omyrobin on 2017/4/6.
 */

public class HotlInfoActivity extends BaseActivity {

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
    @BindView(R.id.check_out_date_et)
    EditText checkOutDateEt;
    //有租赁合同的Layout
    @BindView(R.id.house_have_layout)
    View houseHaveLayout;
    //房主身份证照片
    @BindView(R.id.landlord_identity_img)
    ImageView landlordIdentityImg;
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
    @BindView(R.id.house_type_et)
    EditText houseTypeEt;
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
    private ArrayList<String> photos;


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

    @OnTouch({R.id.check_out_date_et})
    protected boolean selectDate(TextView textView, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP)
            super.selectDate(textView);
        return true;
    }

    @Override
    protected void initializeActivity(Bundle savedInstanceState) {
        photos = new ArrayList<>();
        photos.add("TEST");
        setOnCheckedChangeListener();
        initLayoutManager();
    }

    private void initLayoutManager(){
        LinearLayoutManager layout = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true);
        contractOfTenancyRv.setLayoutManager(layout);
        contractOfTenancyRv.setAdapter(new PhotosAdapter(this,photos));
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
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnTouch({R.id.house_type_et,R.id.landlord_country_et,R.id.landlord_gender_et})
    protected boolean selectPosition(TextView editView, MotionEvent event){
        DataAdapter adapter;
        ListAlertDialog dialog = null;
        if(event.getAction() == MotionEvent.ACTION_UP){
            switch (editView.getId()){
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

                case R.id.house_type_et:
                    adapter = new DataAdapter(this, transformToList(App.getInstance().getHouse_type()));
                    dialog = new ListAlertDialog(this, adapter, new IItemClickPosition() {
                        @Override
                        public void itemClickPosition(DataEntity dataEntity) {
                            houseTypeEt.setText(dataEntity.getValue());
                            infoEntity.setHouse_type(dataEntity.getKey());
                        }
                    });
                    break;

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

    @Override
    protected void onResume() {
        super.onResume();
//        if(!TextUtils.isEmpty(infoEntity.getCheckin_date())){
//            checkInDateEt.setText(infoEntity.getCheckin_date());
//        }
        if(!TextUtils.isEmpty(infoEntity.getCheckout_date())){
            checkOutDateEt.setText(infoEntity.getCheckout_date());
        }
        if(!TextUtils.isEmpty(infoEntity.getHouse_address())){
            houseAddressEt.setText(infoEntity.getHouse_address());
        }
//        if(!TextUtils.isEmpty(infoEntity.getPolice_station())){
//            policeStationEt.setText(App.getInstance().getPolice_station().get(infoEntity.getPolice_station()));
//        }
//        if(!TextUtils.isEmpty(infoEntity.getCommunity())){
//            communityEt.setText(App.getInstance().getAllCommunity().get(infoEntity.getPolice_station()).get(infoEntity.getCommunity()));
//        }
        if(!TextUtils.isEmpty(infoEntity.getHouse_type())){
            houseTypeEt.setText(App.getInstance().getHouse_type().get(infoEntity.getHouse_type()));
        }
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
        String checkOutDate = checkOutDateEt.getText().toString();
        infoEntity.setCheckout_date(checkOutDate);
        //房主身份证号
        String landlordIdentity = landlordIdentityEt.getText().toString();
        infoEntity.setLandlord_identity(landlordIdentity);
        //详细地址
        String houseAddress = houseAddressEt.getText().toString();
        infoEntity.setHouse_address(houseAddress);
        //房主中文姓名
        String landlordName = landlordNameEt.getText().toString();
        infoEntity.setLandlord_name(landlordName);
        //房主联系电话
        String landlordPhone = landlordPhoneEt.getText().toString();
        infoEntity.setLandlord_phone(landlordPhone);
    }

}
