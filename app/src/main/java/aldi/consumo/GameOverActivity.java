package aldi.consumo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.LinkedList;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class GameOverActivity extends Activity {
    SharedPreferences sharedPreferences;
    private LinkedList<Float> highScore = new LinkedList<>();
    private float score;
    private String cname = "Default";
    private boolean top5 = false;
    private static final String TAG = "GameView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover);
        Intent intent = getIntent();
        score = intent.getFloatExtra("score",55.0f);

        TextView scoreTxt = findViewById(R.id.textView4);
        scoreTxt.setText("Your Score : " + score);


        sharedPreferences = getDefaultSharedPreferences(this);
        highScore.add(sharedPreferences.getFloat("score1",0));
        highScore.add(sharedPreferences.getFloat("score2",0));
        highScore.add(sharedPreferences.getFloat("score3",0));
        highScore.add(sharedPreferences.getFloat("score4",0));
        highScore.add(sharedPreferences.getFloat("score5",0));


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        for(int i = 0; i<5;i++) {
//            Log.d(TAG, "onCreate: "+score);
            if(score>highScore.get(i)) {
                top5 = true;
                builder.setTitle("Title");

                // Set up the input
                final EditText input = new EditText(this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setMessage("Input your name to be on the TOP 5 leaderboard");
                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            cname = input.getText().toString();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

                break;
            }
        }


    }



    public void next(View v) {
        Intent  intenti = new Intent(this,HighscoreActivity.class);// pindah ke highscrow
        if(top5){
            intenti.putExtra("name",cname);
            intenti.putExtra("score",score);
        }
        startActivity(intenti);
        finish();
    }
}
