package com.example.vinh.booklinkers;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;

public class MessengerActivity extends AppCompatActivity {

    private static final String EXTRA_CURRENT_USER = "EXTRA_CURRENT_USER";
    private static final String EXTRA_OWNER_USER = "EXTRA_OWNER_USER";
    private static final String EXTRA_MESSENGER_USER = "EXTRA_MESSENGER_USER";

    private Firebase myFirebaseRef;
    private ListView lvMessenger;
    private ArrayList<String> messsageItems;
    private ArrayAdapter<String> itemsAdapter;
    private String currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        currentUser = getIntent().getExtras().getString(EXTRA_MESSENGER_USER);

        Firebase.setAndroidContext(MessengerActivity.this);
        myFirebaseRef = new Firebase("https://booklinkers-database.firebaseio.com/messenger/");

        lvMessenger = (ListView)findViewById(R.id.listview_messenger);

        // create adapter list view
        messsageItems = new ArrayList<>();

        itemsAdapter = new ArrayAdapter<String>(this, R.layout.list_messenger_item, messsageItems);

        lvMessenger.setAdapter(itemsAdapter);

        // show list users have message
        myFirebaseRef.child(currentUser).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                messsageItems.add(dataSnapshot.getValue().toString());
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

        // choose item on list
        lvMessenger.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public String clickedItem;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickedItem = lvMessenger.getItemAtPosition(position).toString();

                Intent intent = new Intent(MessengerActivity.this, MessageActivity.class);
                intent.putExtra(EXTRA_CURRENT_USER, currentUser);
                intent.putExtra(EXTRA_OWNER_USER, clickedItem);

                startActivity(intent);
            }
        });
    }
}
