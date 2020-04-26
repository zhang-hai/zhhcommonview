package com.zhh.commonview.adapter;

import android.support.v4.app.FragmentManager;

import com.zhh.commonview.base.BaseMoreFragmentAdv;

import java.util.List;

/**
 * Created by zhanghai on 2019/2/27.
 * function：
 */
public abstract class CommonViewPagerFragmentStateAdapter<T> extends BaseFragmentStatePagerAdapter {

    private FragmentManager mFragmentManager;
    private List<T> mItems;

    public CommonViewPagerFragmentStateAdapter(FragmentManager fm, List<T> mItems) {
        super(fm);
        this.mFragmentManager = fm;
        this.mItems = mItems;
    }

    @Override
    public int getCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public long getItemId(int position) {
        return mItems == null || mItems.size() <=0 ? super.getItemId(position) : mItems.get(position).hashCode();
    }

    @Override
    public int getItemPosition(Object object) {
        if(object instanceof BaseMoreFragmentAdv){
            BaseMoreFragmentAdv item = (BaseMoreFragmentAdv) object;
            int itemValue = item.getFragmentIdentifier();
            for (int i = 0; i < mItems.size(); i++) {
                if (mItems.get(i).hashCode() == itemValue) {
                    return i;
                }
            }
        }
        return POSITION_NONE;
    }


}
