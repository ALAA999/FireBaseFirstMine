package com.example.acer.firebasefirstmine.ViewPagers;

import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.acer.firebasefirstmine.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class SendActivity extends AppCompatActivity {

    EditText notification_message;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    String current_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        final String user_id = getIntent().getStringExtra("user_id");
        final String user_name = getIntent().getStringExtra("user_name");
        firebaseAuth = FirebaseAuth.getInstance();
        current_user_id = firebaseAuth.getUid();
        ((TextView) findViewById(R.id.user_sedning_notificatino)).setText("" + user_name);
        progressBar = findViewById(R.id.progressBar2);
        firestore = FirebaseFirestore.getInstance();
        notification_message = findViewById(R.id.notification_message);
        findViewById(R.id.send_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = notification_message.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                if (message.equals("")) {
                    Toast.makeText(SendActivity.this, "Enter Message", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> Notification_Map = new HashMap<>();
                    Notification_Map.put("message", message);
                    Notification_Map.put("from", current_user_id); // current user id is the sender of the notification
                    firestore.collection("Users/" + user_id + "/Notification").add(Notification_Map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        // we will put the Notification_Map in the collection of user who recives the message
                        public void onSuccess(DocumentReference documentReference) {
                            progressBar.setVisibility(View.INVISIBLE);
                            notification_message.setText("");
                            Toast.makeText(SendActivity.this, "Notification Sent", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SendActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });
    }
}
