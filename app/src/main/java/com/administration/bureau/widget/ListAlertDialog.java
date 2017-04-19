package com.administration.bureau.widget;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.administration.bureau.R;
import com.administration.bureau.entity.DataEntity;
import com.administration.bureau.interfaces.IItemClickPosition;

/**
 * Created by omyrobin on 2017/4/18.
 */

public class ListAlertDialog implements AdapterView.OnItemClickListener {

    private Context context;

    private Dialog mDialog;

    private DisplayMetrics display;

    private View mDialogContentView;

    private BaseAdapter adapter;

    private ListView listView;

    private IItemClickPosition iItemClickPosition;

    public ListAlertDialog(Context context, BaseAdapter adapter, IItemClickPosition iItemClickPosition) {
        super();
        this.context = context;
        this.adapter = adapter;
        this.iItemClickPosition = iItemClickPosition;
        display = context.getResources().getDisplayMetrics();
        initView();
        setListener();
    }

    private ListAlertDialog initView(){
        mDialog = new Dialog(context, R.style.BaseDiaLog_Select);
        mDialogContentView = LayoutInflater.from(context).inflate(R.layout.widget_list_popupwindow, null);
        listView = (ListView) mDialogContentView.findViewById(R.id.pop_lv);
        listView.setAdapter(adapter);
        mDialogContentView.setMinimumWidth(display.widthPixels);
        mDialog.setContentView(mDialogContentView);

        Window dialogWindow = mDialog.getWindow();
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.height = (int) (display.heightPixels/3);
        dialogWindow.setGravity(Gravity.START | Gravity.BOTTOM);
        return this;
    }

    private void setListener(){
        listView.setOnItemClickListener(this);
    }

    public void show(){
        mDialog.show();
    }

    public void dismiss(){
        mDialog.dismiss();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DataEntity dataEntity = (DataEntity) parent.getItemAtPosition(position);
        iItemClickPosition.itemClickPosition(dataEntity);
        dismiss();
    }
}
