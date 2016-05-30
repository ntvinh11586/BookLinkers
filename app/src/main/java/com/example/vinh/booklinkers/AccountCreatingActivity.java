package com.example.vinh.booklinkers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vinh.testers.LocalTesters;

public class AccountCreatingActivity extends AppCompatActivity {

    EditText etUsername, etPassword, etPasswordAgain, etEmail;
    String username, password, passwordAgain, email;
    Button btnCreateNewAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_creating);

        etUsername = (EditText)findViewById(R.id.edit_username);
        etPassword = (EditText)findViewById(R.id.edit_password);
        etEmail = (EditText) findViewById(R.id.edit_email);
        etPasswordAgain = (EditText)findViewById(R.id.edit_password_again);
        btnCreateNewAccount = (Button)findViewById(R.id.button_create_account);

        btnCreateNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = etUsername.getText().toString();
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                passwordAgain = etPasswordAgain.getText().toString();

                if (canCreateAccount(username, email, password, passwordAgain)) {

                    // update db

                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private boolean canCreateAccount(String username, String password, String email, String passwordAgain) {
        if (isAvailableAccount(username)
                && isAvailableEmail(email)
                && passwordMatch(password, passwordAgain))
            return true;

        return false;
    }

    private boolean isAvailableEmail(String email) {
        if (!email.equals(LocalTesters.email))
            return true;
        return false;
    }

    private boolean passwordMatch(String password, String passwordAgain) {
        if (password.equals(passwordAgain))
            return true;
        return false;
    }

    private boolean isAvailableAccount(String username) {
        if (!username.equals(LocalTesters.username))
            return true;
        return false;
    }
}
