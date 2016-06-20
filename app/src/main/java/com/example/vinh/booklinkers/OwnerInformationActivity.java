package com.example.vinh.booklinkers;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vinh.GlobalObject.ConnectingServerData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class OwnerInformationActivity
        extends AppCompatActivity
        implements ActionBar.TabListener{

    private static final String EXTRA_OWNER_USERNAME = "EXTRA_OWNER_USERNAME";
    private static final String EXTRA_YOUR_LOCATION = "EXTRA_YOUR_LOCATION";
    private static final String EXTRA_OWNER_LOCATION = "EXTRA_OWNER_LOCATION";
    private static final String EXTRA_BOOK_NAME = "EXTRA_BOOK_NAME";
    private static final String EXTRA_USERNAME = "EXTRA_USERNAME";
    private static final int EXTRA_REQUEST_CODE = 999;

    private Button btnCall;
    private Button btnSendMessage;
    private String phoneNumber;
    private String email;
    private String ownerUsername;
    private TextView tvName;
    private TextView tvUsername;
    private TextView tvEmail;
    private TextView tvBirthday;
    private TextView tvPhone;
    private TextView tvAddress;
    private Firebase myFirebaseRef;
    private Button btnDirection;
    private ListView lvOnwerBooks;
    private ArrayList<String> books;
    private ArrayAdapter<String> ownerBooksAdapter;
    private ListView lvOnwerNeededBooks;
    private ArrayList<String> neededBooks;
    private ArrayAdapter<String> ownerNeededBooksAdapter;
    private ArrayList<String> listMyBooks;
    private ArrayList<String> listOwnerHavingBooks;
    private ArrayList<String> listOwnerNeededBooks;
    private String strLvItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Firebase.setAndroidContext(OwnerInformationActivity.this);
        myFirebaseRef = new Firebase("https://booklinkers-db.firebaseio.com/");

        ownerUsername = getIntent().getExtras().getString(EXTRA_OWNER_USERNAME);


        listMyBooks = new ArrayList<String>();

        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot
                        .child(ConnectingServerData.username)
                        .child("books")
                        .getChildren()) {
                    listMyBooks.add(postSnapshot.child("name").getValue().toString());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        listOwnerHavingBooks = new ArrayList<String>();

        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot
                        .child(ownerUsername)
                        .child("books")
                        .getChildren()) {

                    if (false && true)
                        break;

                    for (int i = 0; i < listMyBooks.size(); i++)
                        if (listMyBooks.get(i).equals(postSnapshot.child("name").getValue().toString())
                                && (boolean)postSnapshot.child("own").getValue())
                            listOwnerHavingBooks.add(postSnapshot.child("name").getValue().toString());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        listOwnerNeededBooks = new ArrayList<String>();

        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot
                        .child(ownerUsername)
                        .child("books")
                        .getChildren()) {

                    if (false && true)
                        break;

                    for (int i = 0; i < listMyBooks.size(); i++)
                        if (listMyBooks.get(i).equals(postSnapshot.child("name").getValue().toString())
                                && !(boolean)postSnapshot.child("own").getValue())
                            listOwnerNeededBooks.add(postSnapshot.child("name").getValue().toString());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });




