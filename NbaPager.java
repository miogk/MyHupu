package com.example.miogk.myhupu;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.miogk.myhupu.base.BasePager;
import com.example.miogk.myhupu.utils.ConstansUtils;

public class NbaPager extends BasePager {


    public NbaPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.my_list_view, null);
        return view;
    }

    @Override
    public void initData() {
//        TextView textView = (TextView) rootView.findViewById(R.id.tv_nba_page);
//        textView.setText("nba");
        super.initData();
        getDataFromNet(ConstansUtils.SERVER_NAME + "/10007/20180523/list_1.json", false, false);
    }
}