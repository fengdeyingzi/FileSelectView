package com.xl.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.*;
import android.os.*;

/*
 *垂直方向的拖动条
 */

public class VerticalSeekBar extends SeekBar {

	private int start = 0;
	
	private static Handler handler;

	public static final int SET = 0;
	
	
    public VerticalSeekBar(Context context) {
        super(context);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

	
	//设置Handler
	public static void setHandler(Handler mHandler) {
		handler = mHandler;
	}

//	public static Handler getHandler() {
//		return handler;
//	}
	
	
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    protected void onDraw(Canvas canvas) {

        canvas.rotate(90);
        canvas.translate(0, -getWidth());
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
				break;
            case MotionEvent.ACTION_MOVE:
            	start = getMax() - (int) (getMax() * event.getY() / getHeight());
                setProgress(getMax() - start);
                onSizeChanged(getWidth(), getHeight(), 0, 0);
				Message msg = handler.obtainMessage(SET,String.valueOf(getProgress()));
				handler.sendMessage(msg);
				break;
            case MotionEvent.ACTION_UP:
				
                break;
           
        }
        return true;
	}


}
	
