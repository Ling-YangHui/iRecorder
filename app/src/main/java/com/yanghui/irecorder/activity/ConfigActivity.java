package com.yanghui.irecorder.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yanghui.irecorder.R;
import com.yanghui.irecorder.core.ActivityHandler;

public class ConfigActivity extends Activity {

    RadioGroup configActivity_current;
    RadioGroup configActivity_average;
    RadioButton configActivity_current_min;
    RadioButton configActivity_current_h;
    RadioButton configActivity_current_d;
    RadioButton configActivity_average_min;
    RadioButton configActivity_average_h;
    RadioButton configActivity_average_d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        configActivity_current = findViewById(R.id.configActivity_current);
        configActivity_average = findViewById(R.id.configActivity_average);
        configActivity_average_d = findViewById(R.id.configActivity_average_d);
        configActivity_average_h = findViewById(R.id.configActivity_average_h);
        configActivity_average_min = findViewById(R.id.configActivity_average_min);
        configActivity_current_d = findViewById(R.id.configActivity_current_d);
        configActivity_current_h = findViewById(R.id.configActivity_current_h);
        configActivity_current_min = findViewById(R.id.configActivity_current_min);

        String currentConfig = ActivityHandler.configMap.get("currentSpeedUnit");
        String averageConfig = ActivityHandler.configMap.get("averageSpeedUnit");
        switch (currentConfig) {
            case "h":
                configActivity_current_h.setChecked(true);
                break;
            case "min":
                configActivity_current_min.setChecked(true);
                break;
            case "d":
                configActivity_current_d.setChecked(true);
        }
        switch (averageConfig) {
            case "h":
                configActivity_average_h.setChecked(true);
                break;
            case "min":
                configActivity_average_min.setChecked(true);
                break;
            case "d":
                configActivity_average_d.setChecked(true);
        }
        configActivity_current.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.configActivity_current_min) {
                ActivityHandler.configMap.put("currentSpeedUnit", "min");
            } else if (checkedId == R.id.configActivity_current_d) {
                ActivityHandler.configMap.put("currentSpeedUnit", "d");
            } else {
                ActivityHandler.configMap.put("currentSpeedUnit", "h");
            }
        });
        configActivity_average.setOnCheckedChangeListener((Group, checkedId) -> {
            if (checkedId == R.id.configActivity_average_min) {
                ActivityHandler.configMap.put("averageSpeedUnit", "min");
            } else if (checkedId == R.id.configActivity_average_d) {
                ActivityHandler.configMap.put("averageSpeedUnit", "d");
            } else {
                ActivityHandler.configMap.put("averageSpeedUnit", "h");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            DetailActivity detailActivity = ((DetailActivity) ActivityHandler.activityMap.get(
                    "detailActivity"));
            detailActivity.refreshData();
        } catch (Exception ignore) {
        }
    }
}