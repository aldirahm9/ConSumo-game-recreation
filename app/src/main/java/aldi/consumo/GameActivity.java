package aldi.consumo;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import io.github.controlwear.virtual.joystick.android.JoystickView;

public class GameActivity extends AppCompatActivity {
    private GameView gameView;
    boolean paused = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //Initializing game view object

        final FrameLayout game = new FrameLayout(this);
        gameView = new GameView(this);
        LinearLayout gameWidgets = new LinearLayout (this);

        final JoystickView joystick = new JoystickView(this);
        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                gameView.player.move(angle,strength);
            }
        });
//        Bitmap bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.background);
//        bmp = Bitmap.createScaledBitmap(bmp, 110,110, false);
        joystick.setButtonSizeRatio(0.3f);
        joystick.setBackgroundSizeRatio(0.4f);
        joystick.setAlpha(0.5f);
        joystick.setBorderAlpha(1);
        joystick.setButtonColor(Color.WHITE);
        joystick.setBackgroundColor(Color.BLACK);


        final Button pause = new Button(this);

        final Bitmap bmp_pausee = BitmapFactory.decodeResource(this.getResources(), R.drawable.pause);
//        final Bitmap bmp_pause = Bitmap.createScaledBitmap(bmp_pausee, 5,5, false);

        final Bitmap bmp_playy = BitmapFactory.decodeResource(this.getResources(), R.drawable.play);
//        final Bitmap bmp_play = Bitmap.createScaledBitmap(bmp_playy, 5,5, false);

        pause.setBackground(new BitmapDrawable(getResources(),bmp_pausee));

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(paused) {
                    paused = false;
                    gameView.resume();
                    gameView.setAlpha(1f);
                    pause.setBackground(new BitmapDrawable(getResources(),bmp_pausee));
                }else {
                    paused = true;
                    gameView.pause();
                    gameView.setAlpha(0.5f);
                    pause.setBackground(new BitmapDrawable(getResources(),bmp_playy));
                }

            }
        });




        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams paramss = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams pausepar = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);




        pausepar.gravity = Gravity.LEFT | Gravity.TOP;
        pausepar.width = 100;
        pausepar.height = 100;
        params.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        paramss.height = 500;
        paramss.width = 500;

        game.addView(pause,pausepar);
        gameWidgets.addView(joystick,paramss);

        game.addView(gameView);
        game.addView(gameWidgets,params);
        game.setBackgroundResource(R.drawable.background);

        //adding it to contentview
        setContentView(game);
    }


    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }


}
