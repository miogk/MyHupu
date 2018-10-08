package com.example.miogk.myhupu.radiobutton.forumraduibutton;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.example.miogk.myhupu.R;

/**
 * Created by Administrator on 2018/6/2.
 */

public class RecommendPager extends BaseForumPager {
    public RecommendPager(FragmentActivity mActivity) {
        super(mActivity);
    }

    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.forum_recommend, null);
        return view;
    }

    @Override
    protected void initData() {

    }
}
