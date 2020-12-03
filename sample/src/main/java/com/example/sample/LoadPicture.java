package com.example.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;

public class LoadPicture extends AppCompatActivity {

    private ImageView showImg;
    private String path;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_picture);
        textView=(TextView)findViewById(R.id.tv);
//        showImg();

//        //img是ImageView的ID号
//        showImg = findViewById(R.id.img);
//
//        //图片的路径
//        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/123.jpg";
//        System.out.println(path);
//
//        //判断文件是否存在, 存在该图片就显示出来
//        boolean fileExist = fileIsExists(path);
//        if(fileExist) {readImg(showImg);}
    }

    //处理图片
    public void showImg(View view){

        //img是ImageView的ID号
        showImg = findViewById(R.id.img);

        //图片的路径
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/123.jpg"; //一个绝对地址
        System.out.println(path);

        //判断文件是否存在, 存在该图片就显示出来
        boolean fileExist = fileIsExists(path);
        if(fileExist) {readImg(showImg);}

        //在textView组件里显示图片路径, 测试用
        textView.setText(path);
    }

    //读取并显示图片
    public void readImg(View view) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        showImg.setImageBitmap(bitmap);
    }

    //判断文件是否存在
    public boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }




    /*--------------------------------测试用的-----------------------------------------*/
    //处理图片 不带View参数
    public void showImg(){

        //img是ImageView的ID号
        showImg = findViewById(R.id.img);

        //图片的路径
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/123.jpg";
        System.out.println(path);

        //判断文件是否存在, 存在该图片就显示出来
        boolean fileExist = fileIsExists(path);
        if(fileExist) {readImg(showImg);}

        //在textView组件里显示图片路径, 测试用
        textView.setText(path);
    }

    //测试 JSON读图片
    public void FlashIMG() throws JSONException, IOException {
        //img是ImageView的ID号
        showImg = findViewById(R.id.img);

        MainActivity mainActivity = new MainActivity();

        String DIR = mainActivity.GetJson_Syn("name");
        System.out.println("DIR"+DIR);
//        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(DIR, Feature.OrderedField);

        //图片的路径
//        path = jsonObject.getString("dir");
        path= DIR;
        System.out.println(path);

        //判断文件是否存在, 存在该图片就显示出来
        boolean fileExist = fileIsExists(path);
        if(fileExist) {readImg(showImg);}

        //在textView组件里显示图片路径, 测试用
        textView.setText(DIR);
    }

}
