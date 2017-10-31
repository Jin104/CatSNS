package com.jin.catsns.post;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jin.catsns.FirebaseUtils;
import com.jin.catsns.R;
import com.jin.catsns.login.User;

public class PostCreateActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 2;
    private Post mPost;
    private Uri uri = null;
    private ImageView mImageView;
    private EditText editDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_create);

        editDesc = (EditText)findViewById(R.id.edit_desc);
        mImageView = (ImageView) findViewById(R.id.image_view);
        mPost = new Post();


    }

    public void imageButtonClicked(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
                if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && null!=data){
// &&
                    uri = data.getData();
                    mImageView.setImageURI(uri);

//                    uri = data.getData();
//
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
//
//                    int nh = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
//                    Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);
//
//                    imageButton = (ImageButton)findViewById(R.id.imageButton);
//                    imageButton.setImageBitmap(scaled);
//                    imageButton.setBackgroundResource(R.drawable.white);
                }else {
                    Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                Toast.makeText(this, "로딩에 오류가 있습니다.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
        }
    }

    public void uploadButtonClicked(View view){

        final String descValue = editDesc.getText().toString().trim();

        if(!TextUtils.isEmpty(descValue)){

            FirebaseUtils.getUserRef(FirebaseUtils.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    User user = dataSnapshot.getValue(User.class);
                    final String postID = FirebaseUtils.getUid();

                    mPost.setUser(user);
                    mPost.setNumComments(0);
                    mPost.setNumLikes(0);
                    mPost.setTimeCreated(System.currentTimeMillis());
                    mPost.setPostId(postID);
                    mPost.setPostText(descValue);

                    if(uri != null) {
                        FirebaseUtils.getImagesRef()
                                .child(uri.getLastPathSegment())
                                .putFile(uri)
                                .addOnSuccessListener(PostCreateActivity.this,
                                        new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                final Uri downloadurl = taskSnapshot.getDownloadUrl();
                                                //String url = FirebaseUtils.Constants.POST_IMAGES + "/" + uri.getLastPathSegment();
                                                mPost.setPostImageUrl(downloadurl.toString());
                                                addToMyPostList(postID);
                                            }
                                        });

                    }else{
                        addToMyPostList(postID);
                    }
                    Toast.makeText(PostCreateActivity.this, "업로드 완료", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(PostCreateActivity.this, "업로드 실패", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
//            if(mImageView.getVisibility() == View.VISIBLE)
//            {
//
//            }
//            StorageReference filePath = storageReference.child("PostImage").child(uri.getLastPathSegment());
//            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    @SuppressWarnings("VisibleForTests")
//                    final Uri downloadurl = taskSnapshot.getDownloadUrl();
//                    Toast.makeText(PostCreateActivity.this, "Upload Complete",Toast.LENGTH_SHORT).show();
//                    final DatabaseReference newPost = databaseReference.push();
//
//                    mDatabaseUsers.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            newPost.child("title").setValue(titleValue);
//                            newPost.child("desc").setValue(descValue);
//                            newPost.child("image").setValue(downloadurl.toString());
//                            newPost.child("uid").setValue(mCurrentUser.getUid());
//                            newPost.child("username").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if(task.isSuccessful()){
//                                        //Intent postFragmentIntent = new Intent(PostCreateActivity.this, PostFragment.class);
//                                        //startActivity(postFragmentIntent);
//                                        finish();
//                                    }
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//            });
        }
    }

    private void addToMyPostList(String postId) {
        FirebaseUtils.getPostRef().child(postId)
                .setValue(mPost);
        FirebaseUtils.getMyPostRef().child(postId).setValue(true)
                .addOnCompleteListener(PostCreateActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

        FirebaseUtils.addToMyRecord(FirebaseUtils.Constants.POST_KEY, postId);
    }
}


//    private static final int GALLERY_REQUEST = 2;
//    private Post mPost;
//    private Uri uri = null;
//
//    private ImageButton imageButton;
//    private EditText editDesc;
//    private StorageReference storageReference;
//    private FirebaseDatabase database;
//    private DatabaseReference databaseReference;
//    private FirebaseAuth mAuth;
//    private DatabaseReference mDatabaseUsers;
//    private FirebaseUser mCurrentUser;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_post_create);
//
//        editDesc = (EditText)findViewById(R.id.edit_desc);
//
//        storageReference = FirebaseStorage.getInstance().getReference();
//        databaseReference = database.getInstance().getReference().child("CatSNS");
//
//        mAuth = FirebaseAuth.getInstance();
//        mCurrentUser = mAuth.getCurrentUser();
//        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
//    }
//
//    public void imageButtonClick(View view){
//        Intent galleryintent = new Intent(Intent.ACTION_GET_CONTENT);
//        galleryintent.setType("image/*");
//        startActivityForResult(galleryintent,GALLERY_REQUEST);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        try{
//            if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && null!=data){
//                uri = data.getData();
//
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
//
//                int nh = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
//                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);
//
//                imageButton = (ImageButton)findViewById(R.id.imageButton);
//                imageButton.setImageBitmap(scaled);
//                imageButton.setBackgroundResource(R.drawable.white);
//            }else {
//                Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_LONG).show();
//            }
//
//        } catch (Exception e) {
//            Toast.makeText(this, "로딩에 오류가 있습니다.", Toast.LENGTH_LONG).show();
//            e.printStackTrace();
//        }
//    }
//
//    public void submitButtonClicked(View view){
//
//        final String titleValue = editName.getText().toString().trim();
//        final String descValue = editDesc.getText().toString().trim();
//
//        if(!TextUtils.isEmpty(titleValue) && !TextUtils.isEmpty(descValue)){
//            StorageReference filePath = storageReference.child("PostImage").child(uri.getLastPathSegment());
//            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    @SuppressWarnings("VisibleForTests")
//                    final Uri downloadurl = taskSnapshot.getDownloadUrl();
//                    Toast.makeText(PostCreateActivity.this, "Upload Complete",Toast.LENGTH_SHORT).show();
//                    final DatabaseReference newPost = databaseReference.push();
//
//                    mDatabaseUsers.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            newPost.child("title").setValue(titleValue);
//                            newPost.child("desc").setValue(descValue);
//                            newPost.child("image").setValue(downloadurl.toString());
//                            newPost.child("uid").setValue(mCurrentUser.getUid());
//                            newPost.child("username").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if(task.isSuccessful()){
//                                        //Intent postFragmentIntent = new Intent(PostCreateActivity.this, PostFragment.class);
//                                        //startActivity(postFragmentIntent);
//                                        finish();
//                                    }
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//            });
//        }
//    }