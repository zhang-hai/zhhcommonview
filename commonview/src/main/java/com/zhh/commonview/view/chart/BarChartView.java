package com.zhh.commonview.view.chart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.zhh.commonview.R;
import com.zhh.commonview.bean.BarChartData;
import com.zhh.commonview.callback.OnCommonChartViewListener;
import com.zhh.commonview.utils.DensityUtil;

import java.math.BigDecimal;
import java.util.List;


/**
 * Created by zhanghai on 2020/5/7.
 * function：柱状图
 */
public class BarChartView extends View {

    private final int DEFAULT_HEIGHT = 200;//默认高度
    private final int DEFAULT_WIDTH = 300;//默认宽度

    @IntDef({LegendIconShape.square, LegendIconShape.circle})
    public @interface LegendIconShape{
        int square = 1;
        int circle = 2;
    }

    private Context mContext;

    private int mPerBarWidth;       //每个柱状图的宽度
    private int mGroupBarSpace = 10;//组中柱状图之间的间距
    private int mYAxisLineColor;    //Y轴横线线条颜色
    private int mXLabelColor;       //X轴标签文字颜色
    private int mXLabelTextSize;    //X轴标签文字大小 px
    private int mYLabelColor;       //Y轴标签文字颜色
    private int mYLabelTextSize;    //Y轴标签文字大小 px
    private int mLegendLabelColor;  //柱状图的图例文字颜色
    private int mLegendLabelTextSize;//柱状图的图例文字大小 px
    private int mLegendIconWidth;    //柱状图的图例icon宽度 px
    private int mLegendIconShape = LegendIconShape.square;    //柱状图的图例icon形状

    private int mLegendOffY = 40;   //柱状图的图例在Y轴上偏移图表尺寸
    private Paint mLinePaint;       //柱状Y轴刻度图背景线条
    private Paint mRectPaint;       //柱状图paint
    private Paint mLabelPaint;      //文本相关的Paint
    private float mDensity;

    private int mYLineNum = 5;      //Y轴分割份数
    private int mYAxisMinNum = 0;   //y轴最小刻度
    private int mYAxisMaxNum = 100; //y轴最大刻度
    private int mYLabelOffset = 10; //文字离Y轴线间距
    private int mPerLegendSpace = 40;//每个图例之间的间距
    private int mLegendIconLabelSpace = 8;//图例中形状与文本之间的间距

    private float mSelectScaleValue = 1.2f;//选择是图标宽度缩放比例

    private boolean isYAxisLabelWithPercent = true;//Y轴刻度是否带百分号
    private boolean isDrawXGridLine = false;        //是否绘制x轴线上y方向的标线

    private float mChartHeight;       //图表高度 （y轴线最大刻度与最小刻度间距）
    private float mChartWidth;        //图表宽度

    private float mX0,mY0;              //绘制的轴线0,0坐标值（相对View的原点而言的，故该值不是0）

    private List<BarChartData> mChartDataList;  //图表数据

    private OnCommonChartViewListener<BarChartData> onChartViewListener;//点击图表回调事件

    public BarChartView(Context context) {
        this(context,null);
    }

    public BarChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BarChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化
     * @param context
     * @param attrs
     */
    private void init(Context context, @Nullable AttributeSet attrs){
        this.mContext = context;
        if(attrs != null){
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BarChartView);
            mPerBarWidth = typedArray.getDimensionPixelOffset(R.styleable.BarChartView_perBarWidth,10);
            mGroupBarSpace = typedArray.getDimensionPixelOffset(R.styleable.BarChartView_groupPerBarSpace,5);
            mYAxisLineColor = typedArray.getColor(R.styleable.BarChartView_yAxisLineColor, Color.parseColor("#E5E5E5"));
            mXLabelColor = typedArray.getColor(R.styleable.BarChartView_xLabelColor, Color.parseColor("#222222"));
            mXLabelTextSize = typedArray.getDimensionPixelSize(R.styleable.BarChartView_xLabelTextSize,22);
            mYLabelColor = typedArray.getColor(R.styleable.BarChartView_yLabelColor, Color.parseColor("#222222"));
            mYLabelTextSize = typedArray.getDimensionPixelSize(R.styleable.BarChartView_yLabelTextSize,22);
            mLegendLabelColor = typedArray.getColor(R.styleable.BarChartView_legendLabelColor, Color.parseColor("#222222"));
            mLegendLabelTextSize = typedArray.getDimensionPixelSize(R.styleable.BarChartView_legendLabelTextSize,22);
            mLegendIconWidth = typedArray.getDimensionPixelSize(R.styleable.BarChartView_legendIconWidth,20);
            mLegendIconShape = typedArray.getInt(R.styleable.BarChartView_legendIconShape, LegendIconShape.square);
            typedArray.recycle();
        }

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(mYAxisLineColor);

        mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectPaint.setStyle(Paint.Style.FILL);

        mLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        //字体大小进行适配
        mDensity = context.getResources().getDisplayMetrics().density;
        mXLabelTextSize = DensityUtil.getPercentWidthSize(getContext(),1920,mXLabelTextSize);//(int) (mXLabelTextSize / mDensity);
        mYLabelTextSize = DensityUtil.getPercentWidthSize(getContext(),1920,mYLabelTextSize);//(int) (mYLabelTextSize / mDensity);
        mLegendLabelTextSize = DensityUtil.getPercentWidthSize(getContext(),1920,mLegendLabelTextSize);//(int) (mLegendLabelTextSize / mDensity);

        //尺寸适配
        mPerBarWidth = DensityUtil.getPercentWidthSize(getContext(),1920,mPerBarWidth);
        mGroupBarSpace = DensityUtil.getPercentWidthSize(getContext(),1920,mGroupBarSpace);
        mLegendIconWidth = DensityUtil.getPercentWidthSize(getContext(),1920,mLegendIconWidth);
        mLegendOffY = DensityUtil.getPercentHeightSize(getContext(),1920,mLegendOffY);
        mYLabelOffset = DensityUtil.getPercentHeightSize(getContext(),1920,mYLabelOffset);
        mPerLegendSpace = DensityUtil.getPercentWidthSize(getContext(),1920,mPerLegendSpace);
        mLegendIconLabelSpace = DensityUtil.getPercentWidthSize(getContext(),1920,mLegendIconLabelSpace);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getSize(DEFAULT_WIDTH + getPaddingLeft() + getPaddingRight(),widthMeasureSpec),
                getSize(DEFAULT_HEIGHT + getPaddingTop() + getPaddingBottom(),heightMeasureSpec));
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

    private Rect mYAxisLabelRect;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x0 = getPaddingLeft();
        int y0 = getMeasuredHeight() - getPaddingBottom();
        canvas.save();
        canvas.translate(x0,y0);
        // 1.绘制Y轴上横线线条
        drawYAxisLine(canvas);

        // 2.绘制X轴方向上柱状图和label
        drawBarAndLabel(canvas);

        //3.绘制图例
        drawLegend(canvas);

        canvas.restore();
    }

    /**
     * 绘制Y轴上横线线条
     * 坐标轴方向如下
     *    负
     *    ┊
     *    ┊    绘制区域
     *    ┊
     * 0,0┊ ┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈→ x
     *    ┊
     *    ↓
     *    y
     * @param canvas
     */
    private void drawYAxisLine(Canvas canvas) {
        int h = getMeasuredHeight();
        Rect rect = getLabelRect("图标",mLegendLabelTextSize);
        Rect xrect = getLabelRect("图标",mXLabelTextSize);
        //图表绘制区域占用的高度 = 总高度 - paddingBottom - paddingTop - 图例占用高度 - 图例与图表的偏移值 - x轴上刻度文字高度 - 文字与x轴的偏移值
        int xLabelH = (xrect.height() + mYLabelOffset);
        mY0 = xLabelH;//保存坐标轴原点距离View原点上的偏移量
        float sy = h - getPaddingBottom() - getPaddingTop() - (rect.height() + mLegendOffY) - xLabelH;
        mChartHeight = sy;
        //检查线条数量，并保证非0
        mYLineNum = mYLineNum <= 0 ? 1 : mYLineNum;
        //每个分割线的间距
        float perY = sy / mYLineNum;
        float perValue = (mYAxisMaxNum - mYAxisMinNum) * 1f / mYLineNum;
        //设置绘制文字颜色和大小
        mLabelPaint.setColor(mYLabelColor);
        mLabelPaint.setTextSize(mYLabelTextSize);
        //计算Y轴文字最大宽度
        String maxText = getRadioShowStr(mYAxisMaxNum,0);
        Rect mr = getLabelRect(maxText,mYLabelTextSize);
        int labelWidth = mr.width();
        mX0 = labelWidth + mYLabelOffset;//保存坐标x轴0点相对View原点的偏移量
        //可绘制的横向最大宽度
        int endX = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        //图表x轴宽度
        mChartWidth = endX - labelWidth - mYLabelOffset;
        //这里要增加一个（i == mYLineNum）的情况，才能把最大的刻度绘制出来
        for (int i = 0;i <= mYLineNum;i++){
            //计算y轴坐标点
            float y = perY * i + xLabelH;
            //开始绘制线
            canvas.drawLine(labelWidth + mYLabelOffset,-y,endX,-y,mLinePaint);
            //获取y轴文字
            String text = getRadioShowStr(perValue * i + mYAxisMinNum,0);
            //获取要绘制的文字的尺寸
            Rect r = getLabelRect(text,mYLabelTextSize);
            //开始绘制Y轴文字
            canvas.drawText(text,labelWidth - r.width(),-(y - r.height()/2f),mLabelPaint);

        }
    }


    /**
     * 绘制X轴方向上柱状图和label
     * @param canvas
     */
    private void drawBarAndLabel(Canvas canvas) {
        if(mChartDataList == null || mChartDataList.size() == 0){
            return;
        }
        int size = mChartDataList.size();
        //每组X轴上占用的宽度，每组的柱状图和文字都会绘制在该宽度区域内
        float perX = mChartWidth / size;
        //设置X轴label的字体颜色和大小
        mLabelPaint.setColor(mXLabelColor);
        mLabelPaint.setTextSize(mXLabelTextSize);

        for (int i = 0;i < size;i++){
            BarChartData data = mChartDataList.get(i);
            //计算文字的尺寸
            Rect xlr = getLabelRect(data.xAxisLabel,mXLabelTextSize);
            //轴点的位置
            float axisX = perX * i + mX0;
            //计算绘制的起始点
            float sx = axisX + (perX - xlr.width()) / 2f;
            //绘制x轴上的label
            canvas.drawText(data.xAxisLabel,sx,0,mLabelPaint);

            //绘制分割线
            if(isDrawXGridLine){
                canvas.drawLine(axisX,-mY0,axisX,-(mY0 + mChartHeight),mLinePaint);
            }

            //绘制柱状图
            //无数据可绘制
            if((data.dataSet == null || data.dataSet.size() == 0) && data.itemBarData == null){
                return;
            }
            //计算临时数据
            float tempPerBarWidth = mSelectPosition == i ? mPerBarWidth * mSelectScaleValue : mPerBarWidth;

            //绘制组模式的柱状图
            if(data.dataSet != null && data.dataSet.size() > 0){
                int groupCount = data.dataSet.size();
                //开始绘制柱状图位置
                float groupX = axisX + (perX - groupCount * tempPerBarWidth - mGroupBarSpace * (groupCount - 1)) / 2f;
                for (int j = 0;j < groupCount;j++){
                    BarChartData.ItemBarChartData item = data.dataSet.get(j);
                    drawOneBarChart(canvas,item,groupX + j * (tempPerBarWidth + mGroupBarSpace),i);
                }
            }else{//绘制单个柱状图
                drawOneBarChart(canvas,data.itemBarData,axisX + (perX - tempPerBarWidth) / 2f,i);
            }
        }
   }

    /**
     * 绘制单个柱状图
     * @param canvas
     * @param item 数据
     * @param startX 绘制的起始位置
     */
   private void drawOneBarChart(Canvas canvas,BarChartData.ItemBarChartData item,float startX,int index){
       //柱状图的高度
       float barH = mChartHeight * item.value / 100;
       //设置画笔颜色
       mRectPaint.setColor(item.color);
       float tempPerBarWidth = mSelectPosition == index ? mPerBarWidth * mSelectScaleValue : mPerBarWidth;
       canvas.drawRect(startX,-(mY0 + barH),startX + tempPerBarWidth,-mY0,mRectPaint);
   }

    /**
     * 绘制图例
     * @param canvas
     */
    private void drawLegend(Canvas canvas) {
        //无数据时，不绘制图例
        if(mChartDataList == null || mChartDataList.size() == 0){
            return;
        }
        //无组时，也不绘制图例
        List<BarChartData.ItemBarChartData> dataSet = mChartDataList.get(0).dataSet;
        if(dataSet == null || dataSet.size() == 0){
            return;
        }
        //起始绘制图例的y轴坐标
        float startY = mY0 + mChartHeight + mLegendOffY;
        //从右向左绘制,先绘制右侧的，省去计算文字宽度
        float endX = mX0 + mChartWidth;

        //先设置图例文字颜色和大小
        mLabelPaint.setColor(mLegendLabelColor);
        mLabelPaint.setTextSize(mLegendLabelTextSize);

        int size = dataSet.size();
        float startX = endX;
        for (int i = size-1;i >= 0;i--){
            BarChartData.ItemBarChartData item = dataSet.get(i);

            Rect r = getLabelRect(item.name,mLegendLabelTextSize);
            startX = endX - r.width();
            //绘制文字
            canvas.drawText(item.name,startX,-startY,mLabelPaint);

            //新的形状绘制位置 startX = 原来的startX - 形状的宽度 - 形状与文字的间距
            startX = startX - mLegendIconWidth - mLegendIconLabelSpace;
            mRectPaint.setColor(item.color);

            //该值用于绘制形状与文字中心线的偏移量，保证能够看起来让形状与文字的中心在同一水平线上
            float shapeOffY = 2f;
            //绘制形状
            if(mLegendIconShape == LegendIconShape.square){
                //形状绘制的y位置
                float shapeY = startY + (r.height() - mLegendIconWidth) / 2f - shapeOffY;
                canvas.drawRect(startX,-(shapeY + mLegendIconWidth),startX+mLegendIconWidth,-shapeY,mRectPaint);
            }else if(mLegendIconShape == LegendIconShape.circle){
                float radius = mLegendIconWidth/2f;
                canvas.drawCircle(startX + radius,-(startY + r.height() / 2f - shapeOffY),radius,mRectPaint);
            }

            //绘制图例的边界
//            mLinePaint.setStyle(Paint.Style.STROKE);
//            mLinePaint.setStrokeWidth(1f);
//            mLinePaint.setColor(Color.BLACK);
//            canvas.drawRect(startX,-(startY + r.height()),endX,-startY,mLinePaint);

            //重新计算新的endX值
            endX = startX - mPerLegendSpace;
        }
    }

    /**
     * 获取文本占用的尺寸
     * @param textSize
     * @return
     */
    private Rect getLabelRect(String text,float textSize){
        Rect rect = new Rect();
        mLabelPaint.setTextSize(textSize);
        mLabelPaint.getTextBounds(text,0,text.length(),rect);
        return rect;
    }

    /**
     * 得到需要展示的得分率
     *
     * @param radio
     * @param num 保留位数
     * @return
     */
    private String getRadioShowStr(double radio,int num) {
        try {
            BigDecimal bd = new BigDecimal(radio > 1 ? radio : radio * 100);
            bd = bd.setScale(num, BigDecimal.ROUND_HALF_UP);
            return isYAxisLabelWithPercent ? bd + "%" : bd.toString();
        } catch (Exception e) {
            return isYAxisLabelWithPercent ? "0%" : "0";
        }
    }

    /**
     * 设置柱状图相关的数据,并进行ui重绘
     * @param chartDataList
     */
    public void setBarChartData(List<BarChartData> chartDataList){
        this.mChartDataList = chartDataList;
        invalidate();
    }

    /**
     * 图表回调接口
     * @param onChartViewListener
     */
    public void setOnChartViewListener(OnCommonChartViewListener<BarChartData> onChartViewListener) {
        this.onChartViewListener = onChartViewListener;
    }

    private int mSelectPosition = -1;

    private float mDownX,mDownY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                int index = checkBounds(mDownX,mDownY);
                mSelectPosition = index;
                if (index != -1){
                    invalidate();
                }
                Log.i("BarChartView","index------> " + index);
                return mSelectPosition != -1;
            case MotionEvent.ACTION_UP:
                if(mSelectPosition != -1){
                    if(onChartViewListener != null){
                        onChartViewListener.onClick(mChartDataList.get(mSelectPosition),mDownX,mDownY);
                    }
                    mSelectPosition = -1;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if(mSelectPosition != -1){
                    mSelectPosition = -1;
                    invalidate();
                }
                break;

        }
        return true;
    }


    /**
     * 检测点击的x,y坐标的位置，是否在柱状图范围内，若是则返回相应的位置,否则返回-1
     * @param x 点击的x坐标
     * @param y 点击的y坐标
     * @return 位置，-1表示x,y不在点击范围内
     */
    private int checkBounds(float x,float y){
        int position = -1;
        if(mChartDataList != null && mChartDataList.size() > 0){
            //将x,y进行坐标转换,转换成以去除边距后的左下角为原点
            float newX,newY;
            newX = x - getPaddingLeft();
            newY = getMeasuredHeight() - y - getPaddingBottom();
            int size = mChartDataList.size();
            //每组X轴上占用的宽度，每组的柱状图和文字都会绘制在该宽度区域内
            float perX = mChartWidth / size;
            int index = 0;
            float startY = mY0;
            for (BarChartData chartData : mChartDataList){
                //无数据
                if((chartData.dataSet == null || chartData.dataSet.size() == 0) && chartData.itemBarData == null){
                    continue;
                }
                float startX = mX0 + perX * index ;
                int count = 0;
                float barH = 0;
                if(chartData.dataSet != null){
                    count = chartData.dataSet.size();
                    for (int i = 0 ;i < count;i++){
                        barH = Math.max(barH,mChartHeight * chartData.dataSet.get(i).value / 100);
                    }
                }else if(chartData.itemBarData != null){
                    count = 1;
                    barH = Math.max(barH,mChartHeight * chartData.itemBarData.value / 100);
                }
                //一组柱状图占用的宽度
                int groupWidth = mPerBarWidth * count + mGroupBarSpace * (count -1);
                startX = startX + (perX - groupWidth) / 2f;
                RectF rect = new RectF(startX,startY,startX + groupWidth,startY + barH);
                if(rect.contains(newX,newY)){
                    mDownY = getMeasuredHeight() - (startY + barH *2 / 3) - getPaddingBottom();
                    position = index;
                    break;
                }
                index++;
            }
        }
        return position;
    }

}
