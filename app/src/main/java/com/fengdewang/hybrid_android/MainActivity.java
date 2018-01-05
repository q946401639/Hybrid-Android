package com.fengdewang.hybrid_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private Button gotoWebviewBtn;
    private Button clearBtn;
    private Button scanBtn;
    private WebView globalWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        globalWebView = new WebView(this);

        gotoWebviewBtn = findViewById(R.id.gotoWebView);
        clearBtn = findViewById(R.id.clearBtn);
        scanBtn = findViewById(R.id.scanBtn);

        //绑定跳转到下一页的点击事件
        gotoWebviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("跳转至webview的avtivity");
                Toast.makeText(MainActivity.this, "跳转至WebView页面", Toast.LENGTH_LONG).show();

                //intent 跳转
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);

//                Bundle bundle = new Bundle();
//                bundle.putString("url", "http://www.baidu.com");
//                intent.putExtras(bundle);

                startActivity(intent);

            }
        });

        //清空浏览器缓存 （全局清空）
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                globalWebView.clearCache(true);

                Toast.makeText(MainActivity.this, "清除缓存成功", Toast.LENGTH_LONG).show();
            }
        });


        //跳转至扫一扫页面
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("跳转至扫一扫页面");
                Toast.makeText(MainActivity.this, "跳转至扫一扫页面", Toast.LENGTH_LONG).show();

                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);

                intentIntegrator.setPrompt("请扫描");//底部的提示文字，设为""可以置空
                intentIntegrator.setCameraId(0);//前置或者后置摄像头
                //intentIntegrator.setBeepEnabled(false);//扫描成功的「哔哔」声，默认开启
                intentIntegrator.initiateScan();


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //获取二维码解析结果
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result != null){
            if(result.getContents() == null){
                Toast.makeText(this, "取消扫描", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "扫描内容为：" + result.getContents(), Toast.LENGTH_LONG).show();

                if(result.getContents().startsWith("http")){

                    //intent 跳转
                    Intent intent = new Intent(this, WebViewActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("url", result.getContents());
                    intent.putExtras(bundle);

                    startActivity(intent);
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}


