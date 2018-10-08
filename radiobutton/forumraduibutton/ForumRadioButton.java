package com.example.miogk.myhupu.radiobutton.forumraduibutton;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.miogk.myhupu.MainActivity;
import com.example.miogk.myhupu.R;
import com.example.miogk.myhupu.radiobutton.forumraduibutton.plate.PlatePager;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/31.
 */

public class ForumRadioButton extends Fragment {
    private FragmentActivity mActivity;
    private ViewPager viewPager;
    private View rootView;
    private TabPageIndicator tabPageIndicator;
    private MyPagerAdapter mPagerAdapter;
    private List<BaseForumPager> pagers = new ArrayList<>();
    private static final String[] CONTENT = new String[]{"板块", "推荐", "视频"};

    public ForumRadioButton() {

    }

    public ForumRadioButton(MainActivity mainActivity) {
        this.mActivity = mainActivity;
    }

    private void initPageAdapterData() {
        PlatePager platePager = new PlatePager(mActivity);
        RecommendPager recommendPager = new RecommendPager(mActivity);
        VideoPager videoPager = new VideoPager(mActivity);
        pagers.add(platePager);
        pagers.add(recommendPager);
        pagers.add(videoPager);
        viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        tabPageIndicator = (TabPageIndicator) rootView.findViewById(R.id.indicator);
        mPagerAdapter = new MyPagerAdapter();
        viewPager.setAdapter(mPagerAdapter);
        tabPageIndicator.setViewPager(viewPager);
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return CONTENT.length;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BaseForumPager pager = pagers.get(position);
            View view = pager.rootView;
            pager.initData();
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position % CONTENT.length].toUpperCase();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_radio_button, container, false);
        rootView = view;
        initPageAdapterData();
        return view;
    }
}