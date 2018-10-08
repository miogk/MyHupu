package com.example.miogk.myhupu.radiobutton.forumraduibutton.plate.buttonActivities.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.support.v4.view.ViewPager;

import com.example.miogk.myhupu.radiobutton.forumraduibutton.plate.buttonActivities.base.BaseShihuhuFragment;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/8.
 */

public class HotFragment extends BaseShihuhuFragment {
    public HotFragment(LinkedList<Map<String, String>> lists, ViewPager viewPager, SQLiteDatabase db) {
        super(lists, viewPager, db);
    }
}
