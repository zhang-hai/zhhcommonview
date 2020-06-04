package com.zhh.commonview.callback;

/**
 * Created by zhanghai on 2020/5/14.
 * function：折线回调接口
 */
public interface OnLineChartViewListener<T> extends OnCommonChartViewListener<T> {
    void onMove(String value);
}
