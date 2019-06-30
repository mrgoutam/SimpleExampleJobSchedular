package com.stark.examplejobschedular;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void scheduleJob(View v) {
        //https://developer.android.com/reference/android/app/job/JobInfo.Builder.html
        ComponentName componentName = new ComponentName(this, ExampleJobService.class);
        //every job has a unique id that i have to pass
        //we can use this id later like when we want to cancel this job
        JobInfo info = new JobInfo.Builder(123, componentName)
                //Specify that to run this job, the device must be charging (or be a non-battery-powered device connected to permanent power, such as Android TV devices).
                .setRequiresCharging(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED) //job will run only on wifi connection
                .setPersisted(true) //whether or not to persist this job across device reboots.
                .setPeriodic(15 * 60 * 1000)  //after 15 min job will restart
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled");
        } else {
            Log.d(TAG, "Job scheduling failed");
        }
    }

    public void cancelJob(View v) {
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
        Log.d(TAG, "Job cancelled");
    }
}