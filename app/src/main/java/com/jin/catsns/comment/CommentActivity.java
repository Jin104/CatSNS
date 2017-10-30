package com.jin.catsns.comment;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jin.catsns.R;
import com.jin.catsns.login.LoginActivity;
import com.jin.catsns.post.MyPostActivity;
import com.jin.catsns.post.Post;

import com.jin.catsns.comment.Comment;

public class CommentActivity extends AppCompatActivity {

    private Uri uri = null;
    private EditText editComment;
    private ImageButton commentSend;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUser;

    private RecyclerView mCommentList;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        final String post_id = intent.getExtras().getString("PostId");
        final String user_id = intent.getExtras().getString("UserId");

        editComment = (EditText)findViewById(R.id.edit_comment);
        commentSend = (ImageButton)findViewById(R.id.comment_send);

        databaseReference = database.getInstance().getReference().child("Comments").child(post_id).child(user_id);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        mCommentList = (RecyclerView) findViewById(R.id.comment_list);
        mCommentList.setHasFixedSize(true);
        mCommentList.setLayoutManager(new LinearLayoutManager(CommentActivity.this));


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Comments").child(post_id);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };
    }


    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

        FirebaseRecyclerAdapter<Comment, CommentViewHolder> FBRA = new FirebaseRecyclerAdapter<Comment, CommentViewHolder>(

                Comment.class,
                R.layout.comment_row,
                CommentViewHolder.class,
                mDatabase

        ) {

            @Override
            protected void populateViewHolder(CommentViewHolder viewHolder, Comment model, int position) {

                viewHolder.setUsername(model.getUsername());
                viewHolder.setUserid(model.getUserid());
                viewHolder.setDesc(model.getDesc());

            }
        };
        mCommentList.setAdapter(FBRA);


    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder{

        View mView;
        ImageButton mProfileImgBtn;
        DatabaseReference mDatabaseComment;
        FirebaseAuth mAuth;

        public CommentViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mProfileImgBtn = (ImageButton) mView.findViewById(R.id.comment_profile_img);

            mDatabaseComment = FirebaseDatabase.getInstance().getReference().child("Comments");
            //mAuth = FirebaseAuth.getInstance();

        }

        public void setUsername(String name){
            TextView comment_name = (TextView)mView.findViewById(R.id.comment_username);
            comment_name.setText(name);
        }

        public void setUserid(String id){
            TextView comment_id = (TextView)mView.findViewById(R.id.comment_user_id);
            comment_id.setText(id);
        }

//        public void setProfileImg(String img){
//            TextView comment_profile_img = (TextView)mView.findViewById(R.id.comment_profile_img);
//            comment_profile_img.setText(img);
//        }

        public void setDesc(String desc){
            TextView comment_desc = (TextView)mView.findViewById(R.id.comment_desc);
            comment_desc.setText(desc);
        }

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

    public void commentSendButtonClicked(View view){

        final String commentValue = editComment.getText().toString().trim();

        if(!TextUtils.isEmpty(commentValue)){

            final DatabaseReference newComment = databaseReference;

            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    newComment.child("desc").setValue(commentValue);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }
}
