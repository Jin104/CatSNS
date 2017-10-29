package com.jin.catsns.post;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jin.catsns.R;
import com.squareup.picasso.Picasso;

public class SinglePostActivity extends AppCompatActivity {

    private String post_key = null;
    private DatabaseReference mDatabase;
    private ImageView singlePostImage;
    private TextView singlePostTitle;
    private TextView singlePostDesc;
    private Button deleteButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        post_key = getIntent().getExtras().getString("PostId");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("CatSNS");

        singlePostDesc = (TextView)findViewById(R.id.singleDesc);
        singlePostTitle = (TextView)findViewById(R.id.singleTitle);
        singlePostImage = (ImageView)findViewById(R.id.singleImageView);

        mAuth = FirebaseAuth.getInstance();
        deleteButton = (Button)findViewById(R.id.singleDeleteButton);
        deleteButton.setVisibility(View.INVISIBLE);

        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title = (String)dataSnapshot.child("title").getValue();
                String post_desc = (String)dataSnapshot.child("desc").getValue();
                String post_image = (String)dataSnapshot.child("image").getValue();
                String post_uid = (String)dataSnapshot.child("uid").getValue();

                singlePostTitle.setText(post_title);
                singlePostDesc.setText(post_desc);
                Picasso.with(SinglePostActivity.this).load(post_image).into(singlePostImage);

                if(mAuth.getCurrentUser().getUid().equals(post_uid)){
                    deleteButton.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void deleteButtonClicked(View view){
        mDatabase.child(post_key).removeValue();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Likes");
        mDatabase.child(post_key).removeValue();
        //intent
        finish();
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

}
