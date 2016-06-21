package com.example.vinh.booklinkers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vinh.GlobalObject.ConnectingServerData;
import com.example.vinh.Testers.LocalTesters;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String EXTRA_OWNER_USERNAME = "EXTRA_OWNER_USERNAME";
    private Firebase myFirebaseRef;
    private ListView lvHavingBooks;
    private ArrayList<String> books;
    private ArrayAdapter<String> havingBooksAdapter;
    private String strLvItem;
    private String ownerSelection;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // do something on back.
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);


        Firebase.setAndroidContext(MainActivity.this);
        myFirebaseRef = new Firebase("https://booklinkers-db.firebaseio.com/");

        // ListView
        lvHavingBooks = (ListView)findViewById(R.id.listview_notification);

        books = new ArrayList<>();
        havingBooksAdapter = new ArrayAdapter<String>(this, R.layout.list_book_item, books);
        lvHavingBooks.setAdapter(havingBooksAdapter);

        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot
                        .child(ConnectingServerData.username)
                        .child("notification")
                        .getChildren()) {
                    if (postSnapshot.child("message").getValue() != null)
                        books.add(postSnapshot.child("message").getValue().toString());
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
                Intent intent = new Intent(
                        MainActivity.this,
                        OwnerInformationActivity.class);
                
                ownerSelection = lvHavingBooks.getItemAtPosition(position).toString();

                String[] tmp = ownerSelection.split(" ");

                intent.putExtra(EXTRA_OWNER_USERNAME, tmp[0]);

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
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_notifcation) {

        } else if (id == R.id.nav_have_book) {
            Intent intent = new Intent(MainActivity.this, BooksHavingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_need_book) {
            Intent intent = new Intent(MainActivity.this, BooksNeedingActivity.class);
            //finish();
            startActivity(intent);
        } else if (id == R.id.nav_information) {
            Intent intent = new Intent(MainActivity.this, YourInformationActivity.class);
            //finish();
            startActivity(intent);
        } else if (id == R.id.nav_search_book) {
            Intent intent = new Intent(MainActivity.this, BooksSearchingActivity.class);
            //finish();
            startActivity(intent);
        } else if (id == R.id.nav_log_out) {
            AlertDialog.Builder mydialog = new AlertDialog.Builder(this);
            mydialog.setTitle("Logout");
            mydialog.setMessage("Are you sure to logout?");

            mydialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);

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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
