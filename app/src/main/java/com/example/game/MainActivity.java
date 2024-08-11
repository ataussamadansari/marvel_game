package com.example.game;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GridLayout gridLayout;
    private List<Integer> cards;
    private ImageButton firstCard, secondCard;
    private int firstCardIndex, secondCardIndex;
    private boolean isFlipping;
    private int movesCount;
    private TextView movesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        gridLayout = findViewById(R.id.gridLayout);
        movesTextView = findViewById(R.id.movesTextView);

        initializeCards();
        shuffleCards();
        setUpGameBoard();
    }

    private void initializeCards() {
        cards = new ArrayList<>();
        cards.add(R.drawable.ironman);
        cards.add(R.drawable.captainamerica);
        cards.add(R.drawable.thor);
        cards.add(R.drawable.hulk);
        cards.add(R.drawable.blackwidow);
        cards.add(R.drawable.spiderman);
        cards.add(R.drawable.ironman);
        cards.add(R.drawable.captainamerica);
        cards.add(R.drawable.thor);
        cards.add(R.drawable.hulk);
        cards.add(R.drawable.blackwidow);
        cards.add(R.drawable.spiderman);
    }

    private void shuffleCards() {
        Collections.shuffle(cards);
    }

    private void setUpGameBoard() {
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            final int index = i;
            final ImageButton cardButton = (ImageButton) gridLayout.getChildAt(i);
            cardButton.setImageResource(R.drawable.card_back);
            cardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFlipping) return;
                    handleCardFlip(cardButton, index);
                }
            });
        }
    }

    private void handleCardFlip(ImageButton cardButton, int index) {
        cardButton.setImageResource(cards.get(index));

        if (firstCard == null) {
            firstCard = cardButton;
            firstCardIndex = index;
        } else if (secondCard == null) {
            secondCard = cardButton;
            secondCardIndex = index;
            checkForMatch();
        }
    }

    private void checkForMatch() {
        if (cards.get(firstCardIndex).equals(cards.get(secondCardIndex))) {
            firstCard = null;
            secondCard = null;
            checkForGameEnd();
        } else {
            isFlipping = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    firstCard.setImageResource(R.drawable.card_back);
                    secondCard.setImageResource(R.drawable.card_back);
                    firstCard = null;
                    secondCard = null;
                    isFlipping = false;
                }
            }, 1000);
        }
        movesCount++;
        movesTextView.setText("Moves: " + movesCount);
    }

    private void checkForGameEnd() {
        boolean allMatched = true;
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            ImageButton cardButton = (ImageButton) gridLayout.getChildAt(i);
            if (cardButton.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.card_back).getConstantState()) {
                allMatched = false;
                break;
            }
        }
        if (allMatched) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, EndActivity.class);
                    intent.putExtra("MOVES_COUNT", movesCount);
                    startActivity(intent);
                    finish();
                }
            }, 1000);
        }
    }
}