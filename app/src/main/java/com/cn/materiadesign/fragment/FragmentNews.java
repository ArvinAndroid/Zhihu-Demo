package com.cn.materiadesign.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
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
import com.cn.materiadesign.Application;
import com.cn.materiadesign.Constant;
import com.cn.materiadesign.R;
import com.cn.materiadesign.activity.NewsDetailActivity;
import com.cn.materiadesign.bean.LatestNews;
import com.cn.materiadesign.bean.Story;
import com.cn.materiadesign.interfa.IRecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jun on 2/24/16.
 */
public class FragmentNews extends Fragment implements Response.Listener, Response.ErrorListener, IRecyclerItemClickListener{

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Constant.LATESNEWS_URL, this, this);
        Application.getInstance().getRequestQueue().add(request);
        return view;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(Object response) {
        try {
            LatestNews latestNews = JSON.parseObject(response.toString(), LatestNews.class);
            List<Story> stories = new ArrayList<>();
            for (Story story : latestNews.getStories()) {
                stories.add(story);
            }
            ContentAdapter adapter = new ContentAdapter(stories);
            adapter.setOnItemClickListener(this);
            recyclerView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(View v, Object o) {
        Story story = (Story) o;
        Intent intent = new Intent(getContext(), NewsDetailActivity.class);
        intent.putExtra("detail_url",Constant.DETAILNEWS_URL + story.getId());
        startActivity(intent);
    }

    class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {

        private List<Story> items;
        private Context context;
        private IRecyclerItemClickListener clickListener;

        public ContentAdapter(List<Story> stories) {
            this.items = stories != null ? stories : new ArrayList<Story>();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            context = parent.getContext();
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_list, null);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            Story story = items.get(position);
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
                        clickListener.onItemClick(holder.v, items.get(position));
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return items != null ? items.size() : 0;
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
