package com.harish.eat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class result extends AppCompatActivity {

    MediaPlayer player;
    AssetFileDescriptor afd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        try {
            // Read the music file from the asset folder
            afd = getAssets().openFd("pacman_ringtone.mp3");
            // Creation of new media player;
            player = new MediaPlayer();
            // Set the player music source.
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            // Set the looping and play the music.
            player.setLooping(true);
            player.prepare();
            player.start();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        int maxVolume = 50, currVolume = 25; //you can set volume accordingly 25 is half of max volume ie, 50
        float log1=(float)(Math.log(maxVolume-currVolume)/Math.log(maxVolume));
        player.setVolume(1-log1, 1-log1);

        Typeface pacmanTypeface = Typeface.createFromAsset(getAssets(), "fonts/pac-font.ttf");
        Typeface arcadeTypeface = Typeface.createFromAsset(getAssets(), "fonts/arcade.ttf");

        TextView gameOverTV = (TextView) findViewById(R.id.gameover);
        gameOverTV.setTypeface(pacmanTypeface);

        TextView scoreTV = (TextView) findViewById(R.id.scorelabel);
        scoreTV.setTypeface(arcadeTypeface);

        TextView highScoreTV = (TextView) findViewById(R.id.highScoreLabel);
        highScoreTV.setTypeface(arcadeTypeface);

        Button tryAgainButton = (Button) findViewById(R.id.tryAgain);
        tryAgainButton.setTypeface(pacmanTypeface);

        TextView scorelabel = (TextView) findViewById(R.id.scorelabel);
        TextView highScoreLabel = (TextView) findViewById(R.id.highScoreLabel);

        int score = getIntent().getIntExtra("SCORE", 0);
        scorelabel.setText(score + "");

        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        int highScore = settings.getInt("HIGH_SCORE", 0);

        if (score > highScore) {
            highScoreLabel.setText("High Score : " + score);

            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE", score);
            editor.commit();
        } else {
            highScoreLabel.setText("High Score : " + highScore);
        }
    }

    public void tryAgain(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    public void onBackPressed() {
        //  super.onBackPressed();
        moveTaskToBack(true);
    }

    public void onPause() {
        super.onPause();
        player.pause();
    }

    public void onResume() {
        super.onResume();
        try {
            player.start();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    protected void onStop() {
        super.onStop();
        player.stop();
        player = null;
    }

}
