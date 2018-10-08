package com.example.miogk.myhupu.radiobutton.forumraduibutton.plate.buttonActivities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miogk.myhupu.R;
import com.example.miogk.myhupu.db.MyDBHelper;
import com.example.miogk.myhupu.utils.ConstansUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.security.auth.login.LoginException;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {
    private SQLiteDatabase db;
    private EditText content;
    private FileOutputStream out;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        TextView cancel = (TextView) findViewById(R.id.title_bar_back2);
        cancel.setVisibility(View.VISIBLE);
        TextView post = (TextView) findViewById(R.id.title_bar_post);
        content = (EditText) findViewById(R.id.activity_post_content);
        post.setVisibility(View.VISIBLE);
        post.setOnClickListener(this);
        findViewById(R.id.title_bar_search).setVisibility(View.GONE);
        Button addPic = (Button) findViewById(R.id.activity_post_add_pic);
        addPic.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_post:
                TextView t = (TextView) findViewById(R.id.activity_post_title);
                String title = t.getText().toString().trim();
                String c = content.getText().toString().trim();
                valide(title, c);
                break;
            case R.id.title_bar_back2:
                finish();
                break;
            case R.id.activity_post_add_pic:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
                break;
            default:
                break;
        }
    }

    private void valide(String title, String c) {
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "标题为空，请重新输入", Toast.LENGTH_SHORT).show();
            return;
        } else if (title.length() <= 5) {
            Toast.makeText(this, "标题太短,请重新输入", Toast.LENGTH_SHORT).show();
            return;
        } else if (title.length() >= 350) {
            Toast.makeText(this, "标题太长，请重新输入", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(c)) {
            Toast.makeText(this, "内容为空,请重新输入", Toast.LENGTH_SHORT).show();
            return;
        } else if (content.length() <= 5) {
            Toast.makeText(this, "内容太短,请重新输入", Toast.LENGTH_SHORT).show();
            return;
        } else if (content.length() >= 15000) {
            Toast.makeText(this, "内容太长,请重新输入", Toast.LENGTH_SHORT).show();
            return;
        } else {
            db = ConstansUtils.getSqliteDatabase(this);
            ContentValues values = new ContentValues();
            String username = ConstansUtils.getUsername(this);
            values.put("username", username);
            values.put("title", title);
            values.put("content", c);
            values.put("time", System.currentTimeMillis());
            long hasInsertNums = db.insert(MyDBHelper.POST_TABLE, null, values);
            /**
             * 先前的想法好像有点不对，text类型的数据最多只能存储20个字符，不能作为存储USER发过帖的POSTID，
             * 先放在这里，稍后来改，可能可以直接从POST表里提取到所有USER发过的贴,试试看稍后再回来
             * 2018年9月5日15:45:16
             */
//            values.clear();
//            Cursor cursor = db.query(MyDBHelper.USER_TABLE, new String[]{"fatieid"}, "username = ?", new String[]{username}, null, null, null);
//            if (cursor.moveToNext()) {
//                String fatieid = cursor.getString(cursor.getColumnIndex("fatieid"));
//                Log.e("fatieid", "valide: " + fatieid);
//                fatieid += "," + hasInsertNums;
//                values.put("fatieid", fatieid);
//                db.update(MyDBHelper.USER_TABLE, values, "username = ?", new String[]{username});
//            } else {
//                values.put("fatieid", hasInsertNums);
//                db.update(MyDBHelper.USER_TABLE, values, "username = ?", new String[]{username});//更新USER表数据，添加发帖表POSTID.
//            }
            if (hasInsertNums > -1) {
                Toast.makeText(this, "成功输入数据: " + hasInsertNums, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tttt(intent);
            } else {
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    intent = data;
//                    Uri selectedImage = data.getData();
//                    String[] filePath = {MediaStore.Images.Media.DATA};
//                    Cursor cursor = getContentResolver().query(selectedImage, filePath, null, null, null);
//                    cursor.moveToFirst();
//                    int columnIndex = cursor.getColumnIndex(filePath[0]);
//                    String path = cursor.getString(columnIndex);
//                    cursor.close();
//                    Bitmap bitmap = BitmapFactory.decodeFile(path);
//                    m1(bitmap);
                    int hasReadExternalStoragePermission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (hasReadExternalStoragePermission == PackageManager.PERMISSION_GRANTED) {
                        tttt(data);
                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    }
                }
        }
    }

    private void tttt(Intent data) {
        ContentResolver resolver = getContentResolver();
        Uri originalUri = data.getData();
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(resolver.openInputStream(originalUri));
            String sd = Environment.getExternalStorageState();
            if (!sd.equals(Environment.MEDIA_MOUNTED)) {
                Log.e("TestFile", "SD card is not avaiable/writeable right now.");
            }
            Environment.getExternalStorageDirectory();
            File download = Environment.getExternalStoragePublicDirectory("myImage"); //storage/emulated/0/myImage
            String name = Calendar.getInstance(Locale.CHINA).getTimeInMillis() + ".jpg";
            out = null;
//                        File file = new File("/sdcard/myImage/");
            File file = new File(download.getAbsolutePath());
            file.mkdirs();
            String fileName = file.getAbsolutePath() + "/" + name;
            out = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            String myPath = fileName;
            SpannableString span_str = new SpannableString(myPath);
            Bitmap my_bm = BitmapFactory.decodeFile(myPath);
            ImageSpan span = new ImageSpan(this, my_bm);
            span_str.setSpan(span, 0, myPath.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Editable et = content.getText();//先获取Edittext中的内容
            int start = content.getSelectionStart();
            et.insert(start, span_str);//设置ss要添加的位置
            content.setText(et);//把ed添加到Edittext中去
            content.setSelection(start, span_str.length());//设置Edittext中光标在最后面显示
            //
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void m1(Bitmap bitmap) {
//        TextView tv_display = (TextView) findViewById(R.id.tv_display);
        SpannableString tv_ss = new SpannableString(1 + "");
//        Drawable tv_d = getResources().getDrawable(R.drawable.bbsimgai);//加载应用程序中图片
        Drawable tv_d = new BitmapDrawable(bitmap);
        tv_d.setBounds(0, 0, tv_d.getIntrinsicWidth(), tv_d.getIntrinsicHeight()); //设置宽'高
//若该TextView 上既有文本又有图片设置图片与文本底部对齐
        ImageSpan tv_span = new ImageSpan(tv_d, ImageSpan.ALIGN_BASELINE);
        tv_ss.setSpan(tv_span, 0, tv_ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        content.append(tv_ss);
    }
}