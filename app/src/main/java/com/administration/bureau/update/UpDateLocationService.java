package com.administration.bureau.update;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.administration.bureau.App;
import com.administration.bureau.R;
import com.administration.bureau.entity.BaseResponse;
import com.administration.bureau.http.ProgressSubscriber;
import com.administration.bureau.http.RetrofitClient;
import com.administration.bureau.http.RetrofitManager;
import com.administration.bureau.model.PutService;
import com.administration.bureau.utils.LocationUtils;
import com.administration.bureau.utils.ToastUtil;
import com.amap.api.location.AMapLocation;

import java.util.ArrayList;

import javax.net.ssl.SSLSessionBindingEvent;

import retrofit2.Response;
import rx.Observable;

/**
 * 上传地理位置
 */
public class UpDateLocationService extends Service {

    public static final int NOTIFICATION_ID = 0;

    private MediaPlayer mMediaPlayer;

    private int i;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayer = MediaPlayer.create(App.getInstance(), R.raw.no_kill);
        mMediaPlayer.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startPlaySong();
        upDateLocation();
        startForeground();
        return super.onStartCommand(intent, flags, startId);
    }

    private void startPlaySong(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(mMediaPlayer == null){
                    mMediaPlayer = MediaPlayer.create(App.getInstance(), R.raw.no_kill);
                }
                mMediaPlayer.start();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(mMediaPlayer != null){
                    mMediaPlayer.pause();
                }
            }
        }).start();
    }

    private void upDateLocation(){
        final LocationUtils.MyLocationListener listener = new LocationUtils.MyLocationListener() {
            @Override
            public void result(AMapLocation location) {
                if(App.getInstance().getUserEntity() == null)
                    return;
                int user_id = App.getInstance().getUserEntity().getUser().getId();
                String token = "Bearer "+ App.getInstance().getUserEntity().getToken();
                Observable<Response<BaseResponse<ArrayList<ArrayList<String>>>>> observable = RetrofitManager.getRetrofit().create(PutService.class).updateLocation(user_id, location.getLongitude(),location.getLatitude(), token);
                RetrofitClient.client().request(observable, new ProgressSubscriber<ArrayList<ArrayList<String>>>(UpDateLocationService.this) {

                    @Override
                    protected void onSuccess(ArrayList<ArrayList<String>> s) {
                        Log.i("TAG", s.get(0).get(0));
                        ToastUtil.showShort(i++ + "");
                    }

                    @Override
                    protected void onFailure(String message) {

                    }
                });
                Log.i("TAG", location.getLatitude() + "  :   " + location.getLongitude());
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true){
                        LocationUtils.getLocation(listener);
                        Thread.sleep(10000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void startForeground(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            startForeground(NOTIFICATION_ID, new Notification());
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            startForeground(NOTIFICATION_ID, new Notification());
            startService(new Intent(this, InnerService.class));
        }else{
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel("channel", "xxx", NotificationManager.IMPORTANCE_MIN);
            if(null != manager){
                manager.createNotificationChannel(channel);
                Notification notification = new NotificationCompat.Builder(this).build();
                startForeground(NOTIFICATION_ID, notification);
            }
        }
    }

    public static class InnerService extends Service {
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(NOTIFICATION_ID, new Notification());
            stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }
    }

    private void stopPlaySong(){
        if(mMediaPlayer != null){
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaPlayer.pause();
        stopPlaySong();

        // 如果Service被杀死，干掉通知
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
            NotificationManager mManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            mManager.cancel(NOTIFICATION_ID);
        }
        Intent intent = new Intent(getApplicationContext(),UpDateLocationService.class);
        startService(intent);
    }
}
