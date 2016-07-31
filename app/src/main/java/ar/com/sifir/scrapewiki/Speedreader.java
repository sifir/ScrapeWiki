package ar.com.sifir.scrapewiki;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Dante on 31/07/2016.
 */
public class Speedreader extends AppCompatActivity {
    private TextView speedreadView;
    private int totalChildren;
    private Bundle extras;
    private boolean reading;
    private Button startSpeedread;
    private Button stopSpeedread;
    private ArrayList<String> wordsToSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.extras = getIntent().getExtras();
        setContentView(R.layout.speedreading);
        reading = false;

        startSpeedread = (Button) findViewById(R.id.buttonStartSpeedread);
        stopSpeedread = (Button) findViewById(R.id.buttonStopSpeedread);
        speedreadView = (TextView) findViewById(R.id.speedreadView);

        stopSpeedread.setEnabled(false);

        wordsToSpeed = extras.getStringArrayList("wordsToSpeed");

        String newString = "";
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= wordsToSpeed.get(0);
            }
        }
        speedreadView.setText(newString);

    }

    public void startSpeedreading(View view) {
        reading = true;
        stopSpeedread.setEnabled(true);
        startSpeedread.setEnabled(false);
        for (int i = 0;i<wordsToSpeed.size();i++) {
            if (!reading) {
                stopSpeedread.setEnabled(false);
                startSpeedread.setEnabled(true);
                break;
            }
            final Handler handler = new Handler();
            final int currentI = i;

            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    speedreadView.setText(wordsToSpeed.get(currentI));
                }
            },300*i);
        }
    }

    public void stopSpeedreading(View view) {
        reading = false;
        stopSpeedread.setEnabled(false);
        startSpeedread.setEnabled(true);
    }

}
