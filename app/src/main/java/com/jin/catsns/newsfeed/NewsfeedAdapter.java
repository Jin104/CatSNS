package com.jin.catsns.newsfeed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jin.catsns.R;
import com.jin.catsns.newsfeed.NewsfeedObject;
import com.jin.catsns.newsfeed.NewsfeedViewHolder;

import java.util.List;

public class NewsfeedAdapter extends RecyclerView.Adapter<NewsfeedViewHolder>{

    private Context context;
    private List<NewsfeedObject> allNews;

    public NewsfeedAdapter(Context context, List<NewsfeedObject> allNews) {
        this.context = context;
        this.allNews = allNews;
    }

    @Override
    public NewsfeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_feed_layout, parent, false);
        return new NewsfeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsfeedViewHolder holder, int position) {
        NewsfeedObject posts = allNews.get(position);
        holder.postTitle.setText(posts.getPostTitle());
        holder.postAuthor.setText(posts.getPostAuthor());
    }

    @Override
    public int getItemCount() {
        return allNews.size();
    }

}
