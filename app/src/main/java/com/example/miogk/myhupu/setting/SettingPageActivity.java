package com.example.miogk.myhupu.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miogk.myhupu.R;
import com.example.miogk.myhupu.utils.ConstansUtils;

public class SettingPageActivity extends AppCompatActivity implements View.OnClickListener {
    private Button quitLogin;
    private TextView clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);
        ImageButton back = (ImageButton) findViewById(R.id.title_bar_back);
        clear = (TextView) findViewById(R.id.setting_page_clear);
        back.setVisibility(View.VISIBLE);
        TextView textView = (TextView) findViewById(R.id.title_tv);
        textView.setText("设置");
        Button search = (Button) findViewById(R.id.title_bar_search);
        search.setVisibility(View.GONE);
        String name = ConstansUtils.getString(this, ConstansUtils.MY_HUPU, ConstansUtils.USER_NAME, "");
        quitLogin = (Button) findViewById(R.id.quit_login);
        if (!TextUtils.isEmpty(name)) {
            quitLogin.setOnClickListener(this);
            quitLogin.setVisibility(View.VISIBLE);
        } else {
            quitLogin.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quit_login:
                final MyselfDialog dialog = new MyselfDialog(this);
                dialog.show();
                Button yes = (Button) dialog.findViewById(R.id.yes);
                Button no = (Button) dialog.findViewById(R.id.no);
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        ConstansUtils.checkOut(SettingPageActivity.this, ConstansUtils.MY_HUPU);
                        quitLogin.setVisibility(View.INVISIBLE);
                        Toast.makeText(SettingPageActivity.this, "已退出登录", Toast.LENGTH_SHORT).show();
                    }
                });
                break;

        }
    }

    class MyselfDialog extends AlertDialog {

        protected MyselfDialog(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_layout);
        }
    }
}