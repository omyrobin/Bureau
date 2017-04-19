package com.administration.bureau.http;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;


import com.administration.bureau.App;
import com.administration.bureau.utils.NetworkUtil;

import rx.Subscriber;

/**
 * Created by omyrobin on 2017/01/04 15:49.
 */

public  abstract class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener{


    private Dialog dialogHandler;

    public ProgressSubscriber(Context context) {
        //TODO create dialog to show
//        dialogHandler = new ProgressDialog(context);
    }

    /**
     * 显示Dialog
     */
    public void showProgressDialog(){
        if (dialogHandler != null) {
            dialogHandler.show();
        }
    }

    /**
     * 隐藏Dialog
     */
    private void dismissProgressDialog(){
        if (dialogHandler != null) {
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
            onFailure("网络不可用");
        } else if (e instanceof RuntimeException) {
            onFailure(e.getMessage());
        } else {
            onFailure("请求失败，请稍后再试...");
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
