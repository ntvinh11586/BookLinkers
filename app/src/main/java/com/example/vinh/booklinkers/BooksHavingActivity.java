package com.example.vinh.booklinkers;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.vinh.GlobalObject.ConnectingServerData;
import com.example.vinh.Testers.LocalTesters;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class BooksHavingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String EXTRA_USERNAME = "EXTRA_USERNAME";
    private static final String EXTRA_BOOK_NAME = "EXTRA_BOOK_NAME";
    ListView lvHavingBooks;
    private Button btnAddBook;
    private Firebase myFirebaseRef;
    ArrayList<String> books;
    ArrayAdapter<String> havingBooksAdapter;

    String a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_having);

        Firebase.setAndroidContext(BooksHavingActivity.this);
        myFirebaseRef = new Firebase("https://booklinkers-db.firebaseio.com/");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // ListView
        lvHavingBooks = (ListView)findViewById(R.id.listview_having_books);

        books = new ArrayList<>();
        havingBooksAdapter = new ArrayAdapter<String>(this, R.layout.list_book_item, books);
        lvHavingBooks.setAdapter(havingBooksAdapter);

        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot
                        .child(ConnectingServerData.username)
                        .child("books")
                        .getChildren()) {
                    if ((boolean)postSnapshot.child("own").getValue())
                        books.add(postSnapshot.child("name").getValue().toString());
                    havingBooksAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        lvHavingBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                a = lvHavingBooks.getItemAtPosition(position).toString();
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
                                    .equals(a)) {
                                Intent intent = new Intent(BooksHavingActivity.this,
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

        // Control
        btnAddBook = (Button)findViewById(R.id.button_add_book);
        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BooksHavingActivity.this, BookAddingActivity.class);

                //finish();
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        } else if (id == R.id.action_home) {
            Intent intent = new Intent(BooksHavingActivity.this, MainActivity.class);
            finish();
            startActivity(intent);
            return true;
        } else if (id == R.id.action_log_out) {
            AlertDialog.Builder mydialog = new AlertDialog.Builder(this);
            mydialog.setTitle("Logout");
            mydialog.setMessage("Are you sure to logout?");

            mydialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(BooksHavingActivity.this, LoginActivity.class);

                    finish();
                    startActivity(intent);
                }
            });

            mydialog.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // do nothing
                }
            });

            mydialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_have_book) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
