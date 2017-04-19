package com.administration.bureau.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.administration.bureau.App;

/**
 * Created by omyrobin on 2017/4/18.
 */

public class ToastUtil {

    private static Toast toast;
    public static boolean isShow = true;

    static {
        Context context = App.getInstance();
        toast = Toast.makeText(context, "" ,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
    }

    /**
     * 短时间显示Toast
     */
    public static void showShort(CharSequence message){
        show2(message, Toast.LENGTH_SHORT);
    }
    public static void showShort(int message){
        show1(message, Toast.LENGTH_SHORT);
    }
    /**
     * 方法说明：长时间toast
     */
    public static void showLong(CharSequence message) {
        show2(message, Toast.LENGTH_LONG);
    }
    public static void showLong(int message){
        show1(message, Toast.LENGTH_LONG);
    }

    private static void show1(int message,int duration){
        if (isShow){
            toast.setDuration(duration);
            toast.setText(message);
            toast.show();
        }
    }
    private static void show2(CharSequence message,int duration){
        if (isShow){
            toast.setDuration(duration);
            toast.setText(message);
            toast.show();
        }
    }
}
