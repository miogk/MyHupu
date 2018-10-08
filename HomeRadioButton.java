package com.example.miogk.myhupu;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.miogk.myhupu.base.BasePager;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2018/6/1.
 */

public class HomeRadioButton extends Fragment {
    private Activity mActivity;
    private List<BasePager> list = new ArrayList();
    private MyPagerAdapter mPagerAdapter;
    private ViewPager viewPager;
    private TabPageIndicator tabPageIndicator;
    private View rootView;
    private boolean isFirst = true;
    private static final String[] CONTENT = new String[]{"NBA", "中国篮球", "英雄联盟", "健身",
            "Songs", "Playlists", "Genres", "ggggg", "dddddddd", "qqqqqqqq", "ccccccc", "zzzzzzzz"};

    public HomeRadioButton(Activity activity) {
        this.mActivity = activity;
    }

    public HomeRadioButton() {

    }

    private void initPageAdapterData() {
        NbaPager nba = new NbaPager(mActivity);
        CbaPager game = new CbaPager(mActivity);
        LolPager lol = new LolPager(mActivity);
        WorkoutPager workout = new WorkoutPager(mActivity);
        list.add(nba);
        list.add(game);
        list.add(lol);
        list.add(workout);
        int size = list.size();
        for (int i = 0; i < CONTENT.length - size; i++) {
            LolPager loll = new LolPager(mActivity);
            list.add(loll);
        }
        viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        mPagerAdapter = new MyPagerAdapter();
        viewPager.setAdapter(mPagerAdapter);
        tabPageIndicator = (TabPageIndicator) rootView.findViewById(R.id.indicator);
        tabPageIndicator.setViewPager(viewPager);
        tabPageIndicator.setCurrentItem(0);
        list.get(0).initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_radio_button, container, false);
        rootView = view;
        initPageAdapterData();
        Log.e(TAG, "onCreateView: ");
        return view;
    }

    class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return CONTENT.length;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager basePager = list.get(position);
            View view = basePager.rootView;
            basePager.initData();
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
}