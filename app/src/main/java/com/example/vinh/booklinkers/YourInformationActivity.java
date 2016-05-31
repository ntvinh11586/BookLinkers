package com.example.vinh.booklinkers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.vinh.GlobalObject.ConnectingServerData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.w3c.dom.Text;

public class YourInformationActivity extends AppCompatActivity {

    private Button btnChangeInformation;

    private TextView tvName;
    private TextView tvUsername;
    private TextView tvEmail;
    private TextView tvBirthday;
    private TextView tvPhone;
    private TextView tvAddress;

    private Firebase myFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_personal_information);

        tvName = (TextView)findViewById(R.id.text_name);
        tvUsername = (TextView)findViewById(R.id.text_username);
        tvEmail = (TextView) findViewById(R.id.text_email);
        tvBirthday = (TextView)findViewById(R.id.text_birthday);
        tvPhone = (TextView)findViewById(R.id.text_phone);
        tvAddress = (TextView)findViewById(R.id.text_address);

        Firebase.setAndroidContext(YourInformationActivity.this);
        myFirebaseRef = new Firebase("https://booklinkers-db.firebaseio.com/");

        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot snapshot = dataSnapshot
                        .child(ConnectingServerData.username)
                        .child("information");

                tvName.setText(snapshot.child("name").getValue().toString());
                tvUsername.setText("Username            " + snapshot.child("username").getValue().toString());
                tvEmail.setText("Email          " + snapshot.child("email").getValue().toString());

                tvBirthday.setText("Birthday            " + snapshot.child("name").getValue().toString());
                tvPhone.setText("Phone          " + snapshot.child("name").getValue().toString());
                tvAddress.setText("Address         "+ snapshot.child("name").getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        btnChangeInformation = (Button)findViewById(R.id.button_change_information);
        btnChangeInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(YourInformationActivity.this, YourInformationEditingActivity.class);
                startActivity(intent);
            }
        });
    }
}
