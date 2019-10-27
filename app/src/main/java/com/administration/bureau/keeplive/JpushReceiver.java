package com.administration.bureau.keeplive;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.administration.bureau.update.UpDateLocationService;
import com.administration.bureau.utils.ServiceRunningManager;
import com.administration.bureau.utils.ToastUtil;

import cn.jpush.android.api.JPushInterface;

public class JpushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String TAG = "TAG";
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收 Registration Id : " + regId);
        }else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
//            ToastUtil.showShort("收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            Log.d(TAG, "收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            if("trace".equals(bundle.getString(JPushInterface.EXTRA_MESSAGE))){
                startUpDateLocation(context);
            }
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "收到了通知");
            // 在这里可以做些统计，或者做些其他工作
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "用户点击打开了通知");
            // 在这里可以自己写代码去定义用户点击后的行为
        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }

    private void startUpDateLocation(Context context){
        String serviceName = "com.administration.bureau.update.UpDateLocationService";
        if(!ServiceRunningManager.getInstance().isServiceRunning(context, serviceName)){
            Intent intent = new Intent(context, UpDateLocationService.class);
            if(Build.VERSION.SDK_INT >= 26){
                context.startForegroundService(intent);
            }else{
                context.startService(intent);
            }

        }
    }
}
