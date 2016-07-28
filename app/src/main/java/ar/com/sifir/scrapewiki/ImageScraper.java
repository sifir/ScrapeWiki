package ar.com.sifir.scrapewiki;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

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
    private TextView tv;
    private Context context;
    private Drawable d;

    public ImageScraper(String src, TextView currentEle, Context context) { //recibe los objetos q necesita
        url = src; //url donde esta la imagen
        tv = currentEle; //TextView donde va la imagen
        this.context = context; //contexto para crear TextView
        d = null; //drawable, la imagen en si para android
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
        /* Open a new URL and get the InputStream to load data from it. */
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
        /* Buffered is always good for a performance plus. */
            BufferedInputStream bis = new BufferedInputStream(is);
        /* Decode url-data to a bitmap. */
            Bitmap bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
            d = new BitmapDrawable(context.getResources(), bm);
        } catch (IOException ee) {
            Log.e("DEBUGTAG", "Remote Image Exception", ee);
        }
        return null;
    }

    protected void onPostExecute(Void result) {
        tv.setCompoundDrawablesWithIntrinsicBounds(d,null,null,null); //esto hay que hacerlo "postExecute" porque sino android tira error, setea el drawable en el textview

    }
}
