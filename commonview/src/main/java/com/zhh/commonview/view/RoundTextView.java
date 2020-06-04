package com.zhh.commonview.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.zhh.commonview.R;
import com.zhh.commonview.utils.DensityUtil;


/**
 * Created by zhanghai on 2019/1/25.
 * function：自定义的圆角TextView,这里设置替代创建很多重复的Shape资源xml文件
 * 通过设置自定义属性：
 * 1. solid_color 填充色
 * 2.corner 圆角的大小
 * 3.stroke_width 边框线条的粗细
 * 4.stroke_color 边框线条的颜色
 */
public class RoundTextView extends AppCompatTextView {
    //默认的颜色及背景
    private StateListDrawable defaultStateDrawable = null;
    private ColorStateList defaultStateColor = null;

    private int defaultSolidColor, defaultStrokeColor, pressSolidColor, pressStrokeColor, selectSolidColor, selectStrokeColor;//默认的填充和边框颜色
    private float defaultCorner, defaultStrokeWidth;//默认的圆角和边框粗细
    private Drawable defaultDrawable = null;
    private int defaultColor;


    //扩展状态使用的信息
    private int mExtendColor = defaultColor;
    private Drawable mExtendDrawable = null;
    private int mErrorColor = defaultColor;
    private Drawable mErrorDrawable = null;
    private int mGrayStrokeColor = defaultStrokeColor;
    private int[][] mColorStates;

    private Drawable mDrawableLeft;//设置左边图片
    private Drawable mDrawableTop;//设置上边图片
    private Drawable mDrawableRight;//设置右边图片
    private Drawable mDrawableBottom;//设置下边图片
    private int mScaleWidth; // 图片的宽度
    private int mScaleHeight;// 图片的高度
    private Context mContext;

    public RoundTextView(Context context) {
        super(context);
        init(context, null);
    }

