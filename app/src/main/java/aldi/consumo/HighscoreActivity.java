package aldi.consumo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.LinkedList;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class HighscoreActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private LinkedList<Float> highScore = new LinkedList<>();
    private LinkedList<String> names = new LinkedList<>();
    private static final String TAG = "GameView";


    public void menu(View v) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_highscore);
        sharedPreferences = getDefaultSharedPreferences(this);


        highScore.add(sharedPreferences.getFloat("score1",0));
        highScore.add(sharedPreferences.getFloat("score2",0));
        highScore.add(sharedPreferences.getFloat("score3",0));
        highScore.add(sharedPreferences.getFloat("score4",0));
        highScore.add(sharedPreferences.getFloat("score5",0));
        for(int i = 0; i < 5;i++) {
            Log.d(TAG, "onCreate: "+highScore.get(i));
        }

        names.add(sharedPreferences.getString("name1","Unknown"));
        names.add(sharedPreferences.getString("name2","Unknown"));
        names.add(sharedPreferences.getString("name3","Unknown"));
        names.add(sharedPreferences.getString("name4","Unknown"));
        names.add(sharedPreferences.getString("name5","Unknown"));

        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            String name = "";
            float score = 0;

            name = extras.getString("name","Unknown");
            score = extras.getFloat("score",0);

            for(int i = 0; i<5;i++) {
                if(score>highScore.get(i)) {
                    highScore.add(i,score);
                    names.add(i,name);
                    break;
                }
            }
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("score1",highScore.get(0));
        editor.putFloat("score2",highScore.get(1));
        editor.putFloat("score3",highScore.get(2));
        editor.putFloat("score4",highScore.get(3));
        editor.putFloat("score5",highScore.get(4));

        editor.putString("name1",names.get(0));
        editor.putString("name2",names.get(1));
        editor.putString("name3",names.get(2));
        editor.putString("name4",names.get(3));
        editor.putString("name5",names.get(4));
        editor.commit();




        TextView textViewName1 = findViewById(R.id.textViewName1);
        TextView textViewName2 = findViewById(R.id.textViewName2);
        TextView textViewName3 = findViewById(R.id.textViewName3);
        TextView textViewName4 = findViewById(R.id.textViewName4);
        TextView textViewName5 = findViewById(R.id.textViewName5);

        TextView textViewScore1 = findViewById(R.id.textViewScore1);
        TextView textViewScore2 = findViewById(R.id.textViewScore2);
        TextView textViewScore3 = findViewById(R.id.textViewScore3);
        TextView textViewScore4 = findViewById(R.id.textViewScore4);
        TextView textViewScore5 = findViewById(R.id.textViewScore5);


        textViewName1.setText(names.get(0));
        textViewName2.setText(names.get(1));
        textViewName3.setText(names.get(2));
        textViewName4.setText(names.get(3));
        textViewName5.setText(names.get(4));

        textViewScore1.setText(Float.toString(highScore.get(0)));
        textViewScore2.setText(Float.toString(highScore.get(1)));
        textViewScore3.setText(Float.toString(highScore.get(2)));
        textViewScore4.setText(Float.toString(highScore.get(3)));
        textViewScore5.setText(Float.toString(highScore.get(4)));







    }
}
