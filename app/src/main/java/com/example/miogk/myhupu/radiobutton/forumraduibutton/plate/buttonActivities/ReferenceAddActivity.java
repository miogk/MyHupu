package com.example.miogk.myhupu.radiobutton.forumraduibutton.plate.buttonActivities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miogk.myhupu.R;
import com.example.miogk.myhupu.db.MyDBHelper;
import com.example.miogk.myhupu.utils.ConstansUtils;

import static com.example.miogk.myhupu.R.id.activity_reply_add_content;

public class ReferenceAddActivity extends AppCompatActivity implements View.OnClickListener {
    private SQLiteDatabase database;
    private String username;
    private String content;
    private String postId;
    private String tableName;
    private EditText editText;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_add);
        intent = getIntent();
        username = intent.getStringExtra("username");
        content = intent.getStringExtra("content");
        postId = intent.getStringExtra("postid");
        tableName = ConstansUtils.TABLE_PREFIX + postId;
        TextView reference = (TextView) findViewById(R.id.activity_reply_add_reply);
        editText = (EditText) findViewById(activity_reply_add_content);
        reference.setTextSize(16);
        reference.setText("引用@" + username + "发表的 : " + content);
        TextView activity_reply_add_reply = (TextView) findViewById(R.id.activity_reply_add_post);
        activity_reply_add_reply.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_reply_add_post:
                String content = editText.getText().toString().trim();
                if (database == null) {
                    database = ConstansUtils.getSqliteDatabase(ReferenceAddActivity.this);
                }
                String tableName = ConstansUtils.TABLE_PREFIX + postId;
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(ReferenceAddActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if (content.length() < 5) {
                    Toast.makeText(ReferenceAddActivity.this, "内容不能小于5个字", Toast.LENGTH_SHORT).show();
                    return;
                }
                long time = System.currentTimeMillis();
                ContentValues values = new ContentValues();
                values.put("postid", postId);
                values.put("username", username);
                values.put("time", time);
                values.put("content", content);
                database.insert(tableName, null, values);
                values.clear();
                values.put("lastreply", time);
                database.update(MyDBHelper.POST_TABLE, values, "postid = ?", new String[]{postId});
                Toast.makeText(ReferenceAddActivity.this, "回复成功", Toast.LENGTH_SHORT).show();
                Bundle b = new Bundle();
                b.putBoolean("flag", true);
                b.putString("username", username);
                b.putString("content", content);
                intent.putExtras(b);
                setResult(RESULT_OK, intent);
                finish();
                break;
            default:
                break;
        }
    }
}