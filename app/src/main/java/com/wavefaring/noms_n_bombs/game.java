package com.wavefaring.noms_n_bombs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class game extends AppCompatActivity {

    private TextView score;
    private int currentScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Identify editable text
        score = (TextView)findViewById(R.id.score);
    }

    public void placeholderEnd(View view)
    {
        // Create intent as an object to open new activity with special values
        Intent resultIntent = new Intent(getApplicationContext(), over.class);
        // Pass over special values based on values accumulated in this class
        resultIntent.putExtra("finalScore", currentScore);
        // Start the new activity with the desired values sent across
        startActivity(resultIntent);
    }

    public void placeholderScoreIncrement(View view)
    {
        currentScore += 1;
        // Set editable text to desired values
        score.setText("Score: " + currentScore);
    }
}
