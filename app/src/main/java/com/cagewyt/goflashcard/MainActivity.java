package com.cagewyt.goflashcard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        Date today = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
//        String day = sdf.format(today);
//        TextView bannerDay = findViewById(R.id.bannerDay);
//        bannerDay.setText(day);
//
//        SimpleDateFormat sdf2 = new SimpleDateFormat("MMM dd, yyy h:mm a");
//        String date = sdf2.format(today);
//        TextView bannerDate = findViewById(R.id.bannerDate);
//        bannerDate.setText(date);
//
//        mListView = (ListView) findViewById(R.id.dashboard_item_list);
//        aAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, buttons);
//        mListView.setAdapter(aAdapter);
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
//                String buttonName = (String) mListView.getItemAtPosition(position);
//                if("My Flash Cards".equals(buttonName)) {
//                    Intent cardSetListActivity = new Intent(MainActivity.this, CardSetListActivity.class);
//                    startActivity(cardSetListActivity);
//                }
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void myCardSetViewClicked(View view) {
        Intent intent = new Intent(MainActivity.this, CardSetListActivity.class);
        intent.putExtra("from", "myCards");
        startActivity(intent);
    }

    public void unknownCardsViewClicked(View view) {
        Intent intent = new Intent(MainActivity.this, CardListActivity.class);
        intent.putExtra("from", "unknownCards");
        startActivity(intent);
    }

    public void favouriteCardsViewClicked(View view) {
        Intent intent = new Intent(MainActivity.this, CardListActivity.class);
        intent.putExtra("from", "favouriteCards");
        startActivity(intent);
    }

    public void azCardsViewClicked(View view) {
        Intent intent = new Intent(MainActivity.this, CardListActivity.class);
        intent.putExtra("from", "azCards");
        startActivity(intent);
    }
}
