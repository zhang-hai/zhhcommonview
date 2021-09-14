package com.zhh.commonview.demo;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;

import com.zhh.commonview.adapter.CommonViewPagerFragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by zhanghai on 2020/4/26.
 * function：
 */
public class MoreFragmentTestActivity extends AppCompatActivity {

    private ViewPager vp_pager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_fragment_test);

        vp_pager = findViewById(R.id.vp_pager);
        initPagers();
    }

    private void initPagers() {
        final List<ObjectBean> list = new ArrayList<>();
        for (int i = 0;i < 200;i++){
            list.add(new ObjectBean(String.format(Locale.CHINESE,"fragment_%d",i)));
        }
        CommonViewPagerFragmentStateAdapter adapter = new CommonViewPagerFragmentStateAdapter<ObjectBean>(getSupportFragmentManager(),list) {
            @Override
            public Fragment getItem(final int position) {
                ObjectBean bean = list.get(position);
                TestFragment f = new TestFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("identifier",bean.hashCode());
                bundle.putString("index",bean.index);
                f.setArguments(bundle);
                return f;
            }
        };

        vp_pager.setAdapter(adapter);

    }



    public final class ObjectBean{
        public String index;

        public ObjectBean(String index){
            this.index = index;
        }

        @Override
        public int hashCode() {
            return TextUtils.isEmpty(index) ? index.hashCode() : super.hashCode();
        }
    }
}
