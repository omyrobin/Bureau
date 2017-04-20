package com.administration.bureau.interfaces;

import android.graphics.Bitmap;

import java.io.File;

public interface ImageDownLoadCallBack {
    void onDownLoadSuccess(File file);
    void onDownLoadSuccess(Bitmap bitmap);
    void onDownLoadFailed();
}  