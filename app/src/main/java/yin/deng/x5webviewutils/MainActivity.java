package yin.deng.x5webviewutils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import yin.deng.x5webviewutil.web.X5WebView;

public class MainActivity extends AppCompatActivity {
    private X5WebView x5webView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        x5webView = (X5WebView) findViewById(R.id.x5web_view);
        x5webView.loadUrl("https://www.iqshw.com");
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
