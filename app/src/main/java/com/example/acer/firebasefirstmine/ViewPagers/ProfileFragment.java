package com.example.acer.firebasefirstmine.ViewPagers;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.acer.firebasefirstmine.R;
import com.example.acer.firebasefirstmine.Rtd;
import com.example.acer.firebasefirstmine.ViewPagerClass;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.security.AccessController.getContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        final String user_id = firebaseAuth.getCurrentUser().getUid();
        firestore.collection("Users").document(user_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String user_name = documentSnapshot.getString("name");
                String Image_Uri = documentSnapshot.getString("image");
                ((TextView) view.findViewById(R.id.username_Notification)).setText(user_name);
                Log.e("current_user_image", "" + Image_Uri);
                // Don't use RequestOptions beacuse it is not supported by old versions
                Glide.with(container.getContext()).load(Image_Uri).into(((CircleImageView) view.findViewById(R.id.userAccount_image)));
            }
        });
        view.findViewById(R.id.log_out_Notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> map = new HashMap<>();
                map.put("token_id", "");
                firestore.collection("Users").document(user_id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        firebaseAuth.signOut();
                        startActivity(new Intent(getActivity(), LogInActivity.class));
                        getActivity().finish();
                    }
                });

            }
        });
        return view;
    }

}
