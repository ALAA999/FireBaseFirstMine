package com.example.acer.firebasefirstmine.ViewPagers;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.acer.firebasefirstmine.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolders>{

    List<Users> list;
    Context context;


    public UserRecyclerViewAdapter(List<Users> list , Context context){
        this.list = list;
        this.context = context;
    }
    @Override
    public ViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list_adapter , parent , false);
        return new ViewHolders(view);
    }

    @Override
    public void onBindViewHolder(ViewHolders holder, int position) {
        final Users users = list.get(position);
        Glide.with(context).load(users.getImage()).into(holder.circleImageView);
        holder.textView.setText(users.getName());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context , SendActivity.class);
                intent.putExtra("user_id" , users.getUserID());
                intent.putExtra("user_name" , users.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolders extends RecyclerView.ViewHolder{

        CircleImageView circleImageView;
        TextView textView;
        LinearLayout linearLayout;
        public ViewHolders(View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.user_adapter_linear);
            textView = itemView.findViewById(R.id.username_text_Notification);
            circleImageView = itemView.findViewById(R.id.user_image_rv);
        }
    }

}
