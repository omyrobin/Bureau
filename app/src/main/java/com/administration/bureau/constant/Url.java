package com.administration.bureau.constant;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by omyrobin on 2017/4/13.
 */

public class Url {
//    public static final String BUREAU_BASEURL = "http://www.guibenchuxin.com:7070/api/";
    public static final String BUREAU_BASEURL = "http://106.39.33.2:7070/api/";

    public static final String GENERATE = "http://106.39.33.2:7070/generate/notice.jpg";

    public void create(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //创建Looper对象，并保存到ThreadLocal中
                Looper.prepare();
                //从ThreadLocal中取出当前线程的Looper对象
                Looper looper = Looper.myLooper();
                //使用Looper对象创建Handler，使Hander关联当前线程的Looper
                Handler handler = new Handler(looper);
                //开启Looper循环
                Looper.loop();
            }
        }).start();
    }


}
