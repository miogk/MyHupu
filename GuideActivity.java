package com.example.miogk.myhupu;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.miogk.myhupu.base.BaseActivity;

public class GuideActivity extends BaseActivity {
    private int time = 3;
    private Button bt;
    private Handler handler;
    private MyRunnable myRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);
        handler = new Handler();
        myRunnable = new MyRunnable();
        bt = (Button) findViewById(R.id.bt_time);
        bt.setText("" + time);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(myRunnable);
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        handler.postDelayed(myRunnable, 1000);
    }

    class MyRunnable implements Runnable {
        @Override
        public void run() {
            time--;
            bt.setText("" + time);
            if (time == 0) {
                handler.removeCallbacks(myRunnable);
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            handler.postDelayed(this, 1000);
        }
    }
}