    public RoundTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    public RoundTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundTextView);
        //如果设置了背景则使用设置的背景
        if (getBackground() == null) {
            defaultSolidColor = typedArray.getColor(R.styleable.RoundTextView_solid_color, Color.WHITE);
            defaultCorner = typedArray.getDimensionPixelSize(R.styleable.RoundTextView_corner, 0);
            defaultStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.RoundTextView_stroke_width, 1);

            //若不设置，默认使用solidColor
            defaultStrokeColor = typedArray.getColor(R.styleable.RoundTextView_stroke_color, defaultSolidColor);

            //若不设置，默认使用solidColor
            pressSolidColor = typedArray.getColor(R.styleable.RoundTextView_press_solid_color, defaultSolidColor);
            //若不设置，默认使用pressSolidColor
            pressStrokeColor = typedArray.getColor(R.styleable.RoundTextView_press_stroke_color, pressSolidColor);

            //获取选中时的颜色
            selectSolidColor = typedArray.getColor(R.styleable.RoundTextView_select_solid_color, defaultSolidColor);
            selectStrokeColor = typedArray.getColor(R.styleable.RoundTextView_select_stroke_color, selectSolidColor);

            defaultCorner = Math.max(DensityUtil.getPercentWidthSize(getContext(),1920,(int) defaultCorner), DensityUtil.getPercentHeightSize(getContext(),1280,(int) defaultCorner));

            invalidateBackground();
        }
        int pressTextColor = typedArray.getColor(R.styleable.RoundTextView_press_text_color, 0);
        int selectTextColor = typedArray.getColor(R.styleable.RoundTextView_select_text_color, 0);
        typedArray.recycle();
        // 获取设置的drawable
        TypedArray drawableTypeArray = context.obtainStyledAttributes(attrs, R.styleable.ImageTextView);
        mDrawableLeft = drawableTypeArray.getDrawable(R.styleable.ImageTextView_drawableLeft);
        mDrawableTop = drawableTypeArray.getDrawable(R.styleable.ImageTextView_drawableTop);
        mDrawableRight = drawableTypeArray.getDrawable(R.styleable.ImageTextView_drawableRight);
        mDrawableBottom = drawableTypeArray.getDrawable(R.styleable.ImageTextView_drawableBottom);
        mScaleWidth = drawableTypeArray.getDimensionPixelOffset(R.styleable.ImageTextView_drawableWidth, 20);
        mScaleHeight = drawableTypeArray.getDimensionPixelOffset(R.styleable.ImageTextView_drawableHeight, 20);
        drawableTypeArray.recycle();

        initColorState();

        defaultStateColor = buildColorStateList(pressTextColor, selectTextColor);
        setTextColor(defaultStateColor);

        initExtendState();

        int newWidth = DensityUtil.getPercentWidthSize(getContext(),1920,mScaleWidth);
        int newHeight = DensityUtil.getPercentHeightSize(getContext(),1280,mScaleHeight);
        if(mScaleWidth == mScaleHeight){
            mScaleWidth = mScaleHeight = Math.min(newWidth,newHeight);
        }else {
            mScaleWidth = newWidth;
            mScaleHeight = newHeight;
        }
        //初始化时，设置drawable
        setCompoundDrawablesWithIntrinsicBounds(mDrawableLeft,mDrawableTop,mDrawableRight,mDrawableBottom);
    }

    private StateListDrawable getDefaultStateDrawable(){
        return getStateDrawable(buildGradientDrawable(defaultSolidColor, defaultCorner, defaultStrokeWidth, defaultStrokeColor),
                buildGradientDrawable(pressSolidColor, defaultCorner, defaultStrokeWidth, pressStrokeColor),
                buildGradientDrawable(selectSolidColor, defaultCorner, defaultStrokeWidth, selectStrokeColor));
    }

    private void invalidateBackground(){
        defaultStateDrawable = getDefaultStateDrawable();
        setBackground(defaultStateDrawable);
    }

    /**
     * 设置填充色
     * @param defaultColor
     * @param pressColor
     * @param selectColor
     */
    public void setSolidColor(int defaultColor,int pressColor,int selectColor){
        defaultSolidColor = defaultColor;
        pressSolidColor = pressColor;
        selectSolidColor = selectColor;
        invalidateBackground();
    }

    /**
     * 设置边框颜色
     * @param defaultColor
     * @param pressColor
     * @param selectColor
     */
    public void setStrokeColor(int defaultColor,int pressColor,int selectColor){
        defaultStrokeColor = defaultColor;
        pressStrokeColor = pressColor;
        selectStrokeColor = selectColor;

        invalidateBackground();
    }

    private void initColorState() {
        mColorStates = new int[3][];
        mColorStates[0] = new int[]{android.R.attr.state_pressed};
        mColorStates[1] = new int[]{android.R.attr.state_selected};
        mColorStates[2] = new int[]{};
    }


    private StateListDrawable getStateDrawable(Drawable normal, Drawable pressed, Drawable select) {
        defaultDrawable = normal;
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressed);
        stateListDrawable.addState(new int[]{android.R.attr.state_selected}, select);
        stateListDrawable.addState(new int[]{}, normal);
        return stateListDrawable;
    }

    /**
     * 通过给定的参数构造一个GradientDrawable对象
     *
     * @param solidColor  填充颜色
     * @param corner      圆角 px
     * @param strokeWidth 边框线条宽度
     * @param strokeColor 边框线条颜色
     * @return
     */
    private GradientDrawable buildGradientDrawable(int solidColor, float corner, float strokeWidth, int strokeColor) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setStroke((int) strokeWidth, strokeColor);
        gradientDrawable.setCornerRadius(corner);
        gradientDrawable.setColor(solidColor);
        return gradientDrawable;
    }

    /**
     * 构造字体颜色的ColorStateList对象
     *
     * @param pressColor  按下时的字体颜色
     * @param selectColor 选中时的字体颜色
     * @return
     */
    private ColorStateList buildColorStateList(int pressColor, int selectColor) {
        //先获取默认的字体颜色
        ColorStateList stateList = getTextColors();
        int defColor = stateList.getDefaultColor();
        //表示设置了按下时的字体颜色
        pressColor = pressColor == 0 ? defColor : pressColor;
        selectColor = selectColor == 0 ? defColor : selectColor;

        defaultColor = defColor;

        if (mColorStates == null) {
            initColorState();
        }
        return new ColorStateList(mColorStates, new int[]{pressColor, selectColor, defColor});
    }

    /**
     * 设置扩展属性的状态值
     */
    private void initExtendState() {
        int rightcolor = getResources().getColor(R.color.color_right);
        int errorcolor = getResources().getColor(R.color.color_error);
        this.mExtendColor = rightcolor;
        this.mErrorColor = errorcolor;
        this.mGrayStrokeColor = getResources().getColor(R.color.color_result_gray_stroke);

        this.mExtendDrawable = buildGradientDrawable(defaultSolidColor,
                defaultCorner,
                defaultStrokeWidth == 0 ? 1 : defaultStrokeWidth,
                rightcolor == 0 ? defaultStrokeColor : rightcolor);

        this.mErrorDrawable = buildGradientDrawable(defaultSolidColor,
                defaultCorner,
                defaultStrokeWidth == 0 ? 1 : defaultStrokeWidth,
                errorcolor == 0 ? defaultStrokeColor : errorcolor);

    }


    //恢复字体颜色
    public void recoverTextColor() {
        if (defaultStateColor != null) {
            setTextColor(defaultStateColor);
        }
    }



    /**
     * 设置左侧图片并重绘
     *
     * @param drawable
     */
    public void setDrawableLeft(Drawable drawable) {
        this.mDrawableLeft = drawable;
        setCompoundDrawablesWithIntrinsicBounds(mDrawableLeft,mDrawableTop,mDrawableRight,mDrawableBottom);
    }

    /**
     * 设置左侧图片并重绘
     *
     * @param drawableRes
     */
    public void setDrawableLeft(int drawableRes) {
        this.mDrawableLeft = mContext.getResources().getDrawable(drawableRes);
        setCompoundDrawablesWithIntrinsicBounds(mDrawableLeft,mDrawableTop,mDrawableRight,mDrawableBottom);
    }

    /**
     * 设置右侧图片并重绘
     *
     * @param drawable
     */
    public void setDrawableRight(Drawable drawable) {
        this.mDrawableRight = drawable;
        setCompoundDrawablesWithIntrinsicBounds(mDrawableLeft,mDrawableTop,mDrawableRight,mDrawableBottom);
    }

    /**
     * 设置右侧图片并重绘
     *
     * @param drawableRes
     */
    public void setDrawableRight(int drawableRes) {
        this.mDrawableRight = mContext.getResources().getDrawable(drawableRes);
        setCompoundDrawablesWithIntrinsicBounds(mDrawableLeft,mDrawableTop,mDrawableRight,mDrawableBottom);
    }

    /**
     * 设置顶部图片并重绘
     *
     * @param drawable
     */
    public void setDrawableTop(Drawable drawable) {
        this.mDrawableTop = drawable;
        setCompoundDrawablesWithIntrinsicBounds(mDrawableLeft,mDrawableTop,mDrawableRight,mDrawableBottom);
    }

    /**
     * 设置顶部图片并重绘
     *
     * @param drawableRes
     */
    public void setDrawableTop(int drawableRes) {
        this.mDrawableTop = mContext.getResources().getDrawable(drawableRes);
        setCompoundDrawablesWithIntrinsicBounds(mDrawableLeft,mDrawableTop,mDrawableRight,mDrawableBottom);
    }

    /**
     * 设置底部图片并重绘
     *
     * @param drawable
     */
    public void setDrawableBottom(Drawable drawable) {
        this.mDrawableBottom = drawable;
        setCompoundDrawablesWithIntrinsicBounds(mDrawableLeft,mDrawableTop,mDrawableRight,mDrawableBottom);
    }

    /**
     * 设置底部图片并重绘
     *
     * @param drawableRes
     */
    public void setDrawableBottom(int drawableRes) {
        this.mDrawableBottom = mContext.getResources().getDrawable(drawableRes);
        setCompoundDrawablesWithIntrinsicBounds(mDrawableLeft,mDrawableTop,mDrawableRight,mDrawableBottom);
    }

    /**
     * 重写父类方法，设置图片的边界尺寸和图片
     * @param left 左边图片
     * @param top 顶部图片
     * @param right 右侧图片
     * @param bottom 底部图片
     */
    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(@Nullable Drawable left, @Nullable Drawable top, @Nullable Drawable right, @Nullable Drawable bottom) {
        if (left != null) {
            left.setBounds(0, 0, mScaleWidth > 0 ? mScaleWidth : left.getIntrinsicWidth(), mScaleHeight > 0 ? mScaleHeight : left.getIntrinsicHeight());
        }
        if (right != null) {
            right.setBounds(0, 0, mScaleWidth > 0 ? mScaleWidth : right.getIntrinsicWidth(), mScaleHeight > 0 ? mScaleHeight : right.getIntrinsicHeight());
        }
        if (top != null) {
            top.setBounds(0, 0, mScaleWidth > 0 ? mScaleWidth : top.getIntrinsicWidth(), mScaleHeight > 0 ? mScaleHeight : top.getIntrinsicHeight());
        }
        if (bottom != null) {
            bottom.setBounds(0, 0, mScaleWidth > 0 ? mScaleWidth : bottom.getIntrinsicWidth(), mScaleHeight > 0 ? mScaleHeight : bottom.getIntrinsicHeight());
        }
        setCompoundDrawables(left, top, right, bottom);
    }

}
