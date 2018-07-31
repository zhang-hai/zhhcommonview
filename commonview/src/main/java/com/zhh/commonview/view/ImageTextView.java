package com.zhh.commonview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.zhh.commonview.R;
import com.zhh.commonview.utils.DensityUtil;

/**
 * Created by zhanghai on 2018/7/31.
 * function：
 */
public class ImageTextView extends AppCompatTextView{
    private Drawable mDrawableLeft;//设置左边图片
    private Drawable mDrawableTop;//设置上边图片
    private Drawable mDrawableRight;//设置右边图片
    private Drawable mDrawableBottom;//设置下边图片
    private int mScaleWidth; // 图片的宽度
    private int mScaleHeight;// 图片的高度
    private Context mContext;

    public ImageTextView(Context context) {
        super(context);
    }

    public ImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ImageTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.ImageTextView);

        mDrawableLeft = typedArray.getDrawable(R.styleable.ImageTextView_drawableLeft);
        mDrawableTop = typedArray.getDrawable(R.styleable.ImageTextView_drawableTop);
        mDrawableRight = typedArray.getDrawable(R.styleable.ImageTextView_drawableRight);
        mDrawableBottom = typedArray.getDrawable(R.styleable.ImageTextView_drawableBottom);
        mScaleWidth = typedArray.getDimensionPixelOffset(R.styleable.ImageTextView_drawableWidth,
                        DensityUtil.dip2px(context,20));
        mScaleHeight = typedArray.getDimensionPixelOffset(R.styleable.ImageTextView_drawableHeight,
                DensityUtil.dip2px(context,20));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mDrawableLeft != null) {
            mDrawableLeft.setBounds(0,0,mScaleWidth,mScaleHeight);
        }
        if(mDrawableTop != null){
            mDrawableTop.setBounds(0,0,mScaleWidth,mScaleHeight);
        }
        if(mDrawableRight != null){
            mDrawableRight.setBounds(0,0,mScaleWidth,mScaleHeight);
        }
        if(mDrawableBottom != null){
            mDrawableBottom.setBounds(0,0,mScaleWidth,mScaleHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.setCompoundDrawables(mDrawableLeft, mDrawableTop, mDrawableRight, mDrawableBottom);
    }

    /**
     * 设置左侧图片并重绘
     *
     * @param drawable
     */
    public void setDrawableLeft(Drawable drawable) {
        this.mDrawableLeft = drawable;
        invalidate();
    }

    /**
     * 设置左侧图片并重绘
     *
     * @param drawableRes
     */
    public void setDrawableLeft(int drawableRes) {
        this.mDrawableLeft = mContext.getResources().getDrawable(drawableRes);
        invalidate();
    }

    /**
     * 设置顶部图片
     * @param drawable
     */
    public void setDrawableTop(Drawable drawable){
        this.mDrawableTop = drawable;
        invalidate();
    }

    /**
     * 设置顶侧图片并重绘
     *
     * @param drawableRes
     */
    public void setDrawableTop(int drawableRes) {
        this.mDrawableTop = mContext.getResources().getDrawable(drawableRes);
        invalidate();
    }

    /**
     * 设置右侧图片
     * @param drawable
     */
    public void setDrawableRight(Drawable drawable){
        this.mDrawableRight = drawable;
        invalidate();
    }

    /**
     * 设置右侧图片并重绘
     *
     * @param drawableRes
     */
    public void setDrawableRight(int drawableRes) {
        this.mDrawableRight = mContext.getResources().getDrawable(drawableRes);
        invalidate();
    }

    /**
     * 设置底部图片
     * @param drawable
     */
    public void setDrawableBottom(Drawable drawable){
        this.mDrawableBottom = drawable;
        invalidate();
    }

    /**
     * 设置底部图片并重绘
     *
     * @param drawableRes
     */
    public void setDrawableBottom(int drawableRes) {
        this.mDrawableBottom = mContext.getResources().getDrawable(drawableRes);
        invalidate();
    }
}
