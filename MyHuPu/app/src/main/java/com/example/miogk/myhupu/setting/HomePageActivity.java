package com.example.miogk.myhupu.setting;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.example.miogk.myhupu.R;
import com.example.miogk.myhupu.db.MyDBHelper;
import com.example.miogk.myhupu.setting.fragment.NewsCommentFragment;
import com.example.miogk.myhupu.setting.fragment.PostFragment;
import com.example.miogk.myhupu.setting.fragment.RecommendFragment;
import com.example.miogk.myhupu.setting.fragment.ReplyFragment;
import com.example.miogk.myhupu.utils.ConstansUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePageActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private final String[] tabsName = new String[]{"回帖", "新闻评论", "发帖", "推荐"};
    private String username;
    private SQLiteDatabase db;
    private ViewPager home_page_viewpager;
    private FragmentPagerAdapter fpa;
    private List<Map<String, String>> list = new ArrayList<>();
    private TextView lighten;

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
        String postId = cursor.getString(cursor.getColumnIndex("postid"));
        String title = cursor.getString(cursor.getColumnIndex("title"));
        String content = cursor.getString(cursor.getColumnIndex("content"));
        String time = cursor.getString(cursor.getColumnIndex("time"));
        String username = cursor.getString(cursor.getColumnIndex("username"));
        Map map = new HashMap();
        long tt = Long.parseLong(time);
        Date date = new Date(tt);
        String t = date.toLocaleString();
        map.put("username", username);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        initView();
    }

    private void initView() {
        if (db == null) {
            db = ConstansUtils.getSqliteDatabase(this);
        }
        lighten = (TextView) findViewById(R.id.home_page_lighten);
        Cursor c = db.query(MyDBHelper.USER_TABLE, new String[]{"lighten"}, "username = ?", new String[]{username}, null, null, null);
        c.moveToFirst();
        String lighten_number = c.getString(c.getColumnIndex("lighten"));
        if (TextUtils.isEmpty(lighten_number)) {
            lighten.setText("0");
        } else {
            lighten.setText(lighten_number);
        }
        c = null;
        Cursor cursor = db.query(MyDBHelper.USER_TABLE_TABLE, null, "username = ?", new String[]{username}, null, null, "time desc");
        int replyCount = getDateForhuitiePage(cursor);
        int postCount = db.query(MyDBHelper.POST_TABLE, null, "username = ?", new String[]{username}, null, null, null).getCount();
        tabLayout = (TabLayout) findViewById(R.id.home_page_tablayout);
        home_page_viewpager = (ViewPager) findViewById(R.id.home_page_viewpager);
        fpa = new HomePageFragmentAdapter(getSupportFragmentManager());
        tabLayout.setupWithViewPager(home_page_viewpager);
        home_page_viewpager.setAdapter(fpa);
        home_page_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Cursor cursor = null;
                switch (position) {
                    case 0:
                        cursor = db.query(MyDBHelper.USER_TABLE_TABLE, null, "username = ?", new String[]{username}, null, null, "time desc");
                        getDateForhuitiePage(cursor);
                        break;
                    case 1:
                        list.clear();
                        break;
                    case 2:
                        cursor = db.query(MyDBHelper.POST_TABLE, null, "username = ?", new String[]{username}, null, null, "time desc");
                        getDataFromSqliteDatabase(cursor);
                        break;
                    case 3:
                        list.clear();
                        break;
                }
                if (cursor != null) {
                    cursor.close();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        /**
         * tabLayout绑定了adapter之后就不需要加自己创建tab写标题了
         */
//        for (int i = 0; i < tabsName.length; i++) {
//            TabItem item = new TabItem(this);
//            tabLayout.addView(item);
//        }
        for (int i = 0; i < tabsName.length; i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i).setCustomView(R.layout.tab_item);
            TextView tabName = (TextView) tab.getCustomView().findViewById(R.id.tab_item_text);
            TextView tabNumber = (TextView) tab.getCustomView().findViewById(R.id.tab_item_number);
            if (i == 0) {
                tabNumber.setText(replyCount + "");
            }
            if (i == 2) {
                tabNumber.setText(postCount + "");
            }
            tabName.setText(tabsName[i]);
        }
    }


    class HomePageFragmentAdapter extends FragmentPagerAdapter {

        Fragment[] fragments = new Fragment[]{new ReplyFragment(list, username, home_page_viewpager, db, tabLayout), new NewsCommentFragment(list, username, home_page_viewpager, db, tabLayout), new PostFragment(list, username, home_page_viewpager, db, tabLayout), new RecommendFragment(list, username, home_page_viewpager, db, tabLayout)};

        public HomePageFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }


        @Override
        public int getCount() {
            return tabsName.length;
        }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            return tabsName[position];
//        }
    }
}