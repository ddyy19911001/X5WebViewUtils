package yin.deng.x5webviewutils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import yin.deng.x5webviewutil.web.ProgressView;
import yin.deng.x5webviewutil.web.X5WebView;

public class MainActivity extends AppCompatActivity {
    private X5WebView x5webView;
    private ProgressView progressView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressView=findViewById(R.id.progressView);
        x5webView = (X5WebView) findViewById(R.id.x5web_view);
        x5webView.initDefaultSetting(progressView);
        x5webView.loadUrl("https://github.com");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(x5webView!=null) {
            x5webView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(x5webView!=null) {
            x5webView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(x5webView!=null){
            x5webView.onDestroy();
        }
    }
}
