package com.jin.catsns.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jin.catsns.R;
import com.jin.catsns.post.PostActivity;
import com.jin.catsns.post.PostFragment;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameField;
    private EditText emailField;
    private EditText passField;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameField = (EditText)findViewById(R.id.nameField);
        passField = (EditText)findViewById(R.id.passField);
        emailField = (EditText)findViewById(R.id.emailField);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    public void registerButtonClicked(View view){
        final String name = nameField.getText().toString().trim();
        final String email = emailField.getText().toString().trim();
        String pass = passField.getText().toString().trim();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)){
            mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String user_id = mAuth.getCurrentUser().getUid();
                        FirebaseUser firebaseUser = task.getResult().getUser();
                        DatabaseReference current_user_db = mDatabase.child(user_id);
                        current_user_db.child("id").setValue(name);
                        current_user_db.child("email").setValue(email);
                        current_user_db.child("profile_image").setValue("default");
                        current_user_db.child("uid").setValue(firebaseUser.getUid());

                        Toast.makeText(getApplication(), "회원가입 성공", Toast.LENGTH_LONG).show();
                        Intent mainIntent = new Intent(RegisterActivity.this, SetupActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                        finish();

                    }
                    Toast.makeText(getApplication(), "실패", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
