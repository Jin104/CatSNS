package com.jin.catsns.post;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jin.catsns.FirebaseUtils;
import com.jin.catsns.R;
import com.jin.catsns.comment.CommentActivity;
import com.jin.catsns.login.LoginActivity;
import com.squareup.picasso.Picasso;

import static java.sql.DriverManager.println;

public class PostFragment extends Fragment {

    private View mView;
    private FirebaseRecyclerAdapter<Post, PostViewHolder> mPostAdapter;
    private RecyclerView mPostRecyclerView;


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

        mView = inflater.inflate(R.layout.fragment_post, container, false);
//        mPostList = (RecyclerView) mView.findViewById(R.id.post_list);
//        mPostList.setHasFixedSize(true);
//        mPostList.setLayoutManager(new LinearLayoutManager(getActivity()));
//        //mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
//        //mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Post_liked");
        mAuth = FirebaseAuth.getInstance();
//
//        //mDatabaseLike.keepSynced(true);
//

        if(FirebaseUtils.getCurrentUser() == null){
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }



        FloatingActionButton floatingActionButton = (FloatingActionButton) mView.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PostCreateActivity.class));
            }
        });
        init();
        return mView;
    }

    private void init() {

        mPostRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerview_post);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mPostRecyclerView.setLayoutManager(layoutManager);

        setupAdapter();
        mPostRecyclerView.setAdapter(mPostAdapter);
    }





    public void setupAdapter() {

//        mAuth.addAuthStateListener(mAuthListener);

        //Query query = FirebaseUtils.getPostRef().orderByChild("timeCreated");
        mPostAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(
                Post.class,
                R.layout.row_post,
                PostViewHolder.class,
                FirebaseUtils.getPostRef()
        ) {
            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, final Post model, int position) {

                viewHolder.setNumComments(String.valueOf(model.getNumComments()));
                viewHolder.setNumLikes(String.valueOf(model.getNumLikes()));
                viewHolder.setTIme(DateUtils.getRelativeTimeSpanString(model.getTimeCreated()));
                viewHolder.setUsername(model.getUser().getName());
                viewHolder.setPostText(model.getPostText());
                viewHolder.setUserId(model.getUser().getId());
                viewHolder.setLikeBtn(model.getPostId());

                Glide.with(getActivity())
                        .load(model.getUser().getImageUrl())
                        .into(viewHolder.postOwnerDisplayImageView);

                if (model.getPostImageUrl() != null) {

                    viewHolder.postDisplayImageVIew.setVisibility(View.VISIBLE);
                    viewHolder.setImage(getActivity(),model.getPostImageUrl());
//                    StorageReference storageReference = FirebaseStorage.getInstance()
//                            .getReference(model.getPostImageUrl());
//                    Glide.with(getActivity())
//                            .using(new FirebaseImageLoader())
//                            .load(storageReference)
//                            .into(viewHolder.postDisplayImageVIew);
                } else {
                    viewHolder.postDisplayImageVIew.setImageBitmap(null);
                    viewHolder.postDisplayImageVIew.setVisibility(View.GONE);
                }
                viewHolder.postLikeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onLikeClick(model.getPostId());
                    }
                });

//                viewHolder.postCommentLayout.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(getContext(), PostCreateActivity.class);
//                        intent.putExtra(FirebaseUtils.Constants.EXTRA_POST, model);
//                        startActivity(intent);
//                    }
//                });

