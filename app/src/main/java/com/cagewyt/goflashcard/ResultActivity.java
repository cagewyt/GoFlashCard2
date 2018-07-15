package com.cagewyt.goflashcard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.cagewyt.goflashcard.model.FlashCard;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ResultActivity extends AppCompatActivity {
    private String flashCardSetId;
    private ArrayList<FlashCard> flashCardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        flashCardSetId = getIntent().getExtras().getString("flashCardSetId");

        flashCardList = (ArrayList<FlashCard>) getIntent().getSerializableExtra("flashCardList");


        TextView knownPercentageText = findViewById(R.id.knownPercentage);

        calculateKnownPercentage(knownPercentageText);
    }

    private void calculateKnownPercentage(final TextView knownPercentageText)
    {
        int total = flashCardList.size();
        int knownCount = 0;
        for(FlashCard card : flashCardList)
        {
            if("Known".equalsIgnoreCase(card.getStatus()))
            {
                knownCount++;
            }
        }
        if(total == 0)
        {
            knownPercentageText.setText("100%");
            return;
        }

        if(knownCount == total)
        {
            knownPercentageText.setText("100%");
            return;
        }

        long percentage = Math.round(knownCount * 100.0 / total);
        if(percentage == 100)
        {
            // due to round up
            percentage = 99;
        }

        String text = "You have completed "+(int)percentage + "% of cards in this list.";
        knownPercentageText.setText(text);
        return;
    }
//    private void calculateKnownPercentage(final TextView knownPercentageText) {
//
//        final int[] knownCount = {0};
//        final int[] total = {0};
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    for(DataSnapshot d : dataSnapshot.getChildren()) {
//                        HashMap value = (HashMap)(d.getValue());
//                        String status = (String)(value.get("status"));
//
//                        if("Known".equals(status))
//                        {
//                            knownCount[0]++;
//                        }
//
//                        total[0]++;
//                    }
//                    if(total[0] == 0)
//                    {
//                        knownPercentageText.setText("100%");
//                        return;
//                    }
//
//                    if(knownCount[0] == total[0])
//                    {
//                        knownPercentageText.setText("100%");
//                        return;
//                    }
//
//                    long percentage = Math.round(knownCount[0] * 100.0 / total[0]);
//                    if(percentage == 100)
//                    {
//                        // due to round up
//                        percentage = 99;
//                    }
//
//                    knownPercentageText.setText((int)percentage+"%");
//                    return;
//                }
//
//            }//onDataChange
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//
//            }//onCancelled
//        });
//    }
}
