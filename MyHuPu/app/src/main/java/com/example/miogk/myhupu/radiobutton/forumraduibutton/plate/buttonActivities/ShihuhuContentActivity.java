package com.example.miogk.myhupu.radiobutton.forumraduibutton.plate.buttonActivities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.example.miogk.myhupu.R;
import com.example.miogk.myhupu.db.MyDBHelper;
import com.example.miogk.myhupu.setting.HomePageActivity;
import com.example.miogk.myhupu.utils.ConstansUtils;
import com.scwang.smartrefresh.header.material.CircleImageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.sharesdk.onekeyshare.OnekeyShare;

public class ShihuhuContentActivity extends AppCompatActivity implements View.OnClickListener {
    private String imageUrl = "https://raw.githubusercontent.com/miogk/miogk.github.io/master/10007/20180530/nba/1/1.jpg";
    private String imageUrl2 = "https://github.com/miogk/miogk.github.io/blob/master/10007/20180530/nba/1/1.jpg";
    private String imageUrl3 = "https://i1.hoopchina.com.cn/user/233/86233/1369827900_036961big.jpg";
    private String imageUrl4 = "https://i10.hoopchina.com.cn/hupuapp/bbs/45/29395045/thread_29395045_20180706091942_s_119338_w705_h1024_52205.jpg?x-oss-process=image/resize,w_800/format,webp";
    private SQLiteDatabase database;
    private ListView reply_content;
    private SmartRefreshLayout refresh;
    private ImageButton reply_add;
    private Cursor reply_cursor;
    private MyAdapter adapter;
    private String id;
    private boolean flag = false;
    private TextView wheel_view_start_page;
    private TextView wheel_view_end_page;
    private TextView previousPage;
    private TextView nextPage;
    private BottomSheetDialog bsd;
    private TextView title;
    private TextView content;
    private TextView reference;
    private View view;
    private WheelView wheelView;
    private View totalPage;
    private TextView startPage;
    private TextView endPage;
    private List<String> list = new ArrayList<>();
    private Button ok;
    private Button wheel_first_page;
    private Button wheel_last_page;
    private int currentIndex = 0;
    private int pages = 1;
    private int reply_cursorCount;//总的回复数
    private int load_more_count;//加载显示的回复数
    private int currentSelectedPage = 1;
    private int currentId;
    private String tableName;
    private String t;
    private ScrollView sv;
    private String table_row_id;
    private int loadNumber = MyDBHelper.PAGE_SIZE;
    private boolean loadMoreMode = false;
    private boolean firstInLoadMode = true;
    private ImageButton toComment;
    private boolean isFirstComment = false;
    private ImageButton shareSdk;//ShareSdk分享按钮
    private LinearLayout aboveReplyContent;//在ListView上面的所有内容
    private View head;
    private String username;
    private TextView user_name;

    class MyBsd extends BottomSheetDialog {

        public MyBsd(@NonNull Context context) {
            super(context);
        }

        @Override
        public void onBackPressed() {
            super.onBackPressed();
            this.cancel();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shihuhu_content_activity);
        findViewById(R.id.shihuhu_content_activity_back).setOnClickListener(this);
        Intent intent = getIntent();
        t = intent.getStringExtra("title");
        id = intent.getStringExtra("id");
        username = intent.getStringExtra("username");
        table_row_id = intent.getStringExtra("table_row_id");
        tableName = ConstansUtils.TABLE_PREFIX + id;
        initViews();
        if (!TextUtils.isEmpty(t)) {
            title.setText(t);
        }
        createTable(); //按照ConstansUtils.TABLE_PREFIX + postId的格式创建帖子的数据表
        Cursor cursor = database.query(MyDBHelper.POST_TABLE, new String[]{"content"}, "postid = ?", new String[]{id}, null, null, null);
        if (cursor.moveToFirst()) {
            String c = cursor.getString(cursor.getColumnIndex("content"));
            if (TextUtils.isEmpty(t)) {
                t = cursor.getString(cursor.getColumnIndex("title"));
                title.setText(t);
            }
            CharSequence cs = analyzeImage(c.trim());
            content.setText(cs);
        }
        reply_cursorCount = database.query(tableName, null, "postid = ?", new String[]{id}, null, null, null, null).getCount();
        /*ContentValues values = new ContentValues();
        for (int i = 0; i < reply_cursorCount; i++) {
            int number = i + 1;
            values.put("number", number);
            database.update(MyDBHelper.REPLY_TABLE, values, "postid = ?", new String[]{id});
            values.clear();
        }*/
//            int currentId = (currentPage - 1) * Integer.parseInt(MyDBHelper.PAGE_SIZE);
//            reply_cursor = database.query(MyDBHelper.REPLY_TABLE, null, "postid = ? and _id > ?", new String[]{id, currentId + ""}, null, null, null, MyDBHelper.PAGE_SIZE);

