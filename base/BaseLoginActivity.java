package com.example.miogk.myhupu.base;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Administrator on 2018/6/22.
 */

public abstract class BaseLoginActivity {
    public View rootView;
    protected Activity mActivity;

    public BaseLoginActivity(Activity activity) {
        this.mActivity = activity;
        rootView = initView();
        initData();
    }

    protected void initData() {

    }

    protected abstract View initView();
}
