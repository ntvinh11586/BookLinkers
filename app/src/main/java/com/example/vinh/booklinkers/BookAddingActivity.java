package com.example.vinh.booklinkers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.vinh.DataObjects.Book;
import com.example.vinh.GlobalObject.ConnectingServerData;
import com.firebase.client.Firebase;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;

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
    private ImageView ivImageBookAvatar;
    private Button btnBookAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_adding);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Firebase.setAndroidContext(BookAddingActivity.this);
        myFirebaseRef = new Firebase("https://booklinkers-db.firebaseio.com/");

        etName = (EditText)findViewById(R.id.edit_name);
        etAuthor = (EditText)findViewById(R.id.edit_author);
        etDescription = (EditText)findViewById(R.id.edit_description);
        cbOwningBook = (CheckBox)findViewById(R.id.check_owner_book);
        ivImageBookAvatar = (ImageView)findViewById(R.id.image_book_avatar);
        btnBookAvatar = (Button)findViewById(R.id.button_book_avatar);

        ivImageBookAvatar.setImageResource(R.drawable.img_book_avatar);

        btnApplyBooks = (Button)findViewById(R.id.button_apply_change);
        btnApplyBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = etName.getText().toString();
                author = etAuthor.getText().toString();
                description = etDescription.getText().toString();
                own = cbOwningBook.isChecked();

                byte[] imageArray = setImageViewtoByteArray(ivImageBookAvatar);
                String imageString = Base64.encodeToString(imageArray, Base64.DEFAULT);

                book = new Book(name, author, description, imageString, own);

                myFirebaseRef
                        .child(ConnectingServerData.username)
                        .child("books")
                        .push()
                        .setValue(book);

                Toast.makeText(BookAddingActivity.this, "Add one book successfully", Toast.LENGTH_SHORT).show();
                
                finish();
            }
        });

        btnBookAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent()
                        .setType("image/*")
                        .setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==123 && resultCode==RESULT_OK) {
            Uri selectedfile = data.getData(); //The uri with the location of the file

            try {
                Bitmap img = getBitmapFromUri(selectedfile);

                ivImageBookAvatar.setImageBitmap(img);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    public byte[] setImageViewtoByteArray(ImageView imgv){

        BitmapDrawable drawable = (BitmapDrawable) imgv.getDrawable();
        Bitmap bmp = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}
