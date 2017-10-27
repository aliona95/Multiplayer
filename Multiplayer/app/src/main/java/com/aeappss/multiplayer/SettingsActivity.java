package com.aeappss.multiplayer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;


public class SettingsActivity extends AppCompatActivity {
    ImageButton buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //Log.i("TAG", "A0");
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        //Log.i("TAG", "A1");
        android.app.Fragment fragment = new SettingsScreen();
        android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if(savedInstanceState == null){
            Log.i("TAG", "A2");
            fragmentTransaction.add(R.id.relative_layout, fragment, "settings_fragment");
            fragmentTransaction.commit();

           /* SharedPreferences  preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String username = preferences.getString("editTextField", "default_value");
            Log.i("TAG is activity", username);*/
        }else{
            Log.i("TAG", "A3");
            fragment = getFragmentManager().findFragmentByTag("settings_fragment");
        }

        buttonBack = (ImageButton) findViewById(R.id.imageButton7);
        View.OnClickListener onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // gaunamas atstumas

              /*  userName = preferences.getString("editTextField", "default_value");
                HomeActivity.PROXIMITY_RADIUS = Integer.parseInt(userName);
                Log.i("TAG BACK", userName);*/
                switch (view.getId()) {
                    case R.id.imageButton7:
                        // grizimas atgal i pradini langa
                        //Log.i("BACK", userName);
                        Intent settingsIntent = new Intent(getApplicationContext(), MainActivity1.class);
                        startActivity(settingsIntent);
                        finish();
                        break;
                }
            }
        };
        buttonBack.setOnClickListener(onClickListener);
    }

    public static  class SettingsScreen extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // nustatymuose langai atstumas ir shake
            addPreferencesFromResource(R.xml.settings_screen);
            addPreferencesFromResource(R.xml.settings_screen_shake);

            Log.i("TAG", "A1");

        }

    }

}
