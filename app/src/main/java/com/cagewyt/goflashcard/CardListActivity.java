package com.cagewyt.goflashcard;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cagewyt.goflashcard.model.FlashCard;
import com.cagewyt.goflashcard.model.FlashCardSet;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CardListActivity extends AppCompatActivity {
    private String flashCardSetId;
    private String flashCardSetColor;

    private List<FlashCard> flashCardList = new ArrayList<>();

    private RecyclerView cardListView;
    private CardListViewAdapter cardListViewAdapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        flashCardSetId = getIntent().getExtras().getString("flashCardSetId");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addCardActivity = new Intent(CardListActivity.this, AddCardActivity.class);
                addCardActivity.putExtra("flashCardSetId", flashCardSetId);
                startActivity(addCardActivity);
            }
        });

        CardListActivity.this.setTitle("Click a card to start");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("FlashCardSets").child(flashCardSetId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    FlashCardSet flashCardSet = dataSnapshot.getValue(FlashCardSet.class);
                    flashCardSetColor = flashCardSet.getColor();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        cardListView = findViewById(R.id.card_list);
        cardListView.setHasFixedSize(true);
        cardListView.setLayoutManager(new GridLayoutManager(this, 3));
        cardListView.setItemAnimator(new DefaultItemAnimator());

        cardListViewAdapter = new CardListViewAdapter(flashCardList, flashCardSetId, flashCardSetColor);
        cardListView.setAdapter(cardListViewAdapter);

        generateFlashCardList();
        cardListViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();

//        FirebaseRecyclerAdapter<FlashCard, CardListActivity.FlashCardViewHolder> firebaseRecyclerAdapter =
//                new FirebaseRecyclerAdapter<FlashCard, CardListActivity.FlashCardViewHolder>(
//                        FlashCard.class,
//                        R.layout.flashcard_cell,
//                        CardListActivity.FlashCardViewHolder.class,
//                        databaseReference.child("flashCards")
//                ) {
//                    @Override
//                    protected void populateViewHolder(CardListActivity.FlashCardViewHolder viewHolder, FlashCard model, int position) {
//                        final String flashCardId = getRef(position).getKey().toString();
//
//                        final int currentCardIndex = flashCardIds.indexOf(flashCardId);
//
//                        viewHolder.setName(model.getName());
//                        viewHolder.setStatusIcon(model.getStatus());
//                        viewHolder.setColor(flashCardSetColor);
//
//                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent cardActivity = new Intent(CardListActivity.this, CardActivity.class);
//                                cardActivity.putExtra("flashCardSetId", flashCardSetId);
//                                cardActivity.putExtra("flashCardSetColor", flashCardSetColor);
//                                cardActivity.putStringArrayListExtra("flashCardIds", flashCardIds);
//                                cardActivity.putExtra("currentCardIndex", currentCardIndex);
//                                startActivity(cardActivity);
//                            }
//                        });
//                    }
//                };

//        cardListView.setAdapter(firebaseRecyclerAdapter);


    }

    private void generateFlashCardList() {
        // get list of FlashCardIds from database
        databaseReference.child("flashCards").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for(DataSnapshot d : dataSnapshot.getChildren()) {
                        String flashCardId = d.getKey();
                        FlashCard flashCard = d.getValue(FlashCard.class);
                        flashCard.setId(flashCardId);
                        flashCardList.add(flashCard);

                        cardListViewAdapter.notifyDataSetChanged();
                    }
                }
            }//onDataChange

            @Override
            public void onCancelled(DatabaseError error) {

            }//onCancelled
        });

        // shuffle them
    }

    class CardListViewAdapter extends RecyclerView.Adapter<FlashCardViewHolder> {
        private List<FlashCard> flashCardList;
        private String flashCardSetId;
        private String flashCardSetColor;

        public CardListViewAdapter(List<FlashCard> flashCardList, String flashCardSetId, String flashCardSetColor) {
            this.flashCardList = flashCardList;
            this.flashCardSetId = flashCardSetId;
            this.flashCardSetColor = flashCardSetColor;
        }

        @Override
        public FlashCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.flashcard_cell, parent, false);

            return new FlashCardViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(FlashCardViewHolder viewHolder, int position) {
            final String flashCardId = flashCardList.get(position).getId();

            FlashCard model = flashCardList.get(position);

            final int currentCardIndex = position;

            viewHolder.setName(model.getName());
            viewHolder.setStatusIcon(model.getStatus());
            viewHolder.setColor(flashCardSetColor);

            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent cardActivity = new Intent(CardListActivity.this, CardActivity.class);
                    cardActivity.putExtra("flashCardSetId", flashCardSetId);
                    cardActivity.putExtra("flashCardSetColor", flashCardSetColor);
                    //cardActivity.putStringArrayListExtra("flashCardIds", flashCardIds);
                    cardActivity.putExtra("currentCardIndex", currentCardIndex);
                    startActivity(cardActivity);
                }
            });
        }

        @Override
        public int getItemCount() {
            return flashCardList.size();
        }
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
            TextView taskName = mView.findViewById(R.id.flashCardName);
            taskName.setText(name);
        }

        public void setStatusIcon(String cardStatus)
        {
            ImageView imageView = mView.findViewById(R.id.cardStatusIcon);
            if("Known".equalsIgnoreCase(cardStatus))
            {
                imageView.setImageResource(R.drawable.ic_done);
            }
            else {
                imageView.setImageResource(R.drawable.ic_clear);
            }
        }

        public void setColor(String color)
        {
            CardView cardView = mView.findViewById(R.id.flashCardBackground);
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
