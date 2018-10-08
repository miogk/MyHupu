package com.example.miogk.myhupu.setting.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.miogk.myhupu.R;
import com.example.miogk.myhupu.base.BaseLoginActivity;
import com.example.miogk.myhupu.db.MyDBHelper;
import com.example.miogk.myhupu.setting.SubmitNowActivity;
import com.example.miogk.myhupu.utils.ConstansUtils;

/**
 * Created by Administrator on 2018/6/22.
 */

public class Login2Fragment extends BaseLoginActivity implements View.OnClickListener {
    private Button login;
    private EditText username;
    private EditText password;
    private Button submitNow;
    private MyDBHelper myDBHelper;
    private SQLiteDatabase db;

    public Login2Fragment(Activity activity) {
        super(activity);
    }

    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.submit2_layout, null);
        login = (Button) view.findViewById(R.id.login2);
        username = (EditText) view.findViewById(R.id.submit2_username);
        password = (EditText) view.findViewById(R.id.submit2_password);
        submitNow = (Button) view.findViewById(R.id.submit2_submit_now);
        submitNow.setOnClickListener(this);
        login.setOnClickListener(this);
        return view;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit2_submit_now:
                Intent intent = new Intent(mActivity, SubmitNowActivity.class);
                mActivity.startActivity(intent);
                break;
            case R.id.login2:
                String u = username.getText().toString().trim();
                String p = password.getText().toString().trim();
                myDBHelper = new MyDBHelper(mActivity, MyDBHelper.DB_NAME, null, myDBHelper.DB_VERSION);
                db = myDBHelper.getReadableDatabase();
                Cursor c = db.query(MyDBHelper.USER_TABLE, null, "username=?",
                        new String[]{u}, null, null, null);
                if (c.moveToNext()) {
                    String password = c.getString(c.getColumnIndex("password"));
                    if (p.equals(password)) {
                        Toast.makeText(mActivity, "登录成功", Toast.LENGTH_LONG).show();
                        ConstansUtils.putString(mActivity, ConstansUtils.MY_HUPU, "username", c.getString(c.getColumnIndex("username")));
                        mActivity.finish();
                    } else {
                        Toast.makeText(mActivity, "密码错误", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(mActivity, "没有此用户名", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

}
