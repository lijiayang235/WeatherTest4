package com.example.weathertest4;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Bmob.initialize(this,"dd9cb0fddcdd80936bb70c0b305104a1");
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
        String responseText=preferences.getString("weather",null);
        if(responseText!=null){
            Intent intent=new Intent(this,WeatherActicity.class);
            startActivity(intent);
            finish();
        }




    }

}
