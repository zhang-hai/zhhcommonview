package com.zhh.commonview.utils;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by zhanghai on 2019/1/23.
 * function：键盘隐藏和展示
 */
public class KeyboardUtil {

    /**
     * 隐藏软键盘
     *
     * @param activity
     */
    public static void hideSoft(Activity activity) {
        try {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
        }
    }

    /**
     * 强制隐藏键盘
     * @param view
     */
    public static void hideSoftInput(@NonNull View view) {
        try {
            InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 强制显示键盘
     * @param view
     */
    public static void showSoftInput(@NonNull View view) {
        try {
            InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
