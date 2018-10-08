package com.example.miogk.myhupu.radiobutton.forumraduibutton;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

/**
 * Created by Administrator on 2018/5/31.
 */

public abstract class BaseForumPager{
    protected FragmentActivity mActivity;
    protected View rootView;

    public BaseForumPager(FragmentActivity mActivity) {
        this.mActivity = mActivity;
        rootView = initView();
    }

    protected abstract View initView();

    protected abstract void initData();
}