//                final String post_key = getRef(position).getKey().toString();
//
//                viewHolder.setTitle(model.getPostText());
//                viewHolder.setDesc(model.getPostText());
//                viewHolder.setImage(getActivity(), model.getPostImageUrl());
//                //viewHolder.setUserName(model.getUsername());
//
//                viewHolder.setLikeBtn(post_key);
//
//                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent singlePostActivity = new Intent(getActivity(), SinglePostActivity.class);
//                        singlePostActivity.putExtra("PostId", post_key);
//                        startActivity(singlePostActivity);
//                    }
//                });
//
//                viewHolder.mLikeBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        mProcessLike = true;
//
//                        if (mProcessLike) {
//                            mDatabaseLike.addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//
//                                    if (mProcessLike) {
//
//                                        if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {
//
//                                            mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();
//                                            mProcessLike = false;
//
//                                        } else {
//
//                                            mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("RandomValue");
//                                            mProcessLike = false;
//
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//
//                                }
//                            });
//                        }
//                    }
//                });
//
//                viewHolder.mCommentBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(getActivity(), CommentActivity.class);
//                        intent.putExtra("PostId", post_key);
//                        intent.putExtra("UserId", mAuth.getCurrentUser().getUid());
//                        startActivity(intent);
//                    }
//                });

            }
        };
       // mPostList.setAdapter(FBRA);
    }

    private void onLikeClick(final String postId) {
        FirebaseUtils.getPostLikedRef(postId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            //User liked
                            FirebaseUtils.getPostRef()
                                    .child(postId)
                                    .child(FirebaseUtils.Constants.NUM_LIKES_KEY)
                                    .runTransaction(new Transaction.Handler() {
                                        @Override
                                        public Transaction.Result doTransaction(MutableData mutableData) {
                                            long num = (long) mutableData.getValue();
                                            mutableData.setValue(num - 1);

                                            return Transaction.success(mutableData);
                                        }

                                        @Override
                                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                            FirebaseUtils.getPostLikedRef(postId)
                                                    .setValue(null);
                                        }
                                    });
                        } else {
                            FirebaseUtils.getPostRef()
                                    .child(postId)
                                    .child(FirebaseUtils.Constants.NUM_LIKES_KEY)
                                    .runTransaction(new Transaction.Handler() {
                                        @Override
                                        public Transaction.Result doTransaction(MutableData mutableData) {
                                            long num = (long) mutableData.getValue();
                                            mutableData.setValue(num + 1);
                                            return Transaction.success(mutableData);
                                        }

                                        @Override
                                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                            FirebaseUtils.getPostLikedRef(postId)
                                                    .setValue(true);
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder{

        ImageView postOwnerDisplayImageView;
        TextView postOwnerUsernameTextView;
        TextView postOwnerUserIdTextView;
        TextView postTimeCreatedTextView;
        ImageView postDisplayImageVIew;
        TextView postTextTextView;
        ImageView postLikeButton;
        LinearLayout postCommentLayout;
        TextView postNumLikesTextView;
        TextView postNumCommentsTextView;

        public PostViewHolder(View itemView) {
            super(itemView);
            postOwnerDisplayImageView = (ImageView) itemView.findViewById(R.id.iv_post_owner_display);
            postOwnerUsernameTextView = (TextView) itemView.findViewById(R.id.tv_post_username);
            postOwnerUserIdTextView = (TextView) itemView.findViewById(R.id.tv_post_userid);
            postTimeCreatedTextView = (TextView) itemView.findViewById(R.id.tv_time);
            postDisplayImageVIew = (ImageView) itemView.findViewById(R.id.iv_post_display);
            postLikeButton = (ImageView) itemView.findViewById(R.id.iv_like_btn);
            postCommentLayout = (LinearLayout) itemView.findViewById(R.id.comment_layout);
            postNumLikesTextView = (TextView) itemView.findViewById(R.id.tv_likes);
            postNumCommentsTextView = (TextView) itemView.findViewById(R.id.tv_comments);
            postTextTextView = (TextView) itemView.findViewById(R.id.tv_post_text);
        }

        public void setUsername(String username) {
            postOwnerUsernameTextView.setText(username);
        }

        public void setUserId(String userid) {
            postOwnerUserIdTextView.setText(userid);
        }

        public void setTIme(CharSequence time) {
            postTimeCreatedTextView.setText(time);
        }

        public void setNumLikes(String numLikes) {
            postNumLikesTextView.setText(numLikes);
        }

        public void setNumComments(String numComments) {
            postNumCommentsTextView.setText(numComments);
        }

        public void setPostText(String text) {
            postTextTextView.setText(text);
        }

        public void setImage(Context context, String image){
            Picasso.with(context).load(image).into(postDisplayImageVIew);
        }

        public void setLikeBtn(final String post_id){
            FirebaseUtils.getPostLikedRef().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(FirebaseUtils.getCurrentUser()!=null) {
                        if (dataSnapshot.child(FirebaseUtils.getCurrentUser().getUid()).hasChild(post_id)) {
                            postLikeButton.setImageResource(R.mipmap.like_red);

                        } else {
                            postLikeButton.setImageResource(R.mipmap.like_black);

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

}

//package com.jin.catsns.post;
//
//        import android.content.Context;
//        import android.content.Intent;
//        import android.support.annotation.NonNull;
//        import android.support.design.widget.FloatingActionButton;
//        import android.support.v4.app.Fragment;
//        import android.os.Bundle;
//        import android.support.v7.widget.LinearLayoutManager;
//        import android.support.v7.widget.RecyclerView;
//        import android.view.LayoutInflater;
//        import android.view.View;
//        import android.view.ViewGroup;
//        import android.widget.ImageButton;
//        import android.widget.ImageView;
//        import android.widget.TextView;
//
//        import com.firebase.ui.database.FirebaseRecyclerAdapter;
//        import com.google.firebase.auth.FirebaseAuth;
//        import com.google.firebase.database.DataSnapshot;
//        import com.google.firebase.database.DatabaseError;
//        import com.google.firebase.database.DatabaseReference;
//        import com.google.firebase.database.FirebaseDatabase;
//        import com.google.firebase.database.ValueEventListener;
//        import com.jin.catsns.FirebaseUtils;
//        import com.jin.catsns.R;
//        import com.jin.catsns.comment.CommentActivity;
//        import com.jin.catsns.login.LoginActivity;
//        import com.squareup.picasso.Picasso;
//
//public class PostFragment extends Fragment {
//
//    private RecyclerView mPostList;
//    private FirebaseAuth mAuth;
//    private DatabaseReference mDatabase;
//    private DatabaseReference mDatabaseLike;
//    private FirebaseAuth.AuthStateListener mAuthListener;
//    private Post mPost;
//
//    private boolean mProcessLike = false;
//
//    public PostFragment() {
//    }
//
//
//
//    @Override
//    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.fragment_post, container, false);
//
//        mPostList = (RecyclerView)view.findViewById(R.id.post_list);
//        mPostList.setHasFixedSize(true);
//        mPostList.setLayoutManager(new LinearLayoutManager(getActivity()));
//        //mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
//        //mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Post_liked");
//        mAuth = FirebaseAuth.getInstance();
//
//        //mDatabaseLike.keepSynced(true);
//
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                if(firebaseAuth.getCurrentUser()==null){
//                    startActivity(new Intent(getActivity(), LoginActivity.class));
//                }
//            }
//        };
//
//
//        FloatingActionButton floatingActionButton = (FloatingActionButton)view.findViewById(R.id.floating_btn);
//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), PostCreateActivity.class));
//            }
//        });
//
//        return view;
//    }
//

//    @Override
//    public void onStart(){
//        super.onStart();
//
//        mAuth.addAuthStateListener(mAuthListener);
//
//        FirebaseRecyclerAdapter<Post, PostViewHolder> FBRA = new FirebaseRecyclerAdapter<Post, PostViewHolder>(
//
//                Post.class,
//                R.layout.row_post,
//                PostViewHolder.class,
//                //FirebaseUtils.getCommentRef(m)
//                mDatabase
//
//        ){
//
//            @Override
//            protected void populateViewHolder(PostViewHolder viewHolder, Post model, int position) {
//
//                final String post_key = getRef(position).getKey().toString();
//
//                viewHolder.setTitle(model.getPostText());
//                viewHolder.setDesc(model.getPostText());
//                viewHolder.setImage(getActivity(),model.getPostImageUrl());
//                //viewHolder.setUserName(model.getUsername());
//
//                viewHolder.setLikeBtn(post_key);
//
//                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent singlePostActivity = new Intent(getActivity(), SinglePostActivity.class);
//                        singlePostActivity.putExtra("PostId",post_key);
//                        startActivity(singlePostActivity);
//                    }
//                });
//
//                viewHolder.mLikeBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        mProcessLike = true;
//
//                        if(mProcessLike){
//                            mDatabaseLike.addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//
//                                    if(mProcessLike) {
//
//                                        if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {
//
//                                            mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();
//                                            mProcessLike = false;
//
//                                        } else {
//
//                                            mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("RandomValue");
//                                            mProcessLike = false;
//
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//
//                                }
//                            });
//                        }
//                    }
//                });
//
//                viewHolder.mCommentBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(getActivity(), CommentActivity.class);
//                        intent.putExtra("PostId", post_key);
//                        intent.putExtra("UserId", mAuth.getCurrentUser().getUid());
//                        startActivity(intent);
//                    }
//                });
//
//            }
//        };
//        mPostList.setAdapter(FBRA);
//    }
//
//
//    public static class PostViewHolder extends RecyclerView.ViewHolder{
//
//        View mView;
//        ImageButton mLikeBtn;
//        ImageButton mCommentBtn;
//        DatabaseReference mDatabaseLike;
//        //DatabaseReference mDatabaseComment;
//        FirebaseAuth mAuth;
//
//        public PostViewHolder(View itemView) {
//            super(itemView);
//            mView = itemView;
//            mLikeBtn = (ImageButton) mView.findViewById(R.id.like_btn);
//            mCommentBtn = (ImageButton) mView.findViewById(R.id.comment_btn);
//            mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
//            //mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Comment");
//            mAuth = FirebaseAuth.getInstance();
//
//            mDatabaseLike.keepSynced(true);
//        }
//
//        public void setLikeBtn(final String post_key){
//            mDatabaseLike.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//
//                    if(mAuth.getCurrentUser()!=null) {
//                        if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {
//
//                            mLikeBtn.setImageResource(R.mipmap.like_red);
//
//                        } else {
//
//                            mLikeBtn.setImageResource(R.mipmap.like_black);
//
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        }
//
//        public void setTitle(String title){
//            TextView post_title = (TextView)mView.findViewById(R.id.textTitle);
//            post_title.setText(title);
//        }
//
//        public void setDesc(String desc){
//            TextView post_desc = (TextView)mView.findViewById(R.id.textDescription);
//            post_desc.setText(desc);
//        }
//
//        public void setImage(Context context, String image){
//            ImageView post_image = (ImageView)mView.findViewById(R.id.post_image);
//            Picasso.with(context).load(image).into(post_image);
//        }
//
//        public void setUserName(String userName){
//            TextView postUserName = (TextView)mView.findViewById(R.id.textUsername);
//            postUserName.setText(userName);
//        }
//    }
//
//}
//
//
//
//
//












