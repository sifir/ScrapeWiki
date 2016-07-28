package ar.com.sifir.scrapewiki;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = (Button) findViewById(R.id.button);
        final TextView result = (TextView) findViewById(R.id.textView);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText busqueda = (EditText)findViewById(R.id.editText);

                Scraper scrap = new Scraper(busqueda, result); // le pasa el EditText busqueda con la keyword, y el TextView result en donde va a poner la data
                scrap.execute();
            }
        });




        //while (info.nextElementSibling().hasText()) {
        //    info = info.nextElementSibling();
        //    System.out.println(info.text());
        //}
    }
}
