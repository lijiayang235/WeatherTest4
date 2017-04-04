package com.example.weathertest4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.weathertest4.gson.Forecast;
import com.example.weathertest4.gson.Weather;
import com.example.weathertest4.util.HttpSendUrl;
import com.example.weathertest4.util.Utility;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActicity extends AppCompatActivity {
    public TextView titleCity;
    public TextView titleUpdate;
    public TextView nowTemp;
    public TextView nowInfo;
    public ScrollView weatherLayout;
    public TextView aqi;
    public TextView pm25;
    public TextView comfort;
    public TextView carwash;
    public TextView sport;
    public LinearLayout forecastLayout;
    public ImageView bing_Pic;
    public String weatherId;
    public SwipeRefreshLayout swipLayout;
    public Button navButton;
    public DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view=getWindow().getDecorView();
        if(Build.VERSION.SDK_INT>=21){
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_weather);

        titleCity= (TextView) findViewById(R.id.titleCity);
        titleUpdate= (TextView) findViewById(R.id.titleUpdateTime);
        nowInfo= (TextView) findViewById(R.id.nowInfo);
        nowTemp= (TextView) findViewById(R.id.nowTemp);
        aqi= (TextView) findViewById(R.id.aqi);
        pm25= (TextView) findViewById(R.id.pm25);
        comfort= (TextView) findViewById(R.id.comfort);
        carwash= (TextView) findViewById(R.id.carwash);
        sport= (TextView) findViewById(R.id.sport);
        forecastLayout= (LinearLayout) findViewById(R.id.forecastLayout);
        weatherLayout= (ScrollView) findViewById(R.id.weatherLayout);
        bing_Pic= (ImageView) findViewById(R.id.bing_pic);
        swipLayout= (SwipeRefreshLayout) findViewById(R.id.swipLayout);
        navButton= (Button) findViewById(R.id.nav_button);
        swipLayout.setColorSchemeResources(R.color.colorPrimary);
        drawerLayout= (DrawerLayout) findViewById(R.id.drawerLayout);

        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
        String responseText=preferences.getString("weather",null);


        if(responseText!=null){
            Weather weather= Utility.handWeather(responseText);
            weatherLayout.setVisibility(View.GONE);
            showWeather(weather);
            weatherId=weather.basic.weatherId;
        }else{
          Intent intent=getIntent();
            weatherId=intent.getStringExtra("weatherId");
            requestWeather(weatherId);
        }
        String bing_picText=preferences.getString("bing_pic",null);
        if(bing_picText!=null){
            Glide.with(WeatherActicity.this).load(bing_picText).into(bing_Pic);
        }else {
            requestBinPic();
        }
        swipLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipLayout.setRefreshing(true);
                requestWeather(weatherId);
            }
        });

        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void requestBinPic() {
        String address="http://guolin.tech/api/bing_pic";
        HttpSendUrl.sendOkhttp3(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPicText=response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActicity.this).edit();
                        editor.putString("bing_pic",bingPicText);
                        editor.apply();
                        Glide.with(WeatherActicity.this).load(bingPicText).into(bing_Pic);
                    }
                });

            }
        });
    }


    public void requestWeather(String mweatherId) {
        String address="http://guolin.tech/api/weather?cityid="+mweatherId+"&key=19dbb264703e45ff96e4373c0a67268b";
        HttpSendUrl.sendOkhttp3(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipLayout.setRefreshing(false);
                        Toast.makeText(WeatherActicity.this,"get weather failed",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                    final String reponseText=response.body().string();
                final Weather weather=Utility.handWeather(reponseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipLayout.setRefreshing(false);
                        if(!TextUtils.isEmpty(reponseText)&&"ok".equals(weather.status)){
                            Intent intent=new Intent(WeatherActicity.this,AutoUpdateService.class);
                       startService(intent);
                            SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActicity.this).edit();
                            editor.putString("weather",reponseText);
                            editor.apply();

                            weatherId=weather.basic.weatherId;
                            showWeather(weather);
                        }
                    }
                });

            }
        });
    }

    private void showWeather(Weather weather) {
        titleCity.setText(weather.basic.city);
        titleUpdate.setText(weather.basic.update.updateTime);
        nowInfo.setText(weather.now.more.info);
        nowTemp.setText(weather.now.temperature+"℃");
        forecastLayout.removeAllViews();
        for(Forecast forecast:weather.forecastList){
            View view= LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView forecastDate= (TextView) view.findViewById(R.id.forecastDate);
            TextView forecastInfo= (TextView) view.findViewById(R.id.forecastInfo);
            TextView forecatMin= (TextView) view.findViewById(R.id.forecatMin);
            TextView forecatMax= (TextView) view.findViewById(R.id.forecatMax);
            forecastDate.setText(forecast.forecastDate);
            forecastInfo.setText(forecast.more.forecastInfo);
            forecatMin.setText(forecast.temperature.min);
            forecatMax.setText(forecast.temperature.max);
            forecastLayout.addView(view);
        }


        if(weather.aqi!=null){
        aqi.setText(weather.aqi.city.aqi);
        pm25.setText(weather.aqi.city.pm25);}
        comfort.setText("舒适指数:"+weather.suggestion.comfort.info);
        carwash.setText("洗车指数:"+weather.suggestion.carWash.info);
        sport.setText("运动指数:"+weather.suggestion.sport.info);
        weatherLayout.setVisibility(View.VISIBLE);
    }

}
