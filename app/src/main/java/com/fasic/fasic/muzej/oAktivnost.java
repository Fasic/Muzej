package com.fasic.fasic.muzej;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Locale;


public class oAktivnost extends Activity {
    protected String jezik;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        jezik = intent.getStringExtra("jezik");//get i o cemu je...
        setJezik(jezik);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_aktivnost);

        setOmuzeju();
    }

    protected void setJezik(String jezik){
        Locale locale;
        String drzava;

        if(jezik.equals("sr")){
            Log.i("-->", "sr");
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

    protected void setOmuzeju(){
        /*RelativeLayout mainView = (RelativeLayout) findViewById(R.id.mainViewID);
        TextView header = (TextView) findViewById(R.id.headerID);

        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        TextView opis1 = new TextView(this);
        opis1.setText("Ovo ide iz stringa zbog jezika!");
        mainView.addView(opis1);

        ImageView slika1 = new ImageView(this);
        slika1.setImageDrawable(getResources().getDrawable(R.drawable.muzej));
        relativeParams.addRule(RelativeLayout.BELOW, opis1.getId());
        mainView.addView(slika1, relativeParams);

        header.setText("O muzeju");*/

        setContentView(R.layout.omuzeju);
        TextView header = (TextView) findViewById(R.id.headerID);
        header.setText("O muzeju");

        Typeface font = Typeface.createFromAsset(getAssets(),  getResources().getString(R.string.font));
        font.isBold();

        TextView opis1 = (TextView) findViewById(R.id.opis1OmuzejuID);
        opis1.setTypeface(font);

        TextView opis2 = (TextView) findViewById(R.id.opis1OmuzejuID);
        opis2.setTypeface(font);
        header.setTypeface(font);
    }
}
