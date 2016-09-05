package com.fasic.fasic.muzej;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;


public class MainActivity extends Activity {
    Button srb;
    Button eng;
    Button qr;
    Button info;
    String jezik = "sr";
    public static final String PREFS_NAME = "Fasic";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        jezik = settings.getString("jezik", "sr");

        srb = (Button) findViewById(R.id.srbija);
        eng = (Button) findViewById(R.id.engleska);


        qr =  (Button) findViewById(R.id.qrButton);
        info =  (Button) findViewById(R.id.infoButton);


        if(jezik.equals("en")){
            setEng();
        }
        else{
            setSrb();
        }

        setFont();

        srb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setSrb();
            }
        });

        eng.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setEng();
            }
        });

        qr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                intent.putExtra("jezik", jezik);
                startActivityForResult(intent, 0);
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, info.class);
                intent.putExtra("jezik", jezik);
                startActivityForResult(intent, 0);
            }
        });
    }

    protected void onStop(){
        super.onStop();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("jezik", jezik);
        editor.commit();
    }

    private void setSrb(){
        srb.setBackground(getResources().getDrawable(R.drawable.srboja));
        eng.setBackground(getResources().getDrawable(R.drawable.enbezboje));

        jezik = "sr";
    }

    private void setEng(){
        srb.setBackground(getResources().getDrawable(R.drawable.srbezboje));
        eng.setBackground(getResources().getDrawable(R.drawable.enboja));
        jezik = "en";
    }

    private void setFont(){
        Typeface font = Typeface.createFromAsset(getAssets(),  getResources().getString(R.string.font));
        font.isBold();

        info.setTypeface(font);
        qr.setTypeface(font);
    }

}
