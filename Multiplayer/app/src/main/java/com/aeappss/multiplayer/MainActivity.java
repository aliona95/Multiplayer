package com.aeappss.multiplayer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static int SPLASH_TIME_OUT = 2500;
    Button button;
    Intent mainIntent;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_splash_screen);
        Typeface myFont = Typeface.createFromAsset(getAssets(), "fonts/rocko.ttf");

        text = (TextView) findViewById(R.id.textView2);
        text.setTypeface(myFont);

        /*button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("AAA", "AAA");
                mainIntent = new Intent(getApplicationContext(), MainActivity1.class);
                startActivity(mainIntent);
                finish();
            }
        });*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mainIntent = new Intent(getApplicationContext(), MainActivity1.class);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
