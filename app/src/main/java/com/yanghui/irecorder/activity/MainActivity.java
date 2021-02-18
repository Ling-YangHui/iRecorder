package com.yanghui.irecorder.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.yanghui.irecorder.R;
import com.yanghui.irecorder.core.Looper;
import com.yanghui.irecorder.core.Record;
import com.yanghui.irecorder.view.ListItemView;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mainActivity_body_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity_body_list = findViewById(R.id.mainActivity_body_list);
        SetList();
        Looper.setActivity(this);
        Looper.Start();
    }

    private void SetList() {
        mainActivity_body_list.removeAllViews();
        Record.records.add(new Record("BV1pK4y1Q7V2"));
        Record.records.add(new Record("BV18p4y1p7AV"));
        for (int i = 0; i < Record.records.size(); i++) {
            Record record = Record.records.get(i);
            ListItemView listItemView = new ListItemView(this, record);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            listItemView.setNum(i + 1);
            mainActivity_body_list.addView(listItemView, layoutParams);
            listItemView.setOnClickListener(v -> {
                Log.i("2", "clicked");
            });
            record.refreshUI();
        }
    }
}