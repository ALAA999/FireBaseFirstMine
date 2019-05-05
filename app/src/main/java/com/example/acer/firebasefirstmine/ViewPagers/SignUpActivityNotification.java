package com.example.acer.firebasefirstmine.ViewPagers;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.acer.firebasefirstmine.R;
import com.example.acer.firebasefirstmine.SignUpActivity;
import com.example.acer.firebasefirstmine.ViewPagerClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SignUpActivityNotification extends AppCompatActivity {

    Uri img_uri_profile;
    ImageView addImage;
    EditText Email, password, UserName;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    FirebaseFirestore firestore;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();
        findViewById(R.id.back_to_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Email = findViewById(R.id.email_notification);
        password = findViewById(R.id.password_notification);
        UserName = findViewById(R.id.name_notification);
        addImage = findViewById(R.id.add_img);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 2);

            }
        });
        findViewById(R.id.btn_signup_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                final String name_string = UserName.getText().toString();
                final String password_string = password.getText().toString();
                final String email = Email.getText().toString();
                if (img_uri_profile != null) {
                    if (name_string.equals("") || password_string.equals("") || email.equals("")) {
                        Toast.makeText(SignUpActivityNotification.this, "All Fields Required", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    } else {
                        firebaseAuth.createUserWithEmailAndPassword(email, password_string).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    final String user_id = firebaseAuth.getCurrentUser().getUid();
                                    StorageReference filePath = storageReference.child("images/" + user_id + ".jpg");
                                    filePath.putFile(img_uri_profile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> upload_task) {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            final String image_uri = upload_task.getResult().getDownloadUrl().toString();

                                            Map<String, Object> user_map = new HashMap<>();
                                            user_map.put("name", name_string);
                                            user_map.put("token_id", FirebaseInstanceId.getInstance().getToken());
                                            user_map.put("image", image_uri); // the name of the key saved will be saved in cloudfirestore you should be using the same key when you get it back again
//                                            databaseReference.child(user_id).setValue(user_map);
//                                            Toast.makeText(SignUpActivityNotification.this, "New Account Created!", Toast.LENGTH_SHORT).show();
//                                            startActivity(new Intent(SignUpActivityNotification.this, ViewPagerClass.class));
//                                            finish();
                                            firestore.collection("Users").document(user_id).set(user_map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    Toast.makeText(SignUpActivityNotification.this, "New Account Created!", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(SignUpActivityNotification.this, ViewPagerClass.class));
                                                    finish();
                                                }
                                            });
                                        }
                                    });
                                } else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(SignUpActivityNotification.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {
            Uri img_uri = data.getData();
            CropImage.activity(img_uri).setGuidelines(CropImageView.Guidelines.ON)
                    .start(SignUpActivityNotification.this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                addImage.setImageURI(resultUri);
                img_uri_profile = resultUri;
            }
        }
    }
}
