package com.cagewyt.goflashcard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.cagewyt.goflashcard.model.FlashCard;
import com.cagewyt.goflashcard.model.FlashCardSet;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CardListActivity extends AppCompatActivity {
    private String flashCardSetId;
    private RecyclerView cardListView;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        flashCardSetId = getIntent().getExtras().getString("flashCardSetId");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                Intent addCardActivity = new Intent(CardListActivity.this, AddCardActivity.class);
                addCardActivity.putExtra("flashCardSetId", flashCardSetId);
                startActivity(addCardActivity);
            }
        });

        cardListView = (RecyclerView)findViewById(R.id.card_list);
        cardListView.setHasFixedSize(true);
        cardListView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("FlashCardSets").child(flashCardSetId).child("flashCards");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<FlashCard, CardListActivity.FlashCardViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<FlashCard, CardListActivity.FlashCardViewHolder>(
                        FlashCard.class,
                        R.layout.flashcard_row,
                        CardListActivity.FlashCardViewHolder.class,
                        databaseReference
                ) {
                    @Override
                    protected void populateViewHolder(CardListActivity.FlashCardViewHolder viewHolder, FlashCard model, int position) {
                        final String flashCardId = getRef(position).getKey().toString();

                        viewHolder.setName(model.getName());
                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent cardActivity = new Intent(CardListActivity.this, CardActivity.class);
                                cardActivity.putExtra("flashCardSetId", flashCardSetId);
                                cardActivity.putExtra("flashCardId", flashCardId);
                                startActivity(cardActivity);
                            }
                        });
                    }
                };

        cardListView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class FlashCardViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public FlashCardViewHolder(View itemView)
        {
            super(itemView);
            mView = itemView;
        }

        public void setName(String name)
        {
            TextView taskName = (TextView)mView.findViewById(R.id.flashCardName);
            taskName.setText(name);
        }
    }
}
