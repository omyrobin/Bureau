package com.administration.bureau.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

    /**
     * 删除文件
     * @param file
     */
    public static void clearFile(File file){
        if(file.isFile()){
            file.delete();
        }else if(file.isDirectory()){
            for(File f : file.listFiles()){
                clearFile(f);
            }
        }
    }

    public static void write(InputStream inputStream, File file) throws IOException{
        int index;
        byte[] bytes = new byte[1024];
        FileOutputStream downloadFile = new FileOutputStream(file);
        while ((index = inputStream.read(bytes)) != -1) {
            downloadFile.write(bytes, 0, index);
            downloadFile.flush();
        }
        downloadFile.close();
        inputStream.close();
    }

    public static boolean copyLibraryFile(Context context, File fromFile, File toFile) {
        boolean copyIsFinish = false;
        try {
            FileInputStream is = new FileInputStream(fromFile);
            if (toFile.exists()) {
                if (toFile.length() == is.available()) {
                    return true;
                }
            }
            FileOutputStream fos = new FileOutputStream(toFile);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();
            copyIsFinish = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return copyIsFinish;
    }

    public static boolean hasSoFile(Context context) {
        File dir = context.getDir("libs", Activity.MODE_PRIVATE);
        File privateSoFile = new File(dir.getAbsolutePath() + File.separator + "MAP.so");
        if(!privateSoFile.exists()){
            return false;
        }
        return true;
    }
}
