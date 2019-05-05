package com.example.acer.firebasefirstmine.ViewPagers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.acer.firebasefirstmine.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatAdatper extends RecyclerView.Adapter<ChatAdatper.ViewHolders>{

    List<Users> list;
    Context context;


    public ChatAdatper(List<Users> list , Context context){
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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolders extends RecyclerView.ViewHolder{

        public ViewHolders(View itemView) {
            super(itemView);
        }
    }

}
