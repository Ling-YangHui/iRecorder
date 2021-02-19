package com.yanghui.irecorder.core;

import android.annotation.SuppressLint;
import android.app.Activity;

import java.util.Timer;
import java.util.TimerTask;

public class Looper {

    @SuppressLint("StaticFieldLeak")
    public static Activity activity = null;
    public static Timer timer;
    public static TimerTask task;

    public static void setActivity(Activity newActivity) {
        activity = newActivity;
    }

    public static void Start() {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                if (activity != null)
                    for (Record record : Record.records) {
                        record.refreshData();
                        if (record.isValid)
                            activity.runOnUiThread(record::refreshUI);
                        else
                            activity.runOnUiThread(record::defaultUI);
                    }
            }
        };
        timer.schedule(task, 500, 60000);
    }

    public static void Stop() {
        timer.cancel();
    }
}
