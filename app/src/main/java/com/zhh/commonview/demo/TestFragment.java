package com.zhh.commonview.demo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhh.commonview.base.BaseMoreFragmentAdv;

/**
 * Created by zhanghai on 2020/4/26.
 * function：
 */
public class TestFragment extends BaseMoreFragmentAdv {
    private String mData;
    private int mIdentifier;
    @Override
    public int getFragmentIdentifier() {
        return mIdentifier;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tv = view.findViewById(R.id.tv_content);
        if(getArguments() != null){
            mIdentifier = getArguments().getInt("identifier");
            mData = getArguments().getString("index");
            tv.setText(mData);
        }
    }
}
