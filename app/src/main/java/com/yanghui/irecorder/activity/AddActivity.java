package com.yanghui.irecorder.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yanghui.irecorder.R;
import com.yanghui.irecorder.core.ActivityHandler;
import com.yanghui.irecorder.core.Record;

public class AddActivity extends Activity {

    private EditText addActivity_body_input;
    private Button addActivity_body_add;
    private TextView addActivity_body_name;
    private Record record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ActivityHandler.activityMap.put("addActivity", this);

        addActivity_body_input = findViewById(R.id.addActivity_body_input);
        addActivity_body_add = findViewById(R.id.addActivity_body_add);
        addActivity_body_name = findViewById(R.id.addActivity_body_name);
        addActivity_body_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                record = phraseVideo(addActivity_body_input.getText().toString());
                if (record != null) {
                    Thread thread = new Thread(() -> {
                        try {
                            record.refreshData();
                            if (record.name != null)
                                AddActivity.this.runOnUiThread(() -> addActivity_body_name.setText(record.name));
                            else
                                AddActivity.this.runOnUiThread(() -> addActivity_body_name.setText(""));
                        } catch (Exception ignored) {
                        }
                    });
                    thread.start();
                } else {
                    addActivity_body_name.setText("");
                }
            }
        });
        addActivity_body_add.setOnClickListener(v -> {
            if (record != null) {
                Record.records.add(record);
                ((MainActivity) ActivityHandler.activityMap.get("mainActivity")).SetList();
                finish();
            }
        });
    }

    private Record phraseVideo(String text) {
        int index = text.indexOf("?");
        if (index != -1) {
            text = text.substring(0, index);
            text = text.replace("www.bilibili.com/video/", "");
            text = text.replace("https://", "");
            text = text.replace("http://", "");
            text = text.replace("/", "");
        }

        if (text.length() < 2) {
            return null;
        }

        try {
            boolean b = text.charAt(1) == 'V' || text.charAt(1) == 'v';
            if ((text.charAt(0) == 'B' || text.charAt(0) == 'b') && b) {
                return new Record(text, Record.BV);
            } else if ((text.charAt(0) == 'A' || text.charAt(0) == 'a') && b) {
                return new Record(text.substring(2), Record.AV);
            } else if (text.equals(String.valueOf(Integer.valueOf(text)))) {
                return new Record(text, Record.AV);
            } else {
                // 无法识别
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}