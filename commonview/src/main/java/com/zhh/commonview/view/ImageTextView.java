package com.zhh.commonview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.zhh.commonview.R;

/**
 * Created by Admin on 2017/10/23.
 */

public class ImageTextView extends AppCompatTextView {
    private Drawable mDrawableLeft;//设置左边图片
    private Drawable mDrawableTop;//设置上边图片
    private Drawable mDrawableRight;//设置右边图片
    private Drawable mDrawableBottom;//设置下边图片
    private int mScaleWidth; // 图片的宽度
    private int mScaleHeight;// 图片的高度
    private Context mContext;

    //是否需要绘制drawableLeft、drawableRight、drawableTop、drawableBottom
//    private boolean isNeedInvalidateDrawable = false;

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

        mScaleWidth = typedArray
                .getDimensionPixelOffset(
                        R.styleable.ImageTextView_drawableWidth, 20);
        mScaleHeight = typedArray.getDimensionPixelOffset(
                R.styleable.ImageTextView_drawableHeight, 20);
        typedArray.recycle();

//        int newWidth = AutoUtils.getPercentWidthSize(mScaleWidth);
//        int newHeight = AutoUtils.getPercentHeightSize(mScaleHeight);
//        if(mScaleWidth == mScaleHeight){
//            mScaleWidth = mScaleHeight = Math.min(newWidth,newHeight);
//        }else {
//            mScaleWidth = newWidth;
//            mScaleHeight = newHeight;
//        }
        //初始化时，设置边界图片
        setCompoundDrawablesWithIntrinsicBounds(mDrawableLeft,mDrawableTop,mDrawableRight,mDrawableBottom);
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
    public void setCompoundDrawablesWithIntrinsicBounds(@Nullable Drawable left,
                                                        @Nullable Drawable top, @Nullable Drawable right, @Nullable Drawable bottom) {

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
