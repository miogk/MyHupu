package com.example.miogk.myhupu.radiobutton.forumraduibutton.plate.buttonActivities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.miogk.myhupu.R;
import com.example.miogk.myhupu.db.MyDBHelper;
import com.example.miogk.myhupu.radiobutton.forumraduibutton.plate.buttonActivities.fragment.HotFragment;
import com.example.miogk.myhupu.radiobutton.forumraduibutton.plate.buttonActivities.fragment.LastPostFragment;
import com.example.miogk.myhupu.radiobutton.forumraduibutton.plate.buttonActivities.fragment.LastReplyFragment;
import com.example.miogk.myhupu.radiobutton.forumraduibutton.plate.buttonActivities.fragment.NiceFragment;
import com.example.miogk.myhupu.utils.ConstansUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class ShihuhuActivity extends AppCompatActivity {
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private TextView title;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ImageButton back;
    private ImageButton post;
    private SQLiteDatabase db;
    private static final String[] CONTENT = new String[]{"最新回复", "最新发布", "热门", "精华"};
    private FragmentPagerAdapter fpa;
    private LinkedList<Map<String, String>> lists = new LinkedList<>();
    private String start_id = "0";
    private static final String REFRESH = "1";
    private static final String LOAD = "2";
    private static final String NORMAL = "0";


    class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        Fragment[] fragments = new Fragment[]{new LastReplyFragment(lists, viewPager, db), new LastPostFragment(lists, viewPager, db),
                new HotFragment(lists, viewPager, db), new NiceFragment(lists, viewPager, db)};

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = fragments[position];
            return fragment;
        }


        @Override
        public int getCount() {
            return CONTENT.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position];
        }
    }

    private void fillList(Cursor c) {
        String time = c.getString(c.getColumnIndex("time"));
//        String shijian = ConstansUtils.getTime(Long.parseLong(time));//计算发帖至今的时间
        String shijian = ConstansUtils.getPublishTime(time);//显示发帖时间
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
            db = ConstansUtils.getSqliteDatabase(this);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shihuhu);
        if (db == null) {
            db = ConstansUtils.getSqliteDatabase(this);
        }
        final String name = ConstansUtils.getString(this, ConstansUtils.MY_HUPU, ConstansUtils.USER_NAME, "");
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        post = (ImageButton) findViewById(R.id.shihuhu_post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ConstansUtils.getString(ShihuhuActivity.this, ConstansUtils.MY_HUPU, ConstansUtils.USER_NAME, "");
                if (TextUtils.isEmpty(username)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ShihuhuActivity.this);
                    builder.setMessage("请先登录再回帖");
                    builder.setTitle("请先登录");
                    builder.show();
                } else {
                    Intent intent = new Intent(ShihuhuActivity.this, PostActivity.class);
                    startActivity(intent);
                }
            }
        });
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = (TextView) findViewById(R.id.shihuhu_title);
        tabLayout = (TabLayout) findViewById(R.id.shihuhu_table_layout);
        viewPager = (ViewPager) findViewById(R.id.shihuhu_view_pager);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.shihuhu_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ConstansUtils.getString(ShihuhuActivity.this, ConstansUtils.MY_HUPU, ConstansUtils.USER_NAME, "");
                if (TextUtils.isEmpty(username)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ShihuhuActivity.this);
                    builder.setMessage("请先登录再回帖");
                    builder.setTitle("请先登录");
                    builder.show();
                } else {
                    Intent intent = new Intent(ShihuhuActivity.this, PostActivity.class);
                    startActivity(intent);
                }
            }
        });

        fpa = new MyFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fpa);
        back = (ImageButton) findViewById(R.id.shihuhu_back);
        initData();//初始化标题
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Cursor c = db.query(MyDBHelper.POST_TABLE, null, null, null, null, null, "lastreply desc", null);
        if (c.moveToFirst()) {
            getDataFromSqliteDatabase(NORMAL, c);
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

            @Override
            public void onPageSelected(int position) {
                if (db == null) {
                    db = ConstansUtils.getSqliteDatabase(ShihuhuActivity.this);
                }
                switch (position) {
                    case 0:
                        lists.clear();
                        Cursor c = db.query(MyDBHelper.POST_TABLE, null, null, null, null, null, "lastreply desc", null);
                        getDataFromSqliteDatabase(NORMAL, c);
                        break;
                    case 1:
                        lists.clear();
                        Cursor c2 = db.query(MyDBHelper.POST_TABLE, null, null, null, null, null, "postid desc", null);
                        getDataFromSqliteDatabase(NORMAL, c2);
                        break;
                    case 2:
                        lists.clear();
                        break;
                    case 3:
                        lists.clear();
                        break;
                    default:
                        break;
                    //"最新回复", "最新发布", "热门", "精华"
                }
            }
        });
        tabLayout.setupWithViewPager(viewPager);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//去除默认的title
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
//                    smartRefreshLayout.setEnabled(true);
                } else if (verticalOffset <= -240) {
                    int c = android.R.color.holo_blue_light;
                    collapsingToolbarLayout.setContentScrimColor(getResources().getColor(c));
                    title.setVisibility(View.VISIBLE);
                } else {
                    collapsingToolbarLayout.setContentScrim(null);
                    title.setVisibility(View.GONE);
//                    smartRefreshLayout.setEnabled(false);
                }
            }
        });

    }

    private void initData() {
        for (int i = 0; i < CONTENT.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(CONTENT[i]));
        }
    }


    /**
     * @return 36dp
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}