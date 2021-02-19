package com.yanghui.irecorder.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yanghui.irecorder.R;
import com.yanghui.irecorder.core.ActivityHandler;
import com.yanghui.irecorder.core.Record;
import com.yanghui.irecorder.view.CircleImageView;

public class DetailActivity extends Activity {

    public static Record record = null;
    private View detailActivity_head_back_button;
    private TextView detailActivity_body_name;
    private TextView detailActivity_body_bvid;
    private TextView detailActivity_body_time;
    private CircleImageView detailActivity_body_image;
    private TextView detailActivity_body_uploader;
    private TextView detailActivity_body_abstract;
    private TextView detailActivity_body_more;
    private boolean isExpanded = false;
    private String[] abstractGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ActivityHandler.activityMap.put("detailActivity", this);
        detailActivity_head_back_button = findViewById(R.id.detailActivity_head_back_button);
        detailActivity_body_name = findViewById(R.id.detailActivity_body_name);
        detailActivity_body_bvid = findViewById(R.id.detailActivity_body_bvid);
        detailActivity_body_time = findViewById(R.id.detailActivity_body_time);
        detailActivity_body_abstract = findViewById(R.id.detailActivity_body_abstract);
        detailActivity_body_uploader = findViewById(R.id.detailActivity_body_uploader);
        detailActivity_body_image = findViewById(R.id.detailActivity_body_image);
        detailActivity_body_more = findViewById(R.id.detailActivity_body_more);

        detailActivity_body_more.setOnClickListener(v -> {
            if (!isExpanded) {
                detailActivity_body_abstract.setText(DetailActivity.record.append);
                detailActivity_body_more.setText("折叠");
                isExpanded = true;
            } else {
                if (abstractGroup.length >= 2) {
                    String string = abstractGroup[0] + "\n" + abstractGroup[1];
                    detailActivity_body_abstract.setText(string);
                } else {
                    detailActivity_body_abstract.setText(record.append);
                }
                detailActivity_body_more.setText("更多...");
                isExpanded = false;
            }
        });
        detailActivity_head_back_button.setOnClickListener(v -> {
            finish();
        });
        setView();
    }

    @SuppressLint("SetTextI18n")
    public void setView() {
        Record record = DetailActivity.record;
        if (record == null) {
            return;
        }
        record.setOnRefreshListener(r -> {
            if (r.isValid) {
                this.runOnUiThread(() -> {
                    detailActivity_body_time.setText(Integer.toString(r.current[0]));
                });
            }
        });
        if (record.isValid) {
            abstractGroup = record.append.split("\\n");
            detailActivity_body_name.setText(record.name);
            detailActivity_body_bvid.setText(" " + record.bvid + " ");
            detailActivity_body_uploader.setText(record.uploader);
            detailActivity_body_time.setText(Integer.toString(record.current[0]));
            if (abstractGroup.length >= 2) {
                String string = abstractGroup[0] + "\n" + abstractGroup[1];
                detailActivity_body_abstract.setText(string);
            } else {
                detailActivity_body_abstract.setText(record.append);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DetailActivity.record.cancelListener();
        DetailActivity.record = null;
    }
}