package com.example.miogk.myhupu.radiobutton.forumraduibutton;

import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.example.miogk.myhupu.R;

/**
 * Created by Administrator on 2018/6/2.
 */

public class VideoPager extends BaseForumPager {
    public VideoPager(FragmentActivity mActivity) {
        super(mActivity);
    }

    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.forum_video, null);
        return view;
    }

    @Override
    protected void initData() {

    }
}