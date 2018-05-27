package com.cagewyt.goflashcard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddCardSetActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private String cardSetColor = "Yellow";
    private String cardSetName = "No name";

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card_set);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Spinner spinner = (Spinner) findViewById(R.id.cardSetColor);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.card_set_colors, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        database = FirebaseDatabase.getInstance();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        cardSetColor = (String)(parent.getItemAtPosition(pos));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public void saveButtonClicked(View view) {
        EditText cardSetNameText = (EditText)findViewById(R.id.cardSetName);

        cardSetName = cardSetNameText.getText().toString();

        // Time to save
        SimpleDateFormat sdf = new SimpleDateFormat("MMM MM dd, yyyy hh:mm a");

        String dateString = sdf.format(new Date());

        databaseReference = database.getInstance().getReference().child("FlashCardSets");
        DatabaseReference newCardSet = databaseReference.push();
        newCardSet.child("name").setValue(cardSetName);
        newCardSet.child("color").setValue(cardSetColor);


        Intent cardSetListActivity = new Intent(AddCardSetActivity.this, CardSetListActivity.class);
        startActivity(cardSetListActivity);
    }
}
