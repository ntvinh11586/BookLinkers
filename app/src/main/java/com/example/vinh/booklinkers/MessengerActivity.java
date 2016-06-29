package com.example.vinh.booklinkers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MessengerActivity extends AppCompatActivity {

    private ListView lvHavingBooks;
    private String followingUser;
    private String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        followingUser = "vinh456";
        currentUser = "vinh123";

        setTitle(followingUser + "'s message");

        lvHavingBooks = (ListView)findViewById(R.id.listview_message);

        ArrayList<String> items = new ArrayList<>();
        items.add("vinh: hello");
        items.add("v: hello c");
        items.add("vinh: km km km km km km km km");
        items.add("k: h h");
        items.add("k: kkkkkkkkkkkkkkkkkkkkkkkkkkk");
        items.add("vinh: hello");
        items.add("vinh: hello");
        items.add("vinh: hello");
        items.add("vinh: hello");
        items.add("vinh: hello");
        items.add("v: hello c");
        items.add("vinh: km km km km km km km km");
        items.add("k: h h");
        items.add("k: kkkkkkkkkkkkkkkkkkkkkkkkkkk");
        items.add("vinh: hello");
        items.add("vinh: hello");
        items.add("vinh: hello");
        items.add("vinh: hello");
        items.add("vinh: hello");
        items.add("v: hello c");
        items.add("vinh: km km km km km km km km");
        items.add("k: h h");
        items.add("k: kkkkkkkkkkkkkkkkkkkkkkkkkkk");
        items.add("vinh: hello");
        items.add("vinh: hello");
        items.add("vinh: hello");
        items.add("vinh: hello");

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, R.layout.list_message_item,items);

        lvHavingBooks.setAdapter(itemsAdapter);
    }
}
