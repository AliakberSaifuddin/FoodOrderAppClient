package com.example.cks.foodorderappclient;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private EditText email, pass;
    private DatabaseReference mDatabase;
    private FirebaseAuth mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = (EditText) findViewById(R.id.userEmail);
        pass = (EditText) findViewById(R.id.userPass);

        mauth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
    }

    public void singUpButtonClicked(View view) {
        final String text_email = email.getText().toString().trim();
        String text_pass = pass.getText().toString().trim();
        if(!TextUtils.isEmpty(text_email) && !TextUtils.isEmpty(text_pass)) {
            mauth.createUserWithEmailAndPassword(text_email, text_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        String user_id = mauth.getCurrentUser().getUid();
                        final DatabaseReference current_user = mDatabase.child(user_id);
                        current_user.child("Name").setValue(text_email);
                        Intent login = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(login);
                    }
                }
            });
        }
    }

    public void signUpButtonClicked(View view) {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }
}
