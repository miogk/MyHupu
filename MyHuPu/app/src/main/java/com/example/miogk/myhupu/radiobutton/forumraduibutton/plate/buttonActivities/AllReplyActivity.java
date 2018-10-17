package com.example.miogk.myhupu.radiobutton.forumraduibutton.plate.buttonActivities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.miogk.myhupu.R;
import com.example.miogk.myhupu.db.MyDBHelper;
import com.example.miogk.myhupu.utils.ConstansUtils;

import java.util.Date;

import static com.example.miogk.myhupu.R.id.reply_content_layout_reference_content;

public class AllReplyActivity extends AppCompatActivity {
    private SQLiteDatabase database;
    private ListView recyclerView;
    private MyAdapter adapter;
    private String tableName;
    private String rUsername;
    private String rContent;
    private Cursor c;
    private String _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_reply);
        initView();
    }

    private void initView() {
        if (database == null) {
            database = ConstansUtils.getSqliteDatabase(this);
        }
        Intent intent = getIntent();
        tableName = intent.getStringExtra("tableName");
        rUsername = intent.getStringExtra("username");
        rContent = intent.getStringExtra("content");
        _id = intent.getStringExtra("_id");
        recyclerView = (ListView) findViewById(R.id.all_reply_recycler_view);
        c = database.query(tableName, null, "rContent = ? and _rId = ?", new String[]{rContent, _id}, null, null, null);
        adapter = new MyAdapter(this, c, false);
        recyclerView.setAdapter(adapter);
//        if (c != null) {
//            c.close();
//        }
    }

    private class MyAdapter extends CursorAdapter {

        public MyAdapter(Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
        }

        @Override
        public int getCount() {
            return c.getCount();
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = View.inflate(context, R.layout.reply_content_layout, null);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            view.findViewById(R.id.reply_content_layout_reference_content).setVisibility(View.VISIBLE);
            TextView t_username = (TextView) view.findViewById(R.id.reply_content_layout_username);
            TextView t_time = (TextView) view.findViewById(R.id.reply_content_layout_time);
            TextView t_content = (TextView) view.findViewById(R.id.reply_content_layout_content);
            TextView t_number = (TextView) view.findViewById(R.id.reply_content_layout_number);
            TextView reference = (TextView) view.findViewById(R.id.reply_content_layout_reference);
            TextView all_reply = (TextView) view.findViewById(R.id.reply_content_all_reply);
            TextView t_rUsername = (TextView) view.findViewById(R.id.reply_content_layout_reference_content_username);
            TextView t_rContent = (TextView) view.findViewById(R.id.reply_content_layout_reference_content_content);
            TextView t_lighten = (TextView) view.findViewById(R.id.reply_content_lighten_number);

// content rUsername rContent lighten
            String _id = cursor.getString(cursor.getColumnIndex("_id"));
            String username = cursor.getString(cursor.getColumnIndex("username"));
            String t = cursor.getString(cursor.getColumnIndex("time"));
            String time = ConstansUtils.getPublishTime(t);
            String content = cursor.getString(cursor.getColumnIndex("content"));
            String rContent = cursor.getString(cursor.getColumnIndex("rContent"));
            String lighten = cursor.getString(cursor.getColumnIndex("lighten"));

            t_username.setText(username);
            t_time.setText(time);
            t_content.setText(content);
            t_rUsername.setText(rUsername);
            t_rContent.setText(rContent);
            t_number.setText("#" + _id);
            if (TextUtils.isEmpty(lighten)) {
                t_lighten.setText("0");
            } else {
                t_lighten.setText(lighten);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (database != null) {
            database.close();
        }
    }
}