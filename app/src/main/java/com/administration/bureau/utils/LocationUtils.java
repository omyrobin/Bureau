package com.administration.bureau.utils;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

public class LocationUtils {

    private static AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public static AMapLocationClientOption mLocationOption = null;

    public static AMapLocation sLocation = null;

    public static void init(Context context) {
        mlocationClient = new AMapLocationClient(context);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(5000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
    }

    public interface MyLocationListener {
        void result(AMapLocation location);
    }

    // 获取之前定位位置,如果之前未曾定位,则重新定位
    public static void getLocation(MyLocationListener listener) {
        if (sLocation == null) {
            getCurrentLocation(listener);
        } else {
            listener.result(sLocation);
        }
    }

    // 获取当前位置,无论是否定位过,重新进行定位
    public static void getCurrentLocation(final MyLocationListener listener) {
        if (mlocationClient == null) {
            return;
        }
        // 设置定位监听
        mlocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation location) {
                if (location != null) {
                    //定位成功,取消定位
                    mlocationClient.stopLocation();
                    sLocation = location;
                    listener.result(location);
                } else {
                    //获取定位数据失败
                }
            }
        });
        // 启动定位
        mlocationClient.stopLocation();
        mlocationClient.startLocation();
    }

    public static void destroy() {
        mlocationClient.onDestroy();
    }
}
