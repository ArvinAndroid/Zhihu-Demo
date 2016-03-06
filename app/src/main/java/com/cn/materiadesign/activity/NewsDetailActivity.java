package com.cn.materiadesign.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.cn.materiadesign.Application;
import com.cn.materiadesign.R;
import com.cn.materiadesign.bean.NewsDetails;

/**
 * Created by jun on 3/4/16.
 */
public class NewsDetailActivity extends Activity implements Response.Listener, Response.ErrorListener {

    private WebView webView;
    private TextView title, imageSource;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);
        title = (TextView) findViewById(R.id.detail_title);
        image = (ImageView) findViewById(R.id.img_details);
        imageSource = (TextView) findViewById(R.id.image_source);
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
        NewsDetails details = JSON.parseObject(response.toString(), NewsDetails.class);
        webView.loadData(details.getBody(), "text/html;charset=UTF-8", null);
        title.setText(details.getTitle());
        imageSource.setText("图片来源：" + details.getImage_source());
        Glide.with(this).load(details.getImage()).placeholder(R.mipmap.avatar).into(image);
    }
}
