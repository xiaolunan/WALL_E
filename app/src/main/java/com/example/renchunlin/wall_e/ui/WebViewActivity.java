package com.example.renchunlin.wall_e.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.ProgressBar;
import com.example.renchunlin.wall_e.R;

/**
 * 项目名：   WAll_E
 * 包名：     com.example.rcl.smartbutler.ui
 * 文件名：   WebViewActivity
 * 创建者：   RCL
 * 创建时间： 2016/9/6 10:27
 * 描述：     偶遇详情
 */
public class WebViewActivity extends BaseActivity {

    //进度条
    private ProgressBar mProgressBar;
    //网页
    private WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        initView();
    }

    private void initView() {
        mProgressBar= (ProgressBar) findViewById(R.id.mProgressBar);
        mWebView= (WebView) findViewById(R.id.mWebView);

        Intent intent=getIntent();
        String title=intent.getStringExtra("title");
        final String url=intent.getStringExtra("url");

        //设置标题
        getSupportActionBar().setTitle(title);

        //进行记载网页的逻辑
        //支持js
        mWebView.getSettings().setJavaScriptEnabled(true);
        //支持缩放
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        //接口回调
        mWebView.setWebChromeClient(new WebViewClient());
        //加载网页
        mWebView.loadUrl(url);
        //本地显示
        mWebView.setWebViewClient(new android.webkit.WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(url);
                //我接受这个事件
                return true;
            }
        });
    }

    public class WebViewClient extends WebChromeClient{

        //进度条变化的监听
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if(newProgress==100){
                mProgressBar.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }
    }
}
