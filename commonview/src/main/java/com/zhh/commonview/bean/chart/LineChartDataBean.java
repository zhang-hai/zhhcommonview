package com.zhh.commonview.bean.chart;

import java.io.Serializable;
import java.util.List;

/**
 * 折线图 每列数据
 */
public class LineChartDataBean implements Serializable {
    public static final int TYPE_LINE = 0;
    public static final int TYPE_DASH_LINE = 1;

    private int type = TYPE_LINE;
    private int color;
    private String name;
    private List<LineChartPointBean> pointList;

    public LineChartDataBean() {
    }

    public LineChartDataBean(int color) {
        this.color = color;
    }

    public LineChartDataBean(int type, int color, String name) {
        this.type = type;
        this.color = color;
        this.name = name;
    }

    public LineChartDataBean(int type, int color) {
        this.type = type;
        this.color = color;
    }

    public LineChartDataBean(int color, List<LineChartPointBean> pointList) {
        this.color = color;
        this.pointList = pointList;
    }

    public LineChartDataBean(int type, int color, List<LineChartPointBean> pointList) {
        this.type = type;
        this.color = color;
        this.pointList = pointList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public List<LineChartPointBean> getPointList() {
        return pointList;
    }

    public void setPointList(List<LineChartPointBean> pointList) {
        this.pointList = pointList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
