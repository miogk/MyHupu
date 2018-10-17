package com.example.miogk.myhupu.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miogk.myhupu.ContentActivity;
import com.example.miogk.myhupu.R;
import com.example.miogk.myhupu.domain.Man;
import com.example.miogk.myhupu.utils.ConstansUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2018/5/15.
 */

public abstract class BasePager {
    protected Activity mActivity;
    public View rootView;
    private Man man;
    private ListView mListView;
    private List<Man.ManData> manData = new ArrayList<>();
    private BitmapUtils bitmapUtils;
    private MyListAdapter mListAdapter;
    private SmartRefreshLayout smartRefreshLayout;
    private String hasMore;
    private String refresh;
    private boolean isEnableLoadMore = true;

    public BasePager(Activity activity) {
        this.mActivity = activity;
        rootView = initView();
    }

    public abstract View initView();

    class ListHolder {
        ImageView image;
        TextView title;
        TextView time;

        public ListHolder(ImageView image, TextView title, TextView time) {
            this.image = image;
            this.title = title;
            this.time = time;
        }
    }

    class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return manData.size();
        }

        @Override
        public Object getItem(int position) {
            return manData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_view_data, null);
                ImageView image = (ImageView) convertView.findViewById(R.id.iv_list_view_data);
                TextView title = (TextView) convertView.findViewById(R.id.tv_title_list_view_data);
                TextView time = (TextView) convertView.findViewById(R.id.tv_time_list_view_data);
                holder = new ListHolder(image, title, time);
                convertView.setTag(holder);
            } else {
                holder = (ListHolder) convertView.getTag();
            }
            Man.ManData md = manData.get(position);
            String imageUrl = md.image;
            try {
                imageUrl = URLEncoder.encode(imageUrl, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String url = ConstansUtils.SERVER_NAME + imageUrl;
            holder.title.setText(md.title);
            holder.time.setText(md.time);
            bitmapUtils.display(holder.image, url);
            return convertView;
        }
    }

    public void initData() {
        bitmapUtils = new BitmapUtils(mActivity);
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.ARGB_4444);
        mListView = (ListView) rootView.findViewById(R.id.listview);
        smartRefreshLayout = (SmartRefreshLayout) rootView.findViewById(R.id.my_list_view_smartRefreshLayout);
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(mActivity));
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                Log.e(TAG, "onRefresh: " + refresh);
                refreshLayout.finishRefresh(1000, true);
                if (!TextUtils.isEmpty(refresh)) {
                    getDataFromNet(ConstansUtils.SERVER_NAME + refresh, false, true);
                } else {
                    Toast.makeText(mActivity, "没有更多更新的内容......", Toast.LENGTH_LONG).show();
                }
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(1000);
                if (isEnableLoadMore) {
                    if (!TextUtils.isEmpty(hasMore)) {
                        getDataFromNet(ConstansUtils.SERVER_NAME + hasMore, true, false);
                    } else {
                        Toast.makeText(mActivity, "没有更多了....", Toast.LENGTH_LONG).show();
                        isEnableLoadMore = false;
                    }
                }
            }
        });
        mListView.setOnItemClickListener(new MyListViewClickListener());
        mListAdapter = new MyListAdapter();
        mListView.setAdapter(mListAdapter);
    }

    protected void getDataFromNet(final String url, final boolean isLoadMore, final boolean refresh) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                rootView.findViewById(R.id.my_list_view_iv).setVisibility(View.GONE);
//                Log.e(TAG, "onSuccess: " + responseInfo.result);
                if (refresh) {
                    Log.e(TAG, "onSuccess: " + url);
                    parseJsonRefresh(responseInfo.result);
                } else if (isLoadMore) {
                    parseJsonLoadMore(responseInfo.result);
                } else {
                    parseJson(responseInfo.result);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e(TAG, "onFailure: " + e);
            }
        });

    }

    private void parseJsonRefresh(String json) {
        Gson gson = new Gson();
        Man man = gson.fromJson(json, Man.class);
        refresh = man.refresh;
        List<Man.ManData> data = man.data;
        manData.addAll(0, data);
        mListAdapter.notifyDataSetChanged();
    }

    private void parseJsonLoadMore(String json) {
        Gson gson = new Gson();
        Man man = gson.fromJson(json, Man.class);
        hasMore = man.more;
        List<Man.ManData> data = man.data;
        manData.addAll(data);
        mListAdapter.notifyDataSetChanged();
    }

    public void parseJson(String json) {
        Gson gson = new Gson();
        man = gson.fromJson(json, Man.class);
        hasMore = man.more;
        refresh = man.refresh;
        manData = man.data;
        if (mListView != null) {
            mListAdapter.notifyDataSetChanged();
        }
    }

    class MyListViewClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Man.ManData md = manData.get(position);
            Intent intent = new Intent(mActivity, ContentActivity.class);
            Log.e(TAG, "onItemClick: " + md);
            intent.putExtra("md", md);
            mActivity.startActivity(intent);
        }
    }
}