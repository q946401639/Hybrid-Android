package com.fengdewang.hybrid_android;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

    private WebView customWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        customWebView = findViewById(R.id.customWebView);

        WebSettings settings = customWebView.getSettings();

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        settings.setJavaScriptEnabled(true);

        //设置自适应屏幕，两者合用
        settings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        settings.setSupportZoom(false);

        //其他细节操作
        //关闭webview中缓存
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //设置可以访问文件
        settings.setAllowFileAccess(true);
        //支持通过JS打开新窗口 允许JS弹窗
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        //支持自动加载图片
        settings.setLoadsImagesAutomatically(true);
        //设置编码格式
        settings.setDefaultTextEncodingName("utf-8");

        //离线加载

//        if(NetStatusUtil){
//
//        }

        // 开启 DOM storage API 功能，就是localStorage
        settings.setDomStorageEnabled(true);
        //开启 database storage API 功能
        settings.setDatabaseEnabled(true);
        //开启 Application Caches 功能
        settings.setAppCacheEnabled(true);

        //原生webview load url 会直接跳转至系统浏览器，所有重新覆盖此跳转
        customWebView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                view.loadUrl(request.getUrl().toString());

                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //super.onPageStarted(view, url, favicon);
                //todo 开始加载网页 显示loading
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //super.onPageFinished(view, url);
                //todo 网页加载结束 隐藏loading
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                //super.onReceivedError(view, request, error);
                //todo 加载失败了 显示404

            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //处理https请求 默认不支持https

                super.onReceivedSslError(view, handler, error);
                handler.proceed();//表示等待证书响应

            }
        });

        customWebView.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //super.onProgressChanged(view, newProgress);

                if(newProgress >= 100){

                } else {

                }

            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                //super.onReceivedTitle(view, title);
                //todo 设置title
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {

                //todo alert拦截
                AlertDialog.Builder alert = new AlertDialog.Builder(WebViewActivity.this);
                alert.setTitle("提示");
                alert.setMessage(message);
                alert.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        result.confirm();
                    }
                });
                alert.setCancelable(false);
                alert.create().show();

                return true;
            }
        });


        // 通过addJavascriptInterface()将Java对象映射到JS对象
        //参数1：Java对象名
        //参数2：Javascript对象名
        JSBridge jsbridge = new JSBridge();
        customWebView.addJavascriptInterface(jsbridge, "JSBridge");


        customWebView.loadUrl("file:///android_asset/JSBridgeDemo.html");
        //customWebView.loadUrl("https://www.baidu.com");

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK && customWebView.canGoBack()){

            customWebView.goBack();
            return true;

        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {

        if(customWebView != null){
            customWebView.clearHistory();
            customWebView.destroy();
            customWebView = null;
        }

        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();

        customWebView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        customWebView.onResume();
    }
}
