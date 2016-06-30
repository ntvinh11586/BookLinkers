package com.example.vinh.booklinkers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vinh.GlobalObject.ConnectingServerData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class YourProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Button btnChangeInformation;

    private TextView tvName;
    private TextView tvUsername;
    private TextView tvEmail;
    private TextView tvBirthday;
    private TextView tvPhone;
    private TextView tvAddress;

    private Firebase myFirebaseRef;
    private ImageView ivAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_personal_profile);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        tvName = (TextView)findViewById(R.id.text_name);
        tvUsername = (TextView)findViewById(R.id.text_username);
        tvEmail = (TextView) findViewById(R.id.text_email);
        tvBirthday = (TextView)findViewById(R.id.text_birthday);
        tvPhone = (TextView)findViewById(R.id.text_phone);
        tvAddress = (TextView)findViewById(R.id.text_address);
        ivAvatar = (ImageView)findViewById(R.id.image_avatar);


        Firebase.setAndroidContext(YourProfileActivity.this);
        myFirebaseRef = new Firebase("https://booklinkers-db.firebaseio.com/");

        // set information profile
        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot snapshot = dataSnapshot
                        .child(ConnectingServerData.username)
                        .child("information");

                tvName.setText(snapshot.child("name").getValue().toString());
                tvUsername.setText(snapshot.child("username").getValue().toString());
                tvEmail.setText(snapshot.child("email").getValue().toString());

                tvBirthday.setText(snapshot.child("birthday").getValue().toString());
                tvPhone.setText(snapshot.child("phone").getValue().toString());
                tvAddress.setText(snapshot.child("address").getValue().toString());

                byte[] imageArray = Base64.decode(dataSnapshot
                                .child(ConnectingServerData.username)
                                .child("information")
                                .child("avatar").getValue().toString(),
                        Base64.DEFAULT);

                Bitmap bmp = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);
                ivAvatar.setImageBitmap(bmp);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        btnChangeInformation = (Button)findViewById(R.id.button_change_information);
        btnChangeInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(YourProfileActivity.this, YourProfileEditingActivity.class);
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



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_notifcation) {
            Intent intent = new Intent(YourProfileActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_book) {
            Intent intent = new Intent(YourProfileActivity.this, BooksActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_information) {

        } else if (id == R.id.nav_search_book) {
            Intent intent = new Intent(YourProfileActivity.this, BooksSearchingActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
