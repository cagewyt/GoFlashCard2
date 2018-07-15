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
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.Collections;
import java.util.List;

public class CardListActivity extends AppCompatActivity {
    private String from;
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
        CardListActivity.this.setTitle("Click a card to start");

        from = getIntent().getExtras().getString("from");
        flashCardSetId = getIntent().getExtras().getString("flashCardSetId");
        flashCardSetColor = getIntent().getExtras().getString("flashCardSetColor");

        FloatingActionButton fab = findViewById(R.id.fab);
        if(flashCardSetId != null) {
            fab.show();
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent addCardActivity = new Intent(CardListActivity.this, AddCardActivity.class);
                    addCardActivity.putExtra("flashCardSetId", flashCardSetId);
                    startActivity(addCardActivity);
                }
            });
        }
        else
        {
            fab.hide();
        }

        if(flashCardSetId != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("FlashCardSets").child(flashCardSetId);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        FlashCardSet flashCardSet = dataSnapshot.getValue(FlashCardSet.class);
                        flashCardSetColor = flashCardSet.getColor();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        cardListView = findViewById(R.id.card_list);
        cardListView.setHasFixedSize(true);
        cardListView.setLayoutManager(new GridLayoutManager(this, 3));
        cardListView.setItemAnimator(new DefaultItemAnimator());

        cardListViewAdapter = new CardListViewAdapter(flashCardList, flashCardSetId, flashCardSetColor);
        cardListView.setAdapter(cardListViewAdapter);

        generateFlashCardList();
        cardListViewAdapter.notifyDataSetChanged();
    }

    private void generateFlashCardList() {
        if(flashCardSetId != null)
        {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("FlashCardSets").child(flashCardSetId).child("flashCards");
            // get list of FlashCardIds from database
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for(DataSnapshot d : dataSnapshot.getChildren()) {
                            String flashCardId = d.getKey();
                            FlashCard flashCard = d.getValue(FlashCard.class);
                            flashCard.setId(flashCardId);
                            flashCard.setFlashCardSetId(flashCardSetId);
                            flashCardList.add(flashCard);
                            cardListViewAdapter.notifyDataSetChanged();
                        }
                    }
                }//onDataChange
                @Override
                public void onCancelled(DatabaseError error) {

                }//onCancelled
            });
        }
        else
        {
            // load all cards based on the "from"
            databaseReference = FirebaseDatabase.getInstance().getReference().child("FlashCardSets");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for(DataSnapshot d : dataSnapshot.getChildren()) {
                            String flashCardSetId = d.getKey();
                            FlashCardSet flashCardSet = d.getValue(FlashCardSet.class);
                            flashCardSet.setId(flashCardSetId);
                            if(flashCardSet.getFlashCards() != null) {

                                for(String key : flashCardSet.getFlashCards().keySet()) {
                                    FlashCard card = flashCardSet.getFlashCards().get(key);
                                    card.setId(key);

                                    card.setFlashCardSetId(flashCardSetId);

                                    if ("unknownCards".equalsIgnoreCase(from) && "Unknown".equalsIgnoreCase(card.getStatus())) {
                                        flashCardList.add(card);
                                    } else if ("favouriteCards".equalsIgnoreCase(from) && card.isFavourite()) {
                                        flashCardList.add(card);
                                    } else if ("azCards".equalsIgnoreCase(from)) {
                                        flashCardList.add(card);
                                        Collections.sort(flashCardList);
                                    }
                                }

                                cardListViewAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }//onDataChange
                @Override
                public void onCancelled(DatabaseError error) {

                }//onCancelled
            });
        }
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
                    cardActivity.putExtra("flashCardList", (ArrayList)flashCardList);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(flashCardSetId != null) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }
        else
            return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.delete) {
            //Delete the current card set
            deleteCardSet();

            Intent intent = new Intent(this, CardSetListActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteCardSet() {
        if(flashCardSetId != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("FlashCardSets").child(flashCardSetId);
            databaseReference.removeValue();
        }
    }
}
