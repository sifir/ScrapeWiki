package ar.com.sifir.scrapewiki;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
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
    private Context context;
    private Drawable d;
    private ImageView iv;
    private Bitmap bm;

    public ImageScraper(String src, TextView currentEle, Context context, ImageView currentImage) { //recibe los objetos q necesita
        url = src; //url donde esta la imagen
        this.context = context; //contexto para crear TextView
        d = null; //drawable, la imagen en si para android, se inicializa para evitar errores
        this.iv = currentImage; //el ImageView donde va la imagen
        bm = null;
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
        //iv.setImageBitmap(bm);
        iv.setScaleType(ImageView.ScaleType.CENTER);
        //LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(
        //        LinearLayout.LayoutParams.WRAP_CONTENT,
        //        LinearLayout.LayoutParams.MATCH_PARENT);
        //iv.setLayoutParams(imageViewParams);
        iv.setImageDrawable(d);
        //iv.setAdjustViewBounds(true);
        //tv.setCompoundDrawablesWithIntrinsicBounds(d,null,null,null); //esto hay que hacerlo "postExecute" porque sino android tira error, setea el drawable en el textview

    }
}
