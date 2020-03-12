package com.yizhitong.wisdombuilding.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 *
 */
public class noFocusableGridleView extends GridView {
    public noFocusableGridleView(Context context) {
        super(context);
    }

    public noFocusableGridleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public noFocusableGridleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_MOVE){
            return true;//true:禁止滚动
        }
        return super.dispatchTouchEvent(ev);
    }
}
