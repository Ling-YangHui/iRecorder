package com.yanghui.irecorder.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.yanghui.irecorder.R;
import com.yanghui.irecorder.core.ActivityHandler;
import com.yanghui.irecorder.core.Record;
import com.yanghui.irecorder.view.CircleImageView;

import java.io.IOException;

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
    private TextView[] total;
    private TextView[] avgSpeed;
    private TextView[] nowSpeed;
    private boolean isExpanded = false;
    private String[] abstractGroup;
    private Handler retryHandler;

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
        total = new TextView[]{
                findViewById(R.id.detailActivity_body_viewTotal),
                findViewById(R.id.detailActivity_body_favoTotal),
                findViewById(R.id.detailActivity_body_likeTotal),
                findViewById(R.id.detailActivity_body_coinTotal),
                findViewById(R.id.detailActivity_body_replyTotal),
                findViewById(R.id.detailActivity_body_shareTotal)
        };
        avgSpeed = new TextView[]{
                findViewById(R.id.detailActivity_body_viewAvg),
                findViewById(R.id.detailActivity_body_favoAvg),
                findViewById(R.id.detailActivity_body_likeAvg),
                findViewById(R.id.detailActivity_body_coinAvg),
                findViewById(R.id.detailActivity_body_replyAvg),
                findViewById(R.id.detailActivity_body_shareAvg)
        };
        nowSpeed = new TextView[]{
                findViewById(R.id.detailActivity_body_viewNow),
                findViewById(R.id.detailActivity_body_favoNow),
                findViewById(R.id.detailActivity_body_likeNow),
                findViewById(R.id.detailActivity_body_coinNow),
                findViewById(R.id.detailActivity_body_replyNow),
                findViewById(R.id.detailActivity_body_shareNow)
        };

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
    }

    @Override
    protected void onStart() {
        super.onStart();
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
                    refreshData();
                });
            }
        });
        if (record.isValid) {
            abstractGroup = record.append.split("\\n");
            detailActivity_body_name.setText(record.name);
            detailActivity_body_bvid.setText("  " + record.bvid + "  ");
            detailActivity_body_uploader.setText(record.uploader);
            detailActivity_body_time.setText(Integer.toString(record.current[0]));
            Thread thread = new Thread(() -> {
                if (record.face == null) {
                    try {
                        record.downloadImage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                this.runOnUiThread(() -> detailActivity_body_image.setImage(record.face, this));
            });
            thread.start();
            if (abstractGroup.length >= 2) {
                String string = abstractGroup[0] + "\n" + abstractGroup[1];
                detailActivity_body_abstract.setText(string);
            } else {
                detailActivity_body_abstract.setText(record.append);
            }

            refreshData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (DetailActivity.record != null)
            DetailActivity.record.cancelListener();
        DetailActivity.record = null;
    }

    @SuppressLint("DefaultLocale")
    private void refreshData() {
        try {
            for (int i = 0; i < 6; i++) {
                total[i].setText(String.format("%d", record.current[i]));
                nowSpeed[i].setText(String.format("%.2f/min", record.velocity[i]));
                double time = System.currentTimeMillis() - record.pubDate;
                avgSpeed[i].setText(String.format("%.2f/d", record.current[i] / (time / 1000 / 3600 / 24)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}