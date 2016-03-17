package com.cn.jason.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.cn.jason.activity.NewsDetailActivity;
import com.cn.jason.bean.LatestNews;
import com.cn.jason.bean.Story;
import com.cn.jason.interfa.IRecyclerItemClickListener;
import com.cn.jason.widgets.MyRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jun on 2/24/16.
 */
public class FragmentNews extends Fragment implements Response.Listener, Response.ErrorListener, IRecyclerItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private MyRecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;

    private ContentAdapter adapter = null;
    private List<Story> stories = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(this);

        recyclerView = (MyRecyclerView) view.findViewById(R.id.recycler);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Constant.LATESNEWS_URL, this, this);
        Application.getInstance().getRequestQueue().add(request);
        return view;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "request failed", Toast.LENGTH_LONG).show();
        dismisRefreshLayout();
    }

    @Override
    public void onResponse(Object response) {
        try {
            if (refreshLayout.isRefreshing()) {
                Toast.makeText(getContext(), "refresh success", Toast.LENGTH_LONG).show();
                dismisRefreshLayout();
            }
            LatestNews latestNews = JSON.parseObject(response.toString(), LatestNews.class);
            List<Story> tempStorys = new ArrayList<>();
            for (Story story : latestNews.getStories()) {
                tempStorys.add(story);
            }
            stories = tempStorys;
            if (adapter == null) {
                adapter = new ContentAdapter();
                adapter.setOnItemClickListener(this);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(View v, Object o) {
        Story story = (Story) o;
        Intent intent = new Intent(getContext(), NewsDetailActivity.class);
        intent.putExtra("detail_url", Constant.DETAILNEWS_URL + story.getId());
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Constant.LATESNEWS_URL, this, this);
        Application.getInstance().getRequestQueue().add(request);
    }

    class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {

        private Context context;
        private IRecyclerItemClickListener clickListener;

        public ContentAdapter() {
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            context = parent.getContext();
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_list, null);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            Story story = stories.get(position);
            holder.title.setText(story.getTitle());
            String imgUrl = story.getImages();
            Glide.with(context)
                    .load(imgUrl.substring(2, imgUrl.length() - 2))
                    .placeholder(R.mipmap.paris)
                    .error(R.mipmap.avatar)
                    .into(holder.image);
            holder.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.onItemClick(holder.v, stories.get(position));
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return stories != null ? stories.size() : 0;
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

    private void dismisRefreshLayout() {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }
}
