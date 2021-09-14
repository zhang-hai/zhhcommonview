package com.zhh.commonview.utils;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.DisplayMetrics;

/**
 * Created by zhanghai on 2018/7/31.
 * function：
 */

public class DensityUtil {
    private static float density = -1F;

    private DensityUtil() {
    }

    public static float getDensity(@NonNull Context context) {
        if (density <= 0F) {
            density = context.getResources().getDisplayMetrics().density;
        }
        return density;
    }

    public static int dip2px(@NonNull Context context,float dpValue) {
        return (int) (dpValue * getDensity(context) + 0.5F);
    }

    public static int px2dip(@NonNull Context context,float pxValue) {
        return (int) (pxValue / getDensity(context) + 0.5F);
    }

    /**
     * 屏幕适配宽度
     * @param context 上下文
     * @param baseWidth 设计UI时的基准屏幕宽度 像素为单位
     * @param size 尺寸 像素为单位
     * @return 返回转换后的尺寸
     */
    public static int getPercentWidthSize(@NonNull Context context,int baseWidth,int size){
        if(baseWidth <= 0) return size;
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        return size * width / baseWidth;
    }

    /**
     * 屏幕适配宽度
     * @param context 上下文
     * @param baseHeight 设计UI时的基准屏幕高度 像素为单位
     * @param size 尺寸 像素为单位
     * @return 返回转换后的尺寸
     */
    public static int getPercentHeightSize(@NonNull Context context,int baseHeight,int size){
        if(baseHeight <= 0) return size;
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int height = dm.heightPixels;
        return size * height / baseHeight;
    }
}
