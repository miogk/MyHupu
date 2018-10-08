package com.example.miogk.myhupu.radiobutton.forumraduibutton.plate.buttonActivities.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.miogk.myhupu.R;
import com.example.miogk.myhupu.db.MyDBHelper;
import com.example.miogk.myhupu.radiobutton.forumraduibutton.plate.buttonActivities.ShihuhuContentActivity;
import com.example.miogk.myhupu.radiobutton.forumraduibutton.plate.buttonActivities.fragment.HotFragment;
import com.example.miogk.myhupu.radiobutton.forumraduibutton.plate.buttonActivities.fragment.LastPostFragment;
import com.example.miogk.myhupu.radiobutton.forumraduibutton.plate.buttonActivities.fragment.LastReplyFragment;
import com.example.miogk.myhupu.radiobutton.forumraduibutton.plate.buttonActivities.fragment.NiceFragment;
import com.example.miogk.myhupu.utils.ConstansUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/3.
 */

public class BaseShihuhuFragment extends Fragment {
    protected MyRecyclerViewAdapter adapter;
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;
    protected ViewPager viewPager;
    protected LinkedList<Map<String, String>> lists = new LinkedList<>();
    private String start_id = "0";
    protected static final String REFRESH = "1";
    private static final String LOAD = "2";
    private static final String NORMAL = "0";
    private boolean isVisible = false;
    private View view;
    private SQLiteDatabase db;
    private FloatingActionButton fab;
    private NestedScrollView nsv;
    private int offset = 0;

    public BaseShihuhuFragment(LinkedList<Map<String, String>> lists, ViewPager viewPager, SQLiteDatabase db) {
        this.viewPager = viewPager;
        this.lists = lists;
        this.db = db;
        Log.e("base", "BaseShihuhuFragment: ");
    }

    private void fillList(Cursor c) {
        String time = c.getString(c.getColumnIndex("time"));
        String shijian = ConstansUtils.getTime(Long.parseLong(time));//计算发帖至今的时间
        String postId = c.getString(c.getColumnIndex("postid"));
        String username = c.getString(c.getColumnIndex("username"));
        String title = c.getString(c.getColumnIndex("title"));
        String content = c.getString(c.getColumnIndex("content"));
        Map<String, String> map = new HashMap<>();
        map.put("id", postId);
        map.put("time", shijian);
        map.put("username", username);
        map.put("title", title);
        map.put("content", content);
        lists.add(map);
        start_id = postId;
    }

