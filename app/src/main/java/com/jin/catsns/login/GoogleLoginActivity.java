package com.jin.catsns.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jin.catsns.R;
import com.jin.catsns.post.PostFragment;

public class GoogleLoginActivity extends AppCompatActivity{

    private View mProgressView;
    private SignInButton mSigninBtn;
    private GoogleApiClient mGoogleAPIClient;
    private GoogleSignInOptions mGoogleSignInoptions;
    private static final int GOOGLE_LOGIN_OPEN = 100;
    private FirebaseAuth mAuth;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_login);

        mProgressView = (ProgressBar) findViewById(R.id.login_progress);
        mSigninBtn = (SignInButton) findViewById(R.id.google_sign_in_btn);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mUserRef = mDatabase.getReference("Users");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mGoogleSignInoptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .requestIdToken(getString(R.string.default_web_client_id))
                                    .requestEmail()
                                    .build();

        mGoogleAPIClient = new GoogleApiClient.Builder(this)
                                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                                    @Override
                                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                                        // 실패시 처리
                                    }
                                })
                                .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInoptions)
                                .build();

        mSigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleAPIClient);
        startActivityForResult(signInIntent, GOOGLE_LOGIN_OPEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GOOGLE_LOGIN_OPEN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isComplete()){
                            if(task.isSuccessful()){
                                FirebaseUser firebaseUser = task.getResult().getUser();

                                final User user = new User();

                                ///////////////////////////////////////////////////////////

                                ///////////////////////////////////////////////////////////

                                user.setEmail(firebaseUser.getEmail());
                                user.setUid(firebaseUser.getUid());

                                //user.setName(firebaseUser.getDisplayName());
                                //if(firebaseUser.getPhotoUrl()!=null)
                                //    user.setImgUrl(firebaseUser.getPhotoUrl().toString());

                                mUserRef.child(user.getUid()).setValue(user, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                        if(databaseError==null) {
                                            Bundle eventBundle = new Bundle();
                                            eventBundle.putString("email", user.getEmail());
                                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, eventBundle);

                                            if(user.getName()==null) {
                                                Intent intent = new Intent(GoogleLoginActivity.this, GoogleSetupActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    }
                                });


                            }else{
                                Snackbar.make(mProgressView, "로그인에 실패하였습니다.", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    ValueEventListener userListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            User user0 = dataSnapshot.getValue(User.class);
            Toast.makeText(GoogleLoginActivity.this,user0.getName()+" "+user0.getUid(),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}
