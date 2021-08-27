package com.example.mytestapplication.source;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class SlipSolveListView extends ListView {
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public SlipSolveListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
