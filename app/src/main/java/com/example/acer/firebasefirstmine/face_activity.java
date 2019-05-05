package com.example.acer.firebasefirstmine;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.acer.firebasefirstmine.Models.Posts;
import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


public class face_activity extends AppCompatActivity {
    RecyclerView rv_posts;
    DatabaseReference mDatabase;
    DatabaseReference mDatabaseUsers;
    DatabaseReference mDatabaseLikes;
    DatabaseReference mDatabase_Post;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListiner;
    FirebaseUser firebaseUser;
    private boolean isLike = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_activity);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseLikes = FirebaseDatabase.getInstance().getReference().child("Likes");
        mDatabase.keepSynced(true);
        mDatabaseUsers.keepSynced(true);
        mDatabaseLikes.keepSynced(true);
        rv_posts = findViewById(R.id.rv);
        rv_posts.setHasFixedSize(true);
        rv_posts.setLayoutManager(new LinearLayoutManager(this));
        mAuthListiner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent intent = new Intent(face_activity.this, Log_in.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    checkUserExist();
                }
            }
        };

    }


    public void checkUserExist() {
//        final String uIdCurrentUser = firebaseUser.getUid().toString();
//        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (!dataSnapshot.hasChild(uIdCurrentUser)) {
//                    Intent intent = new Intent(face_activity.this, Log_in.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListiner);
        FirebaseRecyclerAdapter<Posts, BlogViewHolder> arrayadapter = new FirebaseRecyclerAdapter<Posts, BlogViewHolder>(
                Posts.class,
                R.layout.item,
                BlogViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(final BlogViewHolder viewHolder, Posts model, int position) {
                final String KeyPost = getRef(position).getKey().toString();
                mDatabase_Post = FirebaseDatabase.getInstance().getReference().child("Users").child(model.getUid());
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDetails(model.getDetails());
                viewHolder.setImage(getApplicationContext(), model.getImage());
                viewHolder.setUsername(model.getUsername());
                viewHolder.setLikePost(KeyPost);
                mDatabase_Post.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        viewHolder.setImageUser(getApplicationContext(), dataSnapshot.child("Image").getValue().toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                viewHolder.like_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        isLike = true;
                        mDatabaseLikes.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (isLike) {
                                    if (dataSnapshot.child(KeyPost).hasChild(mAuth.getCurrentUser().getUid())) {
                                        mDatabaseLikes.child(KeyPost).child(mAuth.getCurrentUser().getUid()).removeValue();
                                        viewHolder.like_btn.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                                    }
                                    isLike = false;
                                } else {
                                    mDatabaseLikes.child(KeyPost).child(mAuth.getCurrentUser().getUid()).setValue("Any Data");
                                    viewHolder.like_btn.setImageResource(R.drawable.ic_thumb_up_red_24dp);
                                    isLike = false;

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });

            }
        };

       // rv_posts.setAdapter(arrayadapter);
    }
    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mView;
        ImageButton like_btn;
        DatabaseReference mDatabaseLike;
        FirebaseAuth mAuth;

        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            like_btn = itemView.findViewById(R.id.like_btn);
            mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
            mAuth = FirebaseAuth.getInstance();
        }

        public void setLikePost(final String PostKey) {
            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(PostKey).hasChild(mAuth.getCurrentUser().getUid())) {
                        like_btn.setImageResource(R.drawable.ic_thumb_up_red_24dp);
                    } else {
                        like_btn.setImageResource(R.drawable.ic_thumb_up_black_24dp);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public void setTitle(String title) {
            TextView txt_title = mView.findViewById(R.id.txt_title);
            txt_title.setText(title);
        }

        public void setUsername(String username) {
            TextView txtusername = mView.findViewById(R.id.username);
            txtusername.setText(username);
        }

        public void setDetails(String Details) {
            TextView txtdetails = mView.findViewById(R.id.txt_desc);
            txtdetails.setText(Details);
        }

        public void setImage(final Context mCtx, final String img) {
            final ImageView img_post = mView.findViewById(R.id.img_psot);
            Picasso.with(mCtx).load(img).into(img_post);
            Picasso.with(mCtx).load(img).networkPolicy(NetworkPolicy.OFFLINE).into(img_post, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(mCtx).load(img).into(img_post);
                }
            });
        }

        public void setImageUser(final Context mCtx, final String img) {
            final ImageView img_post = mView.findViewById(R.id.img_user);
            Picasso.with(mCtx).load(img).into(img_post);
            Picasso.with(mCtx).load(img).networkPolicy(NetworkPolicy.OFFLINE).into(img_post, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(mCtx).load(img).into(img_post);
                }
            });
        }
    }
}
