package com.example.vinh.booklinkers;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vinh.GlobalObject.ConnectingServerData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.w3c.dom.Text;

public class BookInformationActivity extends AppCompatActivity {

    private final String EXTRA_USERNAME = "EXTRA_USERNAME";
    private final String EXTRA_BOOK_NAME = "EXTRA_BOOK_NAME";
    Button btnEditBook;
    private Firebase myFirebaseRef;
    private TextView tvBookTitle;
    private TextView tvBookAuthor;
    private TextView tvBookDescription;
    private String gt1;
    private String gt2;
    private TextView tvBookName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_information);

        Firebase.setAndroidContext(BookInformationActivity.this);
        myFirebaseRef = new Firebase("https://booklinkers-db.firebaseio.com/");

        // I don't know why in this case, I can't set the support actionbar
        //Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(myToolbar);

//        tvBookTitle = (TextView)findViewById(R.id.text_book_name);
        tvBookName = (TextView)findViewById(R.id.text_book_name);
        tvBookTitle = (TextView)findViewById(R.id.text_book_title);
        tvBookAuthor = (TextView)findViewById(R.id.text_book_author);
        tvBookDescription = (TextView)findViewById(R.id.text_book_description);

        gt1 = getIntent().getExtras().getString(EXTRA_USERNAME);
        gt2 = getIntent().getExtras().getString(EXTRA_BOOK_NAME);

        Toast.makeText(getApplicationContext(), gt1 + " " + gt2, Toast.LENGTH_SHORT).show();

        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot snapshot = dataSnapshot
                        .child(ConnectingServerData.username)
                        .child("books")
                        .child(gt2);

                tvBookName.setText(snapshot.child("name").getValue().toString());
                tvBookTitle.setText(snapshot.child("name").getValue().toString());
                tvBookAuthor.setText(snapshot.child("author").getValue().toString());
                tvBookDescription.setText(snapshot.child("description").getValue().toString());

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        // set the Up Button
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        btnEditBook = (Button)findViewById(R.id.button_edit_book);
        btnEditBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookInformationActivity.this, BookEditingActivity.class);

                intent.putExtra(EXTRA_BOOK_NAME, gt2);

                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mif = getMenuInflater();
        mif.inflate(R.menu.main_book_information, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            // handle the up button on Action Bar
            case R.id.action_home:
                return true;

            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_favorite:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
