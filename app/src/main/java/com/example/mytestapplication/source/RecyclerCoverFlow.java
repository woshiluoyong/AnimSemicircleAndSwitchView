package com.example.mytestapplication.source;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 继承RecyclerView重写{@link #getChildDrawingOrder(int, int)}对Item的绘制顺序进行控制
 *
 * @version V1.0
 * @Datetime 2017-04-18
 */

public class RecyclerCoverFlow extends RecyclerView {
    /**
     * 按下的X轴坐标
     */
    private float mDownX;
    private float mDownY;

    /**
     * 布局器构建者
     */
    private CoverFlowLayoutManger.Builder mManagerBuilder;

    public RecyclerCoverFlow(Context context) {
        super(context);
        init();
    }

    public RecyclerCoverFlow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecyclerCoverFlow(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        createManageBuilder();
        setLayoutManager(mManagerBuilder.build());
        setChildrenDrawingOrderEnabled(true); //开启重新排序
        setOverScrollMode(OVER_SCROLL_NEVER);
//        if (isStart){
//            onStop();
//            isStart = false;
//        }
//        doStart();
    }

    /**
     * 创建布局构建器
     */
    private void createManageBuilder() {
        if (mManagerBuilder == null) {
            mManagerBuilder = new CoverFlowLayoutManger.Builder();
        }
    }

    /**
     * 设置是否为普通平面滚动
     *
     * @param isFlat true:平面滚动；false:叠加缩放滚动
     */
    public void setFlatFlow(boolean isFlat) {
        createManageBuilder();
        mManagerBuilder.setFlat(isFlat);
        setLayoutManager(mManagerBuilder.build());
    }

    /**
     * 设置Item灰度渐变
     *
     * @param greyItem true:Item灰度渐变；false:Item灰度不变
     */
    public void setGreyItem(boolean greyItem) {
        createManageBuilder();
        mManagerBuilder.setGreyItem(greyItem);
        setLayoutManager(mManagerBuilder.build());
    }

    /**
     * 设置Item透明度渐变
     *
     * @param alphaItem true:Item半透渐变；false:Item透明度不变
     */
    public void setAlphaItem(boolean alphaItem) {
        createManageBuilder();
        mManagerBuilder.setAlphaItem(alphaItem);
        setLayoutManager(mManagerBuilder.build());
    }

    /**
     * 设置Item的间隔比例
     *
     * @param intervalRatio Item间隔比例。
     *                      即：item的宽 x intervalRatio
     */
    public void setIntervalRatio(float intervalRatio) {
        createManageBuilder();
        mManagerBuilder.setIntervalRatio(intervalRatio);
        setLayoutManager(mManagerBuilder.build());
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        if (!(layout instanceof CoverFlowLayoutManger)) {
            throw new IllegalArgumentException("The layout manager must be CoverFlowLayoutManger");
        }
        super.setLayoutManager(layout);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        int center = getCoverFlowLayout().getCenterPosition()
                - getCoverFlowLayout().getFirstVisiblePosition(); //计算正在显示的所有Item的中间位置
        if (center < 0) center = 0;
        else if (center > childCount) center = childCount;
        int order;
        if (i == center) {
            order = childCount - 1;
        } else if (i > center) {
            order = center + childCount - 1 - i;
        } else {
            order = i;
        }
        return order;
    }

    /**
     * 获取LayoutManger，并强制转换为CoverFlowLayoutManger
     */
    public CoverFlowLayoutManger getCoverFlowLayout() {
        return ((CoverFlowLayoutManger) getLayoutManager());
    }

    /**
     * 获取被选中的Item位置
     */
    public int getSelectedPos() {
        return getCoverFlowLayout().getSelectedPos();
    }

    /**
     * 设置选中监听
     *
     * @param l 监听接口
     */
    public void setOnItemSelectedListener(CoverFlowLayoutManger.OnSelected l) {
        getCoverFlowLayout().setOnSelectedListener(l);
    }

    public int getCurrentPage() {
        return mManagerBuilder.build().getCenterPosition();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(ev.getX() - mDownX) > 5) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                /*if ((ev.getX() > mDownX && getCoverFlowLayout().getCenterPosition() == 0) ||
                        (ev.getX() < mDownX && getCoverFlowLayout().getCenterPosition() ==
                                getCoverFlowLayout().getItemCount() - 1)) {
                    //如果是滑动到了最前和最后，开放父类滑动事件不拦截
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    //滑动到中间，设置父类拦截滑动事件
                    getParent().requestDisallowInterceptTouchEvent(true);
                }*/
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    Timer mTimer;
    boolean isStart = false;
    int TIME = 3000;

    private void doStart() {
        isStart = true;
        if (mTimer == null) {
            mTimer = new Timer();
        }
        mTimer.schedule(new AutoTask(), TIME, TIME);
    }

    private class AutoTask extends TimerTask {
        @Override
        public void run() {
            mHandler.post(runnable);
        }
    }


    private static final String TAG = "RecyclerCoverFlow";
    private AutoHandler mHandler = new AutoHandler();
    int startX = 0;
    float endX = 15;
    ValueAnimator valueAnimator;

    private final static class AutoHandler extends android.os.Handler {
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (valueAnimator != null && valueAnimator.isRunning()) {
                valueAnimator.end();
            }
            valueAnimator = ValueAnimator.ofFloat(startX, endX);
            Log.d(TAG, "run: startX==" + startX + ";;;endX==" + endX);
            valueAnimator.setDuration(1000);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float end = (float) animation.getAnimatedValue();
                    Log.d(TAG, "onAnimationUpdate: end ==" + end);
                    scrollBy((int) end, 0);
                }
            });
            valueAnimator.start();
        }
    };

    public void onStop() {
        isStart = false;
        //先取消定时器
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    public void onDestroy() {
        onStop();
        valueAnimator.end();
    }
}
