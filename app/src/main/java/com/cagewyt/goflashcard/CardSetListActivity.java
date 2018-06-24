package com.cagewyt.goflashcard;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.cagewyt.goflashcard.model.FlashCardSet;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CardSetListActivity extends AppCompatActivity {

    private RecyclerView cardSetListView;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_set_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent addCardSetActivity = new Intent(CardSetListActivity.this, AddCardSetActivity.class);
                startActivity(addCardSetActivity);
            }
        });

        cardSetListView = (RecyclerView)findViewById(R.id.card_set_list);
        cardSetListView.setHasFixedSize(true);
        cardSetListView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("FlashCardSets");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<FlashCardSet, FlashCardSetViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<FlashCardSet, FlashCardSetViewHolder>(
                        FlashCardSet.class,
                        R.layout.flashcard_set_row,
                        FlashCardSetViewHolder.class,
                        databaseReference
                ) {
                    @Override
                    protected void populateViewHolder(FlashCardSetViewHolder viewHolder, FlashCardSet model, int position) {
                        final String flashCardSetKey = getRef(position).getKey().toString();

                        viewHolder.setName(model.getName());
                        viewHolder.setColor(model.getColor());

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent cardListActivity = new Intent(CardSetListActivity.this, CardListActivity.class);
                                cardListActivity.putExtra("flashCardSetId", flashCardSetKey);
                                startActivity(cardListActivity);
                            }
                        });
                    }
                };

        cardSetListView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class FlashCardSetViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public FlashCardSetViewHolder(View itemView)
        {
            super(itemView);
            mView = itemView;
        }

        public void setName(String name)
        {
            String firstLetter;
            if(name == null || name.isEmpty())
            {
                firstLetter = "?";
            }
            else
            {
                firstLetter=name.substring(0,1).toUpperCase();
            }
            TextView firstLetterView = mView.findViewById(R.id.cardFirstLetter);
            firstLetterView.setText(firstLetter);

            TextView taskName = mView.findViewById(R.id.flashCardSetName);
            taskName.setText(name);
        }

        public void setColor(String color)
        {
            CardView cardView = mView.findViewById(R.id.cardDisk);
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
                cardView.setCardBackgroundColor(Color.parseColor("#f5cd79"));
            }
            else
            {
                cardView.setCardBackgroundColor(Color.parseColor("#f5cd79"));
            }
        }
    }
}
