package com.administration.bureau.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.administration.bureau.R;

/**
 * Created by omyrobin on 2018/5/6.
 */

public class DialogUtil {
    private OnDialogCallBack listener;
    private Context context;
    private AlertDialog dialog;

    public DialogUtil(Context context, OnDialogCallBack listener){
        this.context = context;
        this.listener = listener;
    }

    public void showDilog(String msg){
        if(dialog == null){
            dialog = new AlertDialog.Builder(context).setTitle("是否使用当前地址")
                    .setMessage(msg)
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listener.submit();
                        }
                    }).create();
        }
        dialog.show();
    }

    public interface OnDialogCallBack{
        void submit();
    }
}
