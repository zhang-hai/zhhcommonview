package com.zhh.commonview.demo;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.TextView;

import com.zhh.commonview.bean.BarChartData;
import com.zhh.commonview.bean.chart.LineChartDataBean;
import com.zhh.commonview.bean.chart.LineChartFullBean;
import com.zhh.commonview.bean.chart.LineChartLegendBean;
import com.zhh.commonview.bean.chart.LineChartPointBean;
import com.zhh.commonview.bean.chart.LineChartRowBean;
import com.zhh.commonview.callback.OnCommonChartViewListener;
import com.zhh.commonview.callback.OnLineChartViewListener;
import com.zhh.commonview.view.chart.BarChartView;
import com.zhh.commonview.view.chart.LineChartView;
import com.zhh.commonview.view.chart.RadarChartView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by zhanghai on 2020/6/4.
 * function：
 */
public class ChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_test);


        initRadarChartView();

        initBarChartView();

        initLineChartView();

    }


    /**
     * 初始化雷达图
     */
    private void initRadarChartView(){
        RadarChartView chartView = findViewById(R.id.rcv_radar);
        chartView.setRadarChartData(addTestData());
        chartView.setOnRadarChartViewListener(new OnCommonChartViewListener<RadarChartView.RadarItemData>() {
            @Override
            public void onCancel() {
                //取消事件，如果展示数据的UI，这里可以关闭

            }

            @Override
            public void onClick(RadarChartView.RadarItemData data, float ix, float iy) {
                //再这里可以展示数据
                Log.i("MainActivity",String.format(Locale.CHINESE,"data--->%s,ix-->%f,iy---->%f",data.toString(), ix,iy));
            }
        });
    }

    /**
     * 初始化柱状图
     */
    private void initBarChartView(){
        BarChartView chart = findViewById(R.id.barChartView);
        chart.setBarChartData(buildBarChartData());

        chart.setOnChartViewListener(new OnCommonChartViewListener<BarChartData>() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onClick(BarChartData data, float ix, float iy) {
                Log.i("MainActivity",String.format(Locale.CHINESE,"data--->%s,ix-->%f,iy---->%f",data.toString(), ix,iy));
            }
        });
    }


    /**
     * 初始化折线图
     */
    private void initLineChartView(){
        final TextView tv_line_date = findViewById(R.id.tv_line_date);
        LineChartView chart = findViewById(R.id.lineChartView);
        LineChartFullBean bean = buildTaskTrend(LineChartFullBean.MODE_MONTH);
        if(bean != null){
            chart.notifyChart(bean);
        }
        //设置曲线模式
        chart.setCurveMode(true);
        chart.setOnLineChartListener(new OnLineChartViewListener<List<LineChartDataBean>>() {

            @Override
            public void onCancel() {}

            @Override
            public void onMove(String value) {
                tv_line_date.setText(value);
            }

            @Override
            public void onClick(List<LineChartDataBean> data, float ix, float iy) {
                //显示选中的详细数据
            }
        });
    }






    /**
     * 构建雷达图数据
     * @return
     */
    private List<RadarChartView.RadarItemData> addTestData(){
        int[] colors = {getResources().getColor(R.color.color_01AC8E),getResources().getColor(R.color.color_FF4081)};
        int[] fillColors = {getResources().getColor(R.color.color_fill_01AC8E),getResources().getColor(R.color.color_fill_FF4081)};

        List<RadarChartView.RadarItemData> mDataList = new ArrayList<>();
        mDataList.add(new RadarChartView.RadarItemData("数学", new float[]{88f,30f}, colors,fillColors));
        mDataList.add(new RadarChartView.RadarItemData("语文", new float[]{50f,40f}, colors,fillColors));
        mDataList.add(new RadarChartView.RadarItemData("英语", new float[]{88f,90f}, colors,fillColors));
        mDataList.add(new RadarChartView.RadarItemData("地理", new float[]{98f,93f}, colors,fillColors));
        mDataList.add(new RadarChartView.RadarItemData("生物", new float[]{88f,27f}, colors,fillColors));
        mDataList.add(new RadarChartView.RadarItemData("化学", new float[]{70f,70f}, colors,fillColors));
        mDataList.add(new RadarChartView.RadarItemData("物理", new float[]{80f,60f}, colors,fillColors));
        mDataList.add(new RadarChartView.RadarItemData("历史", new float[]{88f,80f}, colors,fillColors));
        mDataList.add(new RadarChartView.RadarItemData("政治", new float[]{100f,90f}, colors,fillColors));

        return mDataList;
    }



    /**
     * 构建柱状图所需要的数据
     * @return
     */
    public List<BarChartData> buildBarChartData() {
        List<BarChartData> chartDataList = new ArrayList<>();
        String[] subjects = {"数学","语文","英语","地理","生物","化学","物理","历史","政治","体育"};

        int i = 0;
        for(String s : subjects){
            BarChartData barData = new BarChartData(s);
            List<BarChartData.ItemBarChartData> groups = new ArrayList<>();
            groups.add(new BarChartData.ItemBarChartData("课前导学", Color.parseColor("#01AC8E"),42.6f));
            groups.add(new BarChartData.ItemBarChartData("课堂测验", Color.parseColor("#EB7E65"),92.6f));
            groups.add(new BarChartData.ItemBarChartData("课后作业", Color.parseColor("#5B8FF9"),78.6f));
            barData.dataSet = groups;

            chartDataList.add(barData);

            i++;
        }
        return chartDataList;
    }





    //--------------------------------折线图数据----------------------------

    private LineChartFullBean buildTaskTrend(int mode){
        LineChartFullBean chartBean = null;
        mockLegendList();
        mockRowPercentList();
        if(mode == LineChartFullBean.MODE_WEEK){
            chartBean = mockWeekData();
        }else if(mode == LineChartFullBean.MODE_MONTH){
            chartBean = mockMonthData();
        }else if(mode == LineChartFullBean.MODE_TERM){
            chartBean = mockTermData();
        }

        return chartBean;
    }

    private static final String[] CHART_NAMES = {
            "课前导学",
            "课堂测验",
            "课后作业",
    };
    private static final int LENGTH = CHART_NAMES.length;

    private static final int[] COLORS = {
            Color.parseColor("#01AC8E"),
            Color.parseColor("#EB7E65"),
            Color.parseColor("#5B8FF9"),
            Color.parseColor("#979797"),
    };

    private boolean withClassAvg = true;

    private List<LineChartLegendBean> mLegendList = new ArrayList<>();
    private List<LineChartRowBean> mRowPercentList = new ArrayList<>();

    private LineChartFullBean mockWeekData() {
        LineChartFullBean mWeekFullBean = new LineChartFullBean(LineChartFullBean.MODE_WEEK, mLegendList, mRowPercentList);
        for (int i = 0; i < LENGTH; i++) {
            // mLastVal = -1;
            // 某一数据对应的折线所以的点线条
            LineChartDataBean dataBean = new LineChartDataBean(LineChartDataBean.TYPE_LINE, COLORS[i]);
            dataBean.setName(CHART_NAMES[i]);
            // 点位列表
            List<LineChartPointBean> pointList = new ArrayList<>();
            for (int j = 1; j <= 7; j++) {
                pointList.add(mockLineChartPointBean("05-0" + j, 30));
            }
            // 绑定点位列表
            dataBean.setPointList(pointList);
            // 绑定整个数据
            mWeekFullBean.getColumnList().add(dataBean);
            if (withClassAvg) {
                // 某一数据对应的折线所以的点线条
                LineChartDataBean dashBean = new LineChartDataBean(LineChartDataBean.TYPE_DASH_LINE, COLORS[i]);
                dashBean.setName(CHART_NAMES[i]);
                // 点位列表
                List<LineChartPointBean> dashPointList = new ArrayList<>();
                for (int j = 1; j <= 7; j++) {
                    dashPointList.add(mockLineChartPointBean("05-0" + j, 30));
                }
                // 绑定点位列表
                dashBean.setPointList(dashPointList);
                // 绑定整个数据
                mWeekFullBean.getColumnList().add(dashBean);
            }
        }

        return mWeekFullBean;
    }

    private Random mRandom = new Random();
    private int mLastVal = -1;

    private LineChartPointBean mockLineChartPointBean(String name, int accuracy) {
        int val;
        if (mLastVal == -1) {
            val = mRandom.nextInt(10) + 60;// 起点
        } else {
            val = Math.abs(mLastVal + (mRandom.nextInt(accuracy * 2) - accuracy));
            if (val > 100) {
                val = 100 - val % 100;
            }
        }
        mLastVal = val;
        return new LineChartPointBean(name, val);
    }

    private LineChartFullBean mockMonthData() {
        LineChartFullBean mMonthFullBean = new LineChartFullBean(LineChartFullBean.MODE_MONTH, mLegendList, mRowPercentList);
        for (int i = 0; i < LENGTH; i++) {
            mLastVal = -1;
            // 某一数据对应的折线所以的点线条
            LineChartDataBean dataBean = new LineChartDataBean(LineChartDataBean.TYPE_LINE, COLORS[i]);
            dataBean.setName(CHART_NAMES[i]);
            // 点位列表
            List<LineChartPointBean> pointList = new ArrayList<>();
            for (int j = 1; j <= 31; j++) {
                String ds = j > 9 ? "" + j : "0" + j;
                pointList.add(mockLineChartPointBean("05-" + ds, 10));
            }
            // 绑定点位列表
            dataBean.setPointList(pointList);
            // 绑定整个数据
            mMonthFullBean.getColumnList().add(dataBean);
            if (withClassAvg) {
                // 某一数据对应的折线所以的点线条
                LineChartDataBean dashBean = new LineChartDataBean(LineChartDataBean.TYPE_DASH_LINE, COLORS[i]);
                dashBean.setName(CHART_NAMES[i]);
                // 点位列表
                List<LineChartPointBean> dashPointList = new ArrayList<>();
                for (int j = 1; j <= 31; j++) {
                    String ds = j > 9 ? "" + j : "0" + j;
                    dashPointList.add(mockLineChartPointBean("05-" + ds, 10));
                }
                // 绑定点位列表
                dashBean.setPointList(dashPointList);
                // 绑定整个数据
                mMonthFullBean.getColumnList().add(dashBean);
            }
        }

        return mMonthFullBean;
    }

    private LineChartFullBean mockTermData() {
        int termMonthCount = mRandom.nextInt(4) + 1;
        int termDayCount = termMonthCount * 30 + 8;

        LineChartFullBean mTermFullBean = new LineChartFullBean(LineChartFullBean.MODE_TERM, mLegendList, mRowPercentList);
        for (int i = 0; i < LENGTH; i++) {
            mLastVal = -1;
//        for (int i = 0; i < 1; i++) {
            // 某一数据对应的折线所以的点线条
            LineChartDataBean dataBean = new LineChartDataBean(LineChartDataBean.TYPE_LINE, COLORS[i]);
            dataBean.setName(CHART_NAMES[i]);
            // 点位列表
            List<LineChartPointBean> pointList = new ArrayList<>();
            for (int j = 1; j <= termDayCount; j++) {
                int m = 1 + j / 30;
                int d = j % 30;
                String ms = "0" + m;
                String ds = d > 9 ? "" + d : "0" + d;
                pointList.add(mockLineChartPointBean(ms + "-" + ds, 4));
            }
            // 绑定点位列表
            dataBean.setPointList(pointList);
            // 绑定整个数据
            mTermFullBean.getColumnList().add(dataBean);
            if (withClassAvg) {
                // 某一数据对应的折线所以的点线条
                LineChartDataBean dashBean = new LineChartDataBean(LineChartDataBean.TYPE_DASH_LINE, COLORS[i]);
                dashBean.setName(CHART_NAMES[i]);
                // 点位列表
                List<LineChartPointBean> dashPointList = new ArrayList<>();
                for (int j = 1; j <= termDayCount; j++) {
                    int m = 1 + j / 30;
                    int d = j % 30;
                    String ms = "0" + m;
                    String ds = d > 9 ? "" + d : "0" + d;
                    dashPointList.add(mockLineChartPointBean(ms + "-" + ds, 4));
                }
                // 绑定点位列表
                dashBean.setPointList(dashPointList);
                // 绑定整个数据
                mTermFullBean.getColumnList().add(dashBean);
            }
        }

        return mTermFullBean;
    }

    private void mockLegendList() {
        if(mLegendList == null || mLegendList.size() == 0){
            mLegendList.add(new LineChartLegendBean(LineChartLegendBean.TYPE_CIRCLE, COLORS[0], "课前导学"));
            mLegendList.add(new LineChartLegendBean(LineChartLegendBean.TYPE_CIRCLE, COLORS[1], "课堂测验"));
            mLegendList.add(new LineChartLegendBean(LineChartLegendBean.TYPE_CIRCLE, COLORS[2], "课后作业"));
        }
    }

    private void mockRowPercentList() {
        if(mRowPercentList == null || mRowPercentList.size() == 0){
            mRowPercentList.add(new LineChartRowBean(100, "%"));
            mRowPercentList.add(new LineChartRowBean(80, "%"));
            mRowPercentList.add(new LineChartRowBean(60, "%"));
            mRowPercentList.add(new LineChartRowBean(40, "%"));
            mRowPercentList.add(new LineChartRowBean(20, "%"));
            mRowPercentList.add(new LineChartRowBean(0));
        }
    }

}
