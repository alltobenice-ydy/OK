package com.example.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;


import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
public class DemoJSON {
    private String mBaseUrl = "http://10.0.2.2:8081/okhttp/"; //服务器地址

    public static Request request;
    public void GetRequest(Request request){
        this.request=request;
    }

    public void PostJson() throws JSONException {

//        OkHttpClient okHttpClient = new OkHttpClient(); //也可以全局创建
//        MediaType JSON = MediaType.parse("application/json; charset=utf8");
        MediaType JSON = MediaType.parse("application/json; charset=gb2312");//中文的
        RequestBody requestBody = RequestBody.create(JSON,JSONObject_full()); //调用了JSONOBJect_full写入函数
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(mBaseUrl + "postString").post(requestBody).build(); //这里的post String 会与服务端交互 服务端会选择对应的方法
        GetRequest(request);
        ReceiveJson();

    }


    public String JSONObject_full() throws JSONException {

        //创建一个JSON数组
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(0,"0");
        jsonArray.put(1,"1");
        jsonArray.put(2,"abcd");
        jsonArray.put(3, "ALI");


        //创建JSON对象，往里面放值
        JSONObject jsonObject =new JSONObject();
        try {
            jsonObject.put("name", "ydy");
            jsonObject.put("int_num",1);
            jsonObject.put("boolean",true);
            jsonObject.put("array",jsonArray);
            jsonObject.put("cn","中文测试字段");
            jsonObject.put("dir","/storage/sdcard/123.jpg");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    private void ReceiveJson() {

        OkHttpClient okHttpClient =new OkHttpClient();
        //3.将Request封装为CALL
        Call call = okHttpClient.newCall(request);

        //4.异步方法，执行CALL
        call.enqueue(new Callback() {
            @Override //执行错误时
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override //执行成功时
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                final String res = response.body().string();//返回值

                // 加上 Feature.OrderedField 参数是为了让字符串顺序一致
                com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(res, Feature.OrderedField); //将服务器传回来的String类型的JSON串转换为JSON对象
                com.alibaba.fastjson.JSONArray jsonArray=jsonObject.getJSONArray("array");

                System.out.println("*********************************************************************************************************\n");
                System.out.println("JSON对象获取[name]："+jsonObject.get("name")); //如果传回来的JSON Str 不全是STring类型会闪退
                System.out.println("JSON对象获取[array]："+jsonObject.get("array"));
                System.out.println("JSON数组单个值： "+jsonArray.get(2));
                System.out.println("JSON对象获取[int_num]："+jsonObject.get("int_num"));
                System.out.println("JSON对象获取[Boolean]："+jsonObject.get("Boolean"));
                System.out.println("JSON对象获取[dir]"+jsonObject.get("dir"));
                System.out.println("JSON对象: "+jsonObject);
                System.out.println("String类型: "+res);
                System.out.println("JSON_toString类型: "+jsonObject.toString());
                System.out.println("JSON_toJSONString类型: "+jsonObject.toJSONString());
                System.out.println("*******************************************************************************************************************\n");



                int i =0 ;
                while (true){
                    System.out.println(jsonArray.get(i));
                    if (jsonArray.size()==i+1)
                        break;
                    else
                        i++;
                }

                //输出到TextView上面
//                final String textJSON = jsonObject.toJSONString();
//                runOnUiThread(new Runnable() {@Override public void run() { mTvResultPostJson.setText(textJSON); }});
            }
        });
    }
}
