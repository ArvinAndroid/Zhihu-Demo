package com.cn.materiadesign.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cn.materiadesign.Application;
import com.cn.materiadesign.R;

/**
 * Created by jun on 3/4/16.
 */
public class NewsDetailActivity extends Activity implements Response.Listener, Response.ErrorListener {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);
        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.setWebViewClient(new WebViewClient());
        String detailUrl = getIntent().getStringExtra("detail_url");
        if (detailUrl == null) {
            return;
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, detailUrl, this, this);
        Application.getInstance().getRequestQueue().add(request);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "加载失败：" + error.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(Object response) {
        JSONObject json = JSONObject.parseObject(response.toString());
        String body = json.getString("body");
        webView.loadData(body, "text/html;charset=UTF-8", null);
    }
}
