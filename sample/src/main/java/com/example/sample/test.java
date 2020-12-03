package com.example.sample;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class test extends AppCompatActivity {

    class JsObject{
        @JavascriptInterface
        public String toString(){
            return  "injectedObject";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JsObject(), "injectObject"); //新建JsObject,并注入到js的injectObject字符串中
        webView.loadData("", "text/html", null);
        webView.loadUrl("http:/10.0.2.2:8000/polls/shell/"); //web服务器地址,根据自己的来

        /*启用alert弹窗,默认是阻止的*/
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });

        /*设置为不跳转自带浏览器*/
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

    }

}