package aldi.consumo;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class StageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage);

        TextView textView = findViewById(R.id.textView4);


        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            String text = "";


            text = extras.getString("stage", "Stage");
            textView.setText(text);
        }


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
