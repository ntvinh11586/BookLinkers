package com.example.vinh.booklinkers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.vinh.testers.LocalTesters;

public class BooksExchangeDetailActivity extends AppCompatActivity {

    private Button btnMapDirection;
    private Button btnViewInformation;
    private ListView lvHavingBooks;
    private ListView lvNeedingBooks;

    static final String EXTRA_YOUR_LOCATION = "com.example.vinh.Booklinkers.EXTRA_YOUR_LOCATION";
    static final String EXTRA_OWNER_LOCATION = "com.example.vinh.Booklinkers.EXTRA_OWNER_LOCATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_exchange_detail);

        btnMapDirection = (Button)findViewById(R.id.button_map_direction);
        btnViewInformation = (Button)findViewById(R.id.button_view_information);
        lvHavingBooks = (ListView)findViewById(R.id.listview_having_books);
        lvNeedingBooks = (ListView)findViewById(R.id.listview_needing_books);

        btnMapDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        BooksExchangeDetailActivity.this,
                        OwnerDirectionMapsActivity.class);

                intent.putExtra(EXTRA_YOUR_LOCATION, "dai hoc khoa hoc tu nhien");
                intent.putExtra(EXTRA_OWNER_LOCATION, "dai hoc bach khoa");

                startActivity(intent);
            }
        });

        btnViewInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        BooksExchangeDetailActivity.this,
                        OwnerInformationActivity.class);
                startActivity(intent);
            }
        });

        ArrayAdapter<String> havingBooksAdapter =
                new ArrayAdapter<String>(this, R.layout.list_book_item, LocalTesters.havingBooksRecently);
        ArrayAdapter<String> needingBooksAdapter =
                new ArrayAdapter<String>(this, R.layout.list_book_item, LocalTesters.needingBooksRecently);

        lvHavingBooks.setAdapter(havingBooksAdapter);
        lvNeedingBooks.setAdapter(needingBooksAdapter);
    }
}
