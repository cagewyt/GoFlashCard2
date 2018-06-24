package com.cagewyt.goflashcard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddCardActivity extends AppCompatActivity {

    private String flashCardSetId;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        flashCardSetId = getIntent().getExtras().getString("flashCardSetId");
    }

    public void saveButtonClicked(View view) {
        EditText cardNameText = (EditText)findViewById(R.id.cardName);
        String cardName = cardNameText.getText().toString();

        EditText cardDescriptionText = (EditText)findViewById(R.id.cardDescription);
        String cardDefinition = cardDescriptionText.getText().toString();

        // Time to save
        SimpleDateFormat sdf = new SimpleDateFormat("MMM MM dd, yyyy hh:mm a");

        String dateString = sdf.format(new Date());

        if(cardName == null || cardName.length()==0)
        {
            Snackbar.make(view, "Please input the card name", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            InputMethodManager imm = (InputMethodManager)this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            return;
        }
        else if(cardName.length() >= 30)
        {
            Snackbar.make(view, "The card name must be less than 30 characters", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            InputMethodManager imm = (InputMethodManager)this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            return;
        }
        cardName = cardName.trim();

        if(cardDefinition == null || cardDefinition.length()==0)
        {
            Snackbar.make(view, "Please input the card definition", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            InputMethodManager imm = (InputMethodManager)this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            return;
        }
        else if(cardDefinition.length() >= 500)
        {
            Snackbar.make(view, "The card definition must be less than 500 characters", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            InputMethodManager imm = (InputMethodManager)this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            return;
        }
        cardName = cardDefinition.trim();



        databaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child("FlashCardSets")
                .child(flashCardSetId)
                .child("flashCards");
        DatabaseReference newCardSet = databaseReference.push();
        newCardSet.child("name").setValue(cardName);
        newCardSet.child("definition").setValue(cardDefinition);
        newCardSet.child("status").setValue("Unknown");
        newCardSet.child("createdAt").setValue(dateString);
        newCardSet.child("lastModifiedAt").setValue(dateString);

        Intent addCardActivity = new Intent(AddCardActivity.this, CardListActivity.class);
        addCardActivity.putExtra("flashCardSetId", flashCardSetId);
        startActivity(addCardActivity);
    }
}
