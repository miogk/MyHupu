package com.example.miogk.myhupu.setting.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.miogk.myhupu.R;
import com.example.miogk.myhupu.db.MyDBHelper;
import com.example.miogk.myhupu.setting.HomePageActivity;
import com.example.miogk.myhupu.setting.adapter.PostFragmentRecyclerViewAdapter;
import com.example.miogk.myhupu.setting.adapter.ReplyFragmentRecyclerViewAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/9/7.
 */

public class BaseHomePageFragment extends Fragment {
    private TabLayout tabLayout;
    private View view;
    private RecyclerView recyclerView;
    private List<Map<String, String>> list;
    private SQLiteDatabase db;
    private RecyclerView.Adapter recyclerViewAdapter;
    private String username;
    private ViewPager home_page_viewpager;
    private SmartRefreshLayout smartRefreshLayout;
    private Class clazz;
    private NestedScrollView nsv;

    public BaseHomePageFragment(List<Map<String, String>> list, String username, ViewPager home_page_viewpager, SQLiteDatabase db, TabLayout tabLayout) {
        this.tabLayout = tabLayout;
        this.db = db;
        this.home_page_viewpager = home_page_viewpager;
        this.list = list;
        this.username = username;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            /**
             * view会慢一步加载，所有先等recyclerViewAdapter初始化后再进行下一步操作，之前写的是view != null也没有bug，
             * 但报错的是recyclerViewAdapter == null，所以就判断recyclerViewAdapter != null.
             */
            Cursor cursor = null;
            if (recyclerViewAdapter != null) {
//                list.clear();
                if (this.getClass() == ReplyFragment.class) {
                    recyclerViewAdapter = new ReplyFragmentRecyclerViewAdapter(getActivity(), list);
                    recyclerView.setAdapter(recyclerViewAdapter);
                } else if (this.getClass() == PostFragment.class) {
                    recyclerViewAdapter = new PostFragmentRecyclerViewAdapter(getActivity(), list);
                    recyclerView.setAdapter(recyclerViewAdapter);
                }
                recyclerViewAdapter.notifyDataSetChanged();
            }
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.home_page_layout, null);
        nsv = (NestedScrollView) view.findViewById(R.id.home_page_nsv);
        smartRefreshLayout = (SmartRefreshLayout) view.findViewById(R.id.home_page_smart_refresh_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.home_page_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //recycylerView各个item之间设置下划线
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        if (this.getClass() == ReplyFragment.class) {
            clazz = ReplyFragment.class;
            recyclerViewAdapter = new ReplyFragmentRecyclerViewAdapter(getActivity(), list);
        } else if (this.getClass() == PostFragment.class) {
            clazz = PostFragment.class;
            recyclerViewAdapter = new PostFragmentRecyclerViewAdapter(getActivity(), list);
        }
        recyclerView.setAdapter(recyclerViewAdapter);
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        smartRefreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                updateData();
                smartRefreshLayout.finishRefresh();
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                updateData();
                smartRefreshLayout.finishLoadMore();
            }
        });
        return view;
    }

    private void updateData() {
        Cursor cursor = null;
        HomePageActivity activity = (HomePageActivity) getActivity();
        TextView lighten_number = (TextView) activity.findViewById(R.id.home_page_lighten);
        cursor = db.query(MyDBHelper.USER_TABLE, new String[]{"lighten"}, "username = ?", new String[]{username}, null, null, null);
        cursor.moveToFirst();
        String lighten = cursor.getString(cursor.getColumnIndex("lighten"));
        //更新点亮
        if (TextUtils.isEmpty(lighten)) {
            lighten_number.setText("0");
        } else {
            lighten_number.setText(lighten);
        }
        //更新回帖
        if (clazz == ReplyFragment.class) {
            cursor = db.query(MyDBHelper.USER_TABLE_TABLE, null, "username = ?", new String[]{username}, null, null, "time desc");
            int count = getDateForhuitiePage(cursor);
            recyclerViewAdapter.notifyDataSetChanged();
            TabLayout.Tab tab = tabLayout.getTabAt(0);
            TextView huitie = (TextView) tab.getCustomView().findViewById(R.id.tab_item_number);
            huitie.setText(count + "");
            //更新发帖
        } else if (clazz == PostFragment.class) {
            cursor = db.query(MyDBHelper.POST_TABLE, null, "username = ?", new String[]{username}, null, null, "time desc");
            int count = getDataFromSqliteDatabase(cursor);
            TabLayout.Tab tab = tabLayout.getTabAt(2);
            TextView fatie = (TextView) tab.getCustomView().findViewById(R.id.tab_item_number);
            fatie.setText(count + "");
            recyclerViewAdapter.notifyDataSetChanged();
        }
        //关闭cursor
        if (cursor != null) {
            cursor.close();
        }
    }

    /**
     * @param cursor
     * @return 获取发帖数量
     */
    private int getDataFromSqliteDatabase(Cursor cursor) {
        list.clear();
        if (cursor.moveToFirst()) {
            fillList(cursor);
            while (cursor.moveToNext()) {
                fillList(cursor);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return list.size();
    }

    private void fillList(Cursor cursor) {
        String title = cursor.getString(cursor.getColumnIndex("title"));
        String content = cursor.getString(cursor.getColumnIndex("content"));
        String time = cursor.getString(cursor.getColumnIndex("time"));
        String postId = cursor.getString(cursor.getColumnIndex("postid"));
        Map map = new HashMap();
        long tt = Long.parseLong(time);
        Date date = new Date(tt);
        String t = date.toLocaleString();
        map.put("postid", postId);
        map.put("title", title);
        map.put("content", content);
        map.put("time", t);
        list.add(map);
    }


    /**
     * 初始化tab[0]的数据
     *
     * @param cursor 获取回帖数据
     * @return 回帖数量
     */
    private int getDateForhuitiePage(Cursor cursor) {
        list.clear();
        if (cursor.moveToFirst()) {
            getDateFromTable(cursor);
            while (cursor.moveToNext()) {
                getDateFromTable(cursor);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return list.size();
    }

    private void getDateFromTable(Cursor cursor) {
        Map<String, String> map = new HashMap<>();
        String table_name = cursor.getString(cursor.getColumnIndex("table_name"));//记录在哪张表
        String table_row_id = cursor.getString(cursor.getColumnIndex("table_row_id"));//记录在RecyclerView第几行
        Cursor cursor2 = db.query(table_name, null, "username = ? and _id = ?", new String[]{username, table_row_id}, null, null, null);
        map.put("table_row_id", table_row_id);
        if (cursor2.moveToFirst()) {
            getDateFromTable2(cursor2, map);
            while (cursor2.moveToNext()) {
                getDateFromTable2(cursor2, map);
            }
        }
        if (cursor2 != null) {
            cursor2.close();
        }
    }

    private void getDateFromTable2(Cursor cursor2, Map map) {
        String time = cursor2.getString(cursor2.getColumnIndex("time"));
        String postid = cursor2.getString(cursor2.getColumnIndex("postid"));
        Cursor cursor3 = db.query(MyDBHelper.POST_TABLE, new String[]{"title"}, "postid = ?", new String[]{postid}, null, null, null);
        cursor3.moveToFirst();
        String title = cursor3.getString(cursor3.getColumnIndex("title"));
        map.put("id", postid);
        map.put("title", title);

        long tt = Long.parseLong(time);
        Date date = new Date(tt);
        String t = date.toLocaleString();
        String content = cursor2.getString(cursor2.getColumnIndex("content"));
        String rUsername = cursor2.getString(cursor2.getColumnIndex("rUsername"));//引用的ID
        String rContent = cursor2.getString(cursor2.getColumnIndex("rContent"));//引用的内容
        if (!TextUtils.isEmpty(rUsername)) {
            map.put("rUsername", rUsername);
            map.put("rContent", rContent);
        }
        map.put("content", content);
        map.put("time", t);
        list.add(map);
        if (cursor3 != null) {
            cursor3.close();
        }
    }
}