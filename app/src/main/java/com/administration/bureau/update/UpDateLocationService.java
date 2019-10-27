package com.administration.bureau.update;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.administration.bureau.App;
import com.administration.bureau.R;
import com.administration.bureau.entity.BaseResponse;
import com.administration.bureau.entity.TraceEntity;
import com.administration.bureau.http.ProgressSubscriber;
import com.administration.bureau.http.RetrofitClient;
import com.administration.bureau.http.RetrofitManager;
import com.administration.bureau.keeplive.JobSchedulerService;
import com.administration.bureau.keeplive.SrceenReceiver;
import com.administration.bureau.model.PutService;
import com.administration.bureau.utils.LocationUtils;
import com.administration.bureau.utils.ToastUtil;
import com.amap.api.location.AMapLocation;

import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;
import retrofit2.Response;
import rx.Observable;

/**
 * 上传地理位置
 */
public class UpDateLocationService extends Service {

    public static final int NOTIFICATION_ID = 0;

    private SrceenReceiver onePixelReceiver;

    private ScreenStateReceiver screenReceiver;

    private MediaPlayer mediaPlayer;

    private boolean isPause = true;//控制暂停

    private int i;

    private boolean isUpDate = true;

    private void startJobService(){
        if(Build.VERSION.SDK_INT >= 21){
            Intent intent = new Intent(this, JobSchedulerService.class);
            startService(intent);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(App.getInstance(), R.raw.no_kill);
}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ToastUtil.showShort("UpDateLocationService 开始启动了");
        registerOnePixelReceiver();
        registerScreenReceiver();
//        startPlaySong();
        startForeground();
//        startJobService();
        upDateLocation();
        return START_STICKY;
    }

    private void registerOnePixelReceiver(){
        // 动态注册广播接收者
        if(onePixelReceiver == null){
            onePixelReceiver = new SrceenReceiver();
        }
        // 创建IntentFilter对象
        IntentFilter filter = new IntentFilter("finish");
        // 添加要注册的action
        filter.addAction("android.intent.action.SCREEN_OFF");
        filter.addAction("android.intent.action.SCREEN_ON");
        // 动态注册广播接收者
        registerReceiver(onePixelReceiver, filter);
    }

    private void registerScreenReceiver(){
        // 动态注册广播接收者
        if(screenReceiver == null){
            screenReceiver = new ScreenStateReceiver();
        }
        // 创建IntentFilter对象
        IntentFilter filter = new IntentFilter("finish");
        // 添加要注册的action
        filter.addAction("_ACTION_SCREEN_OFF");
        filter.addAction("_ACTION_SCREEN_ON");
        // 动态注册广播接收者
        registerReceiver(screenReceiver, filter);
    }

    private void startPlaySong(){
        //播放无声音乐
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.no_kill);
            if (mediaPlayer!= null){
                mediaPlayer.setVolume(0f, 0f);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        if (!isPause) {
                            play();
                        }
                    }
                });
                play();
            }
        }
    }

    private void play() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    private void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    private void stopPlaySong(){
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
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


    private void upDateLocation(){
        final LocationUtils.MyLocationListener listener = new LocationUtils.MyLocationListener() {
            @Override
            public void result(AMapLocation location) {
                if(App.getInstance().getUserEntity() == null)
                    return;
                int user_id = App.getInstance().getUserEntity().getUser().getId();
                String token = "Bearer "+ App.getInstance().getUserEntity().getToken();
                Observable<Response<BaseResponse<TraceEntity>>> observable = RetrofitManager.getRetrofit().create(PutService.class).updateLocation(user_id, location.getLongitude(),location.getLatitude(), token);
                RetrofitClient.client().request(observable, new ProgressSubscriber<TraceEntity>(UpDateLocationService.this) {

                    @Override
                    protected void onSuccess(TraceEntity traceEntity) {
                        isUpDate = traceEntity.isKeep_track();
//                        Log.i("TAG", s.get(0).get(0));
                        ToastUtil.showShort("第" + ++i + "次上传地理位置数据"  + "  \n[ "  + traceEntity.getPosition().get(0).get(0) + " , " + traceEntity.getPosition().get(0).get(1) + " ]");
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
                    while (isUpDate){
                        LocationUtils.getLocation(listener);
                        Thread.sleep(5000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private class ScreenStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            if (intent.getAction().equals("_ACTION_SCREEN_OFF")) {
                isPause = false;
                play();
            } else if (intent.getAction().equals("_ACTION_SCREEN_ON")) {
                isPause = true;
                pause();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.pause();
        stopPlaySong();
        unregisterReceiver(onePixelReceiver);
        unregisterReceiver(screenReceiver);
        // 如果Service被杀死，干掉通知
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
            NotificationManager mManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            mManager.cancel(NOTIFICATION_ID);
        }
        Intent intent = new Intent(getApplicationContext(),UpDateLocationService.class);
        startService(intent);
    }
}
