package com.example.miogk.myhupu.setting.fragment;

import android.app.Activity;
import android.view.View;

import com.example.miogk.myhupu.R;
import com.example.miogk.myhupu.base.BaseLoginActivity;

/**
 * Created by Administrator on 2018/6/22.
 */

public class LoginFragment extends BaseLoginActivity {

    public LoginFragment(Activity activity) {
        super(activity);
    }

    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.submit_layout, null);
        return view;
    }
}
