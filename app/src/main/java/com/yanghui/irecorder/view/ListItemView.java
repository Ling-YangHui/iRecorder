package com.yanghui.irecorder.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.yanghui.irecorder.core.Record;
import com.yanghui.tools.LayoutTool;

public class ListItemView extends androidx.appcompat.widget.AppCompatButton {

    public String name = "";
    public String bvID = "";
    private final Context context;
    public String time = "";
    private int OUTER_WIDTH_SIZE;// 内部大小
    private int OUTER_HEIGHT_SIZE;// 外部大小
    public int num;
    private Paint numPaint;
    private Paint namePaint;
    private Paint appendPaint;
    private int realWidth;// 绘图使用的宽
    private int realHeight;// 绘图使用的高
//    private OnClickListener mCallback = null;

    public ListItemView(Context context, Record record) {
        super(context);
        this.context = context;
        record.setListItemView(this);
        init();
    }

    public ListItemView(Context context) {
        super(context);
        this.context = context;
    }

    public ListItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public void init() {
        setBackgroundColor(0xFFFFFFFF);
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
                Integer.toString(num),
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
                        name,
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
                bvID,
                realWidth * 0.15f,
                realHeight * 0.7f + nameFontSize / 4f,
                appendPaint
        );
        canvas.drawText(
                time,
                realWidth * 0.8f,
                realHeight * 0.7f + nameFontSize / 4f,
                appendPaint
        );
    }

//    @SuppressLint("ClickableViewAccessibility")
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_BUTTON_PRESS) {
//            if (mCallback != null)
//                mCallback.onClick(this);
//        }
//        return true;
//    }

    public void setName(String name) {
        this.name = name;
        invalidate();
    }

    public void setNum(int num) {
        this.num = num;
        invalidate();
    }

    public void setBvID(String bvID) {
        this.bvID = bvID;
        invalidate();
    }

    public void setTime(String time) {
        this.time = time;
        invalidate();
    }

    public void setView(int num, String... params) {
        if (params.length == 3) {
            setName(params[0]);
            setBvID(params[1]);
            setTime(params[2]);
            setNum(num);
        }
    }

    public void setView(Record record) {
        name = record.name;
        bvID = record.bvid;
        if (name == null)
            name = "";
        time = Integer.toString(record.current[0]);
        invalidate();
    }

    public void setDefault() {
        name = "获取错误";
        bvID = "";
        time = "";
    }
}
