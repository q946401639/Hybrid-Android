package com.fengdewang.hybrid_android;


import android.os.Handler;
import android.os.Message;
import android.webkit.JavascriptInterface;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by fengdewang on 2017/12/28.
 *
 * android 4.2 以上  JSBridge 需要加 @JavascriptInterface 防止 恶意js调用系统方法
 *
 */

public class JSBridge extends Object{

    private WebViewActivity webViewActivity;
    private Handler handler;

    public JSBridge(){

    }

    public JSBridge(WebViewActivity webViewActivity){
        this.webViewActivity = webViewActivity;
    }

    public JSBridge(Handler handler){
        this.handler = handler;
    }

    public JSBridge(WebViewActivity webViewActivity, Handler handler){
        this.webViewActivity = webViewActivity;
        this.handler = handler;
    }

    //获取uid test
    @JavascriptInterface
    public void getDeviceId(final String method){
        final String cb = method;
        webViewActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webViewActivity.runJS(cb, "uid: 1234567890");
            }
        });
    }

    //隐藏actionBar
    @JavascriptInterface
    public void hideTitle(){

        //注释部分为耦合性较强的方式 去调用webViewActivity的方法
//        webViewActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                webViewActivity.setTitleVisibility(false);
//            }
//        });
//        Message msg = handler.obtainMessage();
//        msg.obj = "hideTitle";
        //UI主进程以外的地方 修改UI 需要 用handle机制去传递消息
        handler.sendEmptyMessage(1);

    }

    //显示actionBar
    @JavascriptInterface
    public void showTitle(){
        //注释部分为耦合性较强的方式 去调用webViewActivity的方法
//        webViewActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                webViewActivity.setTitleVisibility(true);
//            }
//        });
        //UI主进程以外的地方 修改UI 需要 用handle机制去传递消息
        handler.sendEmptyMessage(2);

    }

    //添加resume 事件 （后台切回前台）
    @JavascriptInterface
    public void addResumeEvent(String resumeEvent){
        System.out.println("============add resume===========");
        webViewActivity.addResumeEvent(resumeEvent);
    }

    //添加pause 事件 （前台切到后台）
    @JavascriptInterface
    public void addPauseEvent(String pauseEvent){
        System.out.println("============add pause===========");
        webViewActivity.addPauseEvent(pauseEvent);
    }

    //打开页面 默认为打开本地demo页面
    @JavascriptInterface
    public void openPage(){
        webViewActivity.openPage();
    }

    //打开新的webview页面
    @JavascriptInterface
    public void openPage(String params){

        String pageUrl = null;

        try {
            JSONObject jsonObject = new JSONObject(params);
            pageUrl = jsonObject.getString("url");
        } catch (JSONException e){
            e.printStackTrace();
        }

        webViewActivity.openPage(pageUrl);
    }

    //pop一个webview页面
    @JavascriptInterface
    public void popPage(String params){
        int step = 1;

        try {
            JSONObject jsonObject = new JSONObject(params);
            step = jsonObject.getInt("step");
        } catch (JSONException e){
            e.printStackTrace();
        }

        webViewActivity.popPage(step);
    }



}
