package com.jin.catsns.newsfeed;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jin.catsns.R;


public class NewsfeedViewHolder extends RecyclerView.ViewHolder{

    public TextView postTitle;
    public TextView postAuthor;
    public ImageView postImage;

    public NewsfeedViewHolder(View itemView, TextView postTitle, TextView postAuthor, ImageView postImage) {
        super(itemView);
        this.postTitle = postTitle;
        this.postAuthor = postAuthor;
        this.postImage = postImage;
    }

    public NewsfeedViewHolder(View itemView) {
        super(itemView);

        postTitle = (TextView)itemView.findViewById(R.id.post_date);
        postAuthor = (TextView)itemView.findViewById(R.id.post_author);
        postImage = (ImageView)itemView.findViewById(R.id.post_image);
    }
}
