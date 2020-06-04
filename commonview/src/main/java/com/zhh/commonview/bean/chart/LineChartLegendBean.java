package com.zhh.commonview.bean.chart;

/**
 * 折线图图例对象
 */
public class LineChartLegendBean {
    public static final int TYPE_CIRCLE = 0;// 圆点
    public static final int TYPE_LINE = 1;// 直线
    public static final int TYPE_DASH_LINE = 2;// 虚线

    private int type = TYPE_CIRCLE;
    private int color;
    private String name;
    private float offsetX, offsetY;
    private float x0, x1, y0, y1;// 记录图例范围 用于判断点击范围
    private boolean clickable;

    private boolean checked;

    public LineChartLegendBean() {
    }

    public LineChartLegendBean(int type, int color, String name) {
        this.type = type;
        this.color = color;
        this.name = name;
        this.checked = true;// 默认选中
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getX0() {
        if (offsetX > 0) {
            float v = x0 - offsetX;
            if (v > 0)
                return v;
            return 0;
        }
        return x0;
    }

    public void setX0(float x0) {
        this.x0 = x0;
    }

    public float getX1() {
        if (offsetX > 0) {
            return x1 + offsetX;
        }
        return x1;
    }

    public void setX1(float x1) {
        this.x1 = x1;
    }

    public float getY0() {
        if (offsetY > 0) {
            float v = y0 - offsetY;
            if (v > 0)
                return v;
            return 0;
        }
        return y0;
    }

    public void setY0(float y0) {
        this.y0 = y0;
    }

    public float getY1() {
        if (offsetY > 0)
            return y1 + offsetY;
        return y1;
    }

    public void setY1(float y1) {
        this.y1 = y1;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "LineChartLegendBean{" +
                "type=" + type +
                ", color=" + color +
                ", name='" + name + '\'' +
                ", offsetX=" + offsetX +
                ", offsetY=" + offsetY +
                ", x0=" + x0 +
                ", x1=" + x1 +
                ", y0=" + y0 +
                ", y1=" + y1 +
                ", clickable=" + clickable +
                ", checked=" + checked +
                '}';
    }
}
