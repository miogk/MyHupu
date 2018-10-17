package com.example.miogk.myhupu.setting.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.miogk.myhupu.R;
import com.example.miogk.myhupu.base.BaseLoginActivity;
import com.example.miogk.myhupu.setting.fragment.Login2Fragment;
import com.example.miogk.myhupu.setting.fragment.LoginFragment;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String[] titles = new String[]{"验证码登录", "账号密码登录"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initViewPager();
    }

    private void initViewPager() {
        final ArrayList<BaseLoginActivity> fragments = new ArrayList<>();
        fragments.add(new LoginFragment(this));
        fragments.add(new Login2Fragment(this));

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                BaseLoginActivity loginActivity = fragments.get(position);
                View v = loginActivity.rootView;
                container.addView(v);
                return v;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initView() {
        TextView tv = (TextView) findViewById(R.id.title_tv);
        tv.setText("登录");
        findViewById(R.id.title_bar_search).setVisibility(View.GONE);
        ImageButton imageButton = (ImageButton) findViewById(R.id.title_bar_back);
        tabLayout = (TabLayout) findViewById(R.id.login_tablayout);
        viewPager = (ViewPager) findViewById(R.id.login_viewpager);
        imageButton.setVisibility(View.VISIBLE);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}