package com.example.vinh.booklinkers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

public class TestActivity extends AppCompatActivity {

    private Button btnMessage;
    private Firebase myFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_activty);

        Firebase.setAndroidContext(TestActivity.this);
        myFirebaseRef = new Firebase("https://booklinkers-database.firebaseio.com/");



        btnMessage = (Button)findViewById(R.id.button);

        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFirebaseRef.createUser("bobtony@firebase.com", "123456", new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        Toast.makeText(TestActivity.this, (String)result.get("uid"), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(FirebaseError firebaseError) {
                        Toast.makeText(TestActivity.this, firebaseError.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
