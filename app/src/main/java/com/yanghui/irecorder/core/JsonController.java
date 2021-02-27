package com.yanghui.irecorder.core;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class JsonController {

    public static void saveData(Context context) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = context.openFileOutput("watchList.json", Context.MODE_PRIVATE);
            // 开始拼接json字符串
            StringBuilder saveStr = new StringBuilder("[");
            for (int i = 0; i < Record.records.size(); i++) {
                saveStr.append("\"").append(Record.records.get(i).bvid).append("\"");
                if (i != Record.records.size() - 1)
                    saveStr.append(",");
            }
            saveStr.append("]");
            fileOutputStream.write(saveStr.toString().getBytes());
        } catch (Exception e) {
            Toast.makeText(context, "无法保存文件", Toast.LENGTH_SHORT).show();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void loadData(Context context) {
        FileInputStream fileInputStream = null;
        Reader reader = null;
        BufferedReader bufferedReader = null;
        try {
            fileInputStream = context.openFileInput("watchList.json");
            reader = new InputStreamReader(fileInputStream);
            bufferedReader = new BufferedReader(reader);
            StringBuilder readStr = new StringBuilder();
            String temp;
            while ((temp = bufferedReader.readLine()) != null) {
                readStr.append(temp);
            }
            JSONArray array = new JSONArray(readStr.toString());
            for (int i = 0; i < array.length(); i++) {
                String id = array.getString(i);
                if (id.toLowerCase().startsWith("bv")) {
                    Record.records.add(new Record(id, Record.BV));
                } else {
                    Record.records.add(new Record(id, Record.AV));
                }
            }
        } catch (Exception e) {
            Toast.makeText(context, "无法保存文件", Toast.LENGTH_SHORT).show();
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }
}