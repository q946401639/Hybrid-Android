package com.fengdewang.hybrid_android;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;


/**
 * Created by fengdewang on 2017/12/29.
 */

public class WebViewUIHandler extends Handler {

    //添加WebViewActivity的弱引用 以防止内存泄露
    private final WeakReference<WebViewActivity> webViewActivityWeakReference;
    public WebViewUIHandler(WebViewActivity webViewActivity){

        this.webViewActivityWeakReference = new WeakReference<WebViewActivity>(webViewActivity);

    }

    @Override
    public void handleMessage(Message msg){
        super.handleMessage(msg);

        WebViewActivity webViewActivity = webViewActivityWeakReference.get();

        //UI主进程以外的地方 修改UI 需要 用handle机制去传递消息
        switch (msg.what){
            case 1:
                webViewActivity.setTitleVisibility(false); //隐藏title
                break;
            case 2:
                webViewActivity.setTitleVisibility(true); //显示title
                break;
            default:
                break;
        }

    }

}
