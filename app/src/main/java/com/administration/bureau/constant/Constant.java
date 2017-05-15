package com.administration.bureau.constant;

import com.administration.bureau.App;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by omyrobin on 2017/4/7.
 */

public class Constant {
    public static final int SELECT_CAMERA = 0;//拍照

    public static final int SELECT_CHOOSER = 1;//相册

    public static final int SELECT_CONTRACT_OF_TENANCY = 2;//房屋租赁合同照片

    public static final int MY_PERMISSIONS_REQUEST_CALL_CAMERA = 0;

    public static final int MY_PERMISSIONS_REQUEST_CALL_CHOOSER = 1;

    public static final String USER_ID = "USER_ID";//用户ID

    public static final String USER_PHONE = "USER_PHONE";//用户手机号

    public static final String USER_TOKEN = "USER_TOKEN";//用户Token

    public static final String LOCALE = "LOCALE";

    public static String [] languages = new String[]{"zh-CN", "en"};

    public static final int PASSPORT_INFO = 0;//护照信息页

    public static final int ENTRY_PAGE = 1;//入境页

    public static final int VISA_PAGE = 2;//签证页

}
