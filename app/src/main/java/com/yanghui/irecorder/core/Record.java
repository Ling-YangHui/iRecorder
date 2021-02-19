package com.yanghui.irecorder.core;

import com.yanghui.irecorder.view.ListItemView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

public class Record {

    public static Vector<Record> records = new Vector<>();

    public final String bvid;
    public final Queue<Integer> queueView = new LinkedList<>();
    public final Queue<Integer> queueFavo = new LinkedList<>();
    public final Queue<Integer> queueLike = new LinkedList<>();
    public final Queue<Integer> queueCoin = new LinkedList<>();
    public final Queue<Integer> queueReply = new LinkedList<>();
    public final Queue<Integer> queueShare = new LinkedList<>();
    public final double[] velocity = new double[6]; // 用于存放六个属性的增速
    public final int[] current = new int[6]; // 用于存放六个属性的当前值
    public boolean isValid = false;
    public boolean isDetailedMode;
    public String name;
    public String append;
    public String uploader;
    private ListItemView listItemView = null;
    private OnRefreshListener listener = null;

    public Record(String bvid) {
        this.bvid = bvid;
    }


    public Record(String bvid, ListItemView listItemView) {
        this.bvid = bvid;
        this.listItemView = listItemView;
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.listener = listener;
    }

    public void cancelListener() {
        listener = null;
    }

    public void setListItemView(ListItemView listItemView) {
        this.listItemView = listItemView;
    }

    private byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    private String readApi(String urlPath) throws IOException {
        URL url = new URL(urlPath);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");

        InputStream iis = url.openStream();
        String s = new String(readInputStream(iis));
        iis.close();
        return s;
    }

    public void refreshUI() {
        if (listItemView != null) {
            listItemView.setView(this);
        }
    }

    public void defaultUI() {
        if (listItemView != null) {
            listItemView.setDefault();
        }
    }

    public void periodicEvents() {
        refreshData();
        listItemView.setView(this);
    }

    public void refreshData() {
        String url = "http://api.bilibili.com/x/web-interface/view?bvid=" + bvid;
        try {
            String info = readApi(url);
            JSONObject j = new JSONObject(info);
            int code = j.getInt("code");
            if (code == 0) {
                JSONObject stat = j.getJSONObject("data").getJSONObject("stat");
                JSONObject data = j.getJSONObject("data");
                JSONObject owner = j.getJSONObject("data").getJSONObject("owner");
                uploader = owner.getString("name");
                name = data.getString("title");
                append = data.getString("desc");
                current[0] = stat.getInt("view");
                current[1] = stat.getInt("favorite");
                current[2] = stat.getInt("like");
                current[3] = stat.getInt("coin");
                current[4] = stat.getInt("reply");
                current[5] = stat.getInt("share");
                queueView.offer(current[0]);
                queueFavo.offer(current[1]);
                queueLike.offer(current[2]);
                queueCoin.offer(current[3]);
                queueReply.offer(current[4]);
                queueShare.offer(current[5]);
            }
            if (queueView.size() > 10) {
                queueView.poll();
                queueFavo.poll();
                queueLike.poll();
                queueCoin.poll();
                queueReply.poll();
                queueShare.poll();
            }
            calculateEachVelocity();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            isValid = false;
        }
        if (listener != null) {
            listener.refresh(this);
        }
        isValid = true;
    }

    /**
     * 计算增速 使用差值法对队列里的数据进行拟合，计算器增长速率，计算结果保存在velocity[]里。
     */
    private void calculateEachVelocity() {
        velocity[0] = calculateVelocity(queueView);
        velocity[1] = calculateVelocity(queueFavo);
        velocity[2] = calculateVelocity(queueLike);
        velocity[3] = calculateVelocity(queueCoin);
        velocity[4] = calculateVelocity(queueReply);
        velocity[5] = calculateVelocity(queueShare);
    }

    /**
     * 计算一个队列的增速，仅被calculateEachVelocity()调用。
     *
     * @param queue 用于计算的队列，其长度不超过10，不过超过了也没关系。
     * @return 返回值为计算结果。
     */
    private double calculateVelocity(Queue<Integer> queue) {
        if (queue.size() <= 1) {
            return 0.0;
        } else if (queue.size() < 10) {
            Integer[] nums = queue.toArray(new Integer[0]);
            return (nums[nums.length - 1] - nums[0]) / (nums.length - 1.0);
        }
        // queue.size() == 10:
        Integer[] nums = queue.toArray(new Integer[0]);
        int sum1 = nums[0] + nums[1] + nums[2] + nums[3] + nums[4];
        int sum2 = nums[5] + nums[6] + nums[7] + nums[8] + nums[9];
        return (sum2 - sum1) / 25.0;
    }

    public double[] getVelocityValues() {
        return this.velocity;
    }

    public int[] getCurrentValues() {
        return this.current;
    }

    public interface OnRefreshListener {
        void refresh(Record record);
    }
}
