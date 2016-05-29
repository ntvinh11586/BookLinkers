package com.example.vinh.booklinkers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vinh.datatesting.ProcessingTest;

public class CreateAccountActivity extends AppCompatActivity {

    EditText etUsername, etPassword, etPasswordAgain;
    String username, password, passwordAgain;
    Button btnCreateNewAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        etUsername = (EditText)findViewById(R.id.edit_username);
        etPassword = (EditText)findViewById(R.id.edit_password);
        etPasswordAgain = (EditText)findViewById(R.id.edit_password_again);
        btnCreateNewAccount = (Button)findViewById(R.id.button_create_account);

        btnCreateNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();
                passwordAgain = etPasswordAgain.getText().toString();

                if (canCreateAccount(username, password, passwordAgain)) {
                    Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean canCreateAccount(String username, String password, String passwordAgain) {
        if (isAvailableAccount(username) && passwordMatch(password, passwordAgain))
            return true;

        return false;
    }

    private boolean passwordMatch(String password, String passwordAgain) {
        if (password.equals(passwordAgain))
            return true;

        return false;
    }

    private boolean isAvailableAccount(String username) {
        if (!username.equals(ProcessingTest.username))
            return true;

        return false;
    }
}
