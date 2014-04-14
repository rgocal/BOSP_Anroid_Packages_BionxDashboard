package com.bionx.res.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.bionx.res.R;

public class BionxWebs extends Activity {

    protected FrameLayout webViewPlaceholder;
	protected WebView webView;
	protected View Webview;
	protected ViewGroup parentViewGroup;

    ProgressBar loadingProgressBar,loadingTitle;

    String url = "http://bionx.webs.com/";
    String linkDomain = "bionx.webs.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.state_preserving_impl);
		initUI();
	}

    protected void initUI()
	{
		webViewPlaceholder = ((FrameLayout)findViewById(R.id.webViewPlaceholder));
		if (Webview == null)
		{
			// Create the webview
            setContentView(R.layout.bionx_webs);
        	Webview = findViewById(R.id.BionxWebview);
            parentViewGroup = (ViewGroup)Webview.getParent();
			webView = (WebView) findViewById(R.id.webview);
			webView.getSettings().setSupportZoom(false);
			webView.getSettings().setBuiltInZoomControls(false);
			webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
			webView.setScrollbarFadingEnabled(true);
			webView.getSettings().setLoadsImagesAutomatically(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDatabaseEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setAppCacheEnabled(true);
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            webView.setDownloadListener(new DownloadListener() {
                @Override
				public void onDownloadStart(String url, String userAgent,
                        String contentDisposition, String mimetype,
                        long contentLength) {
                  Intent i = new Intent(Intent.ACTION_VIEW);
                  i.setData(Uri.parse(url));
                  startActivity(i);
                }
            });

            loadingProgressBar=(ProgressBar)findViewById(R.id.progressbar_Horizontal);
            webView.setWebChromeClient(new WebChromeClient() {

                // this will be called on page loading progress
                @Override
                public void onProgressChanged(WebView view, int newProgress) {

                    super.onProgressChanged(view, newProgress);

                    loadingProgressBar.setProgress(newProgress);

                    // hide the progress bar if the loading is complete
                    if (newProgress == 100) {
                    loadingProgressBar.setVisibility(View.GONE);
                    } else{
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    }
                }
                
            });   

            webView.setWebViewClient(new WebViewClient() {
            
		        @Override
		        public boolean shouldOverrideUrlLoading(WebView view, String url) {


	                // If the site/domain matches, do not override; let myWebView load the page
		            if (Uri.parse(url).getHost().equals(linkDomain)) {
		                return false;
		            }

		            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
		            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		            startActivity(intent);
		            return true;
		        }

            
	        });
	        
			// Load the first page
			webView.loadUrl(url);

		}
		parentViewGroup.removeView(Webview);
		// Attach the WebView to its placeholder
		parentViewGroup.addView(Webview);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);

		if (webView != null)
		{
			// Remove the WebView from the old placeholder
			parentViewGroup.removeView(Webview);
		}

		// Load the layout resource for the new configuration
        setContentView(R.layout.state_preserving_impl);

		// Reinitialize the UI
		initUI();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);

		// Save the state of the WebView
		webView.saveState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);

		// Restore the state of the WebView
		webView.restoreState(savedInstanceState);
	}

    @Override
	public void onBackPressed (){
        if (webView.isFocused() && webView.canGoBack()) {
                webView.goBack();       
        }else {
                BionxWebs.this.finish();
        }
    }
}