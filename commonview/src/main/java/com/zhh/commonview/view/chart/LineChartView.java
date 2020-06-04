package com.zhh.commonview.view.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.zhh.commonview.bean.chart.LineChartDataBean;
import com.zhh.commonview.bean.chart.LineChartFullBean;
import com.zhh.commonview.bean.chart.LineChartLegendBean;
import com.zhh.commonview.bean.chart.LineChartPointBean;
import com.zhh.commonview.bean.chart.LineChartRowBean;
import com.zhh.commonview.callback.OnLineChartViewListener;
import com.zhh.commonview.utils.DensityUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LineChartView extends View {
    private static final String TAG = "LineChartView/D";

    private LineChartFullBean mLineChartFullBean;
    private int mWidth, mHeight, mRowHeight;
    private float mChartHeight, mY0;

    private Map<Integer, Float> mPointMap;
    private boolean needDrawVerticalLine = false;
    private boolean withDashLine = false;
    private int mTargetIndex = -1;
    private boolean curveMode;// 曲线模式
    private boolean needDrawKeyPoint;

    public boolean isWithDashLine() {
        return withDashLine;
    }

    private TextPaint mTxtPaint;
    private int TEXT_SIZE;
    private int LEGEND_BALL_SIZE;
    private int DASH_LINE_SIZE;
    private int LEGEND_LINE_WIDTH;
    private int ROW_HEIGHT;
    private int MAX_COLUMN = 7;

    private Paint mLegendPaint;
    private Paint mDashLinePaint;
    private Path mDashPath;
    private boolean isMoving = false;

    private OnLineChartViewListener onLineChartListener;

    public void setOnLineChartListener(OnLineChartViewListener onLineChartListener) {
        this.onLineChartListener = onLineChartListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        if (mHeight < 200)
            throw new RuntimeException("The height is too low,current is " + mHeight + " min is 200");
        mRowHeight = (mHeight - autoSize(148)) / 5;
        Log.d(TAG, "mWidth = " + mWidth);
        Log.d(TAG, "mHeight = " + mHeight);
        Log.d(TAG, "mRowHeight = " + mRowHeight);
    }

    public void notifyChart(boolean withDashLine) {
        this.withDashLine = withDashLine;
        invalidate();
    }

    public void notifyChart(LineChartFullBean data) {
        if (mPointMap != null)
            mPointMap.clear();
        if (chartPopupBeanList != null)
            chartPopupBeanList.clear();
        needDrawVerticalLine = false;
        mTargetIndex = -1;
        this.mLineChartFullBean = data;
        invalidate();
    }

    public LineChartView(Context context) {
        this(context, null);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        TEXT_SIZE = autoSize(26);
        LEGEND_BALL_SIZE = autoSize(8);
        DASH_LINE_SIZE = autoSize(3);
        LEGEND_LINE_WIDTH = DASH_LINE_SIZE * 5;
        ROW_HEIGHT = autoSize(74);

        mPointMap = new HashMap<>();
        chartPopupBeanList = new ArrayList<>();
        // 文字画笔
        mTxtPaint = new TextPaint();
        mTxtPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTxtPaint.setAntiAlias(true);
        mTxtPaint.setTextSize(TEXT_SIZE);
        mTxtPaint.setColor(Color.parseColor("#555555"));

        // 图例画笔
        mLegendPaint = new Paint();
        mLegendPaint.setAntiAlias(true);
        mLegendPaint.setStyle(Paint.Style.FILL);// 实心
        mLegendPaint.setStrokeWidth(DASH_LINE_SIZE);

        // 绘制虚线的画笔
        mDashLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDashLinePaint.setColor(Color.BLACK);
        mDashLinePaint.setStyle(Paint.Style.STROKE);
        mDashLinePaint.setStrokeWidth(DASH_LINE_SIZE);
        mDashLinePaint.setPathEffect(new DashPathEffect(new float[]{DASH_LINE_SIZE, DASH_LINE_SIZE}, 0));
        mDashPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mLineChartFullBean == null) {
            drawLoadingText(canvas);
            return;
        }
        if (mPointMap != null)
            mPointMap.clear();
        if (chartPopupBeanList != null)
            chartPopupBeanList.clear();
        lastName = "";
        popupBean = null;
        // drawLegend(canvas, mLineChartFullBean.getLegendList());
        drawChart(canvas, mLineChartFullBean);
        if (needDrawVerticalLine && mTargetIndex > -1)
            drawVerticalLine(canvas);
    }

    private void drawVerticalLine(Canvas canvas) {
        if (mPointMap == null) return;
        Float aFloat = mPointMap.get(mTargetIndex);
        mLegendPaint.setStrokeWidth(1);
        mLegendPaint.setColor(Color.parseColor("#A6A6A6"));
        canvas.drawLine(aFloat, mY0, aFloat, mY0 + mChartHeight, mLegendPaint);
    }

    private List<LineChartDataBean> chartPopupBeanList;
    private LineChartDataBean popupBean = null;
    private String lastName = "";

    /**
     * 绘制Chart
     */
    private void drawChart(Canvas canvas, LineChartFullBean fullBean) {
        // 解析行文字
        List<LineChartRowBean> rowValues = fullBean.getRowValues();
        if (rowValues == null || rowValues.size() == 0) return;
        mLegendPaint.setStrokeWidth(1);
        mLegendPaint.setColor(Color.parseColor("#E5E5E5"));

        float x0 = autoSize(80);// marginLeft = 80px
        float x1 = mWidth - autoSize(10);
        mY0 = autoSize(112);// marginTop = 112px
        float y = mY0;
        for (LineChartRowBean item : rowValues) {
            canvas.drawLine(x0, y, x1, y, mLegendPaint);

            // 画文字
            String name = item.getShowText();
            float offset = mTxtPaint.measureText(name);
            mTxtPaint.setColor(Color.parseColor("#555555"));
            canvas.drawText(name, x0 - offset - autoSize(12), y + TEXT_SIZE / 2.4f, mTxtPaint);// 这里有个marginLeft=12px

            // y += ROW_HEIGHT;
            y += mRowHeight;
        }
        // 解析列文字
        List<LineChartDataBean> columnList = fullBean.getColumnList();
        if (columnList == null || columnList.size() == 0) return;

        // y -= ROW_HEIGHT;
        y -= mRowHeight;
        mChartHeight = y - mY0;
        // JLogUtil.d(TAG, "mChartHeight = " + mChartHeight);
        boolean needDrawColumnName = true;
        for (LineChartDataBean chartDataBean : columnList) {
            Log.i(TAG, String.format("名称:%s --> 类型:%s", chartDataBean.getName().hashCode(), chartDataBean.getType()));
            List<LineChartPointBean> pointList = chartDataBean.getPointList();
            if (pointList == null || pointList.size() == 0) continue;
            List<LineChartLegendBean> legendList = fullBean.getLegendList();
            boolean isChecked = false;
            if (legendList != null && legendList.size() > 0) {
                for (LineChartLegendBean legendBean : legendList) {
                    if (chartDataBean.getName().hashCode() == legendBean.getName().hashCode()) {
                        isChecked = legendBean.isChecked();
                        break;
                    }
                }
            }
            if (!isChecked) {
                continue;
            }

            int pointCount = pointList.size();
            int skipSize = 0;
            if (fullBean.getMode() == LineChartFullBean.MODE_MONTH) {// 月度
                skipSize = 3;
            } else if (fullBean.getMode() == LineChartFullBean.MODE_TERM) {// 本学期
                skipSize = pointCount / 10;
                // skipSize = 30;
            }
            Path tempPath = new Path();

            int c = 0;
            int offset = autoSize(10);
            float eachWidth = (x1 - x0) / (pointCount - 1);

            int rowType = chartDataBean.getType();
            for (LineChartPointBean point : pointList) {
                if (needDrawVerticalLine && mTargetIndex == c) {
                    if (isMoving) {
                        if(this.onLineChartListener != null){
                            this.onLineChartListener.onMove(point.getName());
                        }
//                        EventBus.getDefault().post(new LineChartMoveEvent(point.getName()));
                    } else {
                        if (rowType == LineChartDataBean.TYPE_DASH_LINE && !withDashLine) continue;
                        String rowName = chartDataBean.getName();
                        point.setType(rowType);
                        Log.d(TAG, String.format("[%s - %s]point-->%s", rowName, rowType, point.toString()));
                        if (rowName.equals(lastName) && popupBean != null) {
                            popupBean.getPointList().add(point);
                        } else {
                            lastName = rowName;
                            popupBean = new LineChartDataBean(rowType, chartDataBean.getColor(), rowName);
                            popupBean.setPointList(new ArrayList<LineChartPointBean>());
                            popupBean.getPointList().add(point);
                            chartPopupBeanList.add(popupBean);
                        }
                    }
                }

                float px = x0 + eachWidth * c;
                // 因为是从上往下画，所以value越大，应该越靠上，100%在最上面  0在最下面
                float py = mY0 + transValueY(point.getValue());
                if (c == 0)
                    tempPath.moveTo(px, py);
                if (curveMode) {
                    if (c < pointList.size() - 1) {
                        // 绘制三阶贝塞尔曲线
                        LineChartPointBean nextPoint = pointList.get(c + 1);
                        float endX = px + eachWidth;
                        float endY = mY0 + transValueY(nextPoint.getValue());
                        float wt = (px + endX) / 2;
                        PointF p3 = new PointF();
                        p3.y = py;
                        p3.x = wt;
                        PointF p4 = new PointF();
                        p4.y = endY;
                        p4.x = wt;
                        tempPath.cubicTo(p3.x, p3.y, p4.x, p4.y, endX, endY);
                    }
                } else if (c > 0) {
                    tempPath.lineTo(px, py);
                }
                boolean isKeyPoint = skipSize == 0 || c % skipSize == 0;
                // 画一个点
                if (rowType == LineChartDataBean.TYPE_LINE) {
//                    if (isKeyPoint) {
                    if (needDrawKeyPoint || needDrawVerticalLine && mTargetIndex == c) {
                        mLegendPaint.setColor(chartDataBean.getColor());
                        mLegendPaint.setStyle(Paint.Style.FILL);
                        canvas.drawCircle(px, py, LEGEND_BALL_SIZE / 1.2f, mLegendPaint);
                    }
//                    }
                }

                if (needDrawColumnName) {
                    // 画刻度
                    if (isKeyPoint) {
                        mLegendPaint.setStrokeWidth(1);
                        mLegendPaint.setColor(Color.parseColor("#E5E5E5"));
                        canvas.drawLine(px, y, px, y + offset, mLegendPaint);
                    }
                    mPointMap.put(c, px);

                    // 画日期
                    if (isKeyPoint) {
                        String name = point.getName();
                        float nameWidth = mTxtPaint.measureText(name);
                        if (c == 0) {
                            canvas.drawText(name, px, y + TEXT_SIZE + offset, mTxtPaint);
                        } else if (c == pointCount - 1 || px + nameWidth / 2 > mWidth) {
                            canvas.drawText(name, px - nameWidth, y + TEXT_SIZE + offset, mTxtPaint);
                        } else {
                            canvas.drawText(name, px - nameWidth / 2, y + TEXT_SIZE + offset, mTxtPaint);
                        }
                    }
                }
                c++;
            }
            if (rowType == LineChartDataBean.TYPE_LINE) {
                mLegendPaint.setColor(chartDataBean.getColor());
                mLegendPaint.setStrokeWidth(DASH_LINE_SIZE);
                mLegendPaint.setStyle(Paint.Style.STROKE);
                canvas.drawPath(tempPath, mLegendPaint);
            } else {
                // 是否绘制虚线
                if (withDashLine) {
                    mDashLinePaint.setColor(chartDataBean.getColor());
                    mDashLinePaint.setStrokeWidth(autoSize(2));
                    mDashLinePaint.setPathEffect(new DashPathEffect(new float[]{DASH_LINE_SIZE, DASH_LINE_SIZE * 2}, 0));
                    mDashLinePaint.setAlpha((int) (255 * 0.6));
                    canvas.drawPath(tempPath, mDashLinePaint);
                }
            }
            needDrawColumnName = false;
        }
        if (chartPopupBeanList != null && chartPopupBeanList.size() > 0) {
            if(this.onLineChartListener != null){
                this.onLineChartListener.onClick(chartPopupBeanList,mUpX,mUpY);
            }
//            EventBus.getDefault().post(new LineChartPopupEvent(mUpX, mUpY, chartPopupBeanList));
        }
        Log.d(TAG, "chartPopupBeanList = " + Arrays.toString(chartPopupBeanList.toArray(new LineChartDataBean[0])));
        Log.d(TAG, "mPointMap = " + mPointMap.toString());
    }

    private void drawLoadingText(Canvas canvas) {
        String txt = "数据加载中...";
        float measureTextWidth = mTxtPaint.measureText(txt);
        Log.d(TAG, "measureTextWidth = " + measureTextWidth);
        float x = (mWidth - measureTextWidth) / 2f;
        float y = (mHeight - TEXT_SIZE) / 2f;

        canvas.drawText(txt, x, y, mTxtPaint);
    }

    private float transValueY(int val) {
        return mChartHeight * (100 - val) / 100f;
    }

    float mDownX, mDownY;
    float mUpX, mUpY;
    long mLastDownTime;

    //是否进行拦截了,若拦截了就不再进行检查
    // 值分别为：0 初始值 1.需要拦截事件，2.不拦截事件
    int disallowInterceptState = 0;
    float slideX,slideY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        isMoving = true;
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "ACTION_DOWN called with x = " + x + "  y = " + y);
                mLastDownTime = System.currentTimeMillis();
                mDownX = event.getX();
                mDownY = event.getY();
                slideX = mDownX;
                slideY = mDownY;
                disallowInterceptState = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "ACTION_MOVE called with x = " + x + "  y = " + y);
                if(disallowInterceptState == 0){
                     float absX = Math.abs(x - slideX);
                     float absY = Math.abs(y - slideY);
                     if((absX > 5f || absY > 5f)){
                         if (absX > absY){
                             disallowInterceptState = 1;
                         }else {
                             disallowInterceptState = 2;
                         }
                     }
                 }
                if(getParent() != null && disallowInterceptState != 0){
                    //y坐标边界检测
                    boolean bl = y < 0 || y > getMeasuredHeight();
                    disallowInterceptState = bl ? 2 : disallowInterceptState;
                    getParent().requestDisallowInterceptTouchEvent(disallowInterceptState == 1);
                    //将日期设置为空
                    if(this.onLineChartListener != null && disallowInterceptState == 2){
                        this.onLineChartListener.onMove("");
                        //重绘，去除line线
                        mTargetIndex = -1;
                        isMoving = false;
                        invalidate();
                    }else {
                        mDownX = event.getX();
                        mDownY = event.getY();
                        checkTouchEvent();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mUpX = event.getRawX();
                mUpY = event.getRawY();
                mTargetIndex = -1;
                isMoving = false;
                if(this.onLineChartListener != null){
                    this.onLineChartListener.onMove("");
                }
//                EventBus.getDefault().post(new LineChartMoveEvent(""));
                Log.d(TAG, "ACTION_UP called with x = " + x + "  y = " + y);
                Log.d(TAG, "ACTION_UP called with mUpX = " + mUpX + "  mUpY = " + mUpY);
//                if (!onLegendClick(event)) {
                checkTouchEvent();
//                }
                break;
        }
        return true;
        // return super.onTouchEvent(event);
    }

    private boolean onLegendClick(MotionEvent event) {
        List<LineChartLegendBean> legendList = mLineChartFullBean.getLegendList();
        if (legendList != null && legendList.size() > 0) {
            float x = event.getX();
            float y = event.getY();
            for (LineChartLegendBean item : legendList) {
                if (item.isClickable()
                        && x >= item.getX0()
                        && x <= item.getX1()
                        && y > item.getY0()
                        && y <= item.getY1()) {
                    Log.i(TAG, "onLegendClick --> " + item.toString());
                    item.setChecked(!item.isChecked());
                    invalidate();
                    return true;
                }
            }
        }
        return false;
    }

    private void checkTouchEvent() {
        if (mPointMap != null && mDownY > mY0 && mDownY < mY0 + mChartHeight) {
            int accuracy = autoSize(15);
            for (int key : mPointMap.keySet()) {
                Float xVal = mPointMap.get(key);
                mUpX = xVal;// 修正X的左边 使其落在坐标轴上
                float min = xVal - accuracy;
                float max = xVal + accuracy;
                // JLogUtil.d(TAG, key + " --> " + xVal);
//                JLogUtil.d(TAG, "min --> " + min);
//                JLogUtil.d(TAG, "max --> " + max);
                if (mDownX > min && mDownX < max) {
                    if (key == mTargetIndex) return;
                    // 画线
                    mTargetIndex = key;
                    needDrawVerticalLine = true;
                    invalidate();
                    return;
                }
            }
        }
        // EventBus.getDefault().post(new LineChartMoveEvent(""));
        if (needDrawVerticalLine) {
            needDrawVerticalLine = false;
            mTargetIndex = -1;
            invalidate();
        }
    }

    /**
     * 绘制图例
     */
    private void drawLegend(Canvas canvas, List<LineChartLegendBean> legendList) {
        if (legendList == null || legendList.size() == 0) return;
        float x = LEGEND_BALL_SIZE;
        float ty = TEXT_SIZE * 1f;
        float cy = TEXT_SIZE / 1.6f;
        float offset = LEGEND_BALL_SIZE;
        int normalColor = Color.parseColor("#AAAAAA");
        for (LineChartLegendBean item : legendList) {
            item.setClickable(false);// 默认不能点击
            mLegendPaint.setColor(item.isChecked() ? item.getColor() : normalColor);
            mLegendPaint.setStyle(Paint.Style.FILL);
            switch (item.getType()) {
                case LineChartLegendBean.TYPE_CIRCLE:
                    offset = LEGEND_BALL_SIZE;
                    canvas.drawCircle(x, cy, LEGEND_BALL_SIZE, mLegendPaint);
                    item.setClickable(true);
                    break;
                case LineChartLegendBean.TYPE_LINE:
                    offset = LEGEND_LINE_WIDTH;
                    canvas.drawLine(x, cy, x + LEGEND_LINE_WIDTH, cy, mLegendPaint);
                    break;
                case LineChartLegendBean.TYPE_DASH_LINE:
                    offset = LEGEND_LINE_WIDTH;
                    mDashLinePaint.setColor(item.getColor());
                    mDashLinePaint.setStrokeWidth(DASH_LINE_SIZE);
                    mDashLinePaint.setAlpha(255);
                    mDashLinePaint.setPathEffect(new DashPathEffect(new float[]{DASH_LINE_SIZE, DASH_LINE_SIZE}, 0));
                    mDashPath.reset();
                    mDashPath.moveTo(x, cy);
                    mDashPath.lineTo(x + LEGEND_LINE_WIDTH, cy);
                    canvas.drawPath(mDashPath, mDashLinePaint);
                    break;
            }
            // mTxtPaint.setColor(item.isChecked() ? item.getColor() : Color.parseColor("#555555"));
            mTxtPaint.setColor(item.isChecked() ? Color.parseColor("#555555") : normalColor);
            String name = item.getName();
            float tx = x + offset + autoSize(10);
            canvas.drawText(name, tx, ty, mTxtPaint);// 这里有个marginLeft=10px
            float txtWidth = mTxtPaint.measureText(name);
            x += txtWidth + offset + autoSize(40);// 因为有个marginLeft=40px
            item.setOffsetX(0);
            item.setOffsetY(autoSize(10));// Y轴增加±10像素灵敏度
            item.setX0(tx);
            item.setY0(0);
            item.setX1(tx + txtWidth);
            item.setY1(ty);
            // JLogUtil.d(TAG, "item --> " + JSON.toJSONString(item));
        }
    }

    private int autoSize(int size) {
        return DensityUtil.getPercentWidthSize(getContext(),1920,size);
    }

    public void cleanVerticalLine() {
        needDrawVerticalLine = false;
        mTargetIndex = -1;
        invalidate();
    }

    public boolean isCurveMode() {
        return curveMode;
    }

    public void setCurveMode(boolean curveMode) {
        this.curveMode = curveMode;
    }

    public boolean isNeedDrawKeyPoint() {
        return needDrawKeyPoint;
    }

    public void setNeedDrawKeyPoint(boolean needDrawKeyPoint) {
        this.needDrawKeyPoint = needDrawKeyPoint;
    }
}
