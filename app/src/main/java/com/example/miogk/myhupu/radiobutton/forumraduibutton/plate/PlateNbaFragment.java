package com.example.miogk.myhupu.radiobutton.forumraduibutton.plate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.miogk.myhupu.R;
import com.example.miogk.myhupu.radiobutton.forumraduibutton.plate.buttonActivities.ShihuhuActivity;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2018/6/5.
 */

public class PlateNbaFragment extends Fragment {
    private Activity mActivity;
    private List<Button> nba = new ArrayList<>();
    private Button shihuhu;
    private Button hou; // 休斯顿火箭
    private Button cle; // 克利夫兰骑士
    private Button gsw; // 金州勇士
    private Button lal; // 洛杉矶湖人
    private Button sas; // 圣安东尼奥马刺
    private Button cel; // 波士顿凯尔特人
    private Button oct; // 俄克拉荷马雷霆
    private Button lac; // 洛杉矶快船
    private Button nyn; // 纽约尼克斯
    private Button chi; // 芝加哥公牛
    private Button min; // 米尼苏达森林狼
    private Button dal; // 达拉斯独行者
    private Button nets;// 篮网
    private Button phi; // 费城76人
    private Button ind; // 印第安纳步行者
    private Button por; // 波特兰拓荒者
    private Button mia; // 迈阿密热火
    private Button was; // 华盛顿奇才
    private Button uth; // 犹他爵士
    private Button mem; // 孟菲斯灰熊
    private Button phx; // 菲尼克斯太阳
    private Button sac; // 萨克拉门托国王
    private Button pelicans; // 鹈鹕
    private Button mil; // 密尔沃基雄鹿
    private Button tor; // 多伦多猛龙
    private Button den; // 丹佛掘金
    private Button atl; // 亚特兰大老鹰
    private Button det; // 底特律活塞
    private Button orl; // 奥兰多魔术
    private Button bobcants; // 黄蜂

    public PlateNbaFragment(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView:  view");
        View view = inflater.inflate(R.layout.plate_nba_fragment, container, false);
        shihuhu = (Button) view.findViewById(R.id.plate_nba_fragment_shihuhu);
        shihuhu.setOnClickListener(new MyListener());
        hou = (Button) view.findViewById(R.id.plate_nba_fragment_hou);
        nba.add(hou);
        cle = (Button) view.findViewById(R.id.plate_nba_fragment_cle);
        nba.add(cle);
        gsw = (Button) view.findViewById(R.id.plate_nba_fragment_gsw);
        nba.add(gsw);
        lal = (Button) view.findViewById(R.id.plate_nba_fragment_lal);
        nba.add(lal);
        sas = (Button) view.findViewById(R.id.plate_nba_fragment_sas);
        nba.add(sas);
        oct = (Button) view.findViewById(R.id.plate_nba_fragment_oct);
        nba.add(oct);
        cel = (Button) view.findViewById(R.id.plate_nba_fragment_cel);
        nba.add(cel);
        lac = (Button) view.findViewById(R.id.plate_nba_fragment_lac);
        nba.add(lac);
        nyn = (Button) view.findViewById(R.id.plate_nba_fragment_nyn);
        nba.add(nyn);
        chi = (Button) view.findViewById(R.id.plate_nba_fragment_chi);
        nba.add(chi);
        min = (Button) view.findViewById(R.id.plate_nba_fragment_min);
        nba.add(min);
        dal = (Button) view.findViewById(R.id.plate_nba_fragment_dal);
        nba.add(dal);
        nets = (Button) view.findViewById(R.id.plate_nba_fragment_nets);
        nba.add(nets);
        phi = (Button) view.findViewById(R.id.plate_nba_fragment_phi);
        nba.add(phi);
        ind = (Button) view.findViewById(R.id.plate_nba_fragment_ind);
        nba.add(ind);
        por = (Button) view.findViewById(R.id.plate_nba_fragment_por);
        nba.add(por);
        mia = (Button) view.findViewById(R.id.plate_nba_fragment_mia);
        nba.add(mia);
        was = (Button) view.findViewById(R.id.plate_nba_fragment_was);
        nba.add(was);
        uth = (Button) view.findViewById(R.id.plate_nba_fragment_uth);
        nba.add(uth);
        mem = (Button) view.findViewById(R.id.plate_nba_fragment_mem);
        nba.add(mem);
        phx = (Button) view.findViewById(R.id.plate_nba_fragment_phx);
        nba.add(phx);
        sac = (Button) view.findViewById(R.id.plate_nba_fragment_sac);
        nba.add(sac);
        pelicans = (Button) view.findViewById(R.id.plate_nba_fragment_pelicans);
        nba.add(pelicans);
        mil = (Button) view.findViewById(R.id.plate_nba_fragment_mil);
        nba.add(mil);
        tor = (Button) view.findViewById(R.id.plate_nba_fragment_tor);
        nba.add(tor);
        den = (Button) view.findViewById(R.id.plate_nba_fragment_den);
        nba.add(den);
        atl = (Button) view.findViewById(R.id.plate_nba_fragment_atl);
        nba.add(atl);
        det = (Button) view.findViewById(R.id.plate_nba_fragment_det);
        nba.add(det);
        orl = (Button) view.findViewById(R.id.plate_nba_fragment_orl);
        nba.add(orl);
        bobcants = (Button) view.findViewById(R.id.plate_nba_fragment_bobcats);
        nba.add(bobcants);
        for (Button b : nba) {
            Drawable d = b.getCompoundDrawables()[1];
            Rect r = new Rect(0, 0, d.getMinimumWidth() / 5, d.getMinimumHeight() / 5);
            d.setBounds(r);
            b.setCompoundDrawables(null, d, null, null);
        }
        return view;
    }

    class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.plate_nba_fragment_shihuhu:
                    Intent intent = new Intent(mActivity, ShihuhuActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }
}