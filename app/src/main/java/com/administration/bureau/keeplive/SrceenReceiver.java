package com.administration.bureau.keeplive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.administration.bureau.App;

/**
 * Created by wubo on 2019/3/18.
 */

public class SrceenReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {    //屏幕关闭启动1像素Activity
            Log.i("TAG", "屏幕关闭了");
            Intent it = new Intent(App.getInstance().getApplicationContext(), OnePiexlActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            App.getInstance().getApplicationContext().startActivity(it);
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {   //屏幕打开 结束1像素
            Log.i("TAG", "屏幕开启了");
            context.sendBroadcast(new Intent("finish"));
            Intent main = new Intent(Intent.ACTION_MAIN);
            main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            main.addCategory(Intent.CATEGORY_HOME);
            context.startActivity(main);
        }
    }
}
