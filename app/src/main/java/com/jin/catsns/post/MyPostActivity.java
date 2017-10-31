package com.jin.catsns.post;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jin.catsns.R;
import com.jin.catsns.login.LoginActivity;
import com.squareup.picasso.Picasso;

public class MyPostActivity extends AppCompatActivity {

    private RecyclerView mPostList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseLike;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference mDatabaseCurrentUser;
    private Query mQueryCurrentUser;

    private boolean mProcessLike = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPostList = (RecyclerView)findViewById(R.id.my_post_list);
        mPostList.setHasFixedSize(true);
        mPostList.setLayoutManager(new LinearLayoutManager(MyPostActivity.this));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("CatSNS");
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
        mAuth = FirebaseAuth.getInstance();

        mDatabaseLike.keepSynced(true);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    Intent loginIntent = new Intent(MyPostActivity.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }
            }
        };


        String currentUserId = mAuth.getCurrentUser().getUid();
        mDatabaseCurrentUser = FirebaseDatabase.getInstance().getReference().child("CatSNS");
        mQueryCurrentUser = mDatabaseCurrentUser.orderByChild("uid").equalTo(currentUserId);

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.news_floating_btn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPostActivity.this, PostCreateActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart(){
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

        FirebaseRecyclerAdapter<Post, MyPostActivity.PostViewHolder> FBRA = new FirebaseRecyclerAdapter<Post, MyPostActivity.PostViewHolder>(

                Post.class,
                R.layout.row_post,
                MyPostActivity.PostViewHolder.class,
                mQueryCurrentUser

        ){

            @Override
            protected void populateViewHolder(MyPostActivity.PostViewHolder viewHolder, Post model, int position) {

                final String post_key = getRef(position).getKey().toString();

                viewHolder.setTitle(model.getPostText());
                viewHolder.setDesc(model.getPostText());
                viewHolder.setImage(MyPostActivity.this,model.getPostImageUrl());
                //viewHolder.setUserName(model.getUsername());

                viewHolder.setLikeBtn(post_key);

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent singlePostActivity = new Intent(MyPostActivity.this, SinglePostActivity.class);
                        singlePostActivity.putExtra("PostId",post_key);
                        startActivity(singlePostActivity);
                    }
                });

                viewHolder.mLikebtn.setOnClickListener(new View.OnClickListener() {
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

            }
        };
        mPostList.setAdapter(FBRA);
    }


    public static class PostViewHolder extends RecyclerView.ViewHolder{

        View mView;
        ImageButton mLikebtn;
        DatabaseReference mDatabaseLike;
        FirebaseAuth mAuth;

        public PostViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mLikebtn = (ImageButton) mView.findViewById(R.id.like_btn);
            mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
            mAuth = FirebaseAuth.getInstance();

            mDatabaseLike.keepSynced(true);
        }

        public void setLikeBtn(final String post_key){
            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())){

                        mLikebtn.setImageResource(R.mipmap.like_red);

                    }else{

                        mLikebtn.setImageResource(R.mipmap.like_black);

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
