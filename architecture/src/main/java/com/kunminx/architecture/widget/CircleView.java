package com.kunminx.architecture.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class CircleView extends View {
    private Paint circlePaint;
    private int mColor;

    public CircleView(Context context) {
        this(context,null);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        circlePaint=new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setDither(true);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(getWidth()/2,getHeight()/2,getWidth()/2,circlePaint);
    }


    public void exchangeColor(int color){
        mColor=color;
        circlePaint.setColor(ContextCompat.getColor(getContext(),color));
        invalidate();
    }

    public int getCircleColor() {
        return mColor;
    }
}