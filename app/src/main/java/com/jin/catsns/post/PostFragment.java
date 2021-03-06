package com.jin.catsns.post;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jin.catsns.FirebaseUtils;
import com.jin.catsns.R;
import com.jin.catsns.comment.CommentActivity;
import com.jin.catsns.login.LoginActivity;
import com.squareup.picasso.Picasso;

public class PostFragment extends Fragment {

    private RecyclerView mPostList;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseLike;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Post mPost;

    private boolean mProcessLike = false;

    public PostFragment() {
    }



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_post, container, false);

        mPostList = (RecyclerView)view.findViewById(R.id.post_list);
        mPostList.setHasFixedSize(true);
        mPostList.setLayoutManager(new LinearLayoutManager(getActivity()));
        //mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
        //mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Post_liked");
        mAuth = FirebaseAuth.getInstance();

        //mDatabaseLike.keepSynced(true);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        };


        FloatingActionButton floatingActionButton = (FloatingActionButton)view.findViewById(R.id.floating_btn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PostCreateActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

        FirebaseRecyclerAdapter<Post, PostViewHolder> FBRA = new FirebaseRecyclerAdapter<Post, PostViewHolder>(

                Post.class,
                R.layout.row_post,
                PostViewHolder.class,
                //FirebaseUtils.getCommentRef(m)
                mDatabase

        ){

            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, Post model, int position) {

                final String post_key = getRef(position).getKey().toString();

                viewHolder.setTitle(model.getPostText());
                viewHolder.setDesc(model.getPostText());
                viewHolder.setImage(getActivity(),model.getPostImageUrl());
                //viewHolder.setUserName(model.getUsername());

                viewHolder.setLikeBtn(post_key);

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent singlePostActivity = new Intent(getActivity(), SinglePostActivity.class);
                        singlePostActivity.putExtra("PostId",post_key);
                        startActivity(singlePostActivity);
                    }
                });

                viewHolder.mLikeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mProcessLike = true;

                        if(mProcessLike){
                            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if(mProcessLike) {

                                        if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {

                                            mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();
                                            mProcessLike = false;

                                        } else {

                                            mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("RandomValue");
                                            mProcessLike = false;

                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });

                viewHolder.mCommentBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), CommentActivity.class);
                        intent.putExtra("PostId", post_key);
                        intent.putExtra("UserId", mAuth.getCurrentUser().getUid());
                        startActivity(intent);
                    }
                });

            }
        };
        mPostList.setAdapter(FBRA);
    }


    public static class PostViewHolder extends RecyclerView.ViewHolder{

        View mView;
        ImageButton mLikeBtn;
        ImageButton mCommentBtn;
        DatabaseReference mDatabaseLike;
        //DatabaseReference mDatabaseComment;
        FirebaseAuth mAuth;

        public PostViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mLikeBtn = (ImageButton) mView.findViewById(R.id.like_btn);
            mCommentBtn = (ImageButton) mView.findViewById(R.id.comment_btn);
            mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
            //mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Comment");
            mAuth = FirebaseAuth.getInstance();

            mDatabaseLike.keepSynced(true);
        }

        public void setLikeBtn(final String post_key){
            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(mAuth.getCurrentUser()!=null) {
                        if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {

                            mLikeBtn.setImageResource(R.mipmap.like_red);

                        } else {

                            mLikeBtn.setImageResource(R.mipmap.like_black);

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public void setTitle(String title){
            TextView post_title = (TextView)mView.findViewById(R.id.textTitle);
            post_title.setText(title);
        }

        public void setDesc(String desc){
            TextView post_desc = (TextView)mView.findViewById(R.id.textDescription);
            post_desc.setText(desc);
        }

        public void setImage(Context context, String image){
            ImageView post_image = (ImageView)mView.findViewById(R.id.post_image);
            Picasso.with(context).load(image).into(post_image);
        }

        public void setUserName(String userName){
            TextView postUserName = (TextView)mView.findViewById(R.id.textUsername);
            postUserName.setText(userName);
        }
    }

}









