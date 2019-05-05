package com.example.acer.firebasefirstmine.ViewPagers;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.acer.firebasefirstmine.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {


    RecyclerView recyclerView;
    UserRecyclerViewAdapter adapter;
    List<Users> list;
    FirebaseFirestore firestore;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        recyclerView = view.findViewById(R.id.users_rv);
        list = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        adapter = new UserRecyclerViewAdapter(list , getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        list.clear();
        firestore.collection("Users").addSnapshotListener(getActivity() , new EventListener<QuerySnapshot>() {
            @Override // we pass the getActivity on addSnapshotListener to avoid crashes because listener keeps listening another reason is because we are in a fragment
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){
                        Users users = doc.getDocument().toObject(Users.class);
                        Users u = new Users(users.getName() , users.getImage() ,doc.getDocument().getId());
                        // Make sure that varailbes are private and when you call them you use getters and setters
                        list.add(u);
                    }
                }
                adapter.notifyDataSetChanged(); // to refresh out recyclerView adapter
            }
        });
    }
}
