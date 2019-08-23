package com.administration.bureau.download;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.administration.bureau.http.RetrofitManager;
import com.administration.bureau.model.GetService;
import com.administration.bureau.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wubo on 2018/5/8.
 */

public class DownLoadService extends Service {

    private static String URL = "http://94.191.124.237:8800/api/so";

    @Override
    public void onCreate() {
        super.onCreate();
        loadSo(getABI(), getPrivateSoFile());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void downLoadSo(@NonNull String url, String abi, final File file){
        GetService getService =  RetrofitManager.getRetrofit().create(GetService.class);
        getService.downloadFile(url, abi)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Func1<ResponseBody, InputStream>() {
                    @Override
                    public InputStream call(ResponseBody responseBody) {
                        return responseBody.byteStream();
                    }
                })
                .observeOn(Schedulers.computation())
                .doOnNext(new Action1<InputStream>() {
                    @Override
                    public void call(InputStream inputStream) {

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<InputStream>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(InputStream inputStream) {
                        try {
                            FileUtil.write(inputStream, file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        copyAndload();
                    }
                });
    }

    /**
     * 获取当前手机CPU ABI
     */
    private String getABI(){
        String name;
        if (Build.VERSION.SDK_INT >= 21) {
            String[] abis = Build.SUPPORTED_ABIS;
            name = abis[0];
        } else {
            name = Build.CPU_ABI ;
        }
        return name;
    }

    /**
     *  获取/mnt/sdcard/packgername/MAP.so
     */
    private File getSdCardSoFile(){
        File file = new File(FileUtil.getCacheFile() + File.separator+"MAP.so");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 获取data/data/packgername/app_libs/MAP.so
     * @return
     */
    private File getPrivateSoFile(){
        File dir = getDir("libs", Activity.MODE_PRIVATE);
        File privateSoFile = new File(dir.getAbsolutePath() + File.separator + "MAP.so");
        if(!privateSoFile.exists()){
            try {
                privateSoFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return privateSoFile;
    }

    /**
     * 根据文件长度判断so文件的加载方式
     * @param abi
     * @param privateFile
     */
    private void loadSo(String abi, File privateFile){
        if(privateFile.length()==0){
            downLoadSo(URL, abi, getSdCardSoFile());
        }else{
            System.load(getPrivateSoFile().getAbsolutePath());
            DownLoadService.this.stopSelf();
        }
    }

    /**
     * copy下载的so文件到data/data/packgername/app_libs目录中
     */
    private void copyAndload(){
        if (FileUtil.copyLibraryFile(DownLoadService.this, getSdCardSoFile(), getPrivateSoFile())){
            System.load(getPrivateSoFile().getAbsolutePath());
        }
        DownLoadService.this.stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
