package ar.com.sifir.scrapewiki;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = (Button) findViewById(R.id.button);
        final ScrollView scroller = (ScrollView) findViewById(R.id.scrollView);
        final LinearLayout viewsLayout = (LinearLayout) findViewById(R.id.viewsLayout);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText busqueda = (EditText)findViewById(R.id.editText);

                Scraper scrap = new Scraper(scroller, viewsLayout, busqueda, getApplicationContext()); // le pasa el ScrollView donde estara el layout, el layout, la keyword y el contexto para crear TextViews
                scrap.execute();
            }
        });
    }
}
