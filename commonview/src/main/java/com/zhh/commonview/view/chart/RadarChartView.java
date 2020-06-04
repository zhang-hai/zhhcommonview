package com.zhh.commonview.view.chart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.zhh.commonview.R;
import com.zhh.commonview.callback.OnCommonChartViewListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by zhanghai on 2020/4/27.
 * function：雷达图
 */
public class RadarChartView extends View {
    private final int DEFAULT_SELECT_CIRCLE_RADIUS = 4;
    private final int DEFAULT_RADIUS = 50;//默认半径大小
    private Context mContext;
    //雷达半径
    private int mRadius;
    //雷达背景线条颜色
    private int backgroundLineColor;
    //雷达边缘指标文字大小
    private int edgeTextSize;
    //雷达边缘指标文字颜色
    private int edgeTextColor;
    //雷达区域颜色
    private int areaColor;
    //雷达指标折线颜色
    private int lineColor;

    private Paint mBackgroundLinePaint,mEdgeTextPaint,mAreaPaint,mLinePaint;

    //中心点坐标
    private int centerX,centerY;
    //多边形的边数
    private int mEdgeNum;

    private List<RadarItemData> mDataList;

    //点击事件
    private OnCommonChartViewListener<RadarItemData> onRadarChartViewListener;

    //上次点击的指标位置
    private int mCurTouchPosition = -1;

    private int mSelectCircleRadius = DEFAULT_SELECT_CIRCLE_RADIUS;

    public RadarChartView(Context context) {
        this(context,null);
    }

