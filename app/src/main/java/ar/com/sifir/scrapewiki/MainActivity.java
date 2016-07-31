package ar.com.sifir.scrapewiki;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.BreakIterator;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private LinearLayout queryLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button buttonSearch = (Button) findViewById(R.id.buttonSearch);
        final Button buttonSpeedread = (Button) findViewById(R.id.buttonSpeedRead);
        final ScrollView scroller = (ScrollView) findViewById(R.id.scrollView);
        final LinearLayout viewsLayout = (LinearLayout) findViewById(R.id.viewsLayout);
        final EditText busqueda = (EditText)findViewById(R.id.editText);

        buttonSpeedread.setEnabled(false);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Scraper scrap = new Scraper(scroller, viewsLayout, busqueda, getApplicationContext(), buttonSpeedread); // le pasa el ScrollView donde estara el layout, el layout, la keyword y el contexto para crear TextViews
                scrap.execute();
                queryLayout = scrap.getViewsLayout();
            }
        });

    }

    public void speedreadCurrent(View view) {
        Intent speedreadCurrent = new Intent(this, Speedreader.class);
        ArrayList<String> wordsToSpeed = new ArrayList<>();
        for (int i = 0;i<queryLayout.getChildCount();i++) {
            if (queryLayout.getChildAt(i).getClass() == TextView.class) {
                TextView v = (TextView) queryLayout.getChildAt(i);
                //speedreadCurrent.putExtra(String.valueOf(i), v.getText());

                BreakIterator boundary = BreakIterator.getWordInstance();
                boundary.setText(v.getText().toString());
                int start = boundary.first();
                for (int end = boundary.next();
                        end != BreakIterator.DONE;
                        start = end, end = boundary.next()) {
                    wordsToSpeed.add(v.getText().toString().substring(start,end));
                }
            } else {
                wordsToSpeed.add("");
            }
        }
        speedreadCurrent.putStringArrayListExtra("wordsToSpeed",wordsToSpeed);
        startActivity(speedreadCurrent);
    }
}
