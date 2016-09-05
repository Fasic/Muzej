package com.fasic.fasic.muzej;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;


public class info extends Activity {
    protected String jezik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        jezik = intent.getStringExtra("jezik");
        setJezik(jezik);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        setFont();


        ImageView logo = (ImageView) findViewById(R.id.logoID);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

    private void setFont(){
        Typeface font = Typeface.createFromAsset(getAssets(),  getResources().getString(R.string.font));
        font.isBold();

        TextView head = (TextView) findViewById(R.id.headerID);
        TextView muzej = (TextView) findViewById(R.id.oMuzejuID);
        TextView risovaca = (TextView) findViewById(R.id.oRisovaci);

        head.setTypeface(font);
        muzej.setTypeface(font);
        risovaca.setTypeface(font);
    }
}
