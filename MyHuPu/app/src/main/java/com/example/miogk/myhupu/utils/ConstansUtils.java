package com.example.miogk.myhupu.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.miogk.myhupu.db.MyDBHelper;
import com.example.miogk.myhupu.radiobutton.forumraduibutton.plate.buttonActivities.ShihuhuContentActivity;

import java.util.Date;

/**
 * Created by Administrator on 2018/5/17.
 */

public class ConstansUtils {
    public static final String SERVER_NAME = "https://raw.githubusercontent.com/miogk/miogk.github.io/master";
    public static final String DAY_NIGHT_MODE = "day_night_mode";
    public static final String DAY = "day";
    public static final String NIGHT = "night";
    public static final String MY_HUPU = "my_hupu";
    public static final String USER_NAME = "username";
    public static final String LIMIT = "10";
    public static MyDBHelper dbHelper;
    public static SQLiteDatabase db;
    public static final String LOCAL_BROAD_CAST = "LOCAL_BROAD_CASET";
    public static final String TABLE_PREFIX = "table_";

    public static String getPublishTime(String s) {
        Long t = Long.parseLong(s);
        return new Date(t).toLocaleString();
    }

    public static String getTime(long millis) {
        long now = System.currentTimeMillis();
        long s = (now - millis) / 1000;
        /**
         *  60 秒
         *  60 * 60 一小时
         *  60 * 60 * 24一天
         *  60 * 60 * 24 * 30一个月
         *  60 * 60 * 24 * 365一年
         */

        if (s < 10) {
            return "刚刚发布";
        } else if (s < 60) {
            return s + "秒前发布";
        } else if (s < 60 * 60) {
            return s / 60 + "分钟前发布";
        } else if (s < 60 * 60 * 24) {
            return s / 60 / 60 + "小时前发布";
        } else if (s < 60 * 60 * 24 * 30) {
            return s / 60 / 60 / 24 + "天前发布";
        } else if (s < 60 * 60 * 24 * 30 * 12) {
            return s / 60 / 60 / 24 / 30 + "月前发布";
        } else if (s < 60 * 60 * 24 * 30 * 12 * 100) {
            return s / 60 / 60 / 24 / 30 / 12 + "年前发布";
        }
        return "";
    }

    public static String getUsername(Activity activity) {
        return getString(activity, ConstansUtils.MY_HUPU, ConstansUtils.USER_NAME, "");
    }

    public static SQLiteDatabase getSqliteDatabase(Activity activity) {
        dbHelper = new MyDBHelper(activity, MyDBHelper.DB_NAME, null, MyDBHelper.DB_VERSION);
        db = dbHelper.getReadableDatabase();
        return db;
    }


    public static void checkOut(Activity activity, String name) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", "");
        editor.commit();
    }

    public static void putString(Activity activity, String name, String key, String value) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(Activity activity, String name, String key, String defaultValue) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(name, Context.MODE_PRIVATE);
        String result = sharedPreferences.getString(key, defaultValue);
        return result;
    }

    public static void toast(Context context, String text) {
        if (TextUtils.isEmpty(text)) {
            Toast.makeText(context, context.getPackageName(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }
    }
}
