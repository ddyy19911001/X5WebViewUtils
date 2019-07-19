package yin.deng.x5webviewutil.web;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;

import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.utils.TbsLog;

import java.util.HashMap;



public class X5WebView extends WebView {
	Context context;
	private WebViewClient client = new WebViewClient() {
		/**
		 * 防止加载网页时调起系统浏览器
		 */
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	};


	/**
	 * 注意在onResume和onPause的时候也要执行x5webview的onResume和onPause方法
	 * 在摧毁的时候调用x5webview.destroy的方法，保证不会内存溢出
	 */

	public void onDestroy(){
		destroy();
	}


	public void x5GoBack(X5WebView x5WebView){
		if(x5WebView!=null&&x5WebView.canGoBack()){
			x5WebView.goBack();
		}
	}

	//一般用以下配置即可让View获得相对完整的功能
	public  void initX5WebGoodSetting() {
			WebSettings webSetting = getSettings();
			webSetting.setSavePassword(true);
			webSetting.setSaveFormData(true);// 保存表单数据
			webSetting.setAllowUniversalAccessFromFileURLs(true);
			webSetting.setAllowFileAccess(true);
			webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
			webSetting.setSupportZoom(true);
			webSetting.setBuiltInZoomControls(true);
			webSetting.setUseWideViewPort(true);
			webSetting.setSupportMultipleWindows(false);
			webSetting.setAppCacheEnabled(true);
			webSetting.setDomStorageEnabled(true);
			webSetting.setJavaScriptEnabled(true);
			webSetting.setGeolocationEnabled(true);
			webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
			webSetting.setAppCachePath(context.getDir("appcache", 0).getPath());
			webSetting.setDatabasePath(context.getDir("databases", 0).getPath());
			webSetting.setGeolocationDatabasePath(context.getDir("geolocation", 0)
					.getPath());
			webSetting.setPluginState(WebSettings.PluginState.ON);
			long time = System.currentTimeMillis();
			TbsLog.d("time-cost", "cost time: "
					+ (System.currentTimeMillis() - time));
			CookieSyncManager.createInstance(context);
			CookieSyncManager.getInstance().sync();
	}


	public static void initX5(Application application){
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
		QbSdk.initTbsSettings(map);
		QbSdk.setDownloadWithoutWifi(true);
		QbSdk.initX5Environment(application, new QbSdk.PreInitCallback() {
			@Override
			public void onCoreInitFinished() {
				Log.w("初始化","X5浏览器已执行初始化");
			}

			@Override
			public void onViewInitFinished(boolean b) {
				Log.i("初始化","X5浏览器初始化："+(b?"初始化成功":"初始化失败"));
			}
		});
	}

	public static void initX5(Application application,QbSdk.PreInitCallback callback){
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
		QbSdk.initTbsSettings(map);
		QbSdk.setDownloadWithoutWifi(true);
		if(callback!=null){
			QbSdk.initX5Environment(application,callback);
		}else {
			QbSdk.initX5Environment(application, new QbSdk.PreInitCallback() {
				@Override
				public void onCoreInitFinished() {
					Log.i("初始化", "X5浏览器初始化：完成");
				}

				@Override
				public void onViewInitFinished(boolean b) {
					Log.i("初始化", "X5浏览器初始化：" + (b ? "初始化成功" : "初始化失败"));
				}
			});
		}
	}


	@SuppressLint("SetJavaScriptEnabled")
	public X5WebView(Context arg0, AttributeSet arg1) {
		super(arg0, arg1);
		this.setWebViewClient(client);
		// this.setWebChromeClient(chromeClient);
		// WebStorage webStorage = WebStorage.getInstance();
		this.context=arg0;
		initX5WebGoodSetting();
		this.getView().setClickable(true);
	}

//	private void initWebViewSettings() {
//		WebSettings webSetting = this.getSettings();
////		webSetting.setMediaPlaybackRequiresUserGesture(false);
//		webSetting.setJavaScriptEnabled(true);
//		webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
//		webSetting.setAllowFileAccess(true);
//		webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
//		webSetting.setSupportZoom(true);
//		webSetting.setBuiltInZoomControls(true);
//		webSetting.setUseWideViewPort(true);
//		webSetting.setSupportMultipleWindows(true);
//		webSetting.setAppCacheEnabled(true);
//		webSetting.setDomStorageEnabled(true);
//		webSetting.setGeolocationEnabled(true);
//		webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
//		webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
//		webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
//		setWebViewClient(new WebViewClient(){
//			@Override
//			public boolean shouldOverrideUrlLoading(WebView webView, String url) {
//				if (url.startsWith("http:") || url.startsWith("https:")) {
//					return false;
//				}
//				try {
//					final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//					context.startActivity(intent);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				return true;
//			}
//		});
//	}


	public X5WebView(Context arg0) {
		super(arg0);
		setBackgroundColor(85621);
	}


	public void switchIsNeedFullPlayVideo(int type) {

		if (getX5WebViewExtension() != null) {
			Bundle data = new Bundle();

			data.putBoolean("standardFullScreen", false);// true表示标准全屏，false表示X5全屏；不设置默认false，

			data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

			data.putInt("DefaultVideoScreen", type);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

			getX5WebViewExtension().invokeMiscMethod("setVideoParams",
					data);
		}
	}


}
