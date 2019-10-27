package com.administration.bureau.keeplive;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;

import com.administration.bureau.App;

/**
 * Created by wubo on 2019/3/18.
 */

public class SrceenReceiver extends BroadcastReceiver {

    android.os.Handler mHander;
    boolean screenOn = true;

    public SrceenReceiver() {
        mHander = new android.os.Handler(Looper.getMainLooper());
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {    //屏幕关闭的时候接受到广播
            screenOn = false;
            mHander.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(!screenOn){
                        Intent intent2 = new Intent(context, OnePixelActivity.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent2, 0);
                        try {
                            pendingIntent.send();
                            /*} catch (PendingIntent.CanceledException e) {*/
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            },1000);
            //通知屏幕已关闭，开始播放无声音乐
            context.sendBroadcast(new Intent("_ACTION_SCREEN_OFF"));
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {   //屏幕打开的时候发送广播  结束一像素
            screenOn = true;
            //通知屏幕已点亮，停止播放无声音乐
            context.sendBroadcast(new Intent("_ACTION_SCREEN_ON"));
        }
    }

//    @Override
//    public void onReceive(Context context, Intent intent) {
//        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {    //屏幕关闭启动1像素Activity
//            Log.i("TAG", "屏幕关闭了");
//            Intent it = new Intent(App.getInstance().getApplicationContext(), OnePixelActivity.class);
//            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            App.getInstance().getApplicationContext().startActivity(it);
//        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {   //屏幕打开 结束1像素
//            Log.i("TAG", "屏幕开启了");
//            context.sendBroadcast(new Intent("finish"));
//            Intent main = new Intent(Intent.ACTION_MAIN);
//            main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            main.addCategory(Intent.CATEGORY_HOME);
//            context.startActivity(main);
//        }
//    }
}
