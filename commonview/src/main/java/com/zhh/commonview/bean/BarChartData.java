package com.zhh.commonview.bean;

import java.util.List;

/**
 * Created by zhanghai on 2020/5/8.
 * function：饼图的数据结构
 */
public class BarChartData {
    public String xAxisLabel; //名称
    public ItemBarChartData itemBarData;   //单独的柱状图
    public List<ItemBarChartData> dataSet; //组模式柱状图数据

    public BarChartData(String xAxisLabel) {
        this.xAxisLabel = xAxisLabel;
    }

    public final static class ItemBarChartData{
        public String name; //柱状图名称
        public int color;   //柱状图颜色
        public float value; //柱状图的值

        public ItemBarChartData(String name, int color, float value) {
            this.name = name;
            this.color = color;
            this.value = value;
        }
    }
}
