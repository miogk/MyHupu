package com.example.miogk.myhupu.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/5/16.
 */

public class Man {
    public List<ManData> data;
    public String refresh;
    public String more;

    public class ManData implements Serializable {
        public String id;
        public String title;
        public String image;
        public String time;
        public String content;

        @Override
        public String toString() {
            return "ManData{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", image='" + image + '\'' +
                    ", time='" + time + '\'' +
                    ", content='" + content + '\'' +
                    '}';
        }
    }
}