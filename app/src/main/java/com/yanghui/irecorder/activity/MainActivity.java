package com.yanghui.irecorder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yanghui.irecorder.R;
import com.yanghui.irecorder.core.ActivityHandler;
import com.yanghui.irecorder.core.Looper;
import com.yanghui.irecorder.core.Record;
import com.yanghui.irecorder.view.ListItemView;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mainActivity_body_list;
    private TextView mainActivity_body_multiChoose_back_button;
    private TextView mainActivity_body_antiChoose_button;
    private TextView mainActivity_body_del_button;
    private Button mainActivity_head_add;
    private boolean isOnChooseMode = false;
    private Intent detailIntent;
    private Intent addIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityHandler.activityMap.put("mainActivity", this);
        Toast.makeText(this, "读取数据中...", Toast.LENGTH_SHORT).show();
        detailIntent = new Intent(this, DetailActivity.class);
        addIntent = new Intent(this, AddActivity.class);

        mainActivity_body_list = findViewById(R.id.mainActivity_body_list);
        mainActivity_head_add = findViewById(R.id.mainActivity_head_add);
        mainActivity_body_multiChoose_back_button =
                findViewById(R.id.mainActivity_body_multiChoose_back_button);
        mainActivity_body_antiChoose_button =
                findViewById(R.id.mainActivity_body_antiChoose_button);
        mainActivity_body_del_button = findViewById(R.id.mainActivity_body_del_button);
        mainActivity_head_add.setOnClickListener(v -> startActivity(addIntent));
        mainActivity_body_del_button.setVisibility(View.INVISIBLE);
        mainActivity_body_multiChoose_back_button.setOnClickListener(v -> {
            changeOnChoseMode();
            for (int i = 0; i < mainActivity_body_list.getChildCount(); i++) {
                if (((ListItemView) mainActivity_body_list.getChildAt(i)).isChosen) {
                    ((ListItemView) mainActivity_body_list.getChildAt(i)).setChosen();
                }
            }
        });
        mainActivity_body_antiChoose_button.setOnClickListener(v -> {
            for (int i = 0; i < mainActivity_body_list.getChildCount(); i++) {
                ((ListItemView) mainActivity_body_list.getChildAt(i)).setChosen();
            }
        });
        mainActivity_body_del_button.setOnClickListener(v -> {
            for (int i = mainActivity_body_list.getChildCount() - 1; i >= 0; i--) {
                if (((ListItemView) mainActivity_body_list.getChildAt(i)).isChosen) {
                    Record.records.remove(i);
                }
            }
            SetList();
            changeOnChoseMode();
        });
        Record.records.add(new Record("BV1pK4y1Q7V2", Record.BV));
        Record.records.add(new Record("BV18p4y1p7AV", Record.BV));
        Record.records.add(new Record("BV1nJ411q7ZR", Record.BV));
        SetList();
        Looper.setActivity(this);
        Looper.Start();
    }

    public void SetList() {
        mainActivity_body_list.removeAllViews();
        for (int i = 0; i < Record.records.size(); i++) {
            Record record = Record.records.get(i);
            ListItemView listItemView = new ListItemView(this, record);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            listItemView.setNum(i + 1);
            mainActivity_body_list.addView(listItemView, layoutParams);
            listItemView.setOnClickListener(v -> {
                if (!isOnChooseMode) {
                    DetailActivity.record = record;
                    startActivity(detailIntent);
                } else {
                    listItemView.setChosen();
                }
            });
            listItemView.setOnLongClickListener(v -> {
                Log.i("1", "long");
                if (!isOnChooseMode)
                    changeOnChoseMode();
                return false;
            });
            record.refreshUI();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Record.records.removeAllElements();
        Looper.Stop();
    }

    private void changeOnChoseMode() {
        if (isOnChooseMode) {
            mainActivity_body_multiChoose_back_button.setText("多选");
            mainActivity_body_del_button.setVisibility(View.INVISIBLE);
            isOnChooseMode = false;
        } else {
            mainActivity_body_multiChoose_back_button.setText("返回");
            mainActivity_body_del_button.setVisibility(View.VISIBLE);
            isOnChooseMode = true;
        }
    }
}