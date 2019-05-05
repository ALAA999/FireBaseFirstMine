package com.example.acer.firebasefirstmine;


import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ctrlplusz.anytextview.AnyEditTextView;
import com.example.acer.firebasefirstmine.ChatFirebase.ChatActivityFirebase;
import com.example.acer.firebasefirstmine.ViewPagers.LogInActivity;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private Firebase mRef;
    private ArrayList<Rtd> arrayList;
    private String names[];
    private ArrayAdapter<String> adapter;
    private ArrayList<String> strings = new ArrayList<>();
    private Rtd std1;
    private StorageReference storageReference;
    private static final int GALLERY_INTENT = 71;
    private DatabaseReference databaseReference, databaseReferenceUpdate;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(MainActivity.this, ChatActivityFirebase.class));
        findViewById(R.id.face_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Log_in.class));
                finish();
            }
        });

        findViewById(R.id.viewpager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LogInActivity.class));
            }
        });

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences preferences = getSharedPreferences("Data", MODE_PRIVATE);
                Log.e("MyToken", "" + preferences.getString("MyToken", null));
            } // if you run the app for the first time it will not generate the token because it need sometime to reach the MyFirebaseInstanceIDService so we make BroadcastReceiver
        };
        registerReceiver(broadcastReceiver, new IntentFilter(MyFirebaseInstanceIDService.TAG));

        findViewById(R.id.pieChart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this , pieChartActivity.class));
            }
        });

        Firebase.setAndroidContext(getApplicationContext());
        FirebaseApp.initializeApp(getApplicationContext());
        mRef = new Firebase("https://firsttest-f8e90.firebaseio.com/");
        //Send_Notification();

        storageReference = FirebaseStorage.getInstance().getReference();
        arrayList = new ArrayList();
        databaseReference = FirebaseDatabase.getInstance().getReference("Rtd");
        (findViewById(R.id.click)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = databaseReference.push().getKey();//This is the id of this object saved in firebase
                String name = ((EditText) findViewById(R.id.name)).getText().toString();
                String num = ((EditText) findViewById(R.id.id)).getText().toString();
                Rtd s = new Rtd(name, num, id);
                databaseReference.child(id).setValue(s);
                Toast.makeText(MainActivity.this, "Data Saved", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Send_Notification();
            }
        });
        listView = findViewById(R.id.list_view);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final Dialog dilog = new Dialog(MainActivity.this);
                dilog.setContentView(R.layout.alert_dilaog);
                std1 = arrayList.get(i);
                databaseReferenceUpdate = FirebaseDatabase.getInstance().
                        getReference("Rtd").child(std1.getId() + "");
                ((TextView) dilog.findViewById(R.id.name_text)).setText(adapterView.getItemAtPosition(i) + "");
                dilog.show();
                dilog.findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String new_name = ((EditText) dilog.findViewById(R.id.new_name)).getText().toString();
                        Rtd std = new Rtd(new_name, std1.getNum(), std1.getId());
                        databaseReferenceUpdate.setValue(std);
                        dilog.dismiss();
                    }
                });
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Rtd r = arrayList.get(i);
                DatabaseReference databaseReference3 = FirebaseDatabase.getInstance()
                        .getReference("Rtd").child(r.getId());
                databaseReference3.removeValue();
            }
        });

        (findViewById(R.id.upload_img)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 71);
                /*Intent cameraIntent = new Intent(Intent.ACTION_PICK);
                cameraIntent.setType("image/*");
                startActivityForResult(cameraIntent, GALLERY_INTENT);*/
            }
        });
       /* findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searched_word = ((EditText)findViewById(R.id.search_edit_txt)).getText().toString();
                ArrayList<String> strings = new ArrayList<>();
                for (int i = 0; i < names.length; ++i){
                    if(names[i].contains(searched_word)){
                        strings.add(names[i]);
                    }
                }
                adapter = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_list_item_1, strings);
                listView.setAdapter(adapter);
            }
        });*/
        ((AnyEditTextView) findViewById(R.id.search_edit_txt)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Toast.makeText(MainActivity.this, "he", Toast.LENGTH_SHORT).show();
                strings.clear();
                if (charSequence.length() == 0) {
                    update_data();
                }
                for (int j = 0; j < names.length; ++j) {
                    if (names[j].contains(charSequence)) {
                        strings.add(names[j]);
                    }
                }
                adapter = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_list_item_1, strings);
                listView.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void Send_Notification() {
        //Send Notification for the current device not on firebase
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(MainActivity.this, "")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle(" Message")
                        .setContentText("Test Message")
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == GALLERY_INTENT) {
            Uri uri = data.getData();
            ((ImageView) findViewById(R.id.img)).setImageURI(uri);
            StorageReference filepath = storageReference.child("images/" + UUID.randomUUID().toString());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String img_URI = taskSnapshot.getDownloadUrl().toString();
                    Toast.makeText(MainActivity.this, "Uploading Done..", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    //few things you have to notic
    //1.class Std starts with captel letter
    //2.class Std must have an empty constracutor
    //this is for realtilme firebase
    //Now you have 2 classes Std & Rtd the first time you run the class on firebase you should not change depending on my knowloadge

    public void update_data() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Rtd student = dataSnapshot1.getValue(Rtd.class);
                    arrayList.add(student);
                }
                names = new String[arrayList.size()];
                for (int i = 0; i < arrayList.size(); ++i) {
                    names[i] = arrayList.get(i).getName();
                }

                adapter = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_list_item_1, names);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        update_data();
    }
}
