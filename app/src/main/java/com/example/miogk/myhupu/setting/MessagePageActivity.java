package com.example.miogk.myhupu.setting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.miogk.myhupu.R;

public class MessagePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_page);
        findViewById(R.id.title_bar_search).setVisibility(View.GONE);
        findViewById(R.id.title_bar_back).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.title_tv)).setText("我的消息");

    }
}