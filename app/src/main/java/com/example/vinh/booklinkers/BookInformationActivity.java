package com.example.vinh.booklinkers;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vinh.GlobalObject.ConnectingServerData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.w3c.dom.Text;

public class BookInformationActivity extends AppCompatActivity {

    private final String EXTRA_REMOVE_BOOK = "EXTRA_REMOVE_BOOK";
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
    private Button btnRemoveBooks;
    private ImageView ivBookAvatar;
    private TextView tvBookStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_information);

        Firebase.setAndroidContext(BookInformationActivity.this);
        myFirebaseRef = new Firebase("https://booklinkers-db.firebaseio.com/");

        tvBookName = (TextView)findViewById(R.id.text_book_name);
        tvBookTitle = (TextView)findViewById(R.id.text_book_title);
        tvBookAuthor = (TextView)findViewById(R.id.text_book_author);
        tvBookDescription = (TextView)findViewById(R.id.text_book_description);
        ivBookAvatar = (ImageView)findViewById(R.id.image_book_avatar);
        tvBookStatus = (TextView)findViewById(R.id.text_book_status);

        gt1 = getIntent().getExtras().getString(EXTRA_USERNAME);
        gt2 = getIntent().getExtras().getString(EXTRA_BOOK_NAME);

        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot snapshot = dataSnapshot
                        .child(gt1)
                        .child("books")
                        .child(gt2);

                if (snapshot.getValue() != null) {
                    tvBookName.setText(snapshot.child("name").getValue().toString());
                    tvBookTitle.setText(snapshot.child("name").getValue().toString());
                    tvBookAuthor.setText(snapshot.child("author").getValue().toString());
                    tvBookDescription.setText(snapshot.child("description").getValue().toString());

                    boolean isHaving = (boolean)snapshot.child("own").getValue();
                    if (isHaving) {
                        tvBookStatus.setText("Having");
                    } else {
                        tvBookStatus.setText("Needing");
                    }

                    byte[] imageArray = Base64.decode(snapshot
                                    .child("avatar").getValue().toString(),
                            Base64.DEFAULT);

                    Bitmap bmp = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);
                    ivBookAvatar.setImageBitmap(bmp);

                }
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

        btnRemoveBooks = (Button)findViewById(R.id.button_remove_book);
        btnRemoveBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mydialog = new AlertDialog.Builder(BookInformationActivity.this);
                mydialog.setTitle("Remove");
                mydialog.setMessage("Are you sure to remove this books? This process cannot be undo");

                mydialog.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });

                mydialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra(EXTRA_REMOVE_BOOK, gt2);

                        setResult(RESULT_OK, intent);

                        finish();
                    }
                });



                mydialog.show();

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


            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}
