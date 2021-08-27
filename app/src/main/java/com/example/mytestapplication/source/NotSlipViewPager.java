package com.example.mytestapplication.source;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class NotSlipViewPager extends ViewPager {
    public NotSlipViewPager(Context context) {
        super(context);
    }

    public NotSlipViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
