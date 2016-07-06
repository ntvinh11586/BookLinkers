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
import com.firebase.client.AuthData;
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

    final String EXTRA_LOGIN_PREFERENCES = "EXTRA_LOGIN_PREFERENCES";
    final String EXTRA_SAVE_LOGIN = "EXTRA_SAVE_LOGIN";
    final String EXTRA_USER_NAME = "EXTRA_USER_NAME";
    final String EXTRA_PASSWORD = "EXTRA_PASSWORD";
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

        loginPreferences = getSharedPreferences(EXTRA_LOGIN_PREFERENCES, MODE_PRIVATE);

        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean(EXTRA_SAVE_LOGIN, false);

        if (saveLogin == true) {
            etUsername.setText(loginPreferences.getString(EXTRA_USER_NAME, ""));
            etPassword.setText(loginPreferences.getString(EXTRA_PASSWORD, ""));
            cbRememberPassword.setChecked(true);
        }

        Firebase.setAndroidContext(LoginActivity.this);

        myFirebaseRef = new Firebase("https://booklinkers-database.firebaseio.com/");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();

                if (cbRememberPassword.isChecked()) {
                    loginPrefsEditor.putBoolean(EXTRA_SAVE_LOGIN, true);
                    loginPrefsEditor.putString(EXTRA_USER_NAME, username);
                    loginPrefsEditor.putString(EXTRA_PASSWORD, password);
                    loginPrefsEditor.commit();
                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }

                myFirebaseRef.authWithPassword(username, password,
                        new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        ConnectingServerData.username = "vinh123";

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        finish();
                        startActivity(intent);
                    }

                            @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                                Toast.makeText(LoginActivity.this, "Error Login", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnCreateNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,
                        CreateNewAccountActivity.class);
                startActivity(intent);
            }
        });
    }
}
