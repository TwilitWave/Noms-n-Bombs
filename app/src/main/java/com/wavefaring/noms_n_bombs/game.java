package com.wavefaring.noms_n_bombs;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
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

    MediaPlayer pickupNoms;
    MediaPlayer pickupBombs;
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
    private int speed = 0;
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

        // Instantiates the playable sounds
        pickupNoms = MediaPlayer.create(this, R.raw.pickupnom);
        pickupBombs = MediaPlayer.create(this, R.raw.pickupbomb);
    }

    // Follow the users finger/stylus/mouse around with the character
    public boolean onTouchEvent(MotionEvent movement) {
        characterX = (int)movement.getX()-175;
        characterY = (int)movement.getY()-460;
        character.setX(characterX);
        character.setY(characterY);

        return false;
    }

    public void reposition() {
        // Randomly selects a spawn location for the noms and sends it flying up from the bottom of the screen
        nomsY -= speed + 5;
        if(nomsY < 0) {
            nomsY = windowHeight;
            nomsX = (int)Math.floor(Math.random() * (windowWidth - noms.getWidth()));
        }
        noms.setX(nomsX);
        noms.setY(nomsY);

        // Randomly selects a spawn location for the bombs and sends it flying up from the bottom of the screen
        bombsY -= speed + 10;
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
        Rect characterRectangle = new Rect(location[0], location[1], location[0] + character.getWidth() - 50, location[1] + character.getHeight() - 50);

        // Hitbox for noms image
        noms.getLocationInWindow(location);
        Rect nomsRectangle = new Rect(location[0], location[1], location[0] + noms.getWidth() - 25, location[1] + noms.getHeight() - 25);

        // Hitbox for bombs image
        bombs.getLocationInWindow(location);
        Rect bombsRectangle = new Rect(location[0], location[1], location[0] + bombs.getWidth() - 35, location[1] + bombs.getHeight() -35);

        // Increments the score by 5 when the character hits a nom and speeds up both the noms and bombs
        if (Rect.intersects(characterRectangle, nomsRectangle)) {
            // Moves the nom off the screen
            nomsY = -200;

            // Play pickup sound
            if (pickupNoms.isPlaying()) {
                pickupNoms.stop();
                pickupNoms.release();
                pickupNoms = MediaPlayer.create(this, R.raw.pickupnom);
            }
            pickupNoms.start();

            currentScore += 5;
            // Set editable text to desired values
            score.setText("Score: " + currentScore);

            // Ups the speed by 1
            speed += 1;
        }

        // Ends the game and displays the results screen when the character hits a bomb
        if (Rect.intersects(characterRectangle, bombsRectangle)) {
            // Moves the bomb off the screen
            bombsY = -200;

            // Play death sound
            if (pickupBombs.isPlaying()) {
                pickupBombs.stop();
                pickupBombs.release();
                pickupBombs = MediaPlayer.create(this, R.raw.pickupbomb);
            }
            pickupBombs.start();

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
