package com.administration.bureau.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by omyrobin on 2017/4/7.
 */

public class FileUtil {

    private static Context context;

    public static void instance(Context context){
        FileUtil.context = context;
    }

    /**
     * 获取该应用的缓存文件根目录(dirt novel photo)
     */
    public static File getCacheFile(){
        String packageName = context.getPackageName();
        File f;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            f = new File(Environment.getExternalStorageDirectory()+File.separator+packageName);
        }else{
            f = new File(context.getCacheDir()+File.separator+packageName);
        }
        if(!f.exists())
            f.mkdirs();
        return f;
    }

    /**
     * 拍照图片(原图)存放目录(photo) 可在缓存中删除
     * @return
     */
    public static File getCamoraFile(){
        File f= new File(getCacheFile() + File.separator+"photo");
        if(!f.exists())
            f.mkdirs();
        return f;
    }

}