        if (reply_cursorCount > 0) {
            pages = reply_cursorCount % MyDBHelper.PAGE_SIZE == 0 ? reply_cursorCount / MyDBHelper.PAGE_SIZE :
                    reply_cursorCount / MyDBHelper.PAGE_SIZE + 1;
            currentId = (currentSelectedPage - 1) * MyDBHelper.PAGE_SIZE;
            reply_cursor = database.query(tableName, null, "postid = ? and _id > ?", new String[]{id, currentId + ""}, null, null, "time");
            startPage.setText("1");
            list.clear();
            for (int i = 0; i < pages; i++) {
                list.add(i + 1 + "");
            }
            if (pages > 1) {
                changeTextColor(nextPage, "");
                changeTextColor(wheel_last_page, "");
            }
            endPage.setText(pages + "");
            wheel_view_start_page.setText("1");
            wheel_view_end_page.setText(pages + "");
            flag = false;
            adapter = new MyAdapter(this, reply_cursor, false);
            reply_content.setAdapter(adapter);
//            setListViewHeightBasedOnChildren(reply_content);
//            reply_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    View v = reply_content.getChildAt(position);
//                    int baseY = (int) reply_content.getY();
//                    int x = (int) v.getX();
//                    int y = (int) v.getY() + baseY;
//                    sv.smoothScrollTo(x, y);
//                }
//            });

            //如果是从主页回帖处点击进来的，就移动到该回复的楼层.
            if (!TextUtils.isEmpty(table_row_id)) {
                reply_content.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("table_row_id", "onCreate: " + table_row_id + " --- " + reply_content.getLastVisiblePosition());
                        final int position = Integer.parseInt(table_row_id);
                        View v = reply_content.getChildAt(position - 1);
                        if (v != null) {
                            int baseY = (int) reply_content.getY();
                            int y = (int) v.getY() + baseY;
                            sv.smoothScrollTo(0, y);
                        }
                    }
                });

            }
        }
    }

    //创建每张回复表
    private void createTable() {
        if (database == null) {
            database = ConstansUtils.getSqliteDatabase(this);
        }
        String sql = "create table if not exists " + tableName + " (_id integer primary key autoincrement, " +
                "postid text, username text, time text, content text, rUsername text, rContent text, lighten text, _rId text)";
        database.execSQL(sql);
        Log.e("tablename", "createTable: " + tableName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                initialLoadMoreState();
                if (resultCode == RESULT_OK) {
                    Intent d = data;
                    flag = d.getBooleanExtra("flag", false);
                    if (flag) {
                        reply_cursorCount = database.query(tableName, null, "postid = ?", new String[]{id}, null, null, null, null).getCount();
                        if (reply_cursorCount > 0) {
                            pages = reply_cursorCount % MyDBHelper.PAGE_SIZE == 0 ? reply_cursorCount / MyDBHelper.PAGE_SIZE :
                                    reply_cursorCount / MyDBHelper.PAGE_SIZE + 1;
                        }
                        if (pages > 1) {
                            changeTextColor(previousPage, "");
                            changeTextColor(wheel_first_page, "");
                        }
                        list.clear();
                        for (int i = 0; i < pages; i++) {
                            list.add(i + 1 + "");
                        }
                        wheelView.setAdapter(new ArrayWheelAdapter(list));
                        currentIndex = pages - 1;
                        wheelView.setCurrentItem(currentIndex);
                        startPage.setText(list.size() + "");
                        endPage.setText(list.size() + "");
                        wheel_view_start_page.setText(list.size() + "");
                        wheel_view_end_page.setText(list.size() + "");
                        currentId = currentIndex * MyDBHelper.PAGE_SIZE;
                        reply_cursor = database.query(tableName, null, "postid = ? and _id > ?", new String[]{id, currentId + ""}, null, null, "time", MyDBHelper.PAGE_SIZE + "");
                        if (adapter == null) {
                            reply_content = (ListView) findViewById(R.id.shihuhu_content_activity_reply_content);
                            adapter = new MyAdapter(this, reply_cursor, false);
                            reply_content.setAdapter(adapter);
                        } else {
                            adapter.changeCursor(reply_cursor);
                        }
                    }
                }
                break;
            case 2:
                break;
            default:
                break;
        }
    }

    private void initViews() {
        bsd = new MyBsd(this);
        head = findViewById(R.id.shihuhu_content_activity_head);
        user_name = (TextView) findViewById(R.id.shihuhu_content_activity_username);
        user_name.setText(username);
        aboveReplyContent = (LinearLayout) findViewById(R.id.shihuhu_content_above_relpy_content);
        sv = (ScrollView) findViewById(R.id.shihuhu_content_activity_sv);
        shareSdk = (ImageButton) findViewById(R.id.shihuhu_content_shareSdk);
        toComment = (ImageButton) findViewById(R.id.shihuhu_content_activity_to_comment);
        reply_content = (ListView) findViewById(R.id.shihuhu_content_activity_reply_content);
        refresh = (SmartRefreshLayout) findViewById(R.id.shihuhu_content_activity_refresh);
        title = (TextView) findViewById(R.id.shihuhu_content_activity_title);
        startPage = (TextView) findViewById(R.id.shihuhu_content_activity_start_page);
        endPage = (TextView) findViewById(R.id.shihuhu_content_activity_end_page);
        content = (TextView) findViewById(R.id.shihuhu_content_activity_content);
        reply_add = (ImageButton) findViewById(R.id.shihuhu_content_activity_reply_add);
        previousPage = (TextView) findViewById(R.id.shihuhu_content_activity_previous_page);
        nextPage = (TextView) findViewById(R.id.shihuhu_content_activity_next_page);
        totalPage = findViewById(R.id.shihuhu_content_activity_total_page);
        previousPage.setOnClickListener(this);
        nextPage.setOnClickListener(this);
        totalPage.setOnClickListener(this);
        toComment.setOnClickListener(this);
        shareSdk.setOnClickListener(this);
        head.setOnClickListener(this);
        user_name.setOnClickListener(this);

        initWheelView();
        bsd.setCancelable(false);
        bsd.setContentView(view);
        reply_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ConstansUtils.getString(ShihuhuContentActivity.this, ConstansUtils.MY_HUPU, ConstansUtils.USER_NAME, "");
                if (TextUtils.isEmpty(username)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ShihuhuContentActivity.this);
                    builder.setMessage("请先登录再回帖");
                    builder.setTitle("请先登录");
                    builder.show();
                } else {
                    Intent intent = new Intent(ShihuhuContentActivity.this, ReplyAddActivity.class);
                    intent.putExtra("title", t);
                    intent.putExtra("postid", id);
                    intent.putExtra("username", username);
                    startActivityForResult(intent, 1);
                }
            }
        });
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadMoreMode = true;//进入加载模式
                //记录进入加载模式时的页面数字
                if (firstInLoadMode) {
                    currentIndex = wheelView.getCurrentItem();
                    currentId = currentIndex * MyDBHelper.PAGE_SIZE;
                }
