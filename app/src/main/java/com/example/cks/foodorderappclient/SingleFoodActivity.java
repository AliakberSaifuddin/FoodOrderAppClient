package com.example.cks.foodorderappclient;

import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SingleFoodActivity extends AppCompatActivity {
    private TextView name, desc, price;
    private ImageView singleFoodImage;
    private String food_key = null;
    private DatabaseReference mDatabase, userData, mRef;
    private FirebaseAuth mAuth;
    private FirebaseUser current_user;
    private String text_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_food);

        food_key = getIntent().getExtras().getString("FoodId");
        name = (TextView) findViewById(R.id.SingleName);
        desc = (TextView) findViewById(R.id.SingleDesc);
        price = (TextView) findViewById(R.id.SinglePrice);
        singleFoodImage = (ImageView) findViewById(R.id.SingleImageView);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Item");
        mRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        mAuth = FirebaseAuth.getInstance();

        current_user = mAuth.getCurrentUser();
        userData = FirebaseDatabase.getInstance().getReference().child("users").child(current_user.getUid());

        mDatabase.child(food_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                text_name = (String) dataSnapshot.child("name").getValue();
                String text_desc = (String) dataSnapshot.child("desc").getValue();
                String text_price = (String) dataSnapshot.child("price").getValue();
                String food_image = (String) dataSnapshot.child("image").getValue();

                name.setText(text_name);
                desc.setText(text_desc);
                price.setText(text_price);
                Picasso.with(SingleFoodActivity.this).load(food_image).into(singleFoodImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void orderItemClicked(View view) {
       final DatabaseReference mDataBaseRef =  mRef.push();

       userData.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               mDataBaseRef.child("itemname").setValue(text_name);
               mDataBaseRef.child("username").setValue(dataSnapshot.child("Name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       startActivity(new Intent(SingleFoodActivity.this,MenuActivity.class));
                   }
               });
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });
    }
}
