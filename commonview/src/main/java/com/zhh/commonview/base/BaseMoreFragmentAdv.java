package com.zhh.commonview.base;

import android.support.v4.app.Fragment;

/**
 * Created by zhanghai on 2019/1/24.
 * function：BaseNormalFragmentAdv
 */
public abstract class BaseMoreFragmentAdv extends Fragment  {
    protected final String TAG = this.getClass().getSimpleName();

    /**
     * 采用ViewPager + Fragment(FragmentStatePagerAdapter)时，需要实现该接口
     * @return
     */
    public abstract int getFragmentIdentifier();

    /**
     * fragment对用户是否可见
     */
    public boolean isVisibleToUser(){
        return isResumed() && getUserVisibleHint();
    }
}
