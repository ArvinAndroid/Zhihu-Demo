package com.cn.jason.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.cn.jason.Application;
import com.cn.jason.Constant;
import com.cn.jason.R;
import com.cn.jason.bean.Recent;
import com.cn.jason.bean.RecentNews;
import com.cn.jason.interfa.IRecyclerItemClickListener;
import com.cn.jason.widgets.MyRecyclerView;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by jun on 2/24/16.
 */
public class FragmentHotNews extends Fragment implements Response.Listener,Response.ErrorListener{

    private MyRecyclerView recyclerView;
    private HotContentAdapter adapter;

    private List<RecentNews> recentNewsList = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot,container,false);
        recyclerView = (MyRecyclerView) view.findViewById(R.id.recycler_hot);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Constant.HOT_NEWS, this, this);
        Application.getInstance().getRequestQueue().add(request);
        return view;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(),"connection failed",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(Object response) {
        try {
            Recent recent = JSON.parseObject(response.toString(), Recent.class);
            List<RecentNews> tempRecentNews = new ArrayList<>();
            for (RecentNews news: recent.getRecent()) {
                tempRecentNews.add(news);
            }
            recentNewsList = tempRecentNews;
            if (adapter == null) {
                adapter = new HotContentAdapter();
                recyclerView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class HotContentAdapter extends RecyclerView.Adapter<ViewHolder> {

        private Context context;
        private IRecyclerItemClickListener clickListener;

        public HotContentAdapter() {
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            context = parent.getContext();
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_list, null);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            RecentNews story = recentNewsList.get(position);
            holder.title.setText(story.getTitle());
            String imgUrl = story.getThumbnail();
            Glide.with(context)
                    .load(imgUrl)
                    .placeholder(R.mipmap.paris)
                    .error(R.mipmap.avatar)
                    .into(holder.image);
            holder.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.onItemClick(holder.v, recentNewsList.get(position));
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return recentNewsList != null ? recentNewsList.size() : 0;
        }

        public void setOnItemClickListener(IRecyclerItemClickListener clickListener) {
            this.clickListener = clickListener;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView title;
        protected ImageView image;
        protected View v;

        public ViewHolder(View itemView) {
            super(itemView);
            this.v = itemView;
            this.title = (TextView) itemView.findViewById(R.id.card_text);
            this.image = (ImageView) itemView.findViewById(R.id.card_image);
        }
    }

}
