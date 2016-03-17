package com.cn.jason.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.cn.jason.Application;
import com.cn.jason.Constant;
import com.cn.jason.R;

import org.json.JSONObject;

/**
 * Created by jun on 2/29/16.
 */
public class SplashActivity extends Activity implements Response.Listener, Response.ErrorListener {

    private ImageView img;
    private TextView title;

    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash);
        img = (ImageView) findViewById(R.id.img);
        title = (TextView) findViewById(R.id.tv_title);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Constant.SPLASH_URL, this, this);
        Application.getInstance().getRequestQueue().add(request);
    }

    @Override
    public void onResponse(Object response) {
        try {
            JSONObject obj = new JSONObject(response.toString());
            String imgUrl = obj.getString("img");
            String text = obj.getString("text");
            title.setText(text);
            Glide.with(this)
                    .load(imgUrl)
                    .placeholder(R.mipmap.avatar)
                    .error(R.mipmap.avatar)
                    .into(img);
            startActivity();
        } catch (Exception e) {
            e.printStackTrace();
            startMainActivity();
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        startMainActivity();
    }

    private void startActivity() {
        if (!flag) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startMainActivity();
                }

            }, 2000);
        }
    }

    private void startMainActivity() {
        if (!flag) {
            flag = true;
            Intent intent = new Intent(SplashActivity.this,
                    MainActivity.class);
            startActivity(intent);
            SplashActivity.this.finish();
        }
    }
}
