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

public class BookEditingActivity extends AppCompatActivity {

    private static final java.lang.String EXTRA_BOOK_NAME = "EXTRA_BOOK_NAME";
    private Button btnApplyChange;
    private EditText etBookName;
    private EditText etBookAuthor;
    private EditText etBookDescription;
    private Firebase myFirebaseRef;
    private String gt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_editing);

        Firebase.setAndroidContext(BookEditingActivity.this);
        myFirebaseRef = new Firebase("https://booklinkers-db.firebaseio.com/");

        etBookName = (EditText)findViewById(R.id.edit_book_name);
        etBookAuthor = (EditText)findViewById(R.id.edit_book_author);
        etBookDescription = (EditText)findViewById(R.id.edit_book_description);

        gt2 = getIntent().getExtras().getString(EXTRA_BOOK_NAME);

        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                etBookName.setText(dataSnapshot
                        .child(ConnectingServerData.username)
                        .child("books")
                        .child(gt2)
                        .child("name")
                        .getValue().toString());

                etBookAuthor.setText(dataSnapshot
                        .child(ConnectingServerData.username)
                        .child("books")
                        .child(gt2)
                        .child("author")
                        .getValue().toString());

                etBookDescription.setText(dataSnapshot
                        .child(ConnectingServerData.username)
                        .child("books")
                        .child(gt2)
                        .child("description")
                        .getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        btnApplyChange = (Button)findViewById(R.id.button_apply_change);
        btnApplyChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFirebaseRef.child(ConnectingServerData.username)
                        .child("books")
                        .child(gt2)
                        .child("name")
                        .setValue(etBookName.getText().toString());
                myFirebaseRef.child(ConnectingServerData.username)
                        .child("books")
                        .child(gt2)
                        .child("author")
                        .setValue(etBookAuthor.getText().toString());
                myFirebaseRef.child(ConnectingServerData.username)
                        .child("books")
                        .child(gt2)
                        .child("description")
                        .setValue(etBookDescription.getText().toString());

                finish();
            }
        });
    }
}
