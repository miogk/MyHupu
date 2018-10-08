package com.example.miogk.myhupu.setting.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miogk.myhupu.R;
import com.example.miogk.myhupu.radiobutton.forumraduibutton.plate.buttonActivities.ShihuhuContentActivity;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/9/8.
 * 2018年9月8日23:30:26
 */

public class ReplyFragmentRecyclerViewAdapter extends RecyclerView.Adapter<ReplyFragmentRecyclerViewAdapter.ReplyFragmentViewHolder> {
    private Activity activity;
    private List<Map<String, String>> list;

    public ReplyFragmentRecyclerViewAdapter(Activity activity, List<Map<String, String>> list) {
        this.activity = activity;
        this.list = list;
    }


    @Override
    public ReplyFragmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(activity, R.layout.reply_fragment_layout, null);
        ReplyFragmentViewHolder fragmentViewHolder = new ReplyFragmentViewHolder(view);
        return fragmentViewHolder;
    }

    @Override
    public void onBindViewHolder(ReplyFragmentViewHolder holder, int position) {
        Map<String, String> map = list.get(position);
        final String postTitle = map.get("title");
        String time = map.get("time");
        final String rUsername = map.get("rUsername");
        String rContent = map.get("rContent");
        String content = map.get("content");
        final String table_row_id = map.get("table_row_id");
        final String postid = map.get("id");
        holder.time.setText(time);
        holder.content.setText(content);
        holder.postTitle.setText(postTitle);
        holder.postTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ShihuhuContentActivity.class);
                intent.putExtra("title", postTitle);
                intent.putExtra("id", postid);
                intent.putExtra("username", rUsername);
                activity.startActivity(intent);
            }
        });
        if (!TextUtils.isEmpty(rUsername)) {
            holder.rUsername.setText(rUsername);
            holder.rContent.setText(rContent);
            holder.reply_fragment_reference.setVisibility(View.VISIBLE);
        } else {
            holder.reply_fragment_reference.setVisibility(View.GONE);
        }
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ShihuhuContentActivity.class);
                intent.putExtra("title", postTitle);
                intent.putExtra("id", postid);
                intent.putExtra("username", rUsername);
                intent.putExtra("table_row_id", table_row_id);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ReplyFragmentViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        TextView content;
        TextView rUsername;
        TextView rContent;
        TextView postTitle;
        View v;
        LinearLayout reply_fragment_reference;

        public ReplyFragmentViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            postTitle = (TextView) itemView.findViewById(R.id.reply_fragment_post_title);
            time = (TextView) itemView.findViewById(R.id.reply_fragment_time);
            content = (TextView) itemView.findViewById(R.id.reply_fragment_content);
            rUsername = (TextView) itemView.findViewById(R.id.reply_fragment_rusername);
            rContent = (TextView) itemView.findViewById(R.id.reply_fragment_rcontent);
            reply_fragment_reference = (LinearLayout) itemView.findViewById(R.id.reply_fragment_reference);
        }
    }
}