package com.zhh.commonview.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.LinearLayout
import com.zhh.commonview.R

/**
 * Created by zhanghai on 2021/9/3.
 * function：左滑删除Layout
 */
class LeftSlideLayout(context: Context, val attrs: AttributeSet?, val defStyleAttr: Int) :
    LinearLayout(context, attrs, defStyleAttr) {

    //最小触摸距离
    private val mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop

    private var isReCompute = false
    private var mInitX = 0f
    private var mInitY = 0f
    private var mRightCanSlide = 0

    private var mValueAnimator: ValueAnimator? = null
    private val mAnimDuring = 200L

    private var mStatusChangeListener: OnDeleteViewStatusChangeListener? = null


    private var mContentViewId : Int = 0
    private var mRightMenuViewId : Int = 0
    private lateinit var mContentView: View
    private lateinit var mRightMenuView: View

    //是否可以滑动
    var isCanSlideOperate = true

    constructor(context: Context):this(context,null)

    constructor(context: Context,attrs: AttributeSet?):this(context, attrs, 0)

    init {
        setBackgroundColor(Color.TRANSPARENT)
        orientation = HORIZONTAL
        initView()
    }

    /**
     * 初始化View
     */
    private fun initView() {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.LeftSlideLayout)
        mContentViewId = typeArray.getResourceId(R.styleable.LeftSlideLayout_contentView, 0)
        mRightMenuViewId = typeArray.getResourceId(R.styleable.LeftSlideLayout_rightMenuView, 0)
        typeArray.recycle()

        //先检测是否设置了以下两个属性
        if (mContentViewId == 0) {
            throw RuntimeException("请在xml中设置contentView属性")
        }
        if (mRightMenuViewId == 0) {
            throw RuntimeException("请在xml中设置rightMenuView属性")
        }

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mContentView = findViewById(mContentViewId)
        mRightMenuView = findViewById(mRightMenuViewId)

        mRightCanSlide = mRightMenuView.layoutParams.width
    }

    /**
     * 拦截触摸事件
     * @param ev
     * @return true or false
     */
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        //不能滑动时，直接返回，不拦截事件
        if (!isCanSlideOperate)
            return super.onInterceptTouchEvent(ev)

        ev?.run {
            when (actionMasked) {
                //按下
                MotionEvent.ACTION_DOWN -> {
                    mInitX = rawX + scrollX
                    mInitY = rawY
                    clearAnim()
                }
                //滑动
                MotionEvent.ACTION_MOVE -> {
                    if (mInitX - ev.getRawX() < 0) {
                        return false
                    }
                    //左滑并超过指定距离
                    if (Math.abs(mInitX - rawX - scrollX) >= mTouchSlop) {
                        //父级容器拦截
                        if (parent != null && isReCompute) {
                            parent.requestDisallowInterceptTouchEvent(true)
                            isReCompute = false
                        }
                        return true
                    }
                }
                //抬起 、取消
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    parent?.requestDisallowInterceptTouchEvent(false)
                    isReCompute = true
                }
                else -> {
                }
            }
        }

        return super.onInterceptTouchEvent(ev)
    }


    /**
     * 处理触摸事件
     */
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.run {
            when (actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    mInitX = rawX + scrollX
                    mInitY = rawY
                    clearAnim()
                }
                MotionEvent.ACTION_MOVE -> {
                    if (Math.abs(mInitX - rawX - scrollX) >= mTouchSlop) {
                        if (parent != null && isReCompute) {
                            parent.requestDisallowInterceptTouchEvent(true)
                            isReCompute = false
                        }
                    }

                    var translationX = mInitX - rawX
                    if (translationX > mRightCanSlide) {
                        mInitY = rawY + mRightCanSlide
                        translationX = mRightCanSlide.toFloat()
                    }

                    if (translationX < 0) {
                        mInitY = rawY
                        translationX = 0f
                    }

                    if (translationX <= mRightCanSlide && translationX >= 0) {
                        scrollTo(translationX.toInt(), 0)
                        return true
                    }
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(false)
                        isReCompute = true
                    }
                    upAnim()
                    return true
                }
                else -> {
                }
            }
        }
        return true
    }

    /**
     * 清除动画
     */
    private fun clearAnim() {
        mValueAnimator?.apply {
            end()
            cancel()
        }
        mValueAnimator = null
    }

    /**
     * 手指抬起 执行动画
     */
    private fun upAnim() {
        if (scrollX == mRightCanSlide || scrollX == 0) {
            mStatusChangeListener?.onStatusChange(scrollX == mRightCanSlide)
            return
        }
        clearAnim()

        //如果滑动 >= mRightCanSlide/2距离，执行动画自动显示，否则动画隐藏
        if (scrollX >= mRightCanSlide / 2) {
            showAnim()
        }else{
            hideAnim()
        }
    }

    private fun resetDeleteStatus(){
        if (scrollX == 0) return

        clearAnim()

        hideAnim()
    }

    /**
     * 显示动画
     */
    private fun showAnim(){
        mValueAnimator = ValueAnimator.ofInt(scrollX,mRightCanSlide)
        //执行动画
        mValueAnimator?.apply {
            addUpdateListener { animation ->
                val value = animation.animatedValue as Int
                scrollTo(value, 0)
            }
            duration = mAnimDuring
            start()
        }
        mStatusChangeListener?.onStatusChange(true)
    }

    /**
     * 隐藏动画
     */
    private fun hideAnim(){
        mValueAnimator = ValueAnimator.ofInt(scrollX,0)
        //执行动画
        mValueAnimator?.apply {
            addUpdateListener { animation ->
                val value = animation.animatedValue as Int
                scrollTo(value, 0)
            }
            duration = mAnimDuring
            start()
        }
        mStatusChangeListener?.onStatusChange(true)
    }

    /**
     * 立即隐藏右侧菜单
     */
    fun hideRightMenuImmediate(){
        //立即隐藏右侧菜单
        if (scrollX > 0){
            //先清除动画
            clearAnim()

            scrollTo(0,0)
        }
    }

    /**
     * 删除按钮状态变化监听接口
     */
    interface OnDeleteViewStatusChangeListener {
        /**
         * 状态变化监听
         * @param show  是否正在显示
         */
        fun onStatusChange(show: Boolean)
    }
}