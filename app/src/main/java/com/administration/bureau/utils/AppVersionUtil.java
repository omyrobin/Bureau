package com.administration.bureau.utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.widget.RemoteViews;

import com.administration.bureau.App;
import com.administration.bureau.BaseActivity;
import com.administration.bureau.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


@SuppressLint("HandlerLeak")
public class AppVersionUtil {
	
	private static final String ACTION_ONCLICK_RECIVER = "com.administration.bureau.utils.AppVersionUtil.OnClickReciver";
    //提示语
    private static String UPDATE_MSG;
    //返回的安装包url
    private static final String APK_URL = "http://106.39.33.2:7070/client/fangshan.apk";

    private static final int DOWNLOAD_SUCCESS = 0;
    private static final int DOWNLOAD_FIAL = 1;
    private static final int DOWNLOAD_PROGRESS = 2;
    private static final int NOTIFY_ID = 101;
    
    private BaseActivity activity;
    private boolean interceptFlag;
    private NotificationManager nManager;
    private NotificationCompat.Builder mBuilder;
    private RemoteViews mRemoteViews;
    private Notification notification;
    private OnClickReciver reciver;
    private int lastProgress;

    public AppVersionUtil(BaseActivity activity){
		this.activity = activity;
		UPDATE_MSG = activity.getString(R.string.have_the_latest_software_package);
    	nManager = (NotificationManager) activity.getSystemService(BaseActivity.NOTIFICATION_SERVICE);
    	showButtonNotify();
    }

	public void showNoticeDialog(){
		showNoticeDialog(UPDATE_MSG, activity.getString(R.string.update), activity.getString(R.string.later));
	}

