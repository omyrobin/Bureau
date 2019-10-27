package com.administration.bureau.keeplive;

import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.administration.bureau.update.UpDateLocationService;
import com.administration.bureau.utils.ServiceRunningManager;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobSchedulerService extends JobService {

    private JobScheduler mJobScheduler;

    private static final int JOB_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        setJobSechedule();
        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i("TAG","JobSchedulerService 开始启动service");
        String serviceName = "com.administration.bureau.update.UpDateLocationService";
        if(!ServiceRunningManager.getInstance().isServiceRunning(this, serviceName)){
            Log.i("TAG","JobSchedulerService 开始启动service   UpDateLocationService ");
            startService(new Intent(this,UpDateLocationService.class));
        }
        setJobSechedule();
        jobFinished(params, false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i("TAG","JobSchedulerService onStopJob()******");
        String serviceName = "com.administration.bureau.update.UpDateLocationService";
        if(!ServiceRunningManager.getInstance().isServiceRunning(this, serviceName)){
            startService(new Intent(this,UpDateLocationService.class));
        }
        return true;
    }

    private void setJobSechedule(){
        mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        int id = JOB_ID;
        mJobScheduler.cancel(id);

        JobInfo.Builder builder = new JobInfo.Builder(id, new ComponentName(this, JobSchedulerService.class));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setMinimumLatency(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS); //执行的最小延迟时间
            builder.setOverrideDeadline(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS);  //执行的最长延时时间
            builder.setMinimumLatency(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS);
            builder.setBackoffCriteria(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS, JobInfo.BACKOFF_POLICY_LINEAR);//线性重试方案
        } else {
            builder.setPeriodic(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS);
        }
        builder.setPersisted(true);  // 设置设备重启时，执行该任务
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setRequiresCharging(true); // 当插入充电器，执行该任务
        JobInfo info = builder.build();

        mJobScheduler.schedule(info); //开始定时执行该系统任务
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
