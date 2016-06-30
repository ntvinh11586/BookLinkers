package com.example.vinh.booklinkers;

import android.content.Intent;
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
import android.widget.Toast;

import com.example.vinh.GlobalObject.ConnectingServerData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class SearchOwnerActivity extends AppCompatActivity {

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
        setContentView(R.layout.content_search_owner);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        setTitle("Search Owner");

        books = new ArrayList<>();

        Firebase.setAndroidContext(SearchOwnerActivity.this);
        myFirebaseRef = new Firebase("https://booklinkers-db.firebaseio.com/");

        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot
                        .child(ConnectingServerData.username)
                        .child("books")
                        .getChildren()) {
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
                                SearchOwnerActivity.this,
                                R.layout.list_search_item,
                                searchResult);

                lvResult.setAdapter(resultAdapter);
            }
        });

        lvResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(
                        SearchOwnerActivity.this,
                        OwnerProfileActivity.class);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message, menu);
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
        } else if (id == R.id.action_log_out) {
            Intent intent = new Intent(SearchOwnerActivity.this, LoginActivity.class);
            finish();
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
