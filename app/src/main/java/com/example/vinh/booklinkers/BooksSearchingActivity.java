package com.example.vinh.booklinkers;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class BooksSearchingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String EXTRA_OWNER_USERNAME = "EXTRA_OWNER_USERNAME";
    private Button btnSearch;
    private ListView lvResult;
    private String ownerSelection;
    private ArrayList<String> books;
    private Firebase myFirebaseRef;
    private ArrayList<String> searchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_searching);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        books = new ArrayList<>();

        Firebase.setAndroidContext(BooksSearchingActivity.this);
        myFirebaseRef = new Firebase("https://booklinkers-db.firebaseio.com/");

        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot
                        .child(ConnectingServerData.username)
                        .child("books")
                        .getChildren()) {
//                    if ((boolean)postSnapshot.child("own").getValue())
//                        books.add(postSnapshot.child("name").getValue().toString());
//                    havingBooksAdapter.notifyDataSetChanged();
                    books.add(postSnapshot.child("name").getValue().toString());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });




        lvResult = (ListView) findViewById(R.id.listview_result);
        btnSearch = (Button)findViewById(R.id.button_search);



        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchResult = getSearchResult();

                if (searchResult == null)
                    Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_SHORT).show();

                ArrayAdapter<String> resultAdapter =
                        new ArrayAdapter<String>(
                                BooksSearchingActivity.this,
                                R.layout.list_search_item,
                                searchResult);

                lvResult.setAdapter(resultAdapter);
            }
        });

        lvResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(
                        BooksSearchingActivity.this,
                        OwnerInformationActivity.class);

                ownerSelection = lvResult.getItemAtPosition(position).toString();

                intent.putExtra(EXTRA_OWNER_USERNAME, ownerSelection);

                startActivity(intent);
            }
        });









    }

    ArrayList<String> result;

    private ArrayList<String> getSearchResult() {
//        String[] res = {"vinh456"};

        result = new ArrayList<>();

        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot
                        .getChildren()) {

                    String aaa = postSnapshot.toString();

                    boolean findData = false;

                    if (!postSnapshot.getKey().toString().equals(ConnectingServerData.username))
                        for (DataSnapshot postNameSnapshot: postSnapshot
                                .child("books")
                                .getChildren()) {


                            String tmp = postNameSnapshot.child("name").getValue().toString();

                            if (!findData) {
                                for (int i = 0; i < books.size(); i++)
                                    if (tmp.equals(books.get(i))) {
                                        result.add(postSnapshot.getKey().toString());
                                        findData = true;
                                        break;
                                    }
                            }

//                            if (findData) break;

                            }

                }
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return result;
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
            Intent intent = new Intent(BooksSearchingActivity.this, MainActivity.class);
            finish();
            startActivity(intent);
            return true;
        } else if (id == R.id.action_log_out) {
            Intent intent = new Intent(BooksSearchingActivity.this, LoginActivity.class);
            finish();
            startActivity(intent);
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
