package com.example.cks.foodorderappclient;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cks.foodorderappclient.Food;
import com.example.cks.foodorderappclient.LoginActivity;
import com.example.cks.foodorderappclient.MainActivity;
import com.example.cks.foodorderappclient.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MenuActivity extends AppCompatActivity {

    private RecyclerView mfoodList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        mfoodList = (RecyclerView) findViewById(R.id.mrecyleList);
        mfoodList.setHasFixedSize(true);
        mfoodList.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Item");

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    Intent loginIntent = new Intent(MenuActivity.this,MainActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseRecyclerAdapter<Food,FoodViewHolder> FBRA = new FirebaseRecyclerAdapter<Food,FoodViewHolder>(
                Food.class,
                R.layout.singlemenuitem,
                FoodViewHolder.class,
                mDatabase
        ){
            @Override
            protected  void populateViewHolder(FoodViewHolder viewHolder, Food model, int position){
                viewHolder.setName(model.getName());
                viewHolder.setPrice(model.getPrice());
                viewHolder.setDesc(model.getDescrip());
                viewHolder.setImage(getApplicationContext(),model.getImage());

                final String food_key = getRef(position).getKey().toString();

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent singleFoodIntent = new Intent(MenuActivity.this, SingleFoodActivity.class);
                        singleFoodIntent.putExtra("FoodId", food_key);
                        startActivity(singleFoodIntent);
                    }
                });
            }
        };
        mfoodList.setAdapter(FBRA);
    }
    public static  class FoodViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public FoodViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }


        public void setName (String name){
            TextView bread_name = (TextView) mView.findViewById(R.id.foodName);
            bread_name.setText(name);
        }
        public void setDesc (String desc){
            TextView bread_desc = (TextView) mView.findViewById(R.id.foodDescrip);
            bread_desc.setText(desc);

        }
        public void setPrice (String price){
            TextView bread_price = (TextView) mView.findViewById(R.id.foodPrice);
            bread_price.setText(price);

        }
        public void setImage (Context ctx, String image){
            ImageView bread_image = (ImageView) mView.findViewById(R.id.foodImage);
            Picasso.with(ctx).load(image).into(bread_image);

        }
    }


}