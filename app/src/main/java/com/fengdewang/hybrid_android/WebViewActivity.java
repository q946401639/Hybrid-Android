package com.fengdewang.hybrid_android;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class WebViewActivity extends AppCompatActivity {

    private Handler uiHandler;

    private ActionBar actionBar;

    private WebView customWebView;
    private ProgressBar progressBar;

    private List<String> resumeEventsList;
    private List<String> pauseEventsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        //要展示的html页面的url
        String pageUrl = null;

        //获取scheme传递过来的参数
        Intent schemeIntent = getIntent();
        String scheme = schemeIntent.getScheme();
        String dataString = schemeIntent.getDataString();
        Uri schemeUri = schemeIntent.getData();

        //获取scheme上的url参数
        if(schemeUri != null){
            if(TextUtils.equals(schemeUri.getHost(), "openPage")){
                pageUrl = schemeUri.getQueryParameter("url");
            } else if(TextUtils.equals(schemeUri.getHost(), "emptyPage")) {
                finish();
            }
        }


        //activity intent url传参
        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null && bundle.getString("url") != null){
            pageUrl = bundle.getString("url");
        }
        System.out.println(pageUrl);

        //自定义handler，用以在UI主进程以外的地方 去修改UI
        uiHandler = new WebViewUIHandler(WebViewActivity.this);

        //获取actionBar
        actionBar = getSupportActionBar();
        initTitleBar();

        //初始化加载进度条
        progressBar = findViewById(R.id.progressBar);
        progressBar.setAlpha(0);

        customWebView = findViewById(R.id.customWebView);

        //设置webview
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


        // 修改ua使得web端正确判断
        String ua = settings.getUserAgentString();
        settings.setUserAgentString(ua + "; JSBridge_Android");


        //原生webview load url 会直接跳转至系统浏览器，所以重新覆盖此跳转
        customWebView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //拦截自定义scheme
                Uri uri = Uri.parse(url);

                if(uri.getScheme().equals("jsbridge")){

                    WebViewActivity.this.customScheme(uri);

                    return true;
                } else if(uri.getScheme().equals("http") || uri.getScheme().equals("https") || uri.getScheme().equals("file")) {
                    //原生webview load url 会直接跳转至系统浏览器，所有重新覆盖此跳转
                    view.loadUrl(url);

                    return false;
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);

//                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    return true;
                }

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //super.onPageStarted(view, url, favicon);
                //todo 开始加载网页 显示loading
                System.out.println(url);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //super.onPageFinished(view, url);
                //todo 网页加载结束 隐藏loading
                System.out.println(url);
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

        //对webview内的网页 进行事件拦截
        customWebView.setWebChromeClient(new WebChromeClient(){

            //加载过程的钩子拦截  可用于进度条展示
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //super.onProgressChanged(view, newProgress);

                if(newProgress >= 100){
                    progressBar.setAlpha(0);
                } else {
                    progressBar.setAlpha(1);
                    progressBar.setProgress(newProgress);
                }

            }

            //html页面的title获取
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                //todo 设置title
                actionBar.setTitle(title);
            }

            //html中alert的拦截
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
        JSBridge jsbridge = new JSBridge(WebViewActivity.this, uiHandler);
        customWebView.addJavascriptInterface(jsbridge, "JSBridgeAndroid");

        //如果没有 pageUrl 则默认打开本地demo测试页面
        if(pageUrl != null){
            customWebView.loadUrl(pageUrl);
        } else {
            customWebView.loadUrl("file:///android_asset/JSBridgeDemo.html");
        }

        //在管理webview堆栈中 添加此实例
        WebViewActivityManager.addActivity(this);

    }

    /**
     * 自定义菜单 创建右上角刷新按钮
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.webview_action_bar, menu);
        MenuItem reloadBtn = menu.findItem(R.id.reloadBtn);

        reloadBtn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                customWebView.reload();

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 监听右下角返回键
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //右下角返回按钮 先判断webview是否为可后退状态 ，是的话优先进行 history.back()
        if(keyCode == KeyEvent.KEYCODE_BACK && customWebView.canGoBack()){

            customWebView.goBack();
            return true;

        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 监听左上角返回键
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //左上角按钮 直接关闭webview
        if(item.getItemId() == android.R.id.home){

            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Activity销毁的钩子
     */
    @Override
    protected void onDestroy() {

        if(customWebView != null){
            customWebView.clearHistory();
            customWebView.destroy();
            customWebView = null;
        }

        super.onDestroy();
    }

    /**
     * Activity暂停 或者 压入后台的钩子
     */
    @Override
    protected void onStop() {
        super.onStop();

        customWebView.onPause();

        if(pauseEventsList != null){

            for(int i = 0; i < pauseEventsList.size(); i++){
                System.out.println(pauseEventsList.get(i));
                customWebView.evaluateJavascript("JSBridge.eventMap['" + pauseEventsList.get(i) + "']()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        System.out.println(s);
                    }
                });
            }

        }
    }

    /**
     * Activity重新唤醒的钩子
     */
    @Override
    protected void onResume() {
        super.onResume();

        customWebView.onResume();

        if(resumeEventsList != null){

            for(int i = 0; i < resumeEventsList.size(); i++){
                System.out.println(resumeEventsList.get(i));
                customWebView.evaluateJavascript("JSBridge.eventMap['" + resumeEventsList.get(i) + "']()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        System.out.println(s);
                    }
                });
            }

        }
    }

    //设置 actionBar的显示隐藏
    public void setTitleVisibility(Boolean show){

        if(!show){
            actionBar.hide();
        } else {
            actionBar.show();
        }

    }

    //初始化设置actionBar
    public void initTitleBar(){
        actionBar.setDisplayHomeAsUpEnabled(true); //是否在左侧返回区域显示返回箭头，默认不显示
        actionBar.setDisplayShowTitleEnabled(true); //是否在左侧返回区域显示左侧标题，默认显示APP名称   setTitle : 设置左侧标题的文本
    }

    //添加resume事件
    public void addResumeEvent(String event){
        if(resumeEventsList == null){
            resumeEventsList = new ArrayList<String>();
        }
        resumeEventsList.add(event);
        System.out.println("======================");
        System.out.println(resumeEventsList);
    }

    //添加pause事件
    public void addPauseEvent(String event){
        if(pauseEventsList == null){
            pauseEventsList = new ArrayList<String>();
        }
        pauseEventsList.add(event);
        System.out.println("======================");
        System.out.println(pauseEventsList);
    }

    //打开demo页面
    public void openPage(){

        Intent intent = new Intent(WebViewActivity.this, WebViewActivity.class);
        startActivity(intent);

    }

    //打开指定url页面
    public void openPage(String pageUrl){

        Intent intent = new Intent(WebViewActivity.this, WebViewActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("url", pageUrl);
        intent.putExtras(bundle);

        startActivity(intent);

    }

    //pop webview页面
    public void popPage(int step){

        WebViewActivityManager.finishActivity(step);

//        if(step == 1){
//            finish();
//        } else {
//            System.out.println("关闭 " + step + "个webview");
//        }

    }


    //自定义scheme拦截
    public void customScheme(Uri uri){

        if(TextUtils.equals(uri.getAuthority(), "openPage")){
            HashMap<String, String> params = new HashMap<>();
            Set<String> query = uri.getQueryParameterNames();

            for(String key : query){
                params.put(key, uri.getQueryParameter(key));
            }

            Intent intent = new Intent(WebViewActivity.this, WebViewActivity.class);

            Bundle bundle = new Bundle();
            bundle.putString("url", params.get("url"));
            intent.putExtras(bundle);

            startActivity(intent);
        }

    }

}
