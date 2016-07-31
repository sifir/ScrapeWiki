package ar.com.sifir.scrapewiki;

import android.content.Context;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.widget.Button;
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
    private String busqueda; // el texto que viene en el EditText y que se agrega a la url de wikipedia para buscar el articulo
    private ScrollView sv; //el ScrollView que contiene las views para hacerlas scrolleables
    private Context context; //el contexto para crear views
    private LinearLayout viewsLayout; //layout que va en el ScrollView, contiene todos los TextView e ImageView
    private Elements selectedDoc; //array de elementos a iterar durante la operatoria
    private Button buttonSpeedread;


    Scraper(ScrollView scroller, LinearLayout viewsLayout, EditText et, Context applicationContext, Button buttonSpeedread) {
        this.sv = scroller;
        busqueda = et.getText().toString(); // saca la keyword a buscar en wikipedia
        this.context = applicationContext;
        this.viewsLayout = viewsLayout;
        this.selectedDoc = null; //inicializa la variable para evitar errores
        this.buttonSpeedread = buttonSpeedread;
    }
    @Override
    protected Void doInBackground(Void... params) {
        Document doc = null; //inicializa el objeto para evitar errores

        try {
            doc = Jsoup.connect("https://en.wikipedia.org/wiki/"+busqueda).ignoreHttpErrors(true).get(); // parsea el articulo de wikipedia como un documento de jsoup
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (isValidDoc(doc)) { // solo si el articulo no da 404
            selectedDoc = doc.select(".mw-headline, p, img"); // crea el array de elementos a iterar con un select al doc
            //selectedDoc = doc.select(".mw-headline, p, .image"); // select para imagenes HQ
        }

        return null;
    }

    private boolean isValidDoc(Document doc) {
        return !doc.select("p").get(0).text().contains("Other reasons this message"); // si el string del elemento 0 TIENE ese texto, entonces NO es valido el doc
    }

    protected void onPostExecute(Void result) {
        if (selectedDoc != null) {
            sv.removeAllViews(); // limpia el scrollview porque solo puede tener 1 view adentro
            viewsLayout.removeAllViews(); // limpia el layout de views viejos

            addSummaryView(); //metodo que agrega el primer subtitulo, que wikipedia no incluye

            createViewsFromSelect(); //crea todos los views necesarios y los agrega al Layout

            sv.addView(viewsLayout); //agrega el layout con todos los TextView al ScrollView
            buttonSpeedread.setEnabled(true);
        } else {
            sv.removeAllViews(); // limpia el ScrollView
            viewsLayout.removeAllViews(); // limpia el layout
            addSorryView(); // agrega TextView sorry al layout
            sv.addView(viewsLayout);  // agrega layout al ScrollView
            buttonSpeedread.setEnabled(false);
        }
    }

    private void addSorryView() {
        TextView sorryView = new TextView(context); // nuevo TextView para informar del error
        sorryView.setText(R.string.sorryText); // setea texto de error
        viewsLayout.addView(sorryView); //agrega view al layout
    }

    private void createViewsFromSelect() {
        for (Element e : selectedDoc) {
            TextView currentEle = new TextView(context); // empieza a armar el TextView para el elemento actual

            if (e.tagName().contains("span")) { //asumo q todos los headlines contienen "span" en el tagname
                addSubtitleView(e, currentEle); //le pasa las variables necesarias al metodo que hace el trabajo
            //} else if (e.attr("class").contains("image")) { // para imagenes HQ, deshabilitado
            } else if (e.tagName().contains("img")) {
                String src = e.absUrl("src"); //obtiene un string con la url solamente
                //String src = "http://www.wikipedia.com"+e.attr("href"); para el scrape de imagenes HQ, deshabilitado

                //para el scrape de width y height de la imagen, deshabilitado
                //int width = Integer.parseInt(e.attr("width"));
                //int height = Integer.parseInt(e.attr("height"));

                ImageView currentImage = new ImageView(context); //crea el ImageView

                addImageView(src, currentImage); //le pasa las variables necesarias al metodo que hace el trabajo
            } else { //si no es headline...
                addTextView(e, currentEle); //le pasa las variables necesarias al metodo que hace el trabajo
            }
        }
    }

    private void addImageView(String src, ImageView currentImage) {
        ImageScraper imgScraper = new ImageScraper(src, context, currentImage); // crea la clase q va a scrapear la imagen
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

    public LinearLayout getViewsLayout() {
        return viewsLayout;
    }
}
