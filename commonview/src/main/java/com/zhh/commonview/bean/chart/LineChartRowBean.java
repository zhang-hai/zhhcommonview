package com.zhh.commonview.bean.chart;

import java.io.Serializable;

public class LineChartRowBean implements Serializable {
    private int value = 0;
    private String unit = "";

    public String getShowText() {
        return value + "" + unit;
    }

    public LineChartRowBean() {
    }

    public LineChartRowBean(int value) {
        this.value = value;
        this.unit = "";
    }

    public LineChartRowBean(int value, String unit) {
        this.value = value;
        this.unit = unit;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
