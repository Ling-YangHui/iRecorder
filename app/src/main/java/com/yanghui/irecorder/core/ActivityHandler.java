package com.yanghui.irecorder.core;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;

public class ActivityHandler {
    public static Map<String, Activity> activityMap = new HashMap<>();
    public static Map<String, String> configMap = new HashMap<>();

    public static void configMapInit() {
        configMap.put("currentSpeedUnit", "h");
        configMap.put("averageSpeedUnit", "h");
    }
}
