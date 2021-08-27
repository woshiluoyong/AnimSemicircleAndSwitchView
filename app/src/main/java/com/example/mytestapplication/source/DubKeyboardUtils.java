package com.example.mytestapplication.source;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;

import java.lang.reflect.Field;
import java.util.Calendar;

public final class DubKeyboardUtils {

    private DubKeyboardUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /*
      避免输入法面板遮挡
      <p>在manifest.xml中activity中设置</p>
      <p>android:windowSoftInputMode="adjustPan"</p>
     */

    /**
     * 动态显示软键盘
     *
     * @param activity activity
     */
    public static void showInputMethod(final Activity activity) {
        View view = activity.getCurrentFocus();
        if (view == null) view = new View(activity);
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 动态显示软键盘
     *
     * @param view 视图
     */
    public static void showInputMethod(Activity activity, final View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 动态隐藏软键盘
     *
     * @param activity activity
     */
    public static void hideSoftInput(final Activity activity) {
        View view = activity.getCurrentFocus();
        if (view == null) view = new View(activity);
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 动态隐藏软键盘
     *
     * @param view 视图
     */
    public static void hideSoftInput(Activity activity, final View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 切换键盘显示与否状态
     */
    public static void toggleSoftInput(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * 点击屏幕空白区域隐藏软键盘
     * <p>根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘</p>
     * <p>需重写dispatchTouchEvent</p>
     * <p>参照以下注释代码</p>
     */
    public static void clickBlankArea2HideSoftInput() {
        Log.d("tips", "U should copy the following code.");
        /*
        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (isShouldHideKeyboard(v, ev)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
        private boolean isShouldHideKeyboard(View v, MotionEvent event) {
            if (v != null && (v instanceof EditText)) {
                int[] l = {0, 0};
                v.getLocationInWindow(l);
                int left = l[0],
                        top = l[1],
                        bottom = top + v.getHeight(),
                        right = left + v.getWidth();
                return !(event.getX() > left && event.getX() < right
                        && event.getY() > top && event.getY() < bottom);
            }
            return false;
        }
        */
    }

    /**
     * 显示软键盘
     */
    public static void showInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * 显示软键盘
     */
    public static void showInputMethod(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 多少时间后显示软键盘
     */
    public static void showInputMethod(final View view, long delayMillis) {
        // 显示输入法
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                DubKeyboardUtils.showInputMethod(view);
            }
        }, delayMillis);
    }

    /**
     * Dialog关毕键盘也关毕
     */
    public static void hideSoftInput(final Activity activity, final Dialog dialog, long delayMillis) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (!dialog.isShowing()) {
                    DubKeyboardUtils.hideSoftInput(activity);
                }
            }
        }, delayMillis);
    }

    /**
     * 用法if (Utils.isFastClick())
     */
    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static final int CLICK_DELAY_TIME = 2000;
    private static long lastClickTime,lastClickTime1;

    /**
     * @return  default 返回false代表连续点击了
     */
    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    public static boolean checkClickFast() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime1 > CLICK_DELAY_TIME) {
            lastClickTime1 = currentTime;
            return false;
        } else {
            return true;
        }
    }

    /**
     * 是否显示软键盘
     */
    public static boolean isSoftShowing(Activity context) {
        //获取当前屏幕内容的高度
        int screenHeight = context.getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return screenHeight - rect.bottom != 0;
    }

    /**
     * 底部虚拟按键栏的高度
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static int getSoftButtonsBarHeight(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

    /**
     * 输入法键盘弹出与否判断和输入法键盘弹出时将界面滚动到指定位置
     */
    public static void pullKeywordTop(final Activity activity,final int lyRootID,final int vID,final int svID) {
        ViewGroup ly = (ViewGroup) activity.findViewById(lyRootID);
        //获取屏幕高度，根据经验，输入法弹出高度一般在屏幕1/3到1/2之间
        final int defaultHeight = ((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
        final int mKeyHeight = defaultHeight / 4;
        ly.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                //获取根布局前后高度差
                int height = oldBottom - bottom;
                ScrollView sv = (ScrollView) activity.findViewById(svID);
                if (height > mKeyHeight) {//当高度差大于屏幕1/4，认为是输入法弹出变动，可能会有特殊机型会失败
                    final int lybottom = bottom;
                    sv.post(new Runnable() {//用post防止有时输入法会自动滚动覆盖我们手动滚动
                        @Override
                        public void run() {
                            ScrollView runSv = (ScrollView) activity.findViewById(svID);
                            //获取要滚动至的控件到屏幕顶部高度
                            View v = (View) activity.findViewById(vID);
                            int[] loca = new int[2];
                            v.getLocationOnScreen(loca);
                            //这种通知栏高度获取方法必须在布局构建完毕后才能生效，否则获取为0
                            Rect frame = new Rect();
                            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                            int statusBarHeight = frame.top;
                            // 要滚动的距离=控件距屏幕顶部距离+控件高度-输入法弹出后的activity高度-通知栏高度
                            int scrollHeight = loca[1] + v.getHeight() - lybottom - statusBarHeight;
                            if (scrollHeight > 0) {
                                runSv.scrollBy(0, scrollHeight);
                            }
                        }
                    });
                } else if (-height > mKeyHeight) {//当输入法收起，回滚回顶部
                    sv.scrollTo(0, 0);
                }
            }
        });
    }

    //InputMethodManager内存泄露现象及解决
    public static void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager == null) {
            return;
        }
        String[] viewArray = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field filed;
        Object filedObject;
        for (String view:viewArray) {
            try{
                filed = inputMethodManager.getClass().getDeclaredField(view);
                if (!filed.isAccessible()) {
                    filed.setAccessible(true);
                }
                filedObject = filed.get(inputMethodManager);
                if (filedObject != null && filedObject instanceof View) {
                    View fileView = (View) filedObject;
                    // 被InputMethodManager持有引用的context是想要目标销毁的
                    if (fileView.getContext() == destContext) {
                        // 置空，破坏掉path to gc节点
                        filed.set(inputMethodManager, null);
                    } else {
                        // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
                        break;
                    }
                }
            }catch(Throwable t){
                t.printStackTrace();
            }
        }
    }

}