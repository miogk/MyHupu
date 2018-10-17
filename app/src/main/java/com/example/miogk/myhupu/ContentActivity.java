package com.example.miogk.myhupu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.miogk.myhupu.base.BaseActivity;
import com.example.miogk.myhupu.domain.Man;
import com.example.miogk.myhupu.utils.ConstansUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ContentActivity extends AppCompatActivity {
    private BitmapUtils bitmapUtils;
    private WebView webview;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        findViewById(R.id.title_bar_back).setVisibility(View.VISIBLE);
        webview = (WebView) findViewById(R.id.activity_content_webview);
        back = (ImageButton) findViewById(R.id.title_bar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        WebSettings setting = webview.getSettings();
        bitmapUtils = new BitmapUtils(this);
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.ARGB_4444);
        Intent intent = getIntent();
        Man.ManData manData = (Man.ManData) intent.getSerializableExtra("md");
        String mdTitle = manData.title;
//        String imageUrl = manData.image;
        String contentUrl = manData.content;
        String mdTime = manData.time;
        try {
//            imageUrl = URLEncoder.encode(imageUrl, "utf-8");
            contentUrl = URLEncoder.encode(contentUrl, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        String mdImage = ConstansUtils.SERVER_NAME + imageUrl;
        String mdContent = ConstansUtils.SERVER_NAME + contentUrl;
        TextView title = (TextView) findViewById(R.id.activity_content_title);
        TextView time = (TextView) findViewById(R.id.activity_content_time);
//        ImageView image = (ImageView) findViewById(R.id.activity_content_image);
//        TextView content = (TextView) findViewById(R.id.activity_content_content);
        title.setText(mdTitle);
        time.setText(mdTime);
//        bitmapUtils.display(image, mdImage);
        getDataFromNet(mdContent);
    }

    private void getDataFromNet(final String mdContent) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpMethod.GET, mdContent, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String response = responseInfo.result;
                Log.e("success", "onSuccess: " + response);
                webview.loadData(response, "text/html", "utf-8");
//                content.setText(response);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("onfailure", "onFailure: " + e);
            }
        });
    }
}