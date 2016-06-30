package com.example.vinh.booklinkers;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.vinh.GlobalObject.ConnectingServerData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class BooksActivity extends AppCompatActivity
        implements ActionBar.TabListener {

    private static final String EXTRA_USERNAME = "EXTRA_USERNAME";
    private static final String EXTRA_BOOK_NAME = "EXTRA_BOOK_NAME";
    private static final int EXTRA_REQUEST_CODE = 999;
    private Firebase myFirebaseRef;
    private ListView lvHavingBooks;
    private ArrayList<String> books;
    private ArrayAdapter<String> havingBooksAdapter;
    private String strLvItem;
    private Button btnAddBook;
    private ListView lvNeedingBooks;
    private ArrayAdapter<String> needingBooksAdapter;
    private ArrayList<String> havingBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        ConnectingServerData.username = "vinh123";
        // set database
        Firebase.setAndroidContext(BooksActivity.this);
        myFirebaseRef = new Firebase("https://booklinkers-db.firebaseio.com/");

        // actionbar tag
        ActionBar ab = getSupportActionBar();
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Three tab to display in actionbar
        ab.addTab(ab.newTab().setText("Having Books").setTabListener(this));
        ab.addTab(ab.newTab().setText("Needed Books").setTabListener(this));

    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        int nTabSelected = tab.getPosition();
        switch (nTabSelected) {
            case 0:
                setContentView(R.layout.content_books_having);

                // ListView
                lvHavingBooks = (ListView)findViewById(R.id.listview_having_books);

                havingBooks = new ArrayList<>();
                havingBooksAdapter = new ArrayAdapter<String>(this, R.layout.list_having_book_item, havingBooks);
                lvHavingBooks.setAdapter(havingBooksAdapter);

                myFirebaseRef
                        .child(ConnectingServerData.username)
                        .child("books")
                        .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if ((boolean)dataSnapshot.child("own").getValue())
                            havingBooks.add(dataSnapshot.child("name").getValue().toString());
                        havingBooksAdapter.notifyDataSetChanged();
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

                lvHavingBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    private String havingBookItem;
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        havingBookItem = lvHavingBooks.getItemAtPosition(position).toString();

                        myFirebaseRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot: dataSnapshot
                                        .child(ConnectingServerData.username)
                                        .child("books")
                                        .getChildren()) {
                                    if (postSnapshot.child("name")
                                            .getValue()
                                            .toString()
                                            .equals(havingBookItem)) {
                                        Intent intent = new Intent(BooksActivity.this,
                                                BookInformationActivity.class);

                                        intent.putExtra(EXTRA_USERNAME, ConnectingServerData.username);

                                        intent.putExtra(EXTRA_BOOK_NAME,
                                                postSnapshot.getKey().toString());

                                        startActivityForResult(intent, EXTRA_REQUEST_CODE);
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
            ////////////////////////////////////////////////////////////////////////////////////////
            case 1:
                setContentView(R.layout.content_books_needing);

                lvNeedingBooks = (ListView)findViewById(R.id.listview_needing_books);

                books = new ArrayList<>();
                needingBooksAdapter = new ArrayAdapter<String>(this, R.layout.list_book_item, books);
                lvNeedingBooks.setAdapter(needingBooksAdapter);


                lvNeedingBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(BooksActivity.this, BookInformationActivity.class);
                        startActivity(intent);
                    }
                });


                myFirebaseRef
                        .child(ConnectingServerData.username)
                        .child("books")
                        .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (!(boolean)dataSnapshot.child("own").getValue())
                            books.add(dataSnapshot.child("name").getValue().toString());
                        needingBooksAdapter.notifyDataSetChanged();
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


                lvNeedingBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    private String needingBookItem;
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        needingBookItem = lvNeedingBooks.getItemAtPosition(position).toString();
                        myFirebaseRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot: dataSnapshot
                                        .child(ConnectingServerData.username)
                                        .child("books")
                                        .getChildren()) {
                                    if (postSnapshot.child("name")
                                            .getValue()
                                            .toString()
                                            .equals(needingBookItem)) {
                                        Intent intent = new Intent(BooksActivity.this,
                                                BookInformationActivity.class);

                                        intent.putExtra(EXTRA_USERNAME, ConnectingServerData.username);

                                        intent.putExtra(EXTRA_BOOK_NAME,
                                                postSnapshot
                                                        .getKey().toString());
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
        getMenuInflater().inflate(R.menu.menu_books, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            startActivity(new Intent(BooksActivity.this, BookAddingActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
