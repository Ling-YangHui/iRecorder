package com.yanghui.irecorder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.yanghui.tools.LayoutTool;

public class ListItemView extends View {

    private final int OUTER_WIDTH_SIZE;// 内部大小
    private final int OUTER_HEIGHT_SIZE;// 外部大小
    private final Context context;
    private final Paint numPaint;
    private final Paint namePaint;
    private final Paint appendPaint;
    public int num;
    public String name;
    public String bvID;
    public String time;
    private int realWidth;// 绘图使用的宽
    private int realHeight;// 绘图使用的高

    public ListItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        OUTER_WIDTH_SIZE = LayoutTool.dip2px(context, 420.0f);
        OUTER_HEIGHT_SIZE = LayoutTool.dip2px(context, 60.0f);

        numPaint = new Paint();
        numPaint.setColor(Color.parseColor("#0179D6"));
        numPaint.setAntiAlias(true);

        namePaint = new Paint();
        namePaint.setColor(Color.BLACK);
        namePaint.setAntiAlias(true);

        appendPaint = new Paint();
        appendPaint.setColor(Color.GRAY);
        appendPaint.setAntiAlias(true);
    }

    /* 这个方法在启动的时候就会执行，用来获取当前被设置的空间大小 */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    /* 测量宽度 */
    private int measureWidth(int widthMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthVal = MeasureSpec.getSize(widthMeasureSpec);
        //处理三种模式
        if (widthMode == MeasureSpec.EXACTLY) {
            return widthVal + getPaddingLeft() + getPaddingRight();
        } else if (widthMode == MeasureSpec.UNSPECIFIED) {
            return OUTER_WIDTH_SIZE;
        } else {
            return Math.min(OUTER_WIDTH_SIZE, widthVal);
        }
    }

    /* 测量高度 */
    private int measureHeight(int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightVal = MeasureSpec.getSize(heightMeasureSpec);
        //处理三种模式
        if (heightMode == MeasureSpec.EXACTLY) {
            return heightVal + getPaddingTop() + getPaddingBottom();
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            return OUTER_HEIGHT_SIZE;
        } else {
            return Math.min(OUTER_HEIGHT_SIZE, heightVal);
        }
    }

    /* 当大小被重新设置以后触发这个方法，用来获取实际生成的大小信息 */
    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        realWidth = w;
        realHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /* 绘制左侧的数字，默认监测量不会超过100 */
        numPaint.setTextSize(realHeight * 0.4f);
        Paint.FontMetrics numMetrics = numPaint.getFontMetrics();
        float numFontSize = numMetrics.bottom - numMetrics.top;
        canvas.drawText(
                "0",
                realWidth / 16f,
                realHeight / 2f + numFontSize / 4,
                numPaint);
        /* 绘制标题 */
        namePaint.setTextSize(realHeight * 0.3f);
        Paint.FontMetrics nameMetrics = namePaint.getFontMetrics();
        float nameFontSize = nameMetrics.bottom - nameMetrics.top;
        canvas.drawText(
                LayoutTool.CutString(
                        namePaint,
                        "【风华引丨洛天依/乐正绫/言和/乐正龙牙】2020新年献曲",
                        realWidth * 0.85f),
                realWidth * 0.15f,
                realHeight * 0.35f + nameFontSize / 4f,
                namePaint
        );
        /* 绘制附加信息 */
        appendPaint.setTextSize(realHeight * 0.2f);
        Paint.FontMetrics appendMetrics = appendPaint.getFontMetrics();
        float appendFontSize = appendMetrics.bottom - appendMetrics.top;
        canvas.drawText(
                "BV17J411h7zd",
                realWidth * 0.15f,
                realHeight * 0.7f + nameFontSize / 4f,
                appendPaint
        );
        canvas.drawText(
                "3小时前",
                realWidth * 0.8f,
                realHeight * 0.7f + nameFontSize / 4f,
                appendPaint
        );
    }
}
