package com.fengdewang.hybrid_android;


import android.os.Handler;
import android.os.Message;
import android.webkit.JavascriptInterface;




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

    @JavascriptInterface
    public String getUID(){
        return "UID: 1234567890";
    }

    @JavascriptInterface
    public void hideTitle(){

//        webViewActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                webViewActivity.setTitleVisibility(false);
//            }
//        });
//        Message msg = handler.obtainMessage();
//        msg.obj = "hideTitle";
        handler.sendEmptyMessage(1);

    }

    @JavascriptInterface
    public void showTitle(){

//        webViewActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                webViewActivity.setTitleVisibility(true);
//            }
//        });
        handler.sendEmptyMessage(2);

    }

}
