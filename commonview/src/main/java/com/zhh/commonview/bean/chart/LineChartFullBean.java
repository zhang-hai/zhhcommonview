package com.zhh.commonview.bean.chart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 折线图有数据
 */
public class LineChartFullBean implements Serializable {
    public static final int MODE_WEEK = 0;
    public static final int MODE_MONTH = 1;
    public static final int MODE_TERM = 2;

    private int mode = MODE_WEEK;
    private List<LineChartLegendBean> legendList;// 图例列表
    private List<LineChartDataBean> columnList;// 数据列表
    private List<LineChartRowBean> rowValues;// 横坐标值

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public LineChartFullBean() {
        this(null);
    }

    public LineChartFullBean(List<LineChartLegendBean> legendList) {
        this(legendList,null);
    }

    public LineChartFullBean(List<LineChartLegendBean> legendList, List<LineChartRowBean> rowValues) {
        this(MODE_WEEK,legendList,rowValues);
    }

    public LineChartFullBean(int mode, List<LineChartLegendBean> legendList, List<LineChartRowBean> rowValues) {
        this(mode,legendList,rowValues,null);
    }


    public LineChartFullBean(int mode,List<LineChartLegendBean> legendList, List<LineChartRowBean> rowValues,List<LineChartDataBean> columnList) {
        this.mode = mode;
        this.legendList = legendList == null ? new ArrayList<LineChartLegendBean>() : legendList;
        this.rowValues = rowValues == null ? new ArrayList<LineChartRowBean>() : rowValues;
        this.columnList = columnList == null ? new ArrayList<LineChartDataBean>() : columnList;
    }

    public List<LineChartLegendBean> getLegendList() {
        return legendList;
    }

    public void setLegendList(List<LineChartLegendBean> legendList) {
        this.legendList = legendList;
    }

    public List<LineChartDataBean> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<LineChartDataBean> columnList) {
        this.columnList = columnList;
    }

    public List<LineChartRowBean> getRowValues() {
        return rowValues;
    }

    public void setRowValues(List<LineChartRowBean> rowValues) {
        this.rowValues = rowValues;
    }

    /**
     * 无数据
     * @return
     */
    public boolean isNoneData(){
        return legendList == null || legendList.size() == 0;
    }
}
