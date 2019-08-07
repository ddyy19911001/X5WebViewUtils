package yin.deng.x5webviewutil.web;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.utils.TbsLog;

import java.util.HashMap;


public class X5WebView extends WebView {
	public ProgressView progressView;//进度条
	Context context;
	OnWebPageChangeListener onWebPageChangeListener;
	public String[]adLinks;
	public String homeLinkUrl;
	OnShouldOverridUrlListener overridAndInterCeptListener;
	OnShouldInterCeptUrlListener interCeptUrlListener;
	public void setOnShouldOverridUrlListener(OnShouldOverridUrlListener overridAndInterCeptListener) {
		this.overridAndInterCeptListener = overridAndInterCeptListener;
	}

	public interface OnShouldOverridUrlListener{
		void onIntentOpenOtherApp(WebView webView, String url);
	}

	public void setOnShouldInterCeptUrlListener(OnShouldInterCeptUrlListener interCeptUrlListener) {
		this.interCeptUrlListener = interCeptUrlListener;
	}

	public interface OnShouldInterCeptUrlListener{
		WebResourceResponse onShouldInterCeptUrl(WebView webView,String url);
	}

	@SuppressLint("SetJavaScriptEnabled")
	public X5WebView(final Context context, AttributeSet attr) {
		super(context, attr);
		this.context=context;
		initDefaultSetting();
	}


	public void initDefaultSetting(){
		initProgressView();
		initX5WebGoodSetting();
		setWebViewClient(client);
		setWebChromeClient(chromeClient);
		setDownloadListener(myDownLoadListener);
		getView().setClickable(true);
	}

	public void initDefaultSetting(ProgressView progressView){
		this.progressView=progressView;
		initProgressView();
		initX5WebGoodSetting();
		setWebViewClient(client);
		setWebChromeClient(chromeClient);
		setDownloadListener(myDownLoadListener);
		getView().setClickable(true);
	}


	public void initDefaultSetting(ProgressView progressView,OnWebPageChangeListener onWebPageChangeListener){
		this.progressView=progressView;
		this.onWebPageChangeListener=onWebPageChangeListener;
		initProgressView();
		initX5WebGoodSetting();
		setWebViewClient(client);
		setWebChromeClient(chromeClient);
		setDownloadListener(myDownLoadListener);
		getView().setClickable(true);
	}


	public void initDefaultSetting(OnWebPageChangeListener onWebPageChangeListener){
		this.onWebPageChangeListener=onWebPageChangeListener;
		initProgressView();
		initX5WebGoodSetting();
		setWebViewClient(client);
		setWebChromeClient(chromeClient);
		setDownloadListener(myDownLoadListener);
		getView().setClickable(true);
	}

	public void initDefaultSetting(String[] adLinks,String homeLinkUrl,OnWebPageChangeListener onWebPageChangeListener){
		this.adLinks=adLinks;
		this.homeLinkUrl=homeLinkUrl;
		this.onWebPageChangeListener=onWebPageChangeListener;
		initProgressView();
		initX5WebGoodSetting();
		setWebViewClient(client);
		setWebChromeClient(chromeClient);
		setDownloadListener(myDownLoadListener);
		getView().setClickable(true);
	}

	public void initDefaultSetting(String[] adLinks,String homeLinkUrl,OnWebPageChangeListener onWebPageChangeListener,ProgressView progressView){
		this.progressView=progressView;
		this.adLinks=adLinks;
		this.homeLinkUrl=homeLinkUrl;
		this.onWebPageChangeListener=onWebPageChangeListener;
		initProgressView();
		initX5WebGoodSetting();
		setWebViewClient(client);
		setWebChromeClient(chromeClient);
		setDownloadListener(myDownLoadListener);
		getView().setClickable(true);
	}



	private WebViewClient client = new WebViewClient() {
		/**
		 * 防止加载网页时调起系统浏览器
		 */
		public boolean shouldOverrideUrlLoading(WebView view, final String url) {
			if(overridAndInterCeptListener!=null){
				if ( urlCanLoad(url.toLowerCase()))
				{  // 加载正常网页
					view.loadUrl(url);
				}
				else
				{  // 打开第三方应用或者下载apk等
					overridAndInterCeptListener.onIntentOpenOtherApp(view,url);
				}
			}else {
				shouldOverrideUrlLoadingInX5(view, url);
			}
			return true;
		}

		@Override
		public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
			if(onWebPageChangeListener!=null){
				onWebPageChangeListener.onPageStart(webView,s);
			}
			super.onPageStarted(webView, s, bitmap);
		}

		@Override
		public void onPageFinished(WebView webView, String s) {
			super.onPageFinished(webView, s);
			if(onWebPageChangeListener!=null){
				onWebPageChangeListener.onPageFinished(webView,s);
			}
		}

