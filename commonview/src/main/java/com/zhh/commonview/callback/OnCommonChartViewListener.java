package com.zhh.commonview.callback;


/**
 * Created by zhanghai on 2020/5/8.
 * function：图表触摸事件回调接口
 */
public interface OnCommonChartViewListener<T>{

    void onCancel();

    /**
     * 点击返回,坐标ix,iy是以view参考，以View的左上角为原点
     * @param data 对应的数据
     * @param ix 对应指标的中间x位置
     * @param iy 对应指标的中间y位置
     */
    void onClick(T data, float ix, float iy);
}