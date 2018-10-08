package com.example.miogk.myhupu.radiobutton.forumraduibutton.plate;

import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.miogk.myhupu.R;
import com.example.miogk.myhupu.radiobutton.forumraduibutton.BaseForumPager;

/**
 * Created by Administrator on 2018/5/31.
 */

public class PlatePager extends BaseForumPager {
    private RadioGroup rg;
    private PlateNbaFragment plateNbaFragment;
    private PlateCbaFragment plateCbaFragment;
    private FragmentManager fm;
    private FragmentTransaction transaction;

    public PlatePager(FragmentActivity mActivity) {
        super(mActivity);
    }

    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.forum_plate, null);
        rg = (RadioGroup) view.findViewById(R.id.forum_plate_rg);
        RadioButton nba = (RadioButton) view.findViewById(R.id.forum_plate_nba);
        nba.setChecked(true);
        fm = mActivity.getSupportFragmentManager();
        plateNbaFragment = new PlateNbaFragment(mActivity);
        plateCbaFragment = new PlateCbaFragment();
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.forum_plate_nba:
                        transaction = fm.beginTransaction();
                        transaction.replace(R.id.forum_plate_content, plateNbaFragment);
                        transaction.commit();
                        break;
                    case R.id.forum_plate_cba:
                        transaction = fm.beginTransaction();
                        transaction.replace(R.id.forum_plate_content, plateCbaFragment);
                        transaction.commit();
                        break;
                    default:
                        break;
                }
            }
        });
        return view;
    }

    @Override
    protected void initData() {
        transaction = fm.beginTransaction();
        transaction.replace(R.id.forum_plate_content, plateNbaFragment);
        transaction.commit();
    }
}
