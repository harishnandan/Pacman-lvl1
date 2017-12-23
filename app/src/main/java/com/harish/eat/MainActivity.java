package com.harish.eat;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TextView scorelabel;
    private TextView startlabel;
    private ImageView box;
    private ImageView black;
    private ImageView blue;
    private ImageView yellow;
    private boolean action_flag = false;
    private boolean start_flag = false;

    private int boxY;
    private int blueX;
    private int blackX;
    private int yellowX;
    private int blueY;
    private int blackY;
    private int yellowY;

    private int frameHeight;
    private int boxSize;
    private int screenWidth;
    private int screenHeight;

    private int score = 0;

    private Handler handler = new Handler();
    private Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scorelabel = (TextView) findViewById(R.id.scorelabel);
        startlabel = (TextView) findViewById(R.id.startlabel);
        box = (ImageView) findViewById(R.id.box);
        black = (ImageView) findViewById(R.id.black);
        blue = (ImageView) findViewById(R.id.blue);
        yellow = (ImageView) findViewById(R.id.yellow);

        Typeface arcadeTypeface = Typeface.createFromAsset(getAssets(), "fonts/arcade.ttf");
        scorelabel.setTypeface(arcadeTypeface);
        startlabel.setTypeface(arcadeTypeface);


        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        //Move out of screen
        black.setX(getPixels(-80));
        black.setY(getPixels(-80));
        blue.setX(getPixels(-80));
        blue.setY(getPixels(-80));
        yellow.setX(getPixels(-80));
        yellow.setY(getPixels(-80));
        scorelabel.setText("Score:0");
    }

    private int getPixels(int dipValue) {
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics());
        return px;
    }

    public boolean onTouchEvent(MotionEvent me) {
        if (start_flag == false) {
            start_flag = true;

            FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
            frameHeight = frame.getHeight();

            boxY = (int) box.getY();
            boxSize = box.getHeight();

            startlabel.setVisibility(View.GONE);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }, 0, 20);
        } else {
            if (me.getAction() == MotionEvent.ACTION_DOWN) {
                action_flag = true;
            } else if (me.getAction() == MotionEvent.ACTION_UP) {
                action_flag = false;
            }
        }
        return true;
    }

    public void changePos() {

        hitCheck();
        //yellow
        yellowX -= 12;
        if (yellowX < 0) {
            yellowX = screenWidth + 20;
            yellowY = (int) Math.floor(Math.random() * (frameHeight - yellow.getHeight()));
        }
        yellow.setX(yellowX);
        yellow.setY(yellowY);

        //black
        blackX -= 16;
        if (blackX < 0) {
            blackX = screenWidth + 10;
            blackY = (int) Math.floor(Math.random() * (frameHeight - black.getHeight()));
        }
        black.setX(blackX);
        black.setY(blackY);

        //blue
        blueX -= 12;
        if (blueX < 0) {
            blueX = screenWidth + 100;
            blueY = (int) Math.floor(Math.random() * (frameHeight - blue.getHeight()));
        }
        blue.setX(blueX);
        blue.setY(blueY);

        if (action_flag == true) {
            //touching
            boxY -= 20;
        } else if (action_flag == false) {
            //released
            boxY += 20;
        }

        if (boxY < 0) boxY = 0;

        if (boxY > frameHeight - boxSize) boxY = frameHeight - boxSize;

        box.setY((boxY));
        scorelabel.setText("Score:" + score);
    }

    public void hitCheck() {

        //if center overlaps it is  a hit

        //yellow
        int yellowCenterX = yellowX + yellow.getWidth() / 2;
        int yellowCenterY = yellowY + yellow.getHeight() / 2;

        if (0 <= yellowCenterX && yellowCenterX <= boxSize && yellowCenterY <= boxY + boxSize && boxY <= yellowCenterY) {
            score += 10;
            yellowX = -10;
        }

        //blue
        int blueCenterX = blueX + blue.getWidth() / 2;
        int blueCenterY = blueY + blue.getHeight() / 2;

        if (0 <= blueCenterX && blueCenterX <= boxSize && blueCenterY <= boxY + boxSize && boxY <= blueCenterY) {
            score += 50;
            blueX = -10;
        }

        //black
        int blackCenterX = blackX + black.getWidth() / 2;
        int blackCenterY = blackY + black.getHeight() / 2;

        if (0 <= blackCenterX && blackCenterX <= boxSize && blackCenterY <= boxY + boxSize && boxY <= blackCenterY) {
            try {
                timer.cancel();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            timer = null;

            Intent intent = new Intent(getApplicationContext(), result.class);
            intent.putExtra("SCORE", score);
            startActivity(intent);
        }

    }
    public void onBackPressed() {
        //  super.onBackPressed();
        moveTaskToBack(true);
    }

}

