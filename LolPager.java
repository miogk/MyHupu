package com.example.miogk.myhupu;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.miogk.myhupu.base.BasePager;

/**
 * Created by Administrator on 2018/5/15.
 */

public class LolPager extends BasePager {
    public LolPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.my_list_view, null);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        getDataFromNet("https://raw.githubusercontent.com/miogk/miogk.github.io/master/10007/20180526/lol/list_1.json", false, false);
    }
}