    public void getDataFromSqliteDatabase(String flag, Cursor c) {
        if (db == null) {
            db = ConstansUtils.getSqliteDatabase(getActivity());
        }
        if (c.moveToFirst()) {
            fillList(c);
            while (c.moveToNext()) {
                fillList(c);
            }
        }
        c.close();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible = true;
            /**
             之前是写view != null,但后来在写PostFragment的时候发现是主要是adapter == null为造成错误，所有暂改为adapter！= null，
             之后有更好的方法再修改
             */
            if (adapter != null) {
                Cursor c = null;
                Class<?> thisClass = this.getClass();
                if (thisClass.equals(LastPostFragment.class)) {
                    lists.clear();
                    c = db.query(MyDBHelper.POST_TABLE, null, null, null, null, null, "postid desc", null);
                    getDataFromSqliteDatabase(REFRESH, c);
                } else if (thisClass.equals(LastReplyFragment.class)) {
                    lists.clear();
                    c = db.query(MyDBHelper.POST_TABLE, null, null, null, null, null, "lastreply desc", null);
                    getDataFromSqliteDatabase(REFRESH, c);
                } else if (thisClass.equals(HotFragment.class)) {
                    lists.clear();
                } else if (thisClass.equals(NiceFragment.class)) {
                    lists.clear();
                }
                adapter.notifyDataSetChanged();
            }
        } else {
            isVisible = false;
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("oncreateview", "onCreateView: " + this + " -- " + isVisible);
        WindowManager manager = getActivity().getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        final int width = metrics.widthPixels;  //获得屏幕长度和宽度
        final int height = metrics.heightPixels;
        view = View.inflate(getActivity(), R.layout.shihuhu_layout, null);
//        fab = (FloatingActionButton) view.findViewById(R.id.shihuhu_fab);
//        fab.setX(width * 6 / 7);
//        fab.setY(height * 2 / 3);
        nsv = (NestedScrollView) view.findViewById(R.id.shihuhu_nsv);
//        AppBarLayout a = (AppBarLayout) getActivity().findViewById(R.id.appbarlayout);
//        a.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
////                fab.setX(width * 6 / 7);
////                fab.setY(height * 2 / 3 - verticalOffset);
////                offset = verticalOffset;
////                Log.e("pixels", "onOffsetChanged: " + width + "  ----  " + height);
////                fab.setX(width / 2);
////                fab.setY(height / 2);
//            }
//        });
//        nsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
////                fab.setX(scrollX + (width * 6 / 7));
////                fab.setY(scrollY + (height * 2 / 3 - offset));
////                fab.setX(width / 2);
////                fab.setY(height / 2);
//            }
//        });
////        fab.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                String username = ConstansUtils.getString(getActivity(), ConstansUtils.MY_HUPU, ConstansUtils.USER_NAME, "");
////                if (TextUtils.isEmpty(username)) {
////                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
////                    builder.setMessage("请先登录再回帖");
////                    builder.setTitle("请先登录");
////                    builder.show();
////                } else {
////                    Intent intent = new Intent(getActivity(), PostActivity.class);
////                    startActivity(intent);
////                }
////            }
////        });
        recyclerView = (RecyclerView) view.findViewById(R.id.shihuhu_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MyRecyclerViewAdapter(lists);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        smartRefreshLayout = (SmartRefreshLayout) view.findViewById(R.id.swipe_layout);
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                updateShuju();
                refreshLayout.finishRefresh(true);
            }
        });
        smartRefreshLayout.setEnableLoadMore(false);
        return view;
    }


    public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
        private List<Map<String, String>> lists;

        public MyRecyclerViewAdapter(List list) {
            this.lists = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.shihuhu_layout2, null, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final Map<String, String> map = lists.get(position);
            holder.time.setText(map.get("time"));
            holder.username.setText(map.get("username"));
            final String title = map.get("title");
            final String id = map.get("id");
            holder.title.setText(title);
            //长按响应事件，用来删除所选的帖子
            holder.v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("是否删除?title");
                    builder.setMessage("是否删除?message");
                    builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ConstansUtils.toast(getActivity(), "yes");
                            //如果有相关联的回复贴也一同删除
                            String tableName = ConstansUtils.TABLE_PREFIX + id;
                            //判断是否有回复贴
                            db.execSQL("drop table if exists " + tableName);
                            db.delete(MyDBHelper.USER_TABLE_TABLE, "table_name = ?", new String[]{tableName});
                            db.delete(MyDBHelper.POST_TABLE, "postid = ?", new String[]{id});
//                            更新数据
                            updateShuju();
                        }
                    });
                    builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.setNeutralButton("不知道", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                    return true;
                }
            });
            holder.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("view", "onClick: " + v + " --- " + v.getMeasuredWidth());
                    Intent intent = new Intent(getActivity(), ShihuhuContentActivity.class);
                    intent.putExtra("title", title);
                    intent.putExtra("id", id);
                    intent.putExtra("username", map.get("username"));
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return lists.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView time;
            TextView username;
            TextView title;
            View v;

            public ViewHolder(View itemView) {
                super(itemView);
                v = itemView;
                time = (TextView) itemView.findViewById(R.id.shihuhu2_time);
                username = (TextView) itemView.findViewById(R.id.shihuhu2_username);
                title = (TextView) itemView.findViewById(R.id.shihuhu2_title);
            }
        }
    }

    /**
     * 提取出来的更新数据的方法，先判断所处的页数，然后根据页数决定排序的方法，然后重新获取数据完成更新.
     */
    private void updateShuju() {
        lists.clear();
        Cursor c = null;
        int position = viewPager.getCurrentItem();
        if (position == 0) {
            c = db.query(MyDBHelper.POST_TABLE, null, null, null, null, null, "lastreply desc", null);
        } else if (position == 1) {
            c = db.query(MyDBHelper.POST_TABLE, null, null, null, null, null, "postid desc", null);
        }
        getDataFromSqliteDatabase(REFRESH, c);
        adapter.notifyDataSetChanged();
    }
}