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
import android.widget.Toast;

import com.example.vinh.GlobalObject.ConnectingServerData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;

public class YourProfileEditingActivity extends AppCompatActivity {

    private Button btnApply;
    private EditText etName;
    private EditText etPassword;
    private EditText etEmail;
    private EditText etBirthday;
    private EditText etAddress;
    private EditText etPhone;
    private Firebase myFirebaseRef;
    private Button btnChangeAvatar;
    private ImageView ivCurrentAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_profile_editing);

        Firebase.setAndroidContext(YourProfileEditingActivity.this);
        myFirebaseRef = new Firebase("https://booklinkers-db.firebaseio.com/");

        etName = (EditText)findViewById(R.id.edit_name);
        etPassword = (EditText)findViewById(R.id.edit_password);
        etEmail = (EditText)findViewById(R.id.edit_email);
        etBirthday = (EditText)findViewById(R.id.edit_birthday);
        etAddress = (EditText)findViewById(R.id.edit_address);
        etPhone = (EditText)findViewById(R.id.edit_phone);
        ivCurrentAvatar = (ImageView)findViewById(R.id.image_current_avatar);

        // show information profile
        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                etName.setText(dataSnapshot
                        .child(ConnectingServerData.username)
                        .child("information")
                        .child("name")
                        .getValue().toString());
                etPassword.setText(dataSnapshot
                        .child(ConnectingServerData.username)
                        .child("information")
                        .child("password")
                        .getValue().toString());
                etEmail.setText(dataSnapshot
                        .child(ConnectingServerData.username)
                        .child("information")
                        .child("email")
                        .getValue().toString());
                etBirthday.setText(dataSnapshot
                        .child(ConnectingServerData.username)
                        .child("information")
                        .child("birthday")
                        .getValue().toString());
                etAddress.setText(dataSnapshot
                        .child(ConnectingServerData.username)
                        .child("information")
                        .child("address")
                        .getValue().toString());
                etPhone.setText(dataSnapshot
                        .child(ConnectingServerData.username)
                        .child("information")
                        .child("phone")
                        .getValue().toString());

                byte[] imageArray = Base64.decode(dataSnapshot
                        .child(ConnectingServerData.username)
                        .child("information")
                        .child("avatar").getValue().toString(),
                        Base64.DEFAULT);

                Bitmap bmp = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);
                ivCurrentAvatar.setImageBitmap(bmp);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        // change avatar button
        btnChangeAvatar = (Button)findViewById(R.id.button_change_avatar);
        btnChangeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent()
                        .setType("image/*")
                        .setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);
            }
        });

        // apply button
        btnApply = (Button)findViewById(R.id.button_apply);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFirebaseRef.child(ConnectingServerData.username)
                        .child("information")
                        .child("name")
                        .setValue(etName.getText().toString());
                myFirebaseRef.child(ConnectingServerData.username)
                        .child("information")
                        .child("password")
                        .setValue(etPassword.getText().toString());
                myFirebaseRef.child(ConnectingServerData.username)
                        .child("information")
                        .child("email")
                        .setValue(etEmail.getText().toString());
                myFirebaseRef.child(ConnectingServerData.username)
                        .child("information")
                        .child("phone")
                        .setValue(etPhone.getText().toString());
                myFirebaseRef.child(ConnectingServerData.username)
                        .child("information")
                        .child("address")
                        .setValue(etAddress.getText().toString());
                myFirebaseRef.child(ConnectingServerData.username)
                        .child("information")
                        .child("birthday")
                        .setValue(etBirthday.getText().toString());

                byte[] imageArray = setImageViewtoByteArray(ivCurrentAvatar);
                String imageString = Base64.encodeToString(imageArray, Base64.DEFAULT);
                myFirebaseRef.child(ConnectingServerData.username)
                        .child("information")
                        .child("avatar")
                        .setValue(imageString);

                Toast.makeText(YourProfileEditingActivity.this, "Edit profile successfully", Toast.LENGTH_SHORT).show();

                finish();
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

                ivCurrentAvatar.setImageBitmap(img);

            } catch (IOException e) {
                e.printStackTrace();
            }
//            showImage(uri);
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
