package com.administration.bureau;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.administration.bureau.constant.Constant;
import com.administration.bureau.entity.DataEntity;
import com.administration.bureau.entity.UserEntity;
import com.administration.bureau.entity.UserRegisterInfoEntity;
import com.administration.bureau.utils.FileUtil;
import com.administration.bureau.utils.SharedPreferencesUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by omyrobin on 2017/3/30.
 */

public class App extends Application {

    //用来判断是否被强杀的状态标识
    public static int mAppStatus = -1;
    //应用内语言
    public static int locale = 1;

    private static App mApp;

    private UserEntity userEntity;
    /**国家**/
    private HashMap<String,String> country;
    /**证件类型**/
    private HashMap<String,String> visa_type;
    /**人员地域类型**/
    private HashMap<String,String> person_area_type;
    /**人员类型**/
    private HashMap<String,String> person_type;
    /**入境口岸**/
    private HashMap<String,String> entry_port;
    /**所属派出所**/
    private HashMap<String,String> police_station;
    /**所属社区**/
    private HashMap<String,HashMap<String,String>> allCommunity;
    /**停留事由**/
    private HashMap<String,String> stay_reason;
    /**签证（注）种类**/
    private HashMap<String,String> credential_type;
    /**住房种类**/
    private HashMap<String,String> house_type;
    /**职业**/
    private HashMap<String,String> occupation;
    /**性别**/
    private HashMap<String,String> gender = new HashMap<>();

    private UserRegisterInfoEntity infoEntity;

    public static int width;

    public static int height;

    public int status = -1;//审核状态

    public String certificate_image;//电子证书图片地址

    public String chinese_name = "";//我的信息页显示中文姓名

    public String reject_reason;//拒绝原因

    public int id;//电子证书id

    public static App getInstance(){
       return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        initLocale();
        initAppInfo();
        initUserEntity();
        //文件工具类初始化
        FileUtil.instance(this);
        //用户申请资料
        String json = (String) SharedPreferencesUtil.getParam(this,Constant.SAVE_USER_REGISTER_INFO,"");
        infoEntity = new Gson().fromJson(json, UserRegisterInfoEntity.class);
        if(infoEntity == null){
            infoEntity = new UserRegisterInfoEntity();
        }
        setInfoEntity(infoEntity);
    }

    public void initLocale(){
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        //应用用户选择语言
        if(locale == 0)
            config.locale = Locale.CHINA;
        else if(locale == 1)
            config.locale = Locale.ENGLISH;
        resources.updateConfiguration(config, dm);
    }

    private void initUserEntity(){
        int user_id = (int)SharedPreferencesUtil.getParam(this, Constant.USER_ID,-1);
        String user_phone = (String)SharedPreferencesUtil.getParam(this, Constant.USER_PHONE, "");
        String user_token = (String)SharedPreferencesUtil.getParam(this, Constant.USER_TOKEN,"");

        if(user_id != -1 || !TextUtils.isEmpty(user_phone) || !TextUtils.isEmpty(user_token)) {
            UserEntity userEntity = new UserEntity();
            userEntity.setToken(user_token);
            UserEntity.UserBean userBean = new UserEntity.UserBean();
            userBean.setId(user_id);
            userBean.setPhone(user_phone);
            userEntity.setUser(userBean);
            App.getInstance().setUserEntity(userEntity);
        }
    }

    public HashMap<String,String> getCountry() {
        return country;
    }

    public void setCountry(HashMap<String,String> country) {
        this.country = country;
    }

    public HashMap<String,String> getVisa_type() {
        return visa_type;
    }

    public void setVisa_type(HashMap<String,String> visa_type) {
        this.visa_type = visa_type;
    }

    public HashMap<String,String> getPerson_area_type() {
        return person_area_type;
    }

    public void setPerson_area_type(HashMap<String,String> person_area_type) {
        this.person_area_type = person_area_type;
    }

    public HashMap<String,String> getPerson_type() {
        return person_type;
    }

    public void setPerson_type(HashMap<String,String> person_type) {
        this.person_type = person_type;
    }

    public HashMap<String,String> getEntry_port() {
        return entry_port;
    }

    public void setEntry_port(HashMap<String,String> entry_port) {
        this.entry_port = entry_port;
    }

    public HashMap<String,String> getPolice_station() {
        return police_station;
    }

    public void setPolice_station(HashMap<String,String> police_station) {
        this.police_station = police_station;
    }

    public HashMap<String, HashMap<String,String>> getAllCommunity() {
        return allCommunity;
    }

    public void setAllCommunity(HashMap<String, HashMap<String,String>> allCommunity) {
        this.allCommunity = allCommunity;
    }

    public HashMap<String,String> getStay_reason() {
        return stay_reason;
    }

    public void setStay_reason(HashMap<String,String> stay_reason) {
        this.stay_reason = stay_reason;
    }

    public HashMap<String,String> getCredential_type() {
        return credential_type;
    }

    public void setCredential_type(HashMap<String,String> credential_type) {
        this.credential_type = credential_type;
    }

    public HashMap<String,String> getHouse_type() {
        return house_type;
    }

    public void setHouse_type(HashMap<String,String> house_type) {
        this.house_type = house_type;
    }

    public HashMap<String,String> getOccupation() {
        return occupation;
    }

    public void setOccupation(HashMap<String,String> occupation) {
        this.occupation = occupation;
    }

    public HashMap<String,String> getGender() {
        gender.put("0", "男");
        gender.put("1", "女");
        return gender;
    }

    private void initAppInfo() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        width = metrics.widthPixels;
        height = metrics.heightPixels;
//        density = metrics.density;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
        //保存用户相关信息
        if(userEntity != null){
            SharedPreferencesUtil.setParam(this, Constant.USER_ID, userEntity.getUser().getId());
            SharedPreferencesUtil.setParam(this, Constant.USER_PHONE, userEntity.getUser().getPhone());
            SharedPreferencesUtil.setParam(this, Constant.USER_TOKEN, userEntity.getToken());
        }
    }

    public UserRegisterInfoEntity getInfoEntity() {
        return infoEntity;
    }

    public void setInfoEntity(UserRegisterInfoEntity infoEntity) {
        this.infoEntity = infoEntity;
    }

}
