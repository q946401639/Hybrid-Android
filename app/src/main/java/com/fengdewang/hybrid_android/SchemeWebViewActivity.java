package com.fengdewang.hybrid_android;



/**
 * Created by fengdewang on 2018/1/4.
 * 继承WebViewActivity自定义webview，以承接外部scheme唤起并跳转至指定页面的功能
 * 在Manifest中设置launchMode="singleTask"  可以使用户在此Activity中点击返回按键时候 继续保持在此app内，如果未设置launchMode 则跳回至外部浏览器
 * js唤起 android的方法：
 * 1： {schemeName}://{host}/{path}?{query=xxx}
 * 2：intent://{host}/{path}?{query=xxx}#Intent;scheme={schemeName};package={packageName};end
 */

public class SchemeWebViewActivity extends WebViewActivity {



}