//        setContentView(R.layout.activity_owner_personal_information);

        ActionBar ab = getSupportActionBar();
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Three tab to display in actionbar
        ab.addTab(ab.newTab().setText("Information").setTabListener(this));
        ab.addTab(ab.newTab().setText("Having Books").setTabListener(this));
        ab.addTab(ab.newTab().setText("Needed Books").setTabListener(this));
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

        //Called when a tab is selected
        int nTabSelected = tab.getPosition();
        switch (nTabSelected) {
            case 0:
                setContentView(R.layout.activity_owner_personal_information);

                btnCall = (Button)findViewById(R.id.button_call);
                btnSendMessage = (Button)findViewById(R.id.button_send_message);
                btnDirection = (Button)findViewById(R.id.button_view_direction);

//                Toast.makeText(getApplicationContext(), getResult, Toast.LENGTH_SHORT).show();

                tvName = (TextView)findViewById(R.id.text_name);
                tvUsername = (TextView)findViewById(R.id.text_username);
                tvEmail = (TextView) findViewById(R.id.text_email);
                tvBirthday = (TextView)findViewById(R.id.text_birthday);
                tvPhone = (TextView)findViewById(R.id.text_phone);
                tvAddress = (TextView)findViewById(R.id.text_address);

                Firebase.setAndroidContext(OwnerInformationActivity.this);
                myFirebaseRef = new Firebase("https://booklinkers-db.firebaseio.com/");

                myFirebaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DataSnapshot snapshot = dataSnapshot
                                .child(ownerUsername)
                                .child("information");

                        tvName.setText(snapshot.child("name").getValue().toString());
                        tvUsername.setText(snapshot.child("username").getValue().toString());
                        tvEmail.setText(snapshot.child("email").getValue().toString());

                        tvBirthday.setText(snapshot.child("birthday").getValue().toString());
                        tvPhone.setText(snapshot.child("phone").getValue().toString());
                        tvAddress.setText(snapshot.child("address").getValue().toString());
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

                phoneNumber = "+84937761608";
                email = "ntvinh.11586@gmail.com";

                btnCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri number = Uri.parse("tel:"+phoneNumber);
                        Intent intent = new Intent(Intent.ACTION_DIAL, number);
                        startActivity(intent);
                    }
                });

                btnSendMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("message/rfc822");
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                        try {
                            startActivity(Intent.createChooser(intent, "Send mail..."));
                        } catch (android.content.ActivityNotFoundException ex) {

                        }
                    }
                });

                btnDirection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(OwnerInformationActivity.this,
                                OwnerDirectionMapsActivity.class);

                        intent.putExtra(EXTRA_YOUR_LOCATION, "Dai hoc Bach Khoa");
                        intent.putExtra(EXTRA_OWNER_LOCATION, tvAddress.getText().toString());

                        startActivity(intent);

                    }
                });

                break;





            case 1:
                setContentView(R.layout.activity_owner_having_books);


                lvOnwerBooks = (ListView)findViewById(R.id.list_owner_books);

                books = listOwnerHavingBooks;

                ownerBooksAdapter = new ArrayAdapter<String>(this, R.layout.list_book_item, books);
                lvOnwerBooks.setAdapter(ownerBooksAdapter);

                lvOnwerBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        strLvItem = lvOnwerBooks.getItemAtPosition(position).toString();
                        myFirebaseRef.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot: dataSnapshot
                                        .child(ownerUsername)
                                        .child("books")
                                        .getChildren()) {
                                    if (postSnapshot.child("name")
                                            .getValue()
                                            .toString()
                                            .equals(strLvItem)) {
                                        Intent intent = new Intent(OwnerInformationActivity.this,
                                                BookInformationActivity.class);

                                        intent.putExtra(EXTRA_USERNAME, ownerUsername);

                                        intent.putExtra(EXTRA_BOOK_NAME,
                                                postSnapshot.getKey().toString());

                                        startActivity(intent);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });

                    }
                });



                break;

            case 2:
                setContentView(R.layout.activity_owner_needed_books);

                lvOnwerNeededBooks = (ListView)findViewById(R.id.list_owner_needed_books);

                neededBooks = listOwnerNeededBooks;
                ownerNeededBooksAdapter = new ArrayAdapter<String>(this, R.layout.list_book_item, neededBooks);
                lvOnwerNeededBooks.setAdapter(ownerNeededBooksAdapter);

                lvOnwerNeededBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        strLvItem = lvOnwerNeededBooks.getItemAtPosition(position).toString();
                        myFirebaseRef.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot: dataSnapshot
                                        .child(ownerUsername)
                                        .child("books")
                                        .getChildren()) {
                                    if (postSnapshot.child("name")
                                            .getValue()
                                            .toString()
                                            .equals(strLvItem)) {
                                        Intent intent = new Intent(OwnerInformationActivity.this,
                                                BookInformationActivity.class);

                                        intent.putExtra(EXTRA_USERNAME, ownerUsername);

                                        intent.putExtra(EXTRA_BOOK_NAME,
                                                postSnapshot.getKey().toString());

                                        startActivity(intent);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });

                    }
                });

                break;
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // Called when a tab unselected.
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

        // Called when a tab is selected again.
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
