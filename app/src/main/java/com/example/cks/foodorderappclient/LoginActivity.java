package com.example.cks.foodorderappclient;

import android.content.Intent;
import android.provider.ContactsContract;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText email, pass;
    private DatabaseReference mdata;
    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = (EditText) findViewById(R.id.user_email);
        pass = (EditText) findViewById(R.id.user_pass);

        mauth = FirebaseAuth.getInstance();
        mdata = FirebaseDatabase.getInstance().getReference().child("users");
    }

    public void loginButonCicked(View view) {
        String text_email = email.getText().toString().trim();
        String text_pass = pass.getText().toString().trim();

        if (!TextUtils.isEmpty(text_email) && !TextUtils.isEmpty(text_pass)) {
            mauth.signInWithEmailAndPassword(text_email, text_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        checkUserExist();
                    }
                }
            });
        }
    }

        public void checkUserExist()
        {
            final String user_id = mauth.getCurrentUser().getUid();
            mdata.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild(user_id))
                    {
                        Intent menuIntent = new Intent(LoginActivity.this, MenuActivity.class);
                        startActivity(menuIntent);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
}
