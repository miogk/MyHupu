package com.example.miogk.myhupu.setting.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/9/5.
 */


public class PostFragment extends BaseHomePageFragment {

    public PostFragment(List<Map<String, String>> list, String username, ViewPager home_page_viewpager, SQLiteDatabase db, TabLayout tabLayout) {
        super(list, username, home_page_viewpager, db, tabLayout);
    }
}