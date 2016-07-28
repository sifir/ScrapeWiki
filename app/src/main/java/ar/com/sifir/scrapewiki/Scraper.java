package ar.com.sifir.scrapewiki;

import android.content.Context;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.ImageView;
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
    ScrollView sv; //el ScrollView que contiene las views para hacerlas scrolleables
    Context context; //el contexto para crear views
    LinearLayout viewsLayout; //layout que va en el ScrollView, contiene todos los TextView e ImageView
    Elements selectedDoc; //array de elementos a iterar durante la operatoria

    Scraper(ScrollView scroller, LinearLayout viewsLayout, EditText et, Context applicationContext) {
        this.sv = scroller;
        busqueda = et.getText().toString(); // saca la keyword a buscar en wikipedia
        this.context = applicationContext;
        this.viewsLayout = viewsLayout;
        this.selectedDoc = null; //inicializa la variable para evitar errores
    }
    @Override
    protected Void doInBackground(Void... params) {
        Document doc = null; //inicializa el objeto para evitar errores

        try {
            doc = Jsoup.connect("https://en.wikipedia.org/wiki/"+busqueda).get(); // parsea el articulo de wikipedia como un documento de jsoup
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        selectedDoc = doc.select(".mw-headline, p, img"); // crea el array de elementos a iterar con un select al doc

        return null;
    }

    protected void onPostExecute(Void result) {
        sv.removeAllViews(); // limpia el scrollview porque solo puede tener 1 view adentro

        addSummaryView(); //metodo que agrega el primer subtitulo, que wikipedia no incluye

        for (Element e : selectedDoc) {
            TextView currentEle = new TextView(context); // empieza a armar el TextView para el elemento actual

            if (e.tagName().toString().contains("span")) { //asumo q todos los headlines contienen "span" en el tagname
                addSubtitleView(e, currentEle); //le pasa las variables necesarias al metodo que hace el trabajo
            } else if (e.tagName().toString().contains("img")) {
                String src = e.absUrl("src"); //obtiene un string con la url solamente
                ImageView currentImage = new ImageView(context); //crea el ImageView
                addImageView(src, currentEle, currentImage); //le pasa las variables necesarias al metodo que hace el trabajo
            } else { //si no es headline...
                addTextView(e, currentEle); //le pasa las variables necesarias al metodo que hace el trabajo
            }
        }
        sv.addView(viewsLayout); //agrega el layout con todos los TextView al ScrollView
    }

    private void addImageView(String src, TextView currentEle, ImageView currentImage) {
        ImageScraper imgScraper = new ImageScraper(src, currentEle, context, currentImage); // crea la clase q va a scrapear la imagen
        imgScraper.execute(); //lo ejecuta
        viewsLayout.addView(currentImage); //agrega el ImageView al layout
    }

    private void addSummaryView() {
        TextView summaryView = new TextView(context); //crea el TextView
        summaryView.setText("Summary"+"\n"); //setea el texto del TextView
        summaryView.setPaintFlags(summaryView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG); //subraya el subtitulo
        viewsLayout.addView(summaryView); //agrega el TextView al layout
    }

    private void addSubtitleView(Element e, TextView currentEle) {
        currentEle.setText("\n" + e.text() + "\n"); //agrega un espacio antes y despues del subtitulo
        currentEle.setPaintFlags(currentEle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); //subraya el subtitulo
        viewsLayout.addView(currentEle); //agrega el TextView al layout
    }

    private void addTextView(Element e, TextView currentEle) {
        currentEle.setText(e.text()); //simplemente pega el texto
        viewsLayout.addView(currentEle); // y lo agrega al layout
    }
}
