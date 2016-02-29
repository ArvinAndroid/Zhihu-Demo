package com.cn.materiadesign.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.cn.materiadesign.R;

/**
 * Created by jun on 2/29/16.
 */
public class SplashActivity extends Activity {

    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        img = (ImageView) findViewById(R.id.img);
    }

}
