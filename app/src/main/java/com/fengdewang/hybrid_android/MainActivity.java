package com.fengdewang.hybrid_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button gotoWebviewBtn;
    private Button clearBtn;
    private WebView globalWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        globalWebView = new WebView(this);

        gotoWebviewBtn = findViewById(R.id.gotoWebView);
        clearBtn = findViewById(R.id.clearBtn);

        gotoWebviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("跳转至webview的avtivity");
                Toast.makeText(MainActivity.this, "跳转至WebView页面", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                startActivity(intent);

            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                globalWebView.clearCache(true);

                Toast.makeText(MainActivity.this, "清除缓存成功", Toast.LENGTH_LONG).show();
            }
        });
    }
}


