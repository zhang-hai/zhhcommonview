package com.zhh.commonview.utils;

import android.content.Context;
import android.support.annotation.NonNull;

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
}
