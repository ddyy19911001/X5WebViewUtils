package yin.deng.x5webviewutils;

import android.app.Application;
import android.util.Log;
import java.util.HashMap;

import yin.deng.x5webviewutil.web.X5WebView;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        X5WebView.initX5(this);
    }


}
