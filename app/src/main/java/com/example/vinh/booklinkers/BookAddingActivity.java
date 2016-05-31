package com.example.vinh.booklinkers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.vinh.DataObjects.Book;
import com.example.vinh.GlobalObject.ConnectingServerData;
import com.firebase.client.Firebase;

public class BookAddingActivity extends AppCompatActivity {

    private Button btnApplyBooks;
    private Firebase myFirebaseRef;
    private EditText etName;
    private EditText etAuthor;
    private EditText etDescription;
    String name;
    String author;
    String description;
    boolean own;
    Book book;
    private CheckBox cbOwningBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_adding);

        Firebase.setAndroidContext(BookAddingActivity.this);
        myFirebaseRef = new Firebase("https://booklinkers-db.firebaseio.com/");

        etName = (EditText)findViewById(R.id.edit_name);
        etAuthor = (EditText)findViewById(R.id.edit_author);
        etDescription = (EditText)findViewById(R.id.edit_description);
        cbOwningBook = (CheckBox)findViewById(R.id.check_owner_book);

        btnApplyBooks = (Button)findViewById(R.id.button_apply_change);
        btnApplyBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = etName.getText().toString();
                author = etAuthor.getText().toString();
                description = etDescription.getText().toString();
                own = cbOwningBook.isChecked();

                book = new Book(name, author, description, own);

                myFirebaseRef
                        .child(ConnectingServerData.username)
                        .child("books")
                        .push()
                        .setValue(book);

                finish();
            }
        });
    }
}
