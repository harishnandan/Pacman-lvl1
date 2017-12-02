package com.harish.eat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class result extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

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

        TextView scorelabel=(TextView)findViewById(R.id.scorelabel);
        TextView highScoreLabel=(TextView)findViewById(R.id.highScoreLabel);

        int score=getIntent().getIntExtra("SCORE",0);
        scorelabel.setText(score+"");

        SharedPreferences settings=getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        int highScore=settings.getInt("HIGH_SCORE",0);

        if(score>highScore){
            highScoreLabel.setText("High Score : "+score);

            SharedPreferences.Editor editor=settings.edit();
            editor.putInt("HIGH_SCORE",score);
            editor.commit();
        }
        else
        {
            highScoreLabel.setText("High Score : "+highScore);
        }
    }

    public void tryAgain(View view){
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
}
