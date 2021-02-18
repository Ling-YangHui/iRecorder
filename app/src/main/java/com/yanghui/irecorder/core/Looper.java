package com.yanghui.irecorder.core;

import android.annotation.SuppressLint;
import android.app.Activity;

import java.util.Timer;
import java.util.TimerTask;

public class Looper {

    @SuppressLint("StaticFieldLeak")
    public static Activity activity = null;
    public static Timer timer = new Timer();
    public static TimerTask task = new TimerTask() {
        @Override
        public void run() {
            if (activity != null)
                for (Record record : Record.records) {
                    int i = record.refreshData();
                    if (i == 1)
                        activity.runOnUiThread(record::refreshUI);
                    else
                        activity.runOnUiThread(record::defaultUI);
                }
        }
    };

    public static void setActivity(Activity newActivity) {
        activity = newActivity;
    }

    public static void Start() {
        timer.schedule(task, 500, 60000);
    }

    public static void Stop() {
        timer.cancel();
    }
}
