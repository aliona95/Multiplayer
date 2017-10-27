package com.aeappss.multiplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    private static int SPLASH_TIME_OUT = 2500;
    Button button;
    Intent mainIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
