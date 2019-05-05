package com.example.acer.firebasefirstmine;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuth_Listiner;
    private EditText et_name, et_email, et_password;
    private ProgressDialog progressDialog;
    private DatabaseReference mDatabase;
    private ImageButton add_img;
    private StorageReference mStorage;
    private Uri img_uri_profile = null;
// here it also worked only with the pixel emulator.
    //but wht when i make the rules == it allows me to enter data from my Rtd class but i can't make a new user by the way below
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        et_email = findViewById(R.id.email);
        et_name = findViewById(R.id.fullname);
        et_password = findViewById(R.id.password);
        progressDialog = new ProgressDialog(this);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mStorage = FirebaseStorage.getInstance().getReference().child("Images_Profile");
        add_img = findViewById(R.id.add_img);
        mAuth_Listiner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

        add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });
        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartRegister();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuth_Listiner);
    }

    public void StartRegister() {
        progressDialog.setMessage("Register ..");
        progressDialog.show();
        final String email = et_email.getText().toString();
        final String name = et_name.getText().toString();
        final String password = et_password.getText().toString();
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(password) && img_uri_profile != null) {
            StorageReference filepath = mStorage.child(img_uri_profile.getLastPathSegment());
            filepath.putFile(img_uri_profile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final String img = taskSnapshot.getDownloadUrl().toString();
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                String id = mAuth.getCurrentUser().getUid();
                                DatabaseReference mDatabaseUser = mDatabase.child(id);
                                mDatabase.child(id).child("Name").setValue(name);
                                mDatabase.child(id).child("Email").setValue(email);
                                mDatabase.child(id).child("Image").setValue(img);
                                mDatabase.child(id).child("Passowrd").setValue(password);
                                Toast.makeText(SignUpActivity.this, "Child made", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUpActivity.this, face_activity.class));
                            } else {
                                Toast.makeText(SignUpActivity.this, "Failed !", Toast.LENGTH_SHORT).show();
                                progressDialog.hide();
                            }


                        }
                    });

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri img_uri = data.getData();
            CropImage.activity(img_uri).setGuidelines(CropImageView.Guidelines.ON)
                    .start(SignUpActivity.this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                add_img.setImageURI(resultUri);
                img_uri_profile = resultUri;
            }
        }
    }
}
