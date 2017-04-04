package com.example.weathertest4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
        String reponseText=preferences.getString("weather",null);
        if(reponseText!=null){
            Intent intent=new Intent(this,WeatherActicity.class);
            startActivity(intent);
            finish();
        }
    }
}
