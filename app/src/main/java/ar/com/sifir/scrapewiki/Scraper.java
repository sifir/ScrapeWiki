package ar.com.sifir.scrapewiki;

import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Sifir on 21/07/2016.
 */
public class Scraper extends AsyncTask<Void, Void, Void> {
    String info; // lo que va a setearse en el TextView
    String busqueda; // el texto que viene en el EditText y que se agrega a la url de wikipedia para buscar el articulo
    TextView tv; // en donde va a ponerse la String info al final de todo el proceso
    EditText et; // solo sirve para sacar la String busqueda

    Scraper(EditText et, TextView tv) {
        this.tv = tv;
        this.et = et;
        busqueda = this.et.getText().toString();
    }
    @Override
    protected Void doInBackground(Void... params) {
        Document doc = null;

        try {
            doc = Jsoup.connect("https://en.wikipedia.org/wiki/"+busqueda).get(); // parsea el articulo de wikipedia como un documento de jsoup
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Elements selectedDoc = doc.select(".mw-headline, p"); // crea el array de elementos a iterar con un select al doc
        //empieza a armar la string info que va a ponerse en el TextView tv
        info = "Summary\n\n"; //los articulos no tienen un subtitulo sumario al principio, asi que lo agregué, el \n\n es como apretar dos enter
        info += selectedDoc.get(0).text(); //1er parrafo

        for (Element e : selectedDoc) { //por cada elemento ("casi seria un parrafo"), hace...
            if (e.tagName().contains("mw-headline")) { //si es el tagName del elemento contiene "headline" en su nombre, agrega un espacio extra antes de escribir el subtitulo
                info += "\n";
            }
            info += "\n"; //si no es "headline", solo tiene un punto y aparte
            info += e.text();  //agrega el texto del elemento al string q estamos construyendo
        }

        return null;
    }

    protected void onPostExecute(Void result) {
        tv.setSingleLine(false); // setea al TextView para multilinea
        tv.setText(info);        // setea la string armada
    }
}
