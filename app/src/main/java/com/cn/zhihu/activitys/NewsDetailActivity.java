package com.cn.zhihu.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.cn.zhihu.Application;
import com.cn.zhihu.R;
import com.cn.zhihu.beans.NewsDetails;

/**
 * Created by jun on 3/4/16.
 */
public class NewsDetailActivity extends Activity implements Response.Listener, Response.ErrorListener {

    private WebView webView;
    private TextView imageSource;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);
        image = (ImageView) findViewById(R.id.img_details);
        imageSource = (TextView) findViewById(R.id.image_source);
        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
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
        StringBuilder html = new StringBuilder();
        html.append("<html>");
        html.append("<head>");
        html.append("</head>");
        html.append("<body>");
        html.append(details.getBody());
        html.append("</body>");
        html.append("</html>");

        webView.loadDataWithBaseURL("", html.toString(), "text/html;charset=UTF-8", "", "");
        Glide.with(this).load(details.getImage()).into(image);
        imageSource.setText("Image Form: " + details.getImage_source());
    }
}
