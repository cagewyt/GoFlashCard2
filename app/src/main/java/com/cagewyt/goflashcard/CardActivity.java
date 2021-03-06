package com.cagewyt.goflashcard;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cagewyt.goflashcard.model.FlashCard;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CardActivity extends AppCompatActivity {
    private String flashCardSetId;
    private String flashCardSetColor;

    private ArrayList<FlashCard> flashCardList;

    private int currentCardIndex = -1;

    private TextView frontText;
    private TextView backText;
    private CardView frontView;
    private CardView backView;

    private DatabaseReference databaseReference;

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_card);

        CardActivity.this.setTitle("Card");

        if(flashCardSetId == null) {
            flashCardSetId = getIntent().getExtras().getString("flashCardSetId");
        }

        if(flashCardSetColor == null) {
            flashCardSetColor = getIntent().getExtras().getString("flashCardSetColor");
        }

        if(flashCardList == null) {
            flashCardList = (ArrayList<FlashCard>) getIntent().getSerializableExtra("flashCardList");
        }

        if(currentCardIndex == -1) {
            currentCardIndex = getIntent().getExtras().getInt("currentCardIndex");
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cardListActivity = new Intent(CardActivity.this, CardListActivity.class);
                cardListActivity.putExtra("flashCardSetId", flashCardSetId);
                startActivity(cardListActivity);
            }
        });

        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        String flashCardId = flashCardList.get(currentCardIndex).getId();

        frontView = findViewById(R.id.cardFrontBackground);
        setColor(frontView, flashCardSetColor);
        backView = findViewById(R.id.cardBackBackground);
        setColor(backView, flashCardSetColor);

        frontText = findViewById(R.id.cardFrontText);
        backText = findViewById(R.id.cardBackText);

        if(flashCardSetId != null) {
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
        else
        {
            frontText.setText(flashCardList.get(currentCardIndex).getName());
            backText.setText(flashCardList.get(currentCardIndex).getDefinition());
        }
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

        String flashCardId = flashCardList.get(currentCardIndex).getId();

        if(flashCardSetId != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("FlashCardSets").child(flashCardSetId).child("flashCards");
        }
        else
        {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("FlashCardSets").child(flashCardList.get(currentCardIndex).getFlashCardSetId()).child("flashCards");
        }
        databaseReference.child(flashCardId).child("lastModifiedAt").setValue(dateString);
        databaseReference.child(flashCardId).child("status").setValue(unknown);
        flashCardList.get(currentCardIndex).setStatus(unknown);

        if(currentCardIndex == flashCardList.size() - 1)
        {
            // this is the last one
            // go to result page
            Intent resultActivity = new Intent(CardActivity.this, ResultActivity.class);
            resultActivity.putExtra("flashCardSetId", flashCardSetId);
            resultActivity.putExtra("flashCardList", (ArrayList)flashCardList);
            startActivity(resultActivity);
        }
        else
        {
            currentCardIndex++;

            Intent cardActivity = new Intent(CardActivity.this, CardActivity.class);
            cardActivity.putExtra("flashCardSetId", flashCardSetId);
            cardActivity.putExtra("flashCardList", (ArrayList)flashCardList);
            cardActivity.putExtra("currentCardIndex", currentCardIndex);
            startActivity(cardActivity);
        }
    }

    public void setColor(CardView cardView, String color)
    {
        if("Blue".equals(color))
        {
            cardView.setCardBackgroundColor(Color.parseColor("#546de5"));
        }
        else if("Green".equals(color))
        {
            cardView.setCardBackgroundColor(Color.parseColor("#05c46b"));
        }
        else if("Pink".equals(color))
        {
            cardView.setCardBackgroundColor(Color.parseColor("#f78fb3"));
        }
        else if("Yellow".equals(color))
        {
            cardView.setCardBackgroundColor(Color.parseColor("#f4df42"));
        }
        else
        {
            cardView.setCardBackgroundColor(Color.parseColor("#f4df42"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.favourite);

        if(flashCardList.get(currentCardIndex).isFavourite())
        {
            item.setIcon(R.drawable.ic_favorite);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.delete) {
            //Delete the current card set
            deleteCard();
        }
        else if (id == R.id.favourite)
        {
            markFavourite();
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteCard() {
        String flashCardId = flashCardList.get(currentCardIndex).getId();

        if(flashCardSetId != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("FlashCardSets").child(flashCardSetId).child("flashCards");
        }
        else
        {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("FlashCardSets").child(flashCardList.get(currentCardIndex).getFlashCardSetId()).child("flashCards");
        }
        databaseReference.child(flashCardId).removeValue();
        flashCardList.remove(currentCardIndex);

        if(currentCardIndex > flashCardList.size() - 1)
        {
            // this is the last one
            // go to result page
            Intent resultActivity = new Intent(CardActivity.this, ResultActivity.class);
            resultActivity.putExtra("flashCardSetId", flashCardSetId);
            resultActivity.putExtra("flashCardList", (ArrayList)flashCardList);
            startActivity(resultActivity);
        }
        else
        {
            Intent cardActivity = new Intent(CardActivity.this, CardActivity.class);
            cardActivity.putExtra("flashCardSetId", flashCardSetId);
            cardActivity.putExtra("flashCardList", (ArrayList)flashCardList);
            cardActivity.putExtra("currentCardIndex", currentCardIndex);
            startActivity(cardActivity);
        }
    }

    private void markFavourite() {
        String flashCardId = flashCardList.get(currentCardIndex).getId();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("FlashCardSets").child(flashCardList.get(currentCardIndex).getFlashCardSetId()).child("flashCards");

        MenuItem item = menu.findItem(R.id.favourite);
        if(flashCardList.get(currentCardIndex).isFavourite())
        {
            // mark as not favourite
            flashCardList.get(currentCardIndex).setFavourite(false);
            item.setIcon(R.drawable.ic_favorite_border);
            databaseReference.child(flashCardId).child("favourite").setValue(false);
        }
        else
        {
            flashCardList.get(currentCardIndex).setFavourite(true);
            item.setIcon(R.drawable.ic_favorite);
            databaseReference.child(flashCardId).child("favourite").setValue(true);
        }
    }
}
