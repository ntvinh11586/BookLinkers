package com.example.vinh.booklinkers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class YourInformationActivity extends AppCompatActivity {

    private Button btnChangeInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_personal_information);

        btnChangeInformation = (Button)findViewById(R.id.button_change_information);
        btnChangeInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(YourInformationActivity.this, YourInformationEditingActivity.class);
                startActivity(intent);
            }
        });
    }
}
