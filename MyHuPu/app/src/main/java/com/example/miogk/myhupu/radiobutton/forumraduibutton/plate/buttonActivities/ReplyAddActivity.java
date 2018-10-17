package com.example.miogk.myhupu.radiobutton.forumraduibutton.plate.buttonActivities;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miogk.myhupu.R;
import com.example.miogk.myhupu.db.MyDBHelper;
import com.example.miogk.myhupu.utils.ConstansUtils;

import java.util.Date;

import static com.example.miogk.myhupu.R.id.activity_reply_add_reply;

public class ReplyAddActivity extends AppCompatActivity implements View.OnClickListener {
    private SQLiteDatabase database;
    private String postid;
    private String username;
    private EditText activity_reply_add_content;
    private Intent intent;
    private String title;
    private String referenceContent;
    private boolean reference;
    private String _id;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_add);
        findViewById(R.id.activity_replay_add_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView activity_reply_add_title = (TextView) findViewById(R.id.activity_reply_add_title);
        TextView activity_reply_add_reply = (TextView) findViewById(R.id.activity_reply_add_reply);
        activity_reply_add_content = (EditText) findViewById(R.id.activity_reply_add_content);
        intent = getIntent();
        _id = intent.getStringExtra("_id");
        title = intent.getStringExtra("title");
        postid = intent.getStringExtra("postid");
        username = intent.getStringExtra("username");
        reference = intent.getBooleanExtra("reference", false);
        if (reference) {
            referenceContent = intent.getStringExtra("content");
            activity_reply_add_reply.setText("引用@" + username + "发表的 : " + referenceContent);
        }
        activity_reply_add_title.setText(title);
        TextView activity_reply_add_post = (TextView) findViewById(R.id.activity_reply_add_post);
        activity_reply_add_post.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (database != null) {
            database.close();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_reply_add_post:
                String content = activity_reply_add_content.getText().toString().trim();
                if (database == null) {
                    database = ConstansUtils.getSqliteDatabase(ReplyAddActivity.this);
                }
                String tableName = ConstansUtils.TABLE_PREFIX + postid;
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(ReplyAddActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if (content.length() < 5) {
                    Toast.makeText(ReplyAddActivity.this, "内容不能小于5个字", Toast.LENGTH_SHORT).show();
                    return;
                }
                String current_user = ConstansUtils.getUsername(this);
                long time = System.currentTimeMillis();
                ContentValues values = new ContentValues();
                values.put("postid", postid);
                values.put("username", current_user);
                values.put("time", time);
                values.put("content", content);
                if (reference) {
                    values.put("_rId", _id);
                    values.put("rUsername", username);
                    values.put("rContent", referenceContent);
                }
//                    Cursor cursor = database.query(MyDBHelper.REPLY_TABLE, null, null, null, null, null, null);
                long infectNum = database.insert(tableName, null, values);
                values.clear();
                values.put("lastreply", time);
                database.update(MyDBHelper.POST_TABLE, values, "postid = ?", new String[]{postid});
                values.clear();
                //向USER_TABLE表插入数据
                values.put("username", current_user);
                values.put("table_name", ConstansUtils.TABLE_PREFIX + postid);
                values.put("table_row_id", infectNum);
                values.put("time", System.currentTimeMillis());
                database.insert(MyDBHelper.USER_TABLE_TABLE, null, values);
                /**
                 * 发帖完成后更新USER数据表数据，插入新帖子的POSTID，REPLYID，方便之后寻找.
                 * 但这里是回帖的ID，写错地方了，先放着，先去找发帖的类写好代码，再来更改
                 * 2018年9月5日15:44:34
                 */
//                Cursor cursor = database.query(MyDBHelper.USER_TABLE, new String[]{"fatieid"}, "username = ?", new String[]{username}, null, null, null);
//                if (cursor.moveToFirst()) {
//                    String fatieid = cursor.getString(cursor.getColumnIndex("fatieid"));
//                    fatieid = fatieid + "," + postid;
//                    ContentValues values2 = new ContentValues();
//                    values2.put("fatieid", fatieid);
//                    database.update(MyDBHelper.USER_TABLE, values2, "username = ?", new String[]{username});
//                } else {
//                    ContentValues values3 = new ContentValues();
//                    values3.put("fatieid", postid);
//                    database.update(MyDBHelper.USER_TABLE, values3, "username = ?", new String[]{username});
//                }
                Toast.makeText(ReplyAddActivity.this, "回复成功", Toast.LENGTH_SHORT).show();
                Bundle b = new Bundle();
                b.putBoolean("flag", true);
                intent.putExtras(b);
                setResult(RESULT_OK, intent);
                finish();
                break;
            default:
                break;
        }
    }

    class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConstansUtils.toast(context, ConstansUtils.LOCAL_BROAD_CAST);
        }
    }
}