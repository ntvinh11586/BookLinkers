package com.example.vinh.booklinkers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.vinh.GlobalObject.ConnectingServerData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;

public class BookEditingActivity extends AppCompatActivity {

    private static final java.lang.String EXTRA_BOOK_NAME = "EXTRA_BOOK_NAME";
    private Button btnApplyChange;
    private EditText etBookName;
    private EditText etBookAuthor;
    private EditText etBookDescription;
    private Firebase myFirebaseRef;
    private String bookName;
    private Button btnChangeBookAvatar;
    private ImageView ivBookAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_editing);

        Firebase.setAndroidContext(BookEditingActivity.this);
        myFirebaseRef = new Firebase("https://booklinkers-db.firebaseio.com/");

        etBookName = (EditText)findViewById(R.id.edit_book_name);
        etBookAuthor = (EditText)findViewById(R.id.edit_book_author);
        etBookDescription = (EditText)findViewById(R.id.edit_book_description);
        ivBookAvatar = (ImageView)findViewById(R.id.image_book_avatar);

        bookName = getIntent().getExtras().getString(EXTRA_BOOK_NAME);

        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                etBookName.setText(dataSnapshot
                        .child(ConnectingServerData.username)
                        .child("books")
                        .child(bookName)
                        .child("name")
                        .getValue().toString());

                etBookAuthor.setText(dataSnapshot
                        .child(ConnectingServerData.username)
                        .child("books")
                        .child(bookName)
                        .child("author")
                        .getValue().toString());

                etBookDescription.setText(dataSnapshot
                        .child(ConnectingServerData.username)
                        .child("books")
                        .child(bookName)
                        .child("description")
                        .getValue().toString());

                byte[] imageArray = Base64.decode(dataSnapshot
                                .child(ConnectingServerData.username)
                                .child("books")
                                .child(bookName)
                                .child("avatar").getValue().toString(),
                        Base64.DEFAULT);

                Bitmap bmp = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);
                ivBookAvatar.setImageBitmap(bmp);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        btnApplyChange = (Button)findViewById(R.id.button_apply_change);
        btnApplyChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFirebaseRef.child(ConnectingServerData.username)
                        .child("books")
                        .child(bookName)
                        .child("name")
                        .setValue(etBookName.getText().toString());
                myFirebaseRef.child(ConnectingServerData.username)
                        .child("books")
                        .child(bookName)
                        .child("author")
                        .setValue(etBookAuthor.getText().toString());
                myFirebaseRef.child(ConnectingServerData.username)
                        .child("books")
                        .child(bookName)
                        .child("description")
                        .setValue(etBookDescription.getText().toString());

                byte[] imageArray = setImageViewtoByteArray(ivBookAvatar);
                String imageString = Base64.encodeToString(imageArray, Base64.DEFAULT);
                myFirebaseRef.child(ConnectingServerData.username)
                        .child("books")
                        .child(bookName)
                        .child("avatar")
                        .setValue(imageString);

                finish();
            }
        });


        btnChangeBookAvatar = (Button)findViewById(R.id.button_change_book_avatar);
        btnChangeBookAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent()
                        .setType("image/*")
                        .setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);
            }
        });
    }

    public byte[] setImageViewtoByteArray(ImageView imgv){

        BitmapDrawable drawable = (BitmapDrawable) imgv.getDrawable();
        Bitmap bmp = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==123 && resultCode==RESULT_OK) {
            Uri selectedfile = data.getData(); //The uri with the location of the file

            try {
                Bitmap img = getBitmapFromUri(selectedfile);

                ivBookAvatar.setImageBitmap(img);

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
}
