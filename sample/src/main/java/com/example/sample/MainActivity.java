package com.example.sample;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.SharedMemory;
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
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView mTvResult ; //doGet()里的TextView
    private TextView mTvResultPost ; //doPost()里的TextView
    private TextView mTvResultPostString ;//doPostString()里的TextView
    private TextView mTvResultPostJson ;//PostJson()里的TextView
    private String mBaseUrl = "http://10.0.2.2:8081/okhttp/"; //服务器地址
//    private String mBaseUrl2 = "http://192.168.235.128:8081/api/deploy/operate/5f7c5fb8dc71e37ab494cd92"; //服务器地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvResult = (TextView) findViewById(R.id.id_tv_result);
        mTvResultPost = (TextView) findViewById(R.id.id_tv_resultPost);
        mTvResultPostString = (TextView) findViewById(R.id.id_tv_resultPostString);
        mTvResultPostJson = (TextView) findViewById(R.id.id_tv_resultPostJson);
    }

    public void PostJson(View view) throws JSONException, IOException {
          PostJson();//不带View的Post方法3
    }

    /*
    OkHttpClient基本步骤：
    1.创建OKHTTPClient对象
    2.构造Request
    3.将Request封装为CALL
    4.异步方法，执行CALL
    */

    //GET方法
    public void doGet(View view) throws IOException {

        //1.创建OKHTTPClient对象
        OkHttpClient okHttpClient = new OkHttpClient();

        //2.构造Request
        Request.Builder builder = new Request.Builder();
        Request request = builder.get().url(mBaseUrl+"login?username=ydy_get&password=123&time=中文").build(); //login是服务端对应的方法名，？后面是参数

        //3.将Request封装为CALL
        Call call = okHttpClient.newCall(request);

        //4.异步方法，执行CALL, 还可以同步方式
        call.enqueue(new Callback() {
            @Override //执行错误时
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                e.printStackTrace();
            }

            @Override //执行成功时
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                final String res = response.body().string();//返回值

                System.out.println(res);

                //在UI线程中run
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvResult.setText(res);
                    }
                });
            }
        });
    }

    //POST方法1
    public void doPost(View view) {

        //1.创建OKHTTPClient对象
        OkHttpClient okHttpClient = new OkHttpClient(); //也可以全局创建

        //2.构造Request
            //2.1构造requestbody
        FormBody.Builder requestBodyBuilder = new FormBody.Builder();
        RequestBody requestBody = requestBodyBuilder
                                    .add("username","invoke")
                                    .add("password","a,b,5")
                                    .add("time","invoke")
//                                    .add("operationName","invoke")
//                                    .add("id","5f7c5fb8dc71e37ab494cd92")
//                                    .add("chainId","5f7c5bdb27fc210a879e8a6a")
                                    .build();
            //2.2构造postrequest
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(mBaseUrl+ "login").post(requestBody).build();

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

                final String res = response.body().string();//从服务器的返回值

                System.out.println(res);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvResultPost.setText(res); //设置TexiView的值
                    }
                });
            }
        });
    }

    //POST方法2
    public void doPostString(View view) throws JSONException {

        OkHttpClient okHttpClient = new OkHttpClient(); //也可以全局创建

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json,charset=utf-8"), "{\"functionName\":\"invoke\",\"args\":\"a,b,5\",\"operation\":\"invoke\",\"operationName\":\"invoke\",\"id\":\"5f7c5fb8dc71e37ab494cd92\",\"chainId\":\"5f7c5bdb27fc210a879e8a6a\"}");
//        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain;chaset=utf-8"),WriteJson());
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(mBaseUrl+"postString").post(requestBody).build(); //这里的postString 是服务端的一个方法

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

                final String res = response.body().string();//从服务的返回值

                System.out.println(res);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvResultPostString.setText(res); //设置TexiView的值
                    }
                });
            }
        });
    }

    //POST方法3
    public void PostJson() throws JSONException {

        OkHttpClient okHttpClient = new OkHttpClient(); //也可以全局创建
//        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        MediaType JSON = MediaType.parse("application/json; charset=gb2312");//中文的
        RequestBody requestBody = RequestBody.create(JSON,WriteJson()); //调用了WriteJson()
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(mBaseUrl + "postString").post(requestBody).build(); //postString是服务器端的一个方法名

        GetRequest(request);
        ReceiveJson();

    }


    public String WriteJson() throws JSONException {

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
    public String WriteJson2(String value) throws JSONException {

        //创建JSON对象，往里面放值
        JSONObject jsonObject =new JSONObject();
        try {
            jsonObject.put("functionName", "invoke");
            jsonObject.put("args","a,b,5");
            jsonObject.put("operation","invoke");
            jsonObject.put("operationName","invoke");
            jsonObject.put("id","5f7c5fb8dc71e37ab494cd92");
            jsonObject.put("chainId","5f7c5bdb27fc210a879e8a6a");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }
    public Request request;
    public void GetRequest(Request request){
        this.request=request;
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
//                System.out.println("JSON数组单个值： "+jsonArray.get(2));
                System.out.println("JSON对象获取[int_num]："+jsonObject.get("int_num"));
                System.out.println("JSON对象获取[Boolean]："+jsonObject.get("Boolean"));
                System.out.println("JSON对象获取[dir]"+jsonObject.get("dir"));
                System.out.println("JSON对象: "+jsonObject);
                System.out.println("String类型: "+res);
                System.out.println("JSON_toString类型: "+jsonObject.toString());
                System.out.println("JSON_toJSONString类型: "+jsonObject.toJSONString());
                System.out.println("*******************************************************************************************************************\n");



//                int i =0 ;
//                while (true){
//                    System.out.println(jsonArray.get(i));
//                    if (jsonArray.size()==i+1)
//                        break;
//                    else
//                        i++;
//                }

                //输出到TextView上面
                final String textJSON = jsonObject.toJSONString();
                runOnUiThread(new Runnable() {@Override public void run() { mTvResultPostJson.setText(textJSON); }});
            }
        });
    }








    /*----------------------------------测试方法可不看-----------------------------------*/
    public String GetJson_Syn(String key_value) throws IOException, JSONException {

        PostJson();
        OkHttpClient okHttpClient = new OkHttpClient();
        Response response = okHttpClient.newCall(request).execute();
        String result = response.body().string();

        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(result, Feature.OrderedField); //将服务器传回来的String类型的JSON串转换为JSON对象
        String json = jsonObject.get(key_value).toString();
        System.out.println("key_value:" + json + "\n");
        return json;
    }
    public String JGetJson_aSyn;
    public String GetJson_aSyn(final String key_value) throws IOException, JSONException {

        PostJson();

        OkHttpClient okHttpClient =new OkHttpClient();
        //3.将Request封装为CALL
        Call call = okHttpClient.newCall(request);
        //4.异步方法，执行CALL
        call.enqueue(new Callback() {
            @Override //执行错误时
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                System.out.println("失败");
            }

            @Override //执行成功时
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                final String res = response.body().string();//返回值

                com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(res, Feature.OrderedField); //将服务器传回来的String类型的JSON串转换为JSON对象

                System.out.println("JSON对象获取："+jsonObject.get(key_value)); //如果传回来的JSON Str 不全是STring类型会闪退

                final String textJSON = jsonObject.get(key_value).toString();

                GetJsonStr_aSyn(textJSON);
                System.out.println("J:"+JGetJson_aSyn);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() { mTvResultPostString.setText(textJSON);
//                    }
//                });
            }
        });

        return this.JGetJson_aSyn;
    }
    public String GetJsonStr_aSyn(String string) throws IOException {
        return this.JGetJson_aSyn=string;
    }
}
