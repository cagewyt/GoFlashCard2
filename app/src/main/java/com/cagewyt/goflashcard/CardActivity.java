package com.cagewyt.goflashcard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CardActivity extends AppCompatActivity {
    private String flashCardSetId;

    private ArrayList<String> flashCardIds;

    private int currentCardIndex = -1;

    private TextView frontText;
    private TextView backText;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_card);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(flashCardSetId == null) {
            flashCardSetId = getIntent().getExtras().getString("flashCardSetId");
        }

        if(flashCardIds == null) {
            flashCardIds = getIntent().getExtras().getStringArrayList("flashCardIds");
        }

        if(currentCardIndex == -1) {
            currentCardIndex = getIntent().getExtras().getInt("currentCardIndex");
        }

        String flashCardId = flashCardIds.get(currentCardIndex);

        frontText = findViewById(R.id.cardFrontText);
        backText = findViewById(R.id.cardBackText);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("FlashCardSets").child(flashCardSetId).child("flashCards");
        databaseReference.child(flashCardId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = (String) dataSnapshot.child("name").getValue();
                String definition = (String) dataSnapshot.child("definition").getValue();
                frontText.setText(name);
                backText.setText(definition);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void unknownClicked(View view) {
        updateCard("Unknown");
    }

    public void knownClicked(View view) {
        updateCard("Known");
    }

    private void updateCard(String unknown) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM MM dd, yyyy hh:mm a");
        String dateString = sdf.format(new Date());

        String flashCardId = flashCardIds.get(currentCardIndex);

        databaseReference.child(flashCardId).child("lastModifiedAt").setValue(dateString);
        databaseReference.child(flashCardId).child("status").setValue(unknown);

        if(currentCardIndex == flashCardIds.size() - 1)
        {
            // this is the last one
            // go to result page
            Intent resultActivity = new Intent(CardActivity.this, ResultActivity.class);
            resultActivity.putExtra("flashCardSetId", flashCardSetId);
            startActivity(resultActivity);
        }
        else
        {
            currentCardIndex++;

            Intent cardActivity = new Intent(CardActivity.this, CardActivity.class);
            cardActivity.putExtra("flashCardSetId", flashCardSetId);
            cardActivity.putStringArrayListExtra("flashCardIds", flashCardIds);
            cardActivity.putExtra("currentCardIndex", currentCardIndex);
            startActivity(cardActivity);
        }
    }
}
