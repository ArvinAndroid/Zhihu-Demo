package com.cn.materiadesign.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cn.materiadesign.Application;
import com.cn.materiadesign.Constant;
import com.cn.materiadesign.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jun on 2/29/16.
 */
public class SplashActivity extends Activity implements Response.Listener, Response.ErrorListener {

    private ImageView img;
    private TextView title;

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
            ImageLoader imageLoader = Application.getInstance().getImageLoader();
            imageLoader.get(imgUrl, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    img.setImageBitmap(response.getBitmap());
                    startMainActivity();
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    img.setImageResource(R.mipmap.avatar);
                    startMainActivity();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.v("cc", error.toString());
    }

    private void startMainActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,
                        MainActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }

        }, 2000);

    }
}
