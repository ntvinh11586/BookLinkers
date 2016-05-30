package com.example.vinh.booklinkers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vinh.testers.LocalTesters;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin, btnCreateNewAccount;
    String username, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LocalTesters.callProcessingTest();

        etUsername = (EditText)findViewById(R.id.edit_username);
        etPassword = (EditText)findViewById(R.id.edit_password);
        btnLogin = (Button)findViewById(R.id.button_login);
        btnCreateNewAccount = (Button)findViewById(R.id.button_create_account);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();

                if (isLoginCorrect(username, password)) {
                    //Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    finish();
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
                }
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

        Button button2 = (Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, BookInformation.class);
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
