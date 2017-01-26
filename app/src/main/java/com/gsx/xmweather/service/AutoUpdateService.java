package com.gsx.xmweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.gsx.xmweather.gson.Weather;
import com.gsx.xmweather.util.HttpUtil;
import com.gsx.xmweather.util.Utility;

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
        updateWeather();
        updatePic();
        AlarmManager manager= (AlarmManager) getSystemService(ALARM_SERVICE);
        long time=SystemClock.elapsedRealtime()+60*60*1000*8;
        Intent i=new Intent(this,AutoUpdateService.class);
        PendingIntent pi=PendingIntent.getService(this,0,i,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME,time,pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updatePic() {
        String url="http://guolin.tech/api/bing_pic";
        HttpUtil.sendRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String picStr = response.body().string();
                if (picStr!=null){
                    SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                    edit.putString("background",picStr);
                    edit.apply();
                }
            }
        });
    }

    private void updateWeather() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherStr = sp.getString("weather", null);
        if (weatherStr!=null){
            Weather weather = Utility.handleWeatherResponse(weatherStr);
            String weatherId = weather.basic.weatherId;
            String url="http://guolin.tech/api/weather?cityid="+weatherId+"&key=81694a31cc38414aa2cf3e8926ad8523";
            HttpUtil.sendRequest(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    String responseStr = response.body().string();
                    Weather wh = Utility.handleWeatherResponse(responseStr);
                    if (wh!=null&&"ok".equals(wh.status)){

                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                        editor.putString("weather",responseStr);
                        editor.apply();
                    }

                }
            });
        }
    }
}
