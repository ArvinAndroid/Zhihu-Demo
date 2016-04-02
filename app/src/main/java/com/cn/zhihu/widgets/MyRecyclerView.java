package com.cn.zhihu.widgets;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by jun on 3/17/16.
 */
public class MyRecyclerView extends RecyclerView {
    public MyRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public MyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context){
        setHasFixedSize(true);
        setLayoutManager(new LinearLayoutManager(context));
        setItemAnimator(new DefaultItemAnimator());
    }
}