	private void showNoticeDialog(String msg, String okBtn, String cencelBtn) {
		Builder builder = new Builder(activity);
		builder.setTitle(R.string.software_version_update);
		builder.setMessage(msg);
		builder.setPositiveButton(okBtn, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				download();
			}
		});
		builder.setNegativeButton(cencelBtn, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				try {
					nManager.cancel(NOTIFY_ID);
				} catch (Exception e) {}

			}
		});
		AlertDialog noticeDialog = builder.create();
		noticeDialog.show();
	}
    
	@SuppressLint("NewApi")
	private void showButtonNotify(){  
        mBuilder = new NotificationCompat.Builder(activity);  
        mRemoteViews = new RemoteViews(activity.getPackageName(), R.layout.layout_remoteview_download);  
        mRemoteViews.setImageViewResource(R.id.img, R.drawable.ic_logo);
        //API3.0 以上的时候显示按钮，否则消失  
        mRemoteViews.setTextViewText(R.id.title, activity.getString(R.string.download));
        Intent intent = new Intent(ACTION_ONCLICK_RECIVER);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, 0, intent , 0);
		mRemoteViews.setOnClickPendingIntent(R.id.delete, pendingIntent);
		mBuilder.setContent(mRemoteViews) 
                .setAutoCancel(false)
                .setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示  
                .setTicker(activity.getString(R.string.begin_download))  
                .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级  
                .setOngoing(false)
                .setSmallIcon(R.drawable.ic_logo);
    }  
    
    private Handler handler = new Handler(){
    	public void handleMessage(Message msg) {
    		switch (msg.what) {
			case DOWNLOAD_SUCCESS:
				collapseStatusBar();
				nManager.cancel(NOTIFY_ID);
				ToastUtil.showShort(activity.getString(R.string.download_complete_please_install));
				installApk();
				break;
			case DOWNLOAD_FIAL:
				collapseStatusBar();
				ToastUtil.showShort(activity.getString(R.string.net_err));
				showNoticeDialog(activity.getString(R.string.continue_to_download), activity.getString(R.string.update), activity.getString(R.string.later));
				break;
			case DOWNLOAD_PROGRESS:
				if(lastProgress != msg.arg1){
					mRemoteViews.setProgressBar(R.id.my_progress, 100, msg.arg1, false);
					nManager.notify(NOTIFY_ID, notification);
					lastProgress = msg.arg1;
				}
				break;
			}
    	}
    };
    
	private void download(){
		notification = mBuilder.build();
		notification.flags = Notification.FLAG_ONGOING_EVENT;
        nManager.notify(NOTIFY_ID, notification);
		interceptFlag = false;
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
	            try {
	                URL url = new URL(APK_URL);

	                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	                conn.connect();
	                File file = new File(FileUtil.getCacheFile(), "apk");
	                if (!file.exists()) {
	                    file.mkdirs();
	                }
	                File mApkFile = new File(file, "poison.apk");
	                int length = conn.getContentLength();
	                InputStream is = conn.getInputStream();
	                FileOutputStream fos1 = new FileOutputStream(mApkFile);  
	                long count = 0;
	                byte buf[] = new byte[1024];
	                do {
	                    int numread = is.read(buf);
	                    count += numread;
	                    
	                    int progress = (int) (((float) count / length) * 100);
	                    Message msg = handler.obtainMessage(DOWNLOAD_PROGRESS);
	                    msg.arg1 = progress;
						//更新进度
	                    handler.sendMessage(msg);
	                    if (numread <= 0) {
	                        //下载完成通知安装
	                    	interceptFlag =  true;
	                        handler.sendEmptyMessage(DOWNLOAD_SUCCESS);
	                        break;
	                    }
	                    fos1.write(buf, 0, numread);
	                } while (!interceptFlag);//点击取消就停止下载.

	                fos1.close();
	                is.close();
	            } catch (MalformedURLException e) {
	                e.printStackTrace();
	                handler.sendEmptyMessage(DOWNLOAD_FIAL);
	            } catch (IOException e) {
	                e.printStackTrace();
	                handler.sendEmptyMessage(DOWNLOAD_FIAL);
	            }
			}
		};
		new Thread(runnable).start();
	}

	public void pause() {
		interceptFlag = true;
	}
	
	/**
     * 安装apk
     *
     */
    private void installApk() {
    	File file = new File(FileUtil.getCacheFile(), "apk");
        File apkfile = new File(file, "poison.apk");
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        activity.startActivity(i);
    }
    
	private void collapseStatusBar() {
		try {
			Object statusBarManager = activity.getSystemService("statusbar");
			Method collapse;
			if (Build.VERSION.SDK_INT <= 16) {
				collapse = statusBarManager.getClass().getMethod("collapse");
			} else {
				collapse = statusBarManager.getClass().getMethod("collapsePanels");
			}
			collapse.invoke(statusBarManager);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	public void onCreate(){
		reciver = new OnClickReciver();
		IntentFilter filter = new IntentFilter(ACTION_ONCLICK_RECIVER);
		activity.registerReceiver(reciver, filter);
	}

	public void onDestroy(){
		try {
			if(reciver != null)
				activity.unregisterReceiver(reciver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if(handler != null){
				if(handler.hasMessages(DOWNLOAD_FIAL))
					handler.removeMessages(DOWNLOAD_FIAL);
				if(handler.hasMessages(DOWNLOAD_SUCCESS))
					handler.removeMessages(DOWNLOAD_SUCCESS);
				if(handler.hasMessages(DOWNLOAD_PROGRESS))
					handler.removeMessages(DOWNLOAD_PROGRESS);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		File file = new File(FileUtil.getCacheFile(), "apk");
		File apkfile = new File(file, "poison.apk");
		if (apkfile.exists()) {

			if((App.getInstance().getVersionCode()+"").equals(getApkVersion(apkfile.getAbsolutePath()))){
				FileUtil.clearFile(file);
			}
		}
	}
    
    
    private class OnClickReciver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			if(ACTION_ONCLICK_RECIVER.equals(intent.getAction())){
				nManager.cancel(NOTIFY_ID);
				ToastUtil.showShort(context.getString(R.string.stop_the_downloading));
				pause();
				collapseStatusBar();
			}
		}
    }
    
    private String getApkVersion(String apkPath){
    	PackageManager pm = activity.getPackageManager(); 
    	PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);    
    	if(info != null){    
            return info.versionName;
        }
    	return null;
    }
   
}
