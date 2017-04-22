package com.wavefaring.noms_n_bombs;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;

public class game extends AppCompatActivity {

    private TextView score;
    private int currentScore = 0;
    private ImageView character;
    private ImageView noms;
    private ImageView bombs;
    private int characterX;
    private int characterY;
    private int nomsX;
    private int nomsY;
    private int bombsX;
    private int bombsY;
    private int windowWidth;
    private int windowHeight;
    private Handler handler = new Handler();
    private Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Identify editable text
        score = (TextView)findViewById(R.id.score);
        character = (ImageView)findViewById(R.id.character);
        noms = (ImageView)findViewById(R.id.noms);
        bombs = (ImageView)findViewById(R.id.bombs);

        // Get window size information so the noms and bombs know where the edges are at
        WindowManager window = getWindowManager();
        Display display = window.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        windowWidth = point.x;
        windowHeight = point.y;

        // Start a timer that will be used to ping where the noms and bombs are at
        timer.schedule(new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        reposition();
                    }
                }
                );
            }
        }, 0, 15);
    }

    // Follow the users finger/stylus/mouse around with the character
    public boolean onTouchEvent(MotionEvent movement) {
        characterX = (int)movement.getX()-135;
        characterY = (int)movement.getY()-365;
        character.setX(characterX);
        character.setY(characterY);

        return false;
    }

    public void reposition() {
        // Randomly selects a spawn location for the noms and sends it flying up from the bottom of the screen
        nomsY -=10;
        if(nomsY < 0) {
            nomsY = windowHeight;
            nomsX = (int)Math.floor(Math.random() * (windowWidth - noms.getWidth()));
        }
        noms.setX(nomsX);
        noms.setY(nomsY);

        // Randomly selects a spawn location for the bombs and sends it flying up from the bottom of the screen
        bombsY -=20;
        if(bombsY < 0) {
            bombsY = windowHeight;
            bombsX = (int)Math.floor(Math.random() * (windowWidth - bombs.getWidth()));
        }
        bombs.setX(bombsX);
        bombs.setY(bombsY);

        // Starts the hitBoxes method
        hitBoxes();
    }

    // Checks to see if any of the noms or bombs collide with the character
    public void hitBoxes() {
        int[] location = new int[2];

        // Hitbox for character image
        character.getLocationInWindow(location);
        Rect characterRectangle = new Rect(location[0], location[1], location[0] + character.getWidth(), location[1] + character.getHeight());

        // Hitbox for noms image
        noms.getLocationInWindow(location);
        Rect nomsRectangle = new Rect(location[0], location[1], location[0] + noms.getWidth(), location[1] + noms.getHeight());

        // Hitbox for bombs image
        bombs.getLocationInWindow(location);
        Rect bombsRectangle = new Rect(location[0], location[1], location[0] + bombs.getWidth(), location[1] + bombs.getHeight());

        // Increments the score by 5 when the character hits a nom
        if (Rect.intersects(characterRectangle, nomsRectangle)) {
            // Moves the nom off the screen
            nomsY = -200;

            currentScore += 5;
            // Set editable text to desired values
            score.setText("Score: " + currentScore);
        }

        // Ends the game and displays the results screen when the character hits a bomb
        if (Rect.intersects(characterRectangle, bombsRectangle)) {
            // Moves the bomb off the screen
            bombsY = -200;

            // Stops the timer so that it doesn't continue running in the background
            timer.cancel();
            timer = null;

            // Create intent as an object to open new activity with special values
            Intent resultIntent = new Intent(getApplicationContext(), over.class);
            // Pass over special values based on values accumulated in this class
            resultIntent.putExtra("finalScore", currentScore);
            // Start the new activity with the desired values sent across
            startActivity(resultIntent);
        }
    }
}
