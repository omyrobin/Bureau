package com.administration.bureau.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.administration.bureau.R;

/**
 * Created by omyrobin on 2018/5/6.
 */

public class DialogUtil {
    private OnDialogCallBack listener;
    private Context context;
    private Dialog dialog;
    private TextView addressTv;
    private EditText houseNumberEt;

    public DialogUtil(Context context, OnDialogCallBack listener){
        this.context = context;
        this.listener = listener;
    }

    public void showDilog(String msg){
        if(dialog == null){
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_house_address_layout, null);
            addressTv = (TextView) view.findViewById(R.id.address_tv);
            houseNumberEt = (EditText) view.findViewById(R.id.house_number_et);
            addressTv.setText(msg);
            dialog = new AlertDialog.Builder(context).setTitle("是否使用当前地址")
                    .setView(view)
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String houseNumber = houseNumberEt.getText().toString();
                            listener.submit(houseNumber);
                        }
                    }).create();
        }
        dialog.show();
    }

    public interface OnDialogCallBack{
        void submit(String houseNumber);
    }
}
