package ar.com.sifir.scrapewiki;

import android.content.Context;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
    String busqueda; // el texto que viene en el EditText y que se agrega a la url de wikipedia para buscar el articulo
    ScrollView sv;
    Context context;
    LinearLayout viewsLayout;
    Elements selectedDoc;

    Scraper(ScrollView scroller, LinearLayout viewsLayout, EditText et, Context applicationContext) {
        this.sv = scroller;
        busqueda = et.getText().toString(); // saque la keyword a buscar en wikipedia
        this.context = applicationContext; //necesita el contexto para crear TextViews correctamente
        this.viewsLayout = viewsLayout; //necesita el layout para poner los TextViews
        this.selectedDoc = null; //inicializa la variable para evitar errores
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
        selectedDoc = doc.select(".mw-headline, p"); // crea el array de elementos a iterar con un select al doc

        return null;
    }

    protected void onPostExecute(Void result) {
        //tv.setSingleLine(false); // setea al TextView para multilinea
        //tv.setText(info);        // setea la string armada
        sv.removeAllViews(); // limpia el scrollview porque solo puede tener 1 view adentro

        //agrega manualmente el primer subtitulo, que wikipedia no incluye
        TextView summaryView = new TextView(context);
        summaryView.setText("Summary"+"\n");
        summaryView.setPaintFlags(summaryView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG); //subraya el subtitulo
        viewsLayout.addView(summaryView);

        for (Element e : selectedDoc) {
            TextView currentEle = new TextView(context); // empieza a armar el TextView para el elemento actual
            if (e.tagName().toString().contains("span")) { //asumo q todos los headlines contienen "span" en el tagname
                currentEle.setText("\n"+e.text()+"\n"); //agrega un espacio antes y despues del subtitulo
                currentEle.setPaintFlags(currentEle.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG); //subraya el subtitulo
                viewsLayout.addView(currentEle); //agrega la view del subtitulo al layout
            } else { //si no es headline...
                currentEle.setText(e.text()); //simplemente pega el texto
                viewsLayout.addView(currentEle); // y lo agrega al layout
            }
        }
        sv.addView(viewsLayout); //agrega el layout con todos los TextView al ScrollView
    }
}