//                currentSelectedPage = Integer.parseInt(list.get(currentIndex));
                loadNumber = loadNumber + MyDBHelper.PAGE_SIZE;
                reply_cursor = database.query(tableName, null, "postid = ? and _id > ?", new String[]{id, currentId + ""}, null, null, "time", loadNumber + "");
                load_more_count = reply_cursor.getCount();
                if (loadNumber > reply_cursorCount) {
                    loadNumber = loadNumber - MyDBHelper.PAGE_SIZE;
                }
                if (currentIndex < list.size() - 1) {
                    currentIndex = currentIndex + 1;
                    wheelView.setCurrentItem(currentIndex);
                    currentSelectedPage = Integer.parseInt(list.get(currentIndex));
                    //如果翻过第一页，更改按钮颜色
                    if (currentSelectedPage > 1) {
                        changeTextColor(wheel_first_page, "");
                        changeTextColor(previousPage, "");
                    }
                    startPage.setText(currentSelectedPage + "");
                    wheel_view_start_page.setText(currentSelectedPage + "");
                    adapter.changeCursor(reply_cursor);
                }
                //如果是最后一页，更改按钮颜色
                if (currentIndex == (list.size() - 1)) {
                    changeTextColor(wheel_last_page, "end");
                    changeTextColor(nextPage, "end");
                }
