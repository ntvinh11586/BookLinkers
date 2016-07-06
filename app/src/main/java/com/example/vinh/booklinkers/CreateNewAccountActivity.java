package com.example.vinh.booklinkers;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.vinh.DataObjects.User;
import com.example.vinh.GlobalObject.ConnectingServerData;
import com.firebase.client.Firebase;

import java.io.ByteArrayOutputStream;

public class CreateNewAccountActivity extends AppCompatActivity {

    EditText etUsername, etPassword, etPasswordAgain, etEmail;
    String username, password, passwordAgain, email;
    Button btnCreateNewAccount;
    private ImageView ivAvatar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_account);

        etUsername = (EditText)findViewById(R.id.edit_username);
        etPassword = (EditText)findViewById(R.id.edit_password);
        etEmail = (EditText) findViewById(R.id.edit_email);
        etPasswordAgain = (EditText)findViewById(R.id.edit_password_again);
        btnCreateNewAccount = (Button)findViewById(R.id.button_create_account);
        ivAvatar = (ImageView)findViewById(R.id.image_avatar);

        ivAvatar.setImageResource(R.drawable.img_your_avatar);

        btnCreateNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = etUsername.getText().toString();
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                passwordAgain = etPasswordAgain.getText().toString();

                // always create account successful [test]
                Firebase.setAndroidContext(CreateNewAccountActivity.this);
                Firebase myFirebaseRef = new Firebase("https://booklinkers-db.firebaseio.com/");

                User user = new User(username, password, email, "none", "none");
                myFirebaseRef.child(username).child("information").setValue(user);

                byte[] imageArray = setImageViewtoByteArray(ivAvatar);
                String imageString = Base64.encodeToString(imageArray, Base64.DEFAULT);
                myFirebaseRef.child(username)
                        .child("information")
                        .child("avatar")
                        .setValue(imageString);

                Toast.makeText(CreateNewAccountActivity.this, "Account creating successfully",
                        Toast.LENGTH_SHORT).show();

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
}
