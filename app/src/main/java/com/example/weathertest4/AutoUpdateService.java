
package com.example.weathertest4;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.weathertest4.gson.Weather;
import com.example.weathertest4.util.HttpSendUrl;
import com.example.weathertest4.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
      return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("myTest","onStartCommand");
        updateBinPic();
        updateWeather();
        AlarmManager manager= (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i=new Intent(this,AutoUpdateService.class);
        PendingIntent pi=PendingIntent.getService(this,0,i,0);
        manager.cancel(pi);
        long hours=8*60*60*1000;
        long triggertime= SystemClock.elapsedRealtime()+hours;
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggertime,pi);



        return super.onStartCommand(intent, flags, startId);
    }

    private void updateBinPic() {
        String address="http://guolin.tech/api/bing_pic";
        HttpSendUrl.sendOkhttp3(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPic=response.body().string();
                SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
            }
        });
    }

    private void updateWeather() {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
        final String reponseText=pref.getString("weather",null);
        if(reponseText!=null){
            Weather weahter= Utility.handWeather(reponseText);
            String weatherId=weahter.basic.weatherId;
            String address="http://guolin.tech/api/weather?cityid="+weatherId+"&key=19dbb264703e45ff96e4373c0a67268b";
            HttpSendUrl.sendOkhttp3(address, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                   e.printStackTrace();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText=response.body().string();
                    SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                    editor.putString("weather",reponseText);
                    editor.apply();
                }
            });
        }
    }
}
