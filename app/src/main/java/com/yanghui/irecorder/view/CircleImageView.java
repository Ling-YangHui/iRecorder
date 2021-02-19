package com.yanghui.irecorder.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.yanghui.tools.LayoutTool;

public class CircleImageView extends View {

    private final Paint imagePaint;
    private final int OUTER_WIDTH_SIZE;// 内部大小
    private final int OUTER_HEIGHT_SIZE;// 外部大小
    private final Context context;
    private Bitmap image = null;
    private int realWidth;// 绘图使用的宽
    private int realHeight;// 绘图使用的高

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        OUTER_WIDTH_SIZE = LayoutTool.dip2px(context, 50.0f);
        OUTER_HEIGHT_SIZE = LayoutTool.dip2px(context, 50.0f);
        setBackgroundColor(0x00000000);
        imagePaint = new Paint();
        imagePaint.setAntiAlias(true);
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
    @SuppressLint("DrawAllocation")
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (image == null) {
            return;
        }
        BitmapShader bitmapShader = new BitmapShader(image, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        imagePaint.setShader(bitmapShader);
        canvas.drawCircle(
                realWidth / 2f,
                realHeight / 2f,
                realHeight / 2f,
                imagePaint
        );
    }

    public void setImage(Bitmap image) {
        this.image = LayoutTool.zoomBitmap(image, realWidth, realHeight);
        invalidate();
    }
}
