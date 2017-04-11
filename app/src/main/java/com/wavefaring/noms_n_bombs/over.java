package com.wavefaring.noms_n_bombs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class over extends AppCompatActivity {

    private TextView score;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over);

        // Identify editable text
        score = (TextView)findViewById(R.id.score);
        result = (TextView)findViewById(R.id.result);

        // Get score data generated in game.class
        int finalScore = getIntent().getIntExtra("finalScore", 0);
        // Set editable text to desired values
        score.setText("Your Score: " + finalScore);

        // Pull top score data from application cache
        SharedPreferences settings = getSharedPreferences("Score_Data", Context.MODE_PRIVATE);
        int topScore = settings.getInt("topScore", 0);

        // Check and see if the new score data is higher than the top score data, replace if so
        if (finalScore > topScore) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("topScore", finalScore);
            editor.commit();
        }

        // Check and see if the new score passes the win requirement of 100 points, output result
        if (finalScore > 100) {
            result.setText("You Win!");
        } else {
            result.setText("You Lose!");
        }
    }

    public void mainMenu(View view)
    {
        startActivity(new Intent(over.this, menu.class));
    }
}
