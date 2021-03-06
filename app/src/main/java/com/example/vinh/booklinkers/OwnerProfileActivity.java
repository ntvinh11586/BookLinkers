package com.example.vinh.booklinkers;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vinh.GlobalObject.ConnectingServerData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class OwnerProfileActivity
        extends AppCompatActivity
        implements ActionBar.TabListener{

    private static final String EXTRA_OWNER_USERNAME = "EXTRA_OWNER_USERNAME";
    private static final String EXTRA_YOUR_LOCATION = "EXTRA_YOUR_LOCATION";
    private static final String EXTRA_OWNER_LOCATION = "EXTRA_OWNER_LOCATION";
    private static final String EXTRA_BOOK_NAME = "EXTRA_BOOK_NAME";
    private static final String EXTRA_USERNAME = "EXTRA_USERNAME";

    private static final String EXTRA_CURRENT_USER = "EXTRA_CURRENT_USER";
    private static final String EXTRA_OWNER_USER = "EXTRA_OWNER_USER";

    private static final int EXTRA_REQUEST_CODE = 999;

    private Button btnCall;
    private Button btnSendMessage;
    private String ownerUsername;

    // show information
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
    private Button btnNotify;
    private String yourUsername;
    private ImageView ivAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Firebase.setAndroidContext(OwnerProfileActivity.this);
        myFirebaseRef = new Firebase("https://booklinkers-db.firebaseio.com/");

        ownerUsername = getIntent().getExtras().getString(EXTRA_OWNER_USERNAME);
        yourUsername = ConnectingServerData.username;

        setTitle(ownerUsername + "'s Profile");

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


//        setContentView(R.layout.activity_owner_profile);

        ActionBar ab = getSupportActionBar();
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ab.setDisplayHomeAsUpEnabled(true);

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

            // owner personal information: information tab
            case 0:
                setContentView(R.layout.activity_owner_profile);

                Firebase.setAndroidContext(OwnerProfileActivity.this);
                myFirebaseRef = new Firebase("https://booklinkers-db.firebaseio.com/");

                btnCall = (Button)findViewById(R.id.button_call);
                btnSendMessage = (Button)findViewById(R.id.button_send_message);
                btnDirection = (Button)findViewById(R.id.button_view_direction);

                tvName = (TextView)findViewById(R.id.text_name);
                tvUsername = (TextView)findViewById(R.id.text_username);
                tvEmail = (TextView) findViewById(R.id.text_email);
                tvBirthday = (TextView)findViewById(R.id.text_birthday);
                tvPhone = (TextView)findViewById(R.id.text_phone);
                tvAddress = (TextView)findViewById(R.id.text_address);
                ivAvatar = (ImageView)findViewById(R.id.image_owner_avatar);

                // load owner information
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

                        byte[] imageArray = Base64.decode(snapshot
                                        .child("avatar").getValue().toString(),
                                Base64.DEFAULT);

                        Bitmap bmp = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);
                        ivAvatar.setImageBitmap(bmp);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

                // call
                btnCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myFirebaseRef.addValueEventListener(new ValueEventListener() {
                            public String phoneNumber;
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                phoneNumber = dataSnapshot.child(ownerUsername)
                                        .child("information")
                                        .child("phone").getValue().toString();
                                Uri number = Uri.parse("tel:"+ phoneNumber);
                                Intent intent = new Intent(Intent.ACTION_DIAL, number);
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    }
                });

                // send email message
                btnSendMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myFirebaseRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                Intent intent = new Intent(OwnerProfileActivity.this, MessageActivity.class);
                                intent.putExtra(EXTRA_CURRENT_USER, ConnectingServerData.username);
                                intent.putExtra(EXTRA_OWNER_USER, ownerUsername);

                                startActivity(intent);

                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    }
                });

                // view direction
                btnDirection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myFirebaseRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String yourLocation;
                                String ownerLocation;

                                yourLocation = dataSnapshot.child(yourUsername)
                                        .child("information")
                                        .child("address")
                                        .getValue().toString();

                                ownerLocation = dataSnapshot.child(ownerUsername)
                                        .child("information")
                                        .child("address")
                                        .getValue().toString();

                                Intent intent = new Intent(OwnerProfileActivity.this,
                                        DirectionMapsActivity.class);

                                intent.putExtra(EXTRA_YOUR_LOCATION, yourLocation);
                                intent.putExtra(EXTRA_OWNER_LOCATION, ownerLocation);

                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    }
                });

                // notify to the owner user
                btnNotify = (Button)findViewById(R.id.button_notify);
                btnNotify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder mydialog = new AlertDialog.Builder(OwnerProfileActivity.this);
                        mydialog.setTitle("Logout");
                        mydialog.setMessage("Are you sure to notify?");

                        mydialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                myFirebaseRef
                                        .child(ownerUsername)
                                        .child("notification")
                                        .push()
                                        .child("message")
                                        .setValue(ConnectingServerData.username +
                                                " wants to share books with you!");

                                Toast.makeText(OwnerProfileActivity.this, "notify sucessfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });

                        mydialog.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        });

                        mydialog.show();
                    }
                });

                break;

            ////////////////////////////////////////////////////////////////////////////////////////

            // having books
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
                                        Intent intent = new Intent(OwnerProfileActivity.this,
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

            // needed books
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
                                        Intent intent = new Intent(OwnerProfileActivity.this,
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
        getMenuInflater().inflate(R.menu.menu_message, menu);
        return true;
    }
}
