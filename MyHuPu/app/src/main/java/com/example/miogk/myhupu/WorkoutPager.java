package com.example.miogk.myhupu;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.miogk.myhupu.base.BasePager;

/**
 * Created by Administrator on 2018/5/15.
 */

public class WorkoutPager extends BasePager {
    public WorkoutPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.workout_page, null);
    }

    @Override
    public void initData() {
        TextView textView = (TextView) rootView.findViewById(R.id.tv_workout_page);
        textView.setText("健身");
    }
}