package com.wavefaring.noms_n_bombs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class menu extends AppCompatActivity {

    private TextView topScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Identify editable text
        topScore = (TextView)findViewById(R.id.topScore);

        // Pull top score data from application cache
        SharedPreferences settings = getSharedPreferences("Score_Data", Context.MODE_PRIVATE);
        int currentTopScore = settings.getInt("topScore", 0);

        // Set editable text to desired values
        topScore.setText("Top Score: " + currentTopScore);
    }

    public void startGame(View view)
    {
        startActivity(new Intent(menu.this, game.class));
    }

    public void instructions(View view)
    {
        startActivity(new Intent(menu.this, instructions.class));
    }
}
