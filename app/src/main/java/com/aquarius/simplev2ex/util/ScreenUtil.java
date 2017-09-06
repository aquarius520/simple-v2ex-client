package com.aquarius.simplev2ex.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * Created by aquarius on 2017/8/8.
 */
public class ScreenUtil {

    /**
     * 获取屏幕的高度
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        return getScreenDisplayMetrics(context).heightPixels;
    }

    /**
     * 获取屏幕的宽度
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        return getScreenDisplayMetrics(context).widthPixels;
    }

    /**
     * 获取屏幕的 density
     * @param context
     * @return
     */
    public static float getDeviceDensity(Context context) {
        return getScreenDisplayMetrics(context).density;
    }

    public static DisplayMetrics getScreenDisplayMetrics(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    /**
     * @param value dp对应的值
     * @return
     */
    public static int dp2px(Context context, int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                context.getResources().getDisplayMetrics());
    }

    public static float dp2px(Context context, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                context.getResources().getDisplayMetrics());
    }

//    public static int dp2px(Context context, float dpValue) {
//        final float scale = context.getResources().getDisplayMetrics().density;
//        return (int) (dpValue * scale + 0.5f);
//    }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int dp2sp(Context context,float dipValue){
        return px2sp(context, dp2px(context, dipValue));
    }
}