    public RadarChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RadarChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs){
        this.mContext = context;
        if(attrs != null){
            TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.RadarChartView);
            mRadius = typedArray.getDimensionPixelSize(R.styleable.RadarChartView_radius,DEFAULT_RADIUS);
            backgroundLineColor = typedArray.getColor(R.styleable.RadarChartView_backgroundLineColor, Color.GRAY);
            edgeTextSize = typedArray.getDimensionPixelSize(R.styleable.RadarChartView_edgeTextSize,0);
            edgeTextColor = typedArray.getColor(R.styleable.RadarChartView_edgeTextColor,Color.BLACK);
            areaColor = typedArray.getColor(R.styleable.RadarChartView_areaColor,Color.GRAY);
            lineColor = typedArray.getColor(R.styleable.RadarChartView_lineColor,Color.BLACK);
            typedArray.recycle();
        }

        //初始化雷达背景线画笔
        mBackgroundLinePaint = new Paint();
        mBackgroundLinePaint.setColor(backgroundLineColor);
        mBackgroundLinePaint.setAntiAlias(true);
        mBackgroundLinePaint.setStrokeWidth(1.5f);
        mBackgroundLinePaint.setStyle(Paint.Style.STROKE);

        //初始化雷达边缘指标画笔
        mEdgeTextPaint = new Paint();
        mEdgeTextPaint.setColor(edgeTextColor);
        edgeTextSize =  (int) (edgeTextSize / context.getResources().getDisplayMetrics().density);
        mEdgeTextPaint.setTextSize(edgeTextSize);
        mEdgeTextPaint.setAntiAlias(true);

        //初始化雷达区域画笔
        mAreaPaint = new Paint();
        mAreaPaint.setColor(areaColor);
        mAreaPaint.setAntiAlias(true);

        //初始化雷达折线画笔
        mLinePaint = new Paint();
        mLinePaint.setColor(lineColor);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(3);
        mLinePaint.setAntiAlias(true);

    }

    /**
     * 设置雷达点击事件
     * @param onRadarChartViewListener
     */
    public void setOnRadarChartViewListener(OnCommonChartViewListener<RadarItemData> onRadarChartViewListener) {
        this.onRadarChartViewListener = onRadarChartViewListener;
    }

    /**
     * 设置雷达数据
     * @param params
     */
    public void setRadarChartData(List<RadarItemData> params){
        this.mDataList = params;
        this.mCurTouchPosition = -1;
        this.mEdgeNum = params == null ? 0 : params.size();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getSize(mRadius * 2 + getPaddingLeft() + getPaddingRight(),widthMeasureSpec),
                getSize(mRadius * 2 + getPaddingTop() + getPaddingBottom(),heightMeasureSpec));
    }

    /**
     * 重新计算尺寸
     * @param defaultSize
     * @param measureSpec
     * @return
     */
    private int getSize(int defaultSize,int measureSpec){
        int viewSize = defaultSize;
        //测量模式
        int mode = MeasureSpec.getMode(measureSpec);
        //测量尺寸
        int size = MeasureSpec.getSize(measureSpec);
        switch (mode){
            case MeasureSpec.UNSPECIFIED://不约束尺寸
                viewSize = defaultSize;
                break;
            case MeasureSpec.EXACTLY://固定尺寸
                viewSize = size;
                break;
            case MeasureSpec.AT_MOST://取计算出的最大尺寸
                viewSize = Math.min(size,defaultSize);
                break;
        }
        return viewSize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(canDrawRadarChart()){
            centerX = getMeasuredWidth() / 2;
            centerY = getMeasuredHeight() / 2;
            canvas.save();
            //将设置中心点坐标设置为原点，参考点。若不设置是以左上角为原点
            canvas.translate(centerX,centerY);

            //1.绘制雷达网状图
            drawRadarChart(canvas);

            //2.绘制折线及各指标文字
            drawRadarChartLine(canvas);

            //3.绘制被选中的指标，并标记实心圆
            drawSelectIndexState(canvas);

            //恢复
            canvas.restore();

        }

    }

    /**
     * 绘制被选中的指标，并标记实心圆
     * @param canvas
     */
    private void drawSelectIndexState(Canvas canvas) {
        if(mCurTouchPosition != -1){
            //取出数据
            RadarItemData data = mDataList.get(mCurTouchPosition);
            int angle = getRateAngle(mEdgeNum,mCurTouchPosition);
            float[] values = data.value;
            for (int i = 0;i < values.length;i++){
                float x = values[i] * mRadius / 100 * cos(angle);
                float y = values[i] * mRadius / 100 * sin(angle);
                mLinePaint.setColor(data.lineColor[i]);
                canvas.drawCircle(x,y,mSelectCircleRadius,mLinePaint);
            }
        }
    }

    /**
     * 检测是否符合绘制雷达图的指标
     * @return
     */
    private boolean canDrawRadarChart(){
        //无数据直接返回
        if(mDataList == null || mDataList.size() == 0){
            return false;
        }
        return true;
    }

    /**
     * 绘制雷达图
     * @param canvas
     */
    private void drawRadarChart(Canvas canvas){
        mEdgeNum = mDataList.size();
        //绘制雷达蜘蛛图,固定有5个多边形嵌套
        for (int i= 1;i <= 5;i++){
            int r = mRadius / 5 * i;
            drawPolygon(canvas,mEdgeNum,r);
        }
    }

    private int getRateAngle(int totalNum,int i){
        if(totalNum > 0){
            return 360 * i / totalNum;
        }
        return 0;
    }

    /**
     * 绘制多边形,若是最外一个多边形则绘制到中心点线
     * @param canvas
     * @param edgeNum
     * @param radius
     */
    private void drawPolygon(Canvas canvas,int edgeNum,int radius){
        if(edgeNum <= 2){
            return;
        }
        Path path = new Path();
        float x,y;
        for (int i = 0;i < edgeNum;i++){
            int angle = getRateAngle(edgeNum,i);
            x = radius * cos(angle);
            y = radius * sin(angle);
            if(i == 0){
                //移动起点位置
                path.moveTo(x,y);
            }else {
                //绘制到第二个点线
                path.lineTo(x,y);
            }
            //表示绘制的是最外层的多边形，这个时候需要绘制圆心到各顶点的连线
            if(radius == mRadius){
                path.moveTo(0,0);
                path.lineTo(x,y);
                //增加该条件，否则会缺少一条边
                if(i == edgeNum - 1){
                    path.lineTo(radius,0);
                }
//                canvas.drawLine(0,0,x,y,mBackgroundLinePaint);
            }
        }
        path.close();
        canvas.drawPath(path,mBackgroundLinePaint);
    }

    private float left=0,right=0,top=0,bottom = 0;

    /**
     * 绘制文字指标及折线和填充色
     * @param canvas
     */
    private void drawRadarChartLine(Canvas canvas){
        int edgeNum = mDataList.size();
        int index = 0;
        //绘制指标文字
        float x,y;
        for (int i = 0; i < edgeNum;i++){
            int angle = getRateAngle(edgeNum,i);
            RadarItemData item = mDataList.get(i);
            //绘制指标文字
            x = mRadius * cos(angle);
            y = mRadius * sin(angle);
            Rect rect = new Rect();
            mEdgeTextPaint.getTextBounds(item.name,0,item.name.length(),rect);
            int textWidth = rect.right - rect.left;
            int textHeight = rect.bottom - rect.top;
            x = x >= 0 ? x : x-textWidth;
            y = y >= 0 ? (y + (y == 0 ? textHeight/2f : textHeight)) : y - textHeight/2f;
            if(x >= 0){
                right = Math.max(right,x);
            }else {
                left = Math.min(left,x);
            }
            if(y >= 0) bottom = Math.max(bottom,y);
            else top = Math.min(top,y);

            //绘制文字
            canvas.drawText(item.name,x,y,mEdgeTextPaint);
        }

        //设置Path
        List<Path> paths = new ArrayList<>();
        for (int i = 0; i < edgeNum;i++){
            int angle = getRateAngle(edgeNum,i);
            RadarItemData item = mDataList.get(i);
            //设置折线路径
            float[] values = item.value;
            for (int j = 0;j < values.length;j++){
                x = values[j] * mRadius / 100 * cos(angle);
                y = values[j] * mRadius / 100 * sin(angle);
                Path path = paths.size() == 0 || paths.size() <= j ? null : paths.get(j);
                if(path == null){
                    path = new Path();
                    paths.add(path);
                }
                if(i == 0){
                    path.moveTo(x,y);
                }else {
                    path.lineTo(x,y);
                }
            }
        }
        //绘制path
        if(paths.size() > 0){
            RadarItemData itemData = mDataList.get(0);
            int i = 0;
            for (Path path : paths){
                path.close();
                mAreaPaint.setColor(itemData.fillColor.length <= i ? Color.parseColor("#40000000") : itemData.fillColor[i]);
                canvas.drawPath(path,mAreaPaint);
                mLinePaint.setColor(itemData.lineColor.length <= i ? Color.BLACK : itemData.lineColor[i]);
                canvas.drawPath(path,mLinePaint);
                i++;
            }
        }
    }

    /**
     * 计算角度的sin值
     * @param num 角度
     * @return
     */
    private float sin(int num){
        return (float) Math.sin(num * Math.PI / 180);
    }

    /**
     * 计算角度的sin值
     * @param num 角度
     * @return
     */
    private float cos(int num){
        return (float) Math.cos(num * Math.PI / 180);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_UP:
                float x = event.getX();
                float y = event.getY();
                showTouchRadarInfo(x,y);
                return true;
        }
        return true;
    }

    /**
     * 根据点击的x,y位置，查找点击的位置
     * @param x 点击的x轴坐标
     * @param y 点击的y轴坐标
     */
    private void showTouchRadarInfo(float x,float y){
        //若未注册事件，不相应点击事件
        if (onRadarChartViewListener == null) return;
        float offx = x - centerX;
        float offy = y - centerY;
        float angle;
        if(offx == 0 && offy == 0 ){
            return;
        }

        //判断点击位置，超过半径了，则表示取消点击
        if(Math.sqrt(offx * offx + offy * offy) > mRadius){
            //若上次有点击的位置，则取消，并更新UI
            if(mCurTouchPosition != -1){
                mCurTouchPosition = -1;
                invalidate();
            }
            //回调
            if(onRadarChartViewListener != null){
                onRadarChartViewListener.onCancel();
            }
            return;
        }

        if(offx == 0){
            angle = offy > 0 ? 90f : 270f;
        }else {
            angle = (float) (Math.atan(offy/offx) * 180 / Math.PI);
            if(offx < 0){
                angle += 180;
            }else if(offx > 0 && offy < 0){
                angle += 360;
            }
        }

        Log.i("RadarChartView",String.format(Locale.CHINESE,"offx--->%f,offy-->%f,angle----->%f",offx,offy,angle));
        int perAngle = 360 / mEdgeNum;
        int halfPerAngle = perAngle / 2;
        int position = -1;
        for (int i = 0;i < mEdgeNum;i++){
            if(i == 0 && angle >= (360 - halfPerAngle)){
                position = i;
                break;
            }else if(angle < perAngle * i + halfPerAngle && angle >= perAngle*i - halfPerAngle){
                position = i;
                break;
            }
        }
        //找到点击的位置了,并且不能是上次点击的位置，防止同一个位置重复绘制
        if(position != -1 && mCurTouchPosition != position){
            //有设置回调，才计算
            if(onRadarChartViewListener != null){
                RadarItemData data = mDataList.get(position);
                float[] values = data.value;
                float cx = 0f,cy = 0f;
                for (int i = 0;i < values.length;i++){
                    cx += values[i] * mRadius / 100 * cos(perAngle * position);
                    cy += values[i] * mRadius / 100 * sin(perAngle * position);
                }

                //回调事件，并对坐标进行转换，将原来以View为坐标原点转换为以view的左上角为坐标原点
                onRadarChartViewListener.onClick(data,cx/2 + centerX,cy/2 + centerY);

                //记录当前点击的指标位置，并重绘UI
                mCurTouchPosition = position;
                invalidate();

                Log.i("RadarChartView",String.format(Locale.CHINESE,"科目--->%s,数据-->%s",data.name, Arrays.toString(data.value)));
            }
        }
    }


    /**
     * 单个数据指标
     */
    public final static class RadarItemData{
        public String name;     //指标名称
        public float[] value;   //指标值
        public int[] lineColor; //折线颜色
        public int[] fillColor;//填充色

        public RadarItemData(String name,float[] value,int[] lineColor,int[] fillColor){
            this.name = name;
            this.lineColor = lineColor;
            this.fillColor = fillColor;
            this.value = value;
        }

        @Override
        public String toString() {
            return String.format(Locale.CHINESE,"{name:%s,value:%s,lineColor:%s,fillColor:%s}",
                    name,Arrays.toString(value),Arrays.toString(lineColor),Arrays.toString(fillColor));
        }
    }

}