//                wheelView.setCurrentItem(currentIndex + 1);
                firstInLoadMode = false;
                refreshLayout.finishLoadMore(true);
            }
        });
    }

    private void initWheelView() {
        view = LayoutInflater.from(ShihuhuContentActivity.this).inflate(R.layout.wheel_view, null);
        wheelView = (WheelView) view.findViewById(R.id.wheelview);
        wheel_view_start_page = (TextView) view.findViewById(R.id.wheel_view_start_page);
        wheel_view_end_page = (TextView) view.findViewById(R.id.wheel_view_end_page);
        wheel_first_page = (Button) view.findViewById(R.id.wheel_view_first_page);
        wheel_last_page = (Button) view.findViewById(R.id.wheel_view_last_page);
        wheel_first_page.setOnClickListener(this);
        wheel_last_page.setOnClickListener(this);
        ok = (Button) view.findViewById(R.id.wheel_view_ok);
        ok.setOnClickListener(this);
        view.findViewById(R.id.wheel_view_cancel).setOnClickListener(this);
        list.add("1");
        wheelView.setAdapter(new ArrayWheelAdapter(list));
        wheelView.setCyclic(false);
        wheelView.setGravity(Gravity.CENTER);
        wheelView.setLineSpacingMultiplier(2.0f);
        wheelView.setCurrentItem(currentIndex);
        wheelView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (pages == 1) {
                    initPages();
                    return;
                }
                if (index == 0) {
                    changeTextColor(wheel_first_page, "start");
                    changeTextColor(previousPage, "start");
                    changeTextColor(wheel_last_page, "");
                    changeTextColor(nextPage, "");
                } else if (index == list.size() - 1) {
                    changeTextColor(wheel_last_page, "end");
                    changeTextColor(nextPage, "end");
                    changeTextColor(wheel_first_page, "");
                    changeTextColor(previousPage, "");
                } else {
                    changeTextColor(wheel_first_page, "");
                    changeTextColor(wheel_last_page, "");
                    changeTextColor(previousPage, "");
                    changeTextColor(nextPage, "");
                }
            }
        });
    }

    private void initPages() {
        changeTextColor(wheel_first_page, "start");
        changeTextColor(previousPage, "start");
        changeTextColor(wheel_last_page, "end");
        changeTextColor(nextPage, "end");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wheel_view_ok:
                initialLoadMoreState();
                currentIndex = wheelView.getCurrentItem();
                currentSelectedPage = Integer.parseInt(list.get(currentIndex));
                startPage.setText(currentSelectedPage + "");
                wheel_view_start_page.setText(currentSelectedPage + "");
                wheel_view_end_page.setText(pages + "");
                currentId = (currentSelectedPage - 1) * MyDBHelper.PAGE_SIZE;
                reply_cursor = database.query(tableName, null, "postid = ? and _id > ?", new String[]{id, String.valueOf(currentId)}, null, null, "time", String.valueOf(MyDBHelper.PAGE_SIZE));
                reply_cursorCount = reply_cursor.getCount();
                if (reply_cursor.moveToFirst()) {
                    adapter.changeCursor(reply_cursor);
                }
                bsd.cancel();
                break;
            case R.id.wheel_view_cancel:
                initialLoadMoreState();
                if (pages == 1) {
                    initPages();
                    bsd.cancel();
                    break;
                }
                if (currentIndex == 0) {
                    changeTextColor(wheel_first_page, "start");
                    changeTextColor(previousPage, "start");
                    changeTextColor(wheel_last_page, "");
                    changeTextColor(nextPage, "");
                } else if (currentIndex == list.size() - 1) {
                    changeTextColor(wheel_first_page, "");
                    changeTextColor(previousPage, "");
                    changeTextColor(wheel_last_page, "end");
                    changeTextColor(nextPage, "end");
                } else {
                    changeTextColor(wheel_first_page, "");
                    changeTextColor(previousPage, "");
                    changeTextColor(wheel_last_page, "");
                    changeTextColor(nextPage, "");
                }
                if (bsd != null) {
                    bsd.cancel();
                }
                break;
            case R.id.shihuhu_content_activity_total_page:
                if (bsd != null) {
                    wheelView.setCurrentItem(currentIndex);
                    bsd.show();
                }
                break;
            case R.id.shihuhu_content_activity_back:
                finish();
                break;
            case R.id.shihuhu_content_activity_previous_page:
                initialLoadMoreState();
                if (pages == 1) {
                    break;
                }
                changeTextColor(nextPage, "");
                changeTextColor(wheel_last_page, "");
                currentIndex = wheelView.getCurrentItem();
                currentSelectedPage = Integer.parseInt(list.get(currentIndex));
                if (currentSelectedPage > 1) {
                    currentIndex = currentIndex - 1;
                    wheelView.setCurrentItem(currentIndex);
                    startPage.setText(currentSelectedPage - 1 + "");
                    wheel_view_start_page.setText(currentSelectedPage - 1 + "");
                    currentId = (currentSelectedPage - 2) * MyDBHelper.PAGE_SIZE;
                    reply_cursor = database.query(tableName, null, "postid = ? and _id > ?", new String[]{id, currentId + ""}, null, null, "time", MyDBHelper.PAGE_SIZE + "");
                    adapter.changeCursor(reply_cursor);
                    if (currentSelectedPage == 2) {
                        changeTextColor(previousPage, "start");
                        changeTextColor(wheel_first_page, "start");
                        aboveReplyContent.setVisibility(View.VISIBLE);
                    }
                    if (currentSelectedPage > 2) {
                        aboveReplyContent.setVisibility(View.GONE);
                    }
                }
                break;
            case R.id.shihuhu_content_activity_next_page:
                initialLoadMoreState();
                if (pages == 1) {
                    break;
                }
                changeTextColor(previousPage, "");
                changeTextColor(wheel_first_page, "");
                currentIndex = wheelView.getCurrentItem();
                currentSelectedPage = Integer.parseInt(list.get(currentIndex));
                if (currentSelectedPage < list.size()) {
                    aboveReplyContent.setVisibility(View.GONE);
                    currentIndex = currentIndex + 1;
                    wheelView.setCurrentItem(currentIndex);
                    startPage.setText(currentSelectedPage + 1 + "");
                    wheel_view_start_page.setText(currentSelectedPage + 1 + "");
                    currentId = currentSelectedPage * MyDBHelper.PAGE_SIZE;
                    reply_cursor = database.query(tableName, null, "postid = ? and _id > ?", new String[]{id, currentId + ""}, null, null, "time", MyDBHelper.PAGE_SIZE + "");
                    adapter.changeCursor(reply_cursor);
                    if (currentSelectedPage == (list.size() - 1)) {
                        changeTextColor(nextPage, "end");
                        changeTextColor(wheel_last_page, "end");
                    }
                }
                break;
            case R.id.wheel_view_first_page:
                initialLoadMoreState();
                if (pages == 1) {
                    break;
                }
                currentIndex = 0;
                currentSelectedPage = Integer.parseInt(list.get(currentIndex));
                wheelView.setCurrentItem(currentIndex);
                startPage.setText("1");
                wheel_view_start_page.setText("1");
                changeTextColor(wheel_first_page, "start");
                changeTextColor(previousPage, "start");
                changeTextColor(wheel_last_page, "");
                changeTextColor(nextPage, "");
                currentId = (currentSelectedPage - 1) * MyDBHelper.PAGE_SIZE;
                reply_cursor = database.query(tableName, null, "postid = ? and _id > ?", new String[]{id, String.valueOf(currentId)}, null, null, "time", String.valueOf(MyDBHelper.PAGE_SIZE));
                adapter.changeCursor(reply_cursor);
                bsd.cancel();
                break;
            case R.id.wheel_view_last_page:
                initialLoadMoreState();
                if (pages == 1) {
                    break;
                }
                currentIndex = list.size() - 1;
                currentSelectedPage = Integer.parseInt(list.get(currentIndex));
                wheelView.setCurrentItem(currentIndex);
                startPage.setText(list.size() + "");
                wheel_view_start_page.setText(list.size() + "");
                changeTextColor(wheel_first_page, "");
                changeTextColor(previousPage, "");
                changeTextColor(wheel_last_page, "end");
                changeTextColor(nextPage, "end");
                currentId = (currentSelectedPage - 1) * MyDBHelper.PAGE_SIZE;
                reply_cursor = database.query(tableName, null, "postid = ? and _id > ?", new String[]{id, String.valueOf(currentId)}, null, null, "time", String.valueOf(MyDBHelper.PAGE_SIZE));
                adapter.changeCursor(reply_cursor);
                bsd.cancel();
                break;
            case R.id.shihuhu_content_activity_to_comment:
                View vv = reply_content.getChildAt(0);
                //没有回帖直接退出判断
                if (vv != null) {
                    //定位到第一楼
                    if (!isFirstComment) {
                        int y = (int) vv.getY();
                        int baseY = (int) reply_content.getY();
                        int fY = y + baseY;
                        sv.smoothScrollTo(0, fY);
                        isFirstComment = true;
                        //已经在第一楼的话再按一次回头顶部
                    } else {
                        if (aboveReplyContent.getVisibility() == View.VISIBLE) {
                            sv.smoothScrollTo(0, 0);
                        }
                        isFirstComment = false;
                    }
                } else {
                    break;
                }
                break;
            case R.id.shihuhu_content_shareSdk:
                OnekeyShare oks = new OnekeyShare();
                //关闭sso授权
//                oks.disableSSOWhenAuthorize();
                // title标题，微信、QQ和QQ空间等平台使用
                oks.setTitle("分享这条网址......");
                // titleUrl QQ和QQ空间跳转链接
                oks.setTitleUrl("http://sharesdk.cn");
                // text是分享文本，所有平台都需要这个字段
                oks.setText(title.getText().toString());
                // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                oks.setImagePath("/sdcard/Pictures/hou.jpg");//确保SDcard下面存在此张图片
                // url在微信、微博，Facebook等平台中使用
                oks.setUrl("http://sharesdk.cn");
                // comment是我对这条分享的评论，仅在人人网使用
                oks.setComment("我是测试评论文本");
                // 启动分享GUI
                oks.show(this);
                break;
            //下面两个点击到同一个HomePageActivity.
            case R.id.shihuhu_content_activity_username:
            case R.id.shihuhu_content_activity_head:
                Intent intent = new Intent(this, HomePageActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    //初始化加载更多内容时的状态

    private void initialLoadMoreState() {
        loadMoreMode = false;
        firstInLoadMode = true;
        loadNumber = MyDBHelper.PAGE_SIZE;
    }

    //改变页数改变时,按钮的颜色的变化
    private void changeTextColor(TextView textView, String state) {
        if (state.equals("end") || state.equals("start")) {
            textView.setTextColor(getResources().getColor(R.color.deep_dark));
        } else {
            textView.setTextColor(getResources().getColor(R.color.blue));
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        //获得adapter
        MyAdapter adapter = (MyAdapter) listView.getAdapter();
        if (adapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            //计算总高度
            totalHeight += listItem.getMeasuredHeight();

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        //计算分割线高度
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
//        params.height = totalHeight + 0;
        //给listview设置高度
        listView.setLayoutParams(params);
    }

    private class MyAdapter extends CursorAdapter {

        public MyAdapter(Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
        }

        @Override
        public int getCount() {
            //判断是否进入加载模式
            if (loadMoreMode) {
                return load_more_count;
                //不是加载模式的话就正常翻页
            } else {
                int yu = reply_cursorCount % MyDBHelper.PAGE_SIZE;
                if (wheelView.getCurrentItem() == (pages - 1) && yu != 0) {
                    return yu;
                } else {
                    return MyDBHelper.PAGE_SIZE;
                }
            }
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = View.inflate(ShihuhuContentActivity.this, R.layout.reply_content_layout, null);
            return view;
        }

        @Override
        public void bindView(View view, Context context, final Cursor cursor) {
            TextView t_username = (TextView) view.findViewById(R.id.reply_content_layout_username);
            TextView t_time = (TextView) view.findViewById(R.id.reply_content_layout_time);
            TextView t_content = (TextView) view.findViewById(R.id.reply_content_layout_content);
            TextView t_number = (TextView) view.findViewById(R.id.reply_content_layout_number);
            TextView reference = (TextView) view.findViewById(R.id.reply_content_layout_reference);
            TextView all_reply = (TextView) view.findViewById(R.id.reply_content_all_reply);
            TextView all_reply_number = (TextView) view.findViewById(R.id.reply_content_reply_number);
            LinearLayout reply_content_all_reply_layout = (LinearLayout) view.findViewById(R.id.reply_content_all_reply_layout);

            final TextView t_lighten = (TextView) view.findViewById(R.id.reply_content_lighten);
            final TextView t_lighten_number = (TextView) view.findViewById(R.id.reply_content_lighten_number);

            String referenceContent = cursor.getString(cursor.getColumnIndex("rContent"));
            String lighten = cursor.getString(cursor.getColumnIndex("lighten"));
            final String referenceUserName = cursor.getString(cursor.getColumnIndex("rUsername"));
            final String username = cursor.getString(cursor.getColumnIndex("username"));
            final String number = cursor.getString(cursor.getColumnIndex("_id"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            final String content = cursor.getString(cursor.getColumnIndex("content"));
            int allReplyNumber = database.query(tableName, null, "rContent = ? and _rId = ?", new String[]{content, number}, null, null, null).getCount();
            if (allReplyNumber == 0) {
                reply_content_all_reply_layout.setVisibility(View.GONE);
            } else {
                all_reply_number.setText(allReplyNumber + "");
                reply_content_all_reply_layout.setVisibility(View.VISIBLE);
            }
            //如果没有赋过值，默认为0.
            if (TextUtils.isEmpty(lighten)) {
                lighten = "0";
            }

            if (!TextUtils.isEmpty(referenceContent)) {
                view.findViewById(R.id.reply_content_layout_reference_content).setVisibility(View.VISIBLE);
                TextView rContent = (TextView) view.findViewById(R.id.reply_content_layout_reference_content_content);
                TextView rUser = (TextView) view.findViewById(R.id.reply_content_layout_reference_content_username);
                rContent.setText(referenceContent);
                rUser.setText(referenceUserName);
                rUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShihuhuContentActivity.this, HomePageActivity.class);
                        Bundle b = new Bundle();
                        b.putString("username", referenceUserName);
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                });
            } else {
                view.findViewById(R.id.reply_content_layout_reference_content).setVisibility(View.GONE);
            }

            t_lighten_number.setText(lighten);
            t_username.setText(username);
//            t_time.setText(ConstansUtils.getTime(Long.parseLong(time)));
            t_time.setText(ConstansUtils.getPublishTime(time));
            t_content.setText(content);
            t_number.setText("#" + number);

            t_lighten.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int plus = Integer.parseInt(t_lighten_number.getText().toString()) + 1;
                    t_lighten_number.setText(plus + "");
                    ContentValues values = new ContentValues();
                    values.put("lighten", plus);
                    database.update(tableName, values, "_id = ?", new String[]{number});
                    values.clear();
                    Cursor c = database.query(MyDBHelper.USER_TABLE, new String[]{"lighten"}, "username = ?", new String[]{username}, null, null, null);
                    c.moveToFirst();
                    String lighten = c.getString(c.getColumnIndex("lighten"));
                    if (TextUtils.isEmpty(lighten)) {
                        lighten = "0";
                    }
                    int count = Integer.parseInt(lighten);
                    count = count + 1;
                    values.put("lighten", count);
                    database.update(MyDBHelper.USER_TABLE, values, "username = ?", new String[]{username});
                }
            });

            reference.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ShihuhuContentActivity.this, ReplyAddActivity.class);
                    Bundle b = new Bundle();
                    b.putString("username", username);
                    b.putString("content", content);
                    b.putString("postid", id);
                    b.putString("_id", number);
                    b.putBoolean("reference", true);
                    intent.putExtras(b);
                    startActivityForResult(intent, 1);
                }
            });

            all_reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ShihuhuContentActivity.this, AllReplyActivity.class);
                    intent.putExtra("tableName", tableName);
                    intent.putExtra("username", username);
                    intent.putExtra("content", content);
                    intent.putExtra("_id", number);
                    startActivity(intent);
                }
            });
        }
    }

    private void resizeBitmap(Bitmap bitmap) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screen_width = dm.widthPixels;
        int screen_height = dm.heightPixels;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scaleWidth = screen_width / width;
        float scaleHeight = screen_height / height;

        float scale = scaleWidth > scaleHeight ? scaleWidth : scaleHeight;
        Bitmap resizeBmp;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        int xStart = (int) (width - width / scale) / 2;
        Log.v("franco-xStart", "xStart = " + xStart); /* * Bitmap source：要从中截图的原始位图 * int x:起始x坐标 * int y：起始y坐标 * int width：要截的图的宽度 * int height：要截的图的宽度 * x+width must be <= bitmap.width()不然会报错 * 原理是先截图再缩放，而不是先缩放再截图！！ */
        resizeBmp = Bitmap.createBitmap(bitmap, xStart, 0, (int) (width / scale), height, matrix, true);
        matrix.postScale(scale, scale);
        int yStart = (int) (scaleHeight - scaleHeight / scale) / 2;
        Log.v("franco-yStart", "yStart = " + yStart);
        resizeBmp = Bitmap.createBitmap(bitmap, 0, yStart, width, (int) (height / scale), matrix, true);
        //Bitmap 转化为 Drawable BitmapDrawable drawable = new BitmapDrawable(getResources(), resizeBmp); //drawable.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT); lrcBackGround.setBackgroundDrawable(drawable); lrcBackGround.getBackground().setAlpha(150);//将背景该图片透明度设低，避免歌词显示效果不好,数值越大透明度越高

    }

    //+_+_+_+_+_+_+_+_+_+_+_+_+定义一个解析图片的方法+_+_+_+_+_+_+_+_
    public CharSequence analyzeImage(String content) {
        SpannableString span_str = new SpannableString(content);
        String download = Environment.getExternalStoragePublicDirectory("myImage").getAbsolutePath();
//        Pattern p = Pattern.compile("/sdcard/myImage/[0-9]{13}+.jpg");
        Pattern p = Pattern.compile(download + "/[0-9]{13}+.jpg");
        Matcher m = p.matcher(content);
        while (m.find()) {
            String mypath = m.group();
            Toast.makeText(this, m.group(), Toast.LENGTH_SHORT);
            Bitmap bitmap = BitmapFactory.decodeFile(mypath);


            //x + width must be <= bitmap.width()
//            Bitmap rbitmap=resizeimg.resizeImage(bitmap, 300, 300);
//            ImageSpan span = new ImageSpan(this, bitmap);
//            span_str.setSpan(new MyImageSpan(this, bitmap, -10000), m.start(), m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            span_str.setSpan(new MyImageSpan(this, bitmap, ImageSpan.ALIGN_BASELINE), m.start(), m.end(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return span_str;
    }

    class MyImageSpan extends ImageSpan {

        public MyImageSpan(Context context, Bitmap b, int verticalAlignment) {
            super(context, b, verticalAlignment);
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
            try {
                Drawable b = getDrawable();
                canvas.save();
                int transY = 0;
                transY = ((bottom - top) - b.getBounds().bottom) / 2 + top;
                canvas.translate(x, transY);
                b.draw(canvas);
                canvas.restore();
            } catch (Exception e) {
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (reply_cursor != null) {
            reply_cursor.close();
        }
        if (database != null) {
            database.close();
        }
    }
}