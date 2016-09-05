package com.fasic.fasic.muzej;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Prikaz extends Activity {
    protected List<Bitmap> listaSlike = new ArrayList<Bitmap>();
    protected List<String> listaPutanjaSlika = new ArrayList<String>();

    protected int prikazanaSlika;
    protected boolean postavljena = false;
    protected ImageView galerija;
    protected MediaPlayer mediaPlayer;
    protected String jezik;
    protected Target t;

    protected String baseURL;
    protected String baseFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        jezik = intent.getStringExtra("jezik");
        setJezik(jezik);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prikaz);
        setFont();
        galerija = (ImageView) findViewById(R.id.slikaID);

        baseFile =  getResources().getString(R.string.baseFile);
        baseURL = getResources().getString(R.string.baseURL);
        String ver = BuildConfig.VERSION_NAME;
        String url = baseURL + baseFile + "?id=" + id + "&len=" + jezik + "&ver=" + ver;
        new GetJSON(url).execute(); //ako fali nesto za url?!

        Button backDugme = (Button) findViewById(R.id.backID);
        backDugme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton playDugme = (ImageButton) findViewById(R.id.playID);
        playDugme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    if (!mediaPlayer.isPlaying()){
                        setPlayDugme(false);
                        mediaPlayer.start();
                    }
                    else{
                        setPlayDugme(true);
                        mediaPlayer.pause();
                    }
                }
            }
        });

        t = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.i("-->", "Loaded");
                listaSlike.add(bitmap);
                if (!postavljena) {
                    galerija.setImageBitmap(bitmap);
                    prikazanaSlika = 0;
                    postavljena = true;
                    Log.i("-->", "" + "Prva"); //Slike nece iz prve, proveri zasto!!!!
                    galerija.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(final View view, final MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                if (listaSlike.size() > 0) {
                                    prikazanaSlika++;
                                    if (prikazanaSlika >= listaSlike.size()) prikazanaSlika = 0;
                                    Prikaz.ImageViewAnimatedChange(Prikaz.this, galerija, listaSlike.get(prikazanaSlika));
                                }
                            }
                            return true;
                        }
                    });
                }

                Log.i("-->", listaSlike.size()+ ":" + listaPutanjaSlika.size());
                if(listaSlike.size() < listaPutanjaSlika.size()){
                    Picasso.with(Prikaz.this).load(listaPutanjaSlika.get(listaSlike.size())).into(t);
                }

            }
            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.i("-->", "" + "Faild");
            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.i("-->", "" + "onPrepareLoad");
            }
        };
    }

    protected void setJezik(String jezik){
        Locale locale;
        String drzava;

        if(jezik.equals("sr")){
            Log.i("-->","sr");
            drzava = "RS";
        }
        else{
            Log.i("-->","en");
            drzava = "US";
        }

        locale = new Locale(jezik, drzava);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    private void setFont(){
        TextView naslovTV = (TextView) findViewById(R.id.naslovID);
        Button backDugme = (Button) findViewById(R.id.backID);
        TextView opisTV = (TextView) findViewById(R.id.txtID);

        Typeface font = Typeface.createFromAsset(getAssets(),  getResources().getString(R.string.font));

        backDugme.setTypeface(font);
        naslovTV.setTypeface(font);
        opisTV.setTypeface(font);
    }

    private class GetJSON extends AsyncTask<String, Void, String> {
        private String url = "";
        private String responseBody = "";

        public GetJSON(String url){
            this.url = url;
        }

        @Override
        protected String doInBackground(String... params) {
            for(int i=0;i<5;i++) {
                try {
                    HttpParams httpParams = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(httpParams, 500);
                    HttpConnectionParams.setSoTimeout(httpParams, 500);
                    HttpParams p = new BasicHttpParams();
                    p.setParameter("user", "1");
                    HttpClient httpclient = new DefaultHttpClient(p);

                    HttpPost httppost = new HttpPost(url);
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("user", "1"));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    responseBody = httpclient.execute(httppost,responseHandler); //Ako je prazan odgovor?!

                    return responseBody;
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            Log.i("-->", "" + "doInBackgroundENDNull");
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jObject = new JSONObject(responseBody); //ako je null odgovor?!
                final String naslov =  jObject.getString("naslov");
                final String opis =  jObject.getString("opis");
                final String audio =  jObject.getString("audio");
                JSONArray slike = jObject.getJSONArray("slike");

                runOnUiThread(new Runnable(){
                    public void run() {
                        mediaPlayer = MediaPlayer.create(Prikaz.this, Uri.parse(baseURL + audio));
                        TextView naslovTV = (TextView) findViewById(R.id.naslovID);
                        TextView opisTV = (TextView) findViewById(R.id.txtID);
                        opisTV.setText(opis);
                        naslovTV.setText(naslov);
                    }

                });

                if(slike.length() > 0) {
                    for (int i = 0; i < slike.length(); i++) {
                        JSONObject e = slike.getJSONObject(i);
                        String s = e.getString("slika");
                        listaPutanjaSlika.add(s);
                    }
                    Picasso.with(Prikaz.this).load(listaPutanjaSlika.get(0)).into(t);

                }else {
                    ViewGroup layout = (ViewGroup) galerija.getParent();
                    if (layout != null) layout.removeView(galerija);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    } //JSON

    public static void ImageViewAnimatedChange(Context c, final ImageView v, final Bitmap new_image) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
        final Animation anim_in  = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
        anim_out.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation)
            {
                v.setImageBitmap(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {}
                    @Override public void onAnimationRepeat(Animation animation) {}
                    @Override public void onAnimationEnd(Animation animation) {}
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    } //Animacija slika

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
        }
    }

    private void setPlayDugme(boolean play){
        ImageButton playDugme = (ImageButton) findViewById(R.id.playID);
        if(play) playDugme.setImageResource(R.drawable.play);
        else playDugme.setImageResource(R.drawable.pause);
    }

}


