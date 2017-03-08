package com.wavefaring.noms_n_bombs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
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
