package com.example.miogk.myhupu;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.example.miogk.myhupu.base.BasePager;
import com.example.miogk.myhupu.domain.Man;
import com.example.miogk.myhupu.domain.Man.ManData;
import com.example.miogk.myhupu.utils.ConstansUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2018/5/15.
 */

public class CbaPager extends BasePager {

    public CbaPager(Activity activity) {
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
        getDataFromNet(ConstansUtils.SERVER_NAME + "/10007/20180525/cba/list_1.json", false, false);
    }
}