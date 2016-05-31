package com.example.vinh.booklinkers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.vinh.GlobalObject.ConnectingServerData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class YourInformationEditingActivity extends AppCompatActivity {

    private Button btnApply;
    private EditText etName;
    private EditText etPassword;
    private EditText etEmail;
    private EditText etBirthday;
    private EditText etAddress;
    private EditText etPhone;
    private Firebase myFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_information_editing);

        Firebase.setAndroidContext(YourInformationEditingActivity.this);
        myFirebaseRef = new Firebase("https://booklinkers-db.firebaseio.com/");

        etName = (EditText)findViewById(R.id.edit_name);
        etPassword = (EditText)findViewById(R.id.edit_password);
        etEmail = (EditText)findViewById(R.id.edit_email);
        etBirthday = (EditText)findViewById(R.id.edit_birthday);
        etAddress = (EditText)findViewById(R.id.edit_address);
        etPhone = (EditText)findViewById(R.id.edit_phone);

        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                etName.setText(dataSnapshot
                        .child(ConnectingServerData.username)
                        .child("information")
                        .child("name")
                        .getValue().toString());
                etPassword.setText(dataSnapshot
                        .child(ConnectingServerData.username)
                        .child("information")
                        .child("password")
                        .getValue().toString());
                etEmail.setText(dataSnapshot
                        .child(ConnectingServerData.username)
                        .child("information")
                        .child("email")
                        .getValue().toString());
                etBirthday.setText(dataSnapshot
                        .child(ConnectingServerData.username)
                        .child("information")
                        .child("birthday")
                        .getValue().toString());
                etAddress.setText(dataSnapshot
                        .child(ConnectingServerData.username)
                        .child("information")
                        .child("address")
                        .getValue().toString());
                etPhone.setText(dataSnapshot
                        .child(ConnectingServerData.username)
                        .child("information")
                        .child("phone")
                        .getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        btnApply = (Button)findViewById(R.id.button_apply);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFirebaseRef.child(ConnectingServerData.username)
                        .child("information")
                        .child("name")
                        .setValue(etName.getText().toString());

                finish();
            }
        });
    }
}
