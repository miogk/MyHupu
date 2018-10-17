package com.example.miogk.myhupu.setting.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.miogk.myhupu.R;
import com.example.miogk.myhupu.radiobutton.forumraduibutton.plate.buttonActivities.ShihuhuContentActivity;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/9/7.
 * 2018年9月7日17:31:38
 */

public class PostFragmentRecyclerViewAdapter extends RecyclerView.Adapter<PostFragmentRecyclerViewAdapter.HomePageViewHolder> {
    private Activity activity;
    private List<Map<String, String>> list;

    public PostFragmentRecyclerViewAdapter(Activity activity, List<Map<String, String>> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public HomePageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(activity, R.layout.shihuhu_layout2, null);
        HomePageViewHolder homePageViewHolder = new HomePageViewHolder(view);
        return homePageViewHolder;
    }

    @Override
    public void onBindViewHolder(HomePageViewHolder holder, int position) {
        Map<String, String> map = list.get(position);
        final String postId = map.get("postid");
        final String title = map.get("title");
        final String username = map.get("username");
        String time = map.get("time");
        holder.title.setText(title);
        holder.time.setText(time);
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ShihuhuContentActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("id", postId);
                intent.putExtra("username", username);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomePageViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView time;
        View v;

        public HomePageViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            itemView.findViewById(R.id.shihuhu2_username).setVisibility(View.GONE);
            title = (TextView) itemView.findViewById(R.id.shihuhu2_title);
            time = (TextView) itemView.findViewById(R.id.shihuhu2_time);
        }
    }
}