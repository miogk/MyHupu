package com.example.miogk.myhupu.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.miogk.myhupu.setting.fragment.LoginActivity;
import com.example.miogk.myhupu.R;
import com.example.miogk.myhupu.utils.ConstansUtils;

/**
 * Created by Administrator on 2018/6/17.
 */

public class SettingRadioButton extends Fragment implements View.OnClickListener {
    private CheckBox nightLight;
    private Activity activity;
    private View login;
    private View view;
    private View checkIn;
    private View setting;
    private String username;
    private Button message;

    public SettingRadioButton() {

    }

    public SettingRadioButton(Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.setting_radio_button, container, false);
        message = (Button) view.findViewById(R.id.setting_radio_button_message);
        login = view.findViewById(R.id.setting_radio_button_login);
        setting = view.findViewById(R.id.setting_radio_button_setting);
        message.setOnClickListener(this);
        setting.setOnClickListener(this);
        login.setOnClickListener(this);
        nightLight = (CheckBox) view.findViewById(R.id.night_light);
        String day_night = ConstansUtils.getString(activity, ConstansUtils.MY_HUPU, ConstansUtils.DAY_NIGHT_MODE, ConstansUtils.DAY);
        if (day_night.equals(ConstansUtils.NIGHT)) {
            nightLight.setChecked(true);
        } else {
            nightLight.setChecked(false);
        }
        nightLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ConstansUtils.putString(activity, ConstansUtils.MY_HUPU, ConstansUtils.DAY_NIGHT_MODE, ConstansUtils.NIGHT);
                } else {
                    ConstansUtils.putString(activity, ConstansUtils.MY_HUPU, ConstansUtils.DAY_NIGHT_MODE, ConstansUtils.DAY);
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        username = ConstansUtils.getString(activity, ConstansUtils.MY_HUPU, ConstansUtils.USER_NAME, "");
        if (!TextUtils.isEmpty(username)) {
            view.findViewById(R.id.setting_radio_button_check_in).setVisibility(View.VISIBLE);
            view.findViewById(R.id.setting_radio_button_login).setVisibility(View.GONE);
            checkIn = view.findViewById(R.id.setting_radio_button_check_in);
            checkIn.setOnClickListener(this);
            TextView username = (TextView) view.findViewById(R.id.setting_radio_button_username);
            username.setText(this.username);
        } else {
            view.findViewById(R.id.setting_radio_button_check_in).setVisibility(View.GONE);
            view.findViewById(R.id.setting_radio_button_login).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_radio_button_login:
                Intent intent = new Intent(activity, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_radio_button_check_in:
                Intent homePage = new Intent(activity, HomePageActivity.class);
                Bundle b = new Bundle();
                b.putString("username", username);
                homePage.putExtras(b);
                startActivity(homePage);
                break;
            case R.id.setting_radio_button_setting:
                Intent settingPage = new Intent(activity, SettingPageActivity.class);
                startActivity(settingPage);
                break;
            case R.id.setting_radio_button_message:
                Intent messagePage = new Intent(activity, MessagePageActivity.class);
                startActivity(messagePage);
                break;
            default:
                break;
        }
    }
}