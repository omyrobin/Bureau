package com.administration.bureau.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import com.administration.bureau.R;

/**
 * Created by wubo on 2018/5/18.
 */

public class RoleDialog implements View.OnClickListener{

    private Context context;

    private int role;

    private  AlertDialog dialog;

    private  ISendMessage listener;

    public RoleDialog(Context context, ISendMessage listener) {
        this.context = context;
        this.listener = listener;
        showMessageObject();
    }

    private void showMessageObject(){
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_message_object,null);
        RadioGroup radioGroup = view.findViewById(R.id.rg_message_object);
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setOnClickListener(this);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

            }
        });
        dialog = new AlertDialog.Builder(context)
                .setView(view)
                .setTitle("选择留言对象")
                .setNegativeButton(R.string.cancle,null)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.senMessage(role);
                    }
                }).create();
    }

    public void show(){
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rb_bureau:
                role = 0;
                break;

            case R.id.rb_community_police:
                role = 1;
                break;

            default:
                role = 2;
                break;
        }
    }

    public interface ISendMessage{
        void senMessage(int role);
    }
}
