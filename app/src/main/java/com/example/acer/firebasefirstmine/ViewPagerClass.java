package com.example.acer.firebasefirstmine;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.acer.firebasefirstmine.ViewPagers.LogInActivity;
import com.example.acer.firebasefirstmine.ViewPagers.ViewPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;

public class ViewPagerClass extends AppCompatActivity {

    ViewPager pager;
    TextView NotificationFragment, ProfileFragment, UserFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        NotificationFragment = findViewById(R.id.NotificationFragment);
        ProfileFragment = findViewById(R.id.ProfileFragment);
        UserFragment = findViewById(R.id.UserFragment);
        pager = findViewById(R.id.pager);// we get the viewpager from xml
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager()); // we initialize ViewPagerAdapter
        pager.setAdapter(viewPagerAdapter); // setting the adapter
        pager.setOffscreenPageLimit(2); // To keep the data synced this means when you go to the third fragment and get back to first one the data that you have uploaded will be saved will not be forgatten
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ChangeTabs(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        NotificationFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(1);
            }
        });
        ProfileFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(0);
            }
        });
        UserFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(2);
            }
        });
    }

    private void ChangeTabs(int position) {
        if (position == 0) {
            NotificationFragment.setTextColor(Color.GRAY);
            NotificationFragment.setTextSize(19);

            ProfileFragment.setTextColor(Color.RED);
            ProfileFragment.setTextSize(22);

            UserFragment.setTextColor(Color.GRAY);
            UserFragment.setTextSize(19);

        } else if (position == 1) {
            NotificationFragment.setTextColor(Color.RED);
            NotificationFragment.setTextSize(22);

            ProfileFragment.setTextColor(Color.GRAY);
            ProfileFragment.setTextSize(19);

            UserFragment.setTextColor(Color.GRAY);
            UserFragment.setTextSize(19);

        } else if (position == 2) {
            NotificationFragment.setTextColor(Color.GRAY);
            NotificationFragment.setTextSize(19);

            ProfileFragment.setTextColor(Color.GRAY);
            ProfileFragment.setTextSize(19);

            UserFragment.setTextColor(Color.RED);
            UserFragment.setTextSize(22);
        }
    }
}
