package com.fengdewang.hybrid_android;


import android.webkit.JavascriptInterface;


/**
 * Created by fengdewang on 2017/12/28.
 *
 * android 4.2 以上  JSBridge 需要加 @JavascriptInterface 防止 恶意js调用系统方法
 *
 */

public class JSBridge extends Object{



    public JSBridge(){

    }

    @JavascriptInterface
    public String getUID(){
        return "UID: 1234567890";
    }

    @JavascriptInterface
    public void hideTitle(){

    }

    @JavascriptInterface
    public void showTitle(){

    }

}
