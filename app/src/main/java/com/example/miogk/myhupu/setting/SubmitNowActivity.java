package com.example.miogk.myhupu.setting;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miogk.myhupu.R;
import com.example.miogk.myhupu.db.MyDBHelper;

public class SubmitNowActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText username;
    private EditText password;
    private Button submit;
    private ImageButton back;
    private MyDBHelper myDBHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_now);
        initView();
        initData();
    }

    private void initData() {
        TextView textView = (TextView) findViewById(R.id.title_tv);
        back.setVisibility(View.VISIBLE);
        textView.setText("注册");
        submit.setOnClickListener(this);
    }

    private void initView() {
        username = (EditText) findViewById(R.id.submit_now_username);
        password = (EditText) findViewById(R.id.submit_now_password);
        submit = (Button) findViewById(R.id.submit_now_submit);
        back = (ImageButton) findViewById(R.id.title_bar_back);
        myDBHelper = new MyDBHelper(this, MyDBHelper.DB_NAME, null, myDBHelper.DB_VERSION);
        db = myDBHelper.getReadableDatabase();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_now_submit:
                String u = username.getText().toString().trim();
                String p = password.getText().toString().trim();
                if (TextUtils.isEmpty(u) || TextUtils.isEmpty(p)) {
                    Toast.makeText(this, "请输入用户名与密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                Cursor cursor = db.query(MyDBHelper.USER_TABLE, new String[]{"username"}, "username=?", new String[]{u}, null, null, null);
                if (cursor.moveToNext()) {
                    Toast.makeText(this, "用户名已存在", Toast.LENGTH_LONG).show();
                } else {
                    ContentValues values = new ContentValues();
                    values.put("username", u);
                    values.put("password", p);
                    db.insert(MyDBHelper.USER_TABLE, null, values);
                    Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                    db.close();
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}