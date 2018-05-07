package com.administration.bureau;

import android.app.Application;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.administration.bureau.constant.Constant;
import com.administration.bureau.entity.UserEntity;
import com.administration.bureau.entity.UserRegisterInfoEntity;
import com.administration.bureau.utils.FileUtil;
import com.administration.bureau.utils.SharedPreferencesUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Locale;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by omyrobin on 2017/3/30.
 */

public class App extends Application {

    //用来判断是否被强杀的状态标识
    public static int mAppStatus = -1;
    //应用内语言
    public static int locale = -1;

    private static App mApp;
    //用来判断住宿信息中开关的开启状态
    private boolean have;

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

    public int samplePhotoIndex;

    private String location;

    public static App getInstance(){
       return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        locale = (int) SharedPreferencesUtil.getParam(this, Constant.LOCALE, -1);
        initLocale();
        initAppInfo();
        initUserEntity();
        initUserRegisterInfoEntity();
        FileUtil.instance(this);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

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

            //用户申请资料
            String json = (String) SharedPreferencesUtil.getParam(this,user_id+"","");
            infoEntity = new Gson().fromJson(json, UserRegisterInfoEntity.class);
        }
    }

    private void initUserRegisterInfoEntity(){
        if(infoEntity == null){
            infoEntity = new UserRegisterInfoEntity();
        }
        setInfoEntity(infoEntity);
    }

    private void initAppInfo() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        width = metrics.widthPixels;
        height = metrics.heightPixels;
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
        gender.put("0", getString(R.string.male));
        gender.put("1", getString(R.string.female));
        return gender;
    }

    public UserRegisterInfoEntity getInfoEntity() {
        return infoEntity;
    }

    public void setInfoEntity(UserRegisterInfoEntity infoEntity) {
        this.infoEntity = infoEntity;
    }

    /**
     * 获取版本号
     * @return
     */
    public int getVersionCode(){
        PackageManager manager = getPackageManager();//获取包管理器
        try {
            //通过当前的包名获取包的信息
            PackageInfo info = manager.getPackageInfo(getPackageName(),0);//获取包对象信息
            return  info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取坂本明
     * @return
     */
    public String getVersionName(){
        PackageManager manager = getPackageManager();
        try {
            //第二个参数代表额外的信息，例如获取当前应用中的所有的Activity
            PackageInfo packageInfo = manager.getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES
            );
            ActivityInfo[] activities = packageInfo.activities;
            showActivities(activities);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
    public void showActivities(ActivityInfo[] activities){
        for(ActivityInfo activity : activities) {
        }
    }

    public void setHave(boolean have) {
        this.have = have;
    }

    public boolean isHave() {
        return have;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }
}
