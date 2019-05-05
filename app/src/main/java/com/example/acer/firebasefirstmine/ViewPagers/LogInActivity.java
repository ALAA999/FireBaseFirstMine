package com.example.acer.firebasefirstmine.ViewPagers;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.acer.firebasefirstmine.R;
import com.example.acer.firebasefirstmine.ViewPagerClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class LogInActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    EditText email, password;
    FirebaseFirestore firestore;
    ProgressBar progressBar;

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(LogInActivity.this, ViewPagerClass.class));
            this.finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar3);
        findViewById(R.id.new_account_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogInActivity.this, SignUpActivityNotification.class));
            }
        });
        email = findViewById(R.id.email_notification);
        password = findViewById(R.id.password_notification);
        findViewById(R.id.btn_login_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                if (Email.equals("") || Password.equals("")) {
                    Toast.makeText(LogInActivity.this, "All fields Required", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String token_id = FirebaseInstanceId.getInstance().getToken();
                                String user_id = firebaseAuth.getCurrentUser().getUid();
                                Map<String, Object> map = new HashMap<>();
                                map.put("token_id", token_id); // we update in oreder to keep the old data if we just call set method hte prevouis data will be deleted
                                // map.put("token_id" , FieldValue.delete()); Deletes the valuue and it's field
                                firestore.collection("Users").document(user_id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        startActivity(new Intent(LogInActivity.this, ViewPagerClass.class));
                                        finish();
                                    }
                                });
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(LogInActivity.this, "Task is not successful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
