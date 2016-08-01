package ar.com.sifir.scrapewiki;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Dante on 27/07/2016.
 */
public class ImageScraper extends AsyncTask<Void, Void, Void> {
    private String url;
    private Context context;
    private Drawable d;
    private ImageView iv;
    private Bitmap bm;
    private int width;
    private int height;

    public ImageScraper(String src, Context context, ImageView currentImage) { //recibe los objetos q necesita
        url = src; //url donde esta la imagen
        this.context = context; //contexto para crear TextView
        d = null; //drawable, la imagen en si para android, se inicializa para evitar errores
        this.iv = currentImage; //el ImageView donde va la imagen
        bm = null;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            //prueba de scrape para las imagenes HQ, deshabilitado porque explota la memoria del programa
//            Connection.Response response = Jsoup
//                    .connect(url)
//                    .followRedirects(true)
//                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
//                    .execute();
//            String newUrl = response.url().toString();
//            Document doc = Jsoup.connect(newUrl).get();
//            Elements selectedDoc = doc.select(".fullImageLink > a");
//            String fullSizeUrl = selectedDoc.first().attr("href");

        /* Open a new URL and get the InputStream to load data from it. */
            //URL aURL = new URL("https://"+fullSizeUrl.substring(2));
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();

        /* Buffered is always good for a performance plus. */
            BufferedInputStream bis = new BufferedInputStream(is);
        /* Decode url-data to a bitmap. */
            is = conn.getInputStream();
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
            d = new BitmapDrawable(context.getResources(), bm);
        } catch (IOException ee) {
            Log.e("DEBUGTAG", "Remote Image Exception", ee);
        }
        return null;
    }

    protected void onPostExecute(Void result) {
        iv.setScaleType(ImageView.ScaleType.FIT_CENTER); //centra el ImageView
        iv.setImageDrawable(d); //setea la imagen del ImageView
    }
}
