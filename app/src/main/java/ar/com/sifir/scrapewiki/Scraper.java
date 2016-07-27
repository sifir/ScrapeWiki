package ar.com.sifir.scrapewiki;

import android.os.AsyncTask;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

/**
 * Created by Sifir on 21/07/2016.
 */
public class Scraper extends AsyncTask<Void, Void, Void> {
    String parrafo;
    String busqueda;
    TextView tv;

    Scraper(TextView tv) {
        this.tv = tv;
        busqueda = tv.getText().toString();
    }
    @Override
    protected Void doInBackground(Void... params) {
        Document doc = null;

        try {
            doc = Jsoup.connect("https://en.wikipedia.org/wiki/"+busqueda).get();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        parrafo = doc.select(".mw-headline, p").get(0).text();
        Element parraf = doc.select(".mw-headline, p").get(0);

        while (parraf.nextElementSibling().hasText()) {
            parraf = parraf.nextElementSibling();
            if (parraf.tagName().contains("headline")) {
                parrafo += "\n\n"+"\n\n";
            }
            parrafo += parraf.text() + "\n";
        }

        return null;
    }

    protected void onPostExecute(Void result) {
        tv.setText(parrafo);
    }
}
