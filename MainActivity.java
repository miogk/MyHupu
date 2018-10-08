package com.example.miogk.myhupu;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.miogk.myhupu.radiobutton.forumraduibutton.ForumRadioButton;
import com.example.miogk.myhupu.setting.SettingRadioButton;
import com.example.miogk.myhupu.utils.ConstansUtils;

public class MainActivity extends FragmentActivity {
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private HomeRadioButton homeRadioButton;
    private ForumRadioButton forumRadioButton;
    private SettingRadioButton settingRadioButton;
    private static final String HOME_RADIO_BUTTON = "home_radio_button";
    private static final String FORUM_RADIO_BUTTON = "forum_radio_button";
    private static final String SETTING_RADIO_BUTTON = "setting_radio_button";
    private long startTime = 0;


    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - startTime) >= 2000) {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            startTime = currentTime;
        } else {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        String mode = ConstansUtils.getString(this, ConstansUtils.MY_HUPU, ConstansUtils.DAY_NIGHT_MODE, ConstansUtils.DAY);
        homeRadioButton = new HomeRadioButton(this);
        forumRadioButton = new ForumRadioButton(this);
        settingRadioButton = new SettingRadioButton(this);
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.main_fragment, homeRadioButton, HOME_RADIO_BUTTON);
        transaction.add(R.id.main_fragment, forumRadioButton, FORUM_RADIO_BUTTON);
        transaction.add(R.id.main_fragment, settingRadioButton, SETTING_RADIO_BUTTON);
        transaction.commit();
        RadioButton[] buttons;
        RadioGroup group = (RadioGroup) findViewById(R.id.main_activity_radio_group);
        RadioButton home = (RadioButton) findViewById(R.id.main_activity_radio_button_home);
        RadioButton game = (RadioButton) findViewById(R.id.main_activity_radio_button_game);
        RadioButton forum = (RadioButton) findViewById(R.id.main_activity_radio_button_forum);
        RadioButton shihuo = (RadioButton) findViewById(R.id.main_activity_radio_button_shihuo);
        RadioButton setting = (RadioButton) findViewById(R.id.main_activity_radio_button_setting);
        initHomeRadioButton();
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.main_activity_radio_button_home:
                        transaction = manager.beginTransaction();
                        transaction.hide(forumRadioButton);
                        transaction.hide(settingRadioButton);
                        transaction.show(homeRadioButton);
                        transaction.commit();
                        break;
                    case R.id.main_activity_radio_button_forum:
                        transaction = manager.beginTransaction();
                        transaction.hide(homeRadioButton);
                        transaction.hide(settingRadioButton);
                        transaction.show(forumRadioButton);
                        transaction.commit();
                        break;
                    case R.id.main_activity_radio_button_setting:
                        transaction = manager.beginTransaction();
                        transaction.hide(homeRadioButton);
                        transaction.hide(forumRadioButton);
                        transaction.show(settingRadioButton);
                        transaction.commit();
                        break;
                    default:
                        break;
                }
            }
        });
        buttons = new RadioButton[]{home, game, forum, shihuo, setting};
        for (RadioButton button : buttons) {
            Drawable d = button.getCompoundDrawables()[1];//1:drawableTop
            Rect r = new Rect(0, 0, d.getMinimumWidth() * 2 / 5, d.getMinimumHeight() * 2 / 5);
            d.setBounds(r);
            button.setCompoundDrawables(null, d, null, null);
        }
        ImageButton back = (ImageButton) findViewById(R.id.title_bar_back);
        if (back.getVisibility() == View.VISIBLE) {
            back.setVisibility(View.GONE);
        }
        if (mode.equals(ConstansUtils.DAY)) {
            group.setBackgroundColor(getResources().getColor(R.color.blue));
        } else {
            group.setBackgroundColor(getResources().getColor(R.color.blue_night));
        }
    }

    private void initHomeRadioButton() {
        transaction = manager.beginTransaction();
        transaction.hide(forumRadioButton);
        transaction.hide(settingRadioButton);
        transaction.show(homeRadioButton);
        transaction.commit();
    }
}