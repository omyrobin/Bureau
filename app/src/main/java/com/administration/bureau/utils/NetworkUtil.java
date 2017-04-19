package com.administration.bureau.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.administration.bureau.App;

public class NetworkUtil {

	private static Context context;
	/** 没有网络 */
	public static final int NETWORKTYPE_INVALID = 0;
	/** wap网络 */
	public static final int NETWORKTYPE_WAP = 1;
	/** 2G网络 */
	public static final int NETWORKTYPE_2G = 2;
	/** 3G和3G以上网络，或统称为快速网络 */
	public static final int NETWORKTYPE_3G = 3;
	/** wifi网络 */
	public static final int NETWORKTYPE_WIFI = 4;
	
	public static int mNetWorkType;
	
	/**
	 * 判断网络是否连接
	 */
	public static boolean isConnected(Context context1) {
		if (NetworkUtil.context == null)
			NetworkUtil.context = App.getInstance();
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (null != connectivity) {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (null != info && info.isConnected()) {
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断是否是wifi连接
	 */
	public static boolean isWifi(Context context) 
	{
		if (NetworkUtil.context == null)
			NetworkUtil.context = App.getInstance();
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null)
			return false;
		return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

	}

	/**
	 * 获取当前网络连接的类型信息
	 */
	public static int getConnectedType(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
				return mNetworkInfo.getType();
			}
		}
		return -1;
	}
	
	/**
     * 获取网络状态，wifi,wap,2g,3g.
     *
     * @param context 上下文
     * @return int 网络状态 
     */
    public static int getNetWorkType(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                mNetWorkType = NETWORKTYPE_WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                String proxyHost = android.net.Proxy.getDefaultHost();
                mNetWorkType = TextUtils.isEmpty(proxyHost)
                        ? (isFastMobileNetwork(context) ? NETWORKTYPE_3G : NETWORKTYPE_2G)
                        : NETWORKTYPE_WAP;
            }
        } else {
            mNetWorkType = NETWORKTYPE_INVALID;
        }
        return mNetWorkType;
    }
    
    private static boolean isFastMobileNetwork(Context context) {
    	TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
    	    switch (telephonyManager.getNetworkType()) {
    	        case TelephonyManager.NETWORK_TYPE_1xRTT:
    	            return false; // ~ 50-100 kbps
    	        case TelephonyManager.NETWORK_TYPE_CDMA:
    	            return false; // ~ 14-64 kbps
    	        case TelephonyManager.NETWORK_TYPE_EDGE:
    	            return false; // ~ 50-100 kbps
    	        case TelephonyManager.NETWORK_TYPE_EVDO_0:
    	            return true; // ~ 400-1000 kbps
    	        case TelephonyManager.NETWORK_TYPE_EVDO_A:
    	            return true; // ~ 600-1400 kbps
    	        case TelephonyManager.NETWORK_TYPE_GPRS:
    	            return false; // ~ 100 kbps
    	        case TelephonyManager.NETWORK_TYPE_HSDPA:
    	            return true; // ~ 2-14 Mbps
    	        case TelephonyManager.NETWORK_TYPE_HSPA:
    	            return true; // ~ 700-1700 kbps
    	        case TelephonyManager.NETWORK_TYPE_HSUPA:
    	            return true; // ~ 1-23 Mbps
    	        case TelephonyManager.NETWORK_TYPE_UMTS:
    	            return true; // ~ 400-7000 kbps
    	        case TelephonyManager.NETWORK_TYPE_EHRPD:
    	            return true; // ~ 1-2 Mbps
    	        case TelephonyManager.NETWORK_TYPE_EVDO_B:
    	            return true; // ~ 5 Mbps
    	        case TelephonyManager.NETWORK_TYPE_HSPAP:
    	            return true; // ~ 10-20 Mbps
    	        case TelephonyManager.NETWORK_TYPE_IDEN:
    	            return false; // ~25 kbps
    	        case TelephonyManager.NETWORK_TYPE_LTE:
    	            return true; // ~ 10+ Mbps
    	        case TelephonyManager.NETWORK_TYPE_UNKNOWN:
    	            return false;
    	        default:
    	            return false;
    	        }
    	    }
   
    /*1、NETWORK_TYPE_1xRTT： 常量值：7 网络类型：1xRTT

    2、NETWORK_TYPE_CDMA ： 常量值：4 网络类型： CDMA （电信2g）

    3、NETWORK_TYPE_EDGE： 常量值：2 网络类型：EDGE（移动2g）

    4、NETWORK_TYPE_EHRPD： 常量值：14 网络类型：eHRPD

    5、NETWORK_TYPE_EVDO_0： 常量值：5 网络类型：EVDO 版本0.（电信3g）

    6、NETWORK_TYPE_EVDO_A： 常量值：6 网络类型：EVDO 版本A （电信3g）

    7、NETWORK_TYPE_EVDO_B： 常量值：12 网络类型：EVDO 版本B（电信3g）

    8、NETWORK_TYPE_GPRS： 常量值：1 网络类型：GPRS （联通2g）

    9、NETWORK_TYPE_HSDPA： 常量值：8 网络类型：HSDPA（联通3g）

    10、NETWORK_TYPE_HSPA： 常量值：10 网络类型：HSPA

    11、NETWORK_TYPE_HSPAP： 常量值：15 网络类型：HSPA+

    12、NETWORK_TYPE_HSUPA： 常量值：9 网络类型：HSUPA

    13、NETWORK_TYPE_IDEN： 常量值：11 网络类型：iDen

    14、NETWORK_TYPE_LTE： 常量值：13 网络类型：LTE(3g到4g的一个过渡，称为准4g)

    15、NETWORK_TYPE_UMTS： 常量值：3 网络类型：UMTS（联通3g）

    16、NETWORK_TYPE_UNKNOWN：常量值：0 网络类型：未知*/
}