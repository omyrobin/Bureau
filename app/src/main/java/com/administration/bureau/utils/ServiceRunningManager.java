package com.administration.bureau.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

public class ServiceRunningManager {

    private static ServiceRunningManager manager;

    public static ServiceRunningManager getInstance(){
        if(manager == null){
            synchronized(ServiceRunningManager.class){
                if(manager == null){
                    manager = new ServiceRunningManager();
                }
            }
        }
        return manager;
    }
    /**
     *  服务是否运行
     */
    public boolean isServiceRunning(Context context, String serviceName) {
        boolean isRunning = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> lists = am.getRunningAppProcesses();
        // 获取运行服务再启动
        for (ActivityManager.RunningAppProcessInfo info : lists) {
            System.out.println(info.processName);
            if (info.processName.equals(serviceName)) {
                isRunning = true;
            }
        }
        return isRunning;
    }
}
