package com.example.vinh.booklinkers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vinh.GlobalObject.ConnectingServerData;
import com.example.vinh.Testers.LocalTesters;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin, btnCreateNewAccount;
    String username, password;

    String tmpUsername;
    String tmpPassword;
    boolean bUsername = false;
    boolean bPassword = false;
    Firebase myFirebaseRef;
    private CheckBox cbRememberPassword;

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LocalTesters.callProcessingTest();

        etUsername = (EditText)findViewById(R.id.edit_username);
        etPassword = (EditText)findViewById(R.id.edit_password);
        btnLogin = (Button)findViewById(R.id.button_login);
        btnCreateNewAccount = (Button)findViewById(R.id.button_create_account);
        cbRememberPassword = (CheckBox)findViewById(R.id.check_save_login);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);

        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);

        if (saveLogin == true) {
            etUsername.setText(loginPreferences.getString("username", ""));
            etPassword.setText(loginPreferences.getString("password", ""));
            cbRememberPassword.setChecked(true);
        }

        Firebase.setAndroidContext(LoginActivity.this);
        myFirebaseRef = new Firebase("https://booklinkers-db.firebaseio.com/");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();

                if (cbRememberPassword.isChecked()) {
                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.putString("username", username);
                    loginPrefsEditor.putString("password", password);
                    loginPrefsEditor.commit();
                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }


                myFirebaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            if (username.equals(postSnapshot.getKey().toString()))
                                bUsername = true;
                        }

                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            tmpPassword = postSnapshot
                                    .child("information")
                                    .child("password")
                                    .getValue()
                                    .toString();
                            if (tmpPassword.equals(password))
                                bPassword = true;
                        }

                        if (bUsername && bPassword) {
                            ConnectingServerData.username = username;
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            finish();
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

//                if (isLoginCorrect(username, password)) {
//                    //Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    finish();
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
//                }
            }
        });

        btnCreateNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,
                        AccountCreatingActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean isLoginCorrect(String username, String password) {
        if (username.equals(LocalTesters.username)
                && password.equals(LocalTesters.password))
            return true;

        return false;
    }
}