		@Override
		public void onReceivedSslError(WebView webView, SslErrorHandler handler, SslError sslError) {
			handler.proceed();
		}

		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view, String url){
			return shouldInterceptRequestInX5(view, url, super.shouldInterceptRequest(view, url));
		}
	};


	public WebChromeClient chromeClient=new WebChromeClient(){
		@Override
		public void onReceivedTitle(WebView webView, String s) {
			super.onReceivedTitle(webView, s);
			if(onWebPageChangeListener!=null){
				onWebPageChangeListener.onReceivedTitle(webView,s);
			}
		}

		@Override
		public void onProgressChanged(WebView webView, int i) {
			super.onProgressChanged(webView, i);
			if(progressView!=null&&i<100&&i>10){
				progressView.setVisibility(VISIBLE);
				progressView.setProgress(i);
			}else{
				 if(progressView!=null){
					progressView.setVisibility(GONE);
					progressView.setProgress(10);
				}
			}
		}
	};


	public DownloadListener myDownLoadListener=new DownloadListener() {
		@Override
		public void onDownloadStart(final String url, final String userAgent, final String contentDisposition, final String mimetype, final long contentLength) {
			AlertDialog.Builder builder=new AlertDialog.Builder(context);
			builder.setTitle("系统提示");
			builder.setMessage("是否打开浏览器下载该文件？");
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					Log.e("X5WebView", "--- onDownloadStart ---" + " url = " + url + ", userAgent = " + userAgent + ", " +
							"contentDisposition = " + contentDisposition + ", mimetype = " + mimetype + ", contentLength = " + contentLength);
					downloadByBrowser(url);
				}
			});
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
		}
	};

	/**
	 * 处理各种intent
	 * @param view
	 * @param url
	 */
	public void shouldOverrideUrlLoadingInX5(WebView view, final String url) {
		if ( urlCanLoad(url.toLowerCase()))
		{  // 加载正常网页
			view.loadUrl(url);
		}
		else
		{  // 打开第三方应用或者下载apk等
			AlertDialog.Builder builder=new AlertDialog.Builder(context);
			builder.setTitle("系统提示");
			builder.setMessage("此网页请求打开第三方应用，是否允许？");
			builder.setPositiveButton("允许打开", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					startThirdpartyApp(url);
				}
			});
			builder.setNegativeButton("拒绝打开", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
		}
	}


	/**
	 * 处理拦截广告
	 * @param view
	 * @param url
	 * @param webResourceResponse
	 * @return
	 */
	public WebResourceResponse shouldInterceptRequestInX5(WebView view, String url, WebResourceResponse webResourceResponse) {
		url= url.toLowerCase();
		if(!TextUtils.isEmpty(homeLinkUrl) &&!url.contains(homeLinkUrl)){
			if(!ADFilterTool.hasAd(url,adLinks)){
				if(interCeptUrlListener!=null){
					return interCeptUrlListener.onShouldInterCeptUrl(view,url);
				}else {
					return webResourceResponse;
				}
			}else{
				return new WebResourceResponse(null,null,null);
			}
		}else{
			if(interCeptUrlListener!=null){
				return interCeptUrlListener.onShouldInterCeptUrl(view,url);
			}else {
				return webResourceResponse;
			}
		}
	}


	public interface OnWebPageChangeListener{
		void onReceivedTitle(WebView webView, String s);
		void onPageStart(WebView webView, String s);
		void onPageFinished(WebView webView, String s);
	}

	/**
	 * 列举正常情况下能正常加载的网页url
	 * @param url
	 * @return
	 */
	public boolean urlCanLoad(String url)
	{
		return url.startsWith("http://") || url.startsWith("https://") ||
				url.startsWith("ftp://") || url.startsWith("file://");
	}


	/**
	 * 打开第三方app。如果没安装则跳转到应用市场
	 * @param url
	 */
	public void startThirdpartyApp(String url)
	{
		try {
			Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME); // 注释1
			context.startActivity(intent);
		}
		catch (Exception e)
		{
			Log.e("X5WebView", e.getMessage());
			Toast.makeText(context,"无法打开该应用！",Toast.LENGTH_SHORT).show();
		}
	}


	/**
	 * 通过浏览器下载
	 * @param url
	 */
	private void downloadByBrowser(String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addCategory(Intent.CATEGORY_BROWSABLE);
		intent.setData(Uri.parse(url));
		context.startActivity(intent);
	}


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
			webSetting.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
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





	private void initProgressView() {
		//初始化进度条
		if(progressView!=null) {
			progressView.setProgress(10);
			Log.i("设置进度条", "成功");
		}
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


	public static class ADFilterTool{
		public static boolean hasAd(String url,String[]adUrls){
			if(adUrls==null||adUrls.length==0){
				return false;
			}
			for(String adUrl :adUrls){
				if(url.contains(adUrl)){
					return true;
				}
			}
			return false;
		}
	}


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


	/**
	 * dp转换成px
	 *
	 * @param context Context
	 * @param dp      dp
	 * @return px值
	 */
	private int dp2px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}


}
