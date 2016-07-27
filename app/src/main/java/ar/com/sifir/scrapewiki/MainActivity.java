package ar.com.sifir.scrapewiki;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                TextView tv1 = (TextView)findViewById(R.id.editText);

                Scraper scrap = new Scraper(tv1);
                scrap.execute();
            }
        });




        //while (parrafo.nextElementSibling().hasText()) {
        //    parrafo = parrafo.nextElementSibling();
        //    System.out.println(parrafo.text());
        //}
    }
}
