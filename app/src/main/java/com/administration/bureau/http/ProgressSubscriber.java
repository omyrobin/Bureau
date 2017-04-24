package com.administration.bureau.http;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.TextView;


import com.administration.bureau.App;
import com.administration.bureau.R;
import com.administration.bureau.utils.NetworkUtil;

import rx.Subscriber;

/**
 * Created by omyrobin on 2017/01/04 15:49.
 */

public  abstract class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener{


    private ProgressDialog dialogHandler;

    private Context context;

    private boolean show;

    public ProgressSubscriber(Context context) {
        this.context = context;
        //TODO create dialog to show
    }

    public ProgressSubscriber(Context context, boolean show) {
        this.context = context;
        this.show = show;
        //TODO create dialog to show
        dialogHandler = new ProgressDialog(context);
        dialogHandler.setMessage(context.getString(R.string.uploading));
    }

    /**
     * 显示Dialog
     */
    public void showProgressDialog(){
        if (dialogHandler != null && show) {
            dialogHandler.show();
        }
    }

    /**
     * 隐藏Dialog
     */
    private void dismissProgressDialog(){
        if (dialogHandler != null && show) {
            dialogHandler.dismiss();
            dialogHandler=null;
        }
    }

    @Override
    public void onCompleted() {
        dismissProgressDialog();
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (!NetworkUtil.isConnected(App.getInstance().getApplicationContext())) {
            onFailure(context.getString(R.string.network_is_not_available));
        } else if (e instanceof RuntimeException) {
            onFailure(e.getMessage());
        } else {
            onFailure(context.getString(R.string.request_failed));
        }
        dismissProgressDialog();
    }


    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }

    protected abstract void onSuccess(T t);

    protected abstract void onFailure(String message);
}
