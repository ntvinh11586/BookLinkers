package com.example.vinh.booklinkers;

import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class MessengerActivity extends AppCompatActivity {

    private ListView lvMessage;
    private String followingUser;
    private String currentUser;
    private String keyMessage;
    private ArrayList<String> messsageItems;
    private ArrayAdapter<String> itemsAdapter;
    private Firebase myFirebaseRef;
    private ImageButton btnSendMessage;
    private EditText etMessageContent;
    private Firebase myFirebaseRefMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        Firebase.setAndroidContext(MessengerActivity.this);
        myFirebaseRef = new Firebase("https://booklinkers-database.firebaseio.com/messager/");

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

//        getActionBar().setDisplayHomeAsUpEnabled(true);

        followingUser = "vinh456";
        currentUser = "vinh123";

        setTitle(followingUser + "'s message");

        lvMessage = (ListView)findViewById(R.id.listview_message);
        btnSendMessage = (ImageButton) findViewById(R.id.button_send_message);
        etMessageContent = (EditText)findViewById(R.id.edit_message_content);

        // create string to connect database
        if (currentUser.compareTo(followingUser) > 0) {
            keyMessage = followingUser + currentUser;
        } else {
            keyMessage = currentUser + followingUser;
        }

        // create adapter list view
        messsageItems = new ArrayList<>();

        itemsAdapter = new ArrayAdapter<String>(this, R.layout.list_message_item, messsageItems);

        lvMessage.setAdapter(itemsAdapter);

        myFirebaseRefMessage = new Firebase(
                "https://booklinkers-database.firebaseio.com/messager/"+keyMessage+"/");

        myFirebaseRefMessage.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.child("message").getValue().toString();
                String name = dataSnapshot.child("owner").getValue().toString();

                messsageItems.add(name + ": " + value);

                itemsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

//        myFirebaseRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot postSnapshot : dataSnapshot.child(keyMessage).getChildren()) {
//                    String value = postSnapshot.child("message").getValue().toString();
//                    String name = postSnapshot.child("owner").getValue().toString();
//
//                    messsageItems.add(name + ": " + value);
//                }
//                itemsAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });

        // send message
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageContent = etMessageContent.getText().toString();
                etMessageContent.setText("");

                com.example.vinh.DataObjects.Message message =
                        new com.example.vinh.DataObjects.Message(messageContent, currentUser);

                myFirebaseRef.child(keyMessage).push().setValue(message);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater(); // reads XML
        inflater.inflate(R.menu.menu_message, menu); // to create
        return super.onCreateOptionsMenu(menu); // the menu
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_send) {
//        // do something;
//        } else if (item.getItemId() == R.id.action_archive) {
//        // do something;
//        } else if (item.getItemId() == R.id.action_open) {
//        // do something;
//        }
        return super.onOptionsItemSelected(item);
    }

}
