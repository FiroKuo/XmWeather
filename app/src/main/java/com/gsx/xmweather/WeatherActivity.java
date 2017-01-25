package com.gsx.xmweather;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gsx.xmweather.gson.Forecast;
import com.gsx.xmweather.gson.Weather;
import com.gsx.xmweather.util.HttpUtil;
import com.gsx.xmweather.util.LogUtil;
import com.gsx.xmweather.util.Utility;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private static final String TAG = "WeatherActivity";
    private TextView tv_city,tv_update_time,tv_tmp,tv_weather_info,
    tv_aqi,tv_pm25,tv_comfrot,tv_wash,tv_sport;
    private LinearLayout ll_forecast;
    private ScrollView sv_weather;
    private ImageView iv_background;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT>=21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.layout_weather);
        tv_city= (TextView) findViewById(R.id.tv_city_name);
        tv_update_time= (TextView) findViewById(R.id.tv_update_time);
        tv_tmp= (TextView) findViewById(R.id.tv_tmp);
        tv_weather_info= (TextView) findViewById(R.id.tv_weather_info);
        ll_forecast= (LinearLayout) findViewById(R.id.ll_forecast);
        tv_aqi= (TextView) findViewById(R.id.tv_aqi);
        tv_pm25= (TextView) findViewById(R.id.tv_pm25);
        tv_comfrot= (TextView) findViewById(R.id.tv_comfort);
        tv_wash= (TextView) findViewById(R.id.tv_wash);
        tv_sport= (TextView) findViewById(R.id.tv_sport);
        sv_weather= (ScrollView) findViewById(R.id.sv_weather);
        iv_background= (ImageView) findViewById(R.id.iv_background);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherStr = sp.getString("weather", null);
        if (weatherStr!=null){
            Weather weather = Utility.handleWeatherResponse(weatherStr);
            parseWeather(weather);
        }else{
            String weather_id = getIntent().getStringExtra("weather_id");
            requestServer(weather_id);
            sv_weather.setVisibility(View.INVISIBLE);
        }
        String background = sp.getString("background", null);
        if (background!=null){
            Glide.with(this).load(background).into(iv_background);
        }else{
            loadBackgroundPic();
        }
    }

    private void loadBackgroundPic() {

        String url="http://guolin.tech/api/bing_pic";
        HttpUtil.sendRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String picStr = response.body().string();
                if (picStr!=null){
                    SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                    edit.putString("background",picStr);
                    edit.apply();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(WeatherActivity.this).load(picStr).into(iv_background);
                        }
                    });
                }
            }
        });
    }

    private void requestServer(String weather_id) {

        String url="http://guolin.tech/api/weather?cityid="+weather_id+"&key=81694a31cc38414aa2cf3e8926ad8523";
        HttpUtil.sendRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取数据失败!!",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String responseStr = response.body().string();
                LogUtil.e(TAG,"response:"+responseStr);
                final Weather weather = Utility.handleWeatherResponse(responseStr);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather!=null&&"ok".equals(weather.status)){
                            SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            edit.putString("weather",responseStr);
                            edit.apply();
                            parseWeather(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this,"获取数据失败!!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        loadBackgroundPic();
    }

    /**
     * 加载缓存中的weather信息
     * @param weather Gson自动封装的对象
     */
    private void parseWeather(Weather weather) {

        tv_city.setText(weather.basic.cityName);
        tv_update_time.setText(weather.basic.update.updateTime);
        tv_tmp.setText(weather.now.temperature+"°");
        tv_weather_info.setText(weather.now.more.info);
        List<Forecast> forecasts = weather.forecasts;
        ll_forecast.removeAllViews();
        for (Forecast forecast:forecasts) {
            View view = LayoutInflater.from(this).inflate(R.layout.layout_forecast_item, ll_forecast,false);
            TextView tv_info= (TextView) view.findViewById(R.id.tv_info);
            TextView tv_date= (TextView) view.findViewById(R.id.tv_date);
            TextView tv_max= (TextView) view.findViewById(R.id.tv_max);
            TextView tv_min= (TextView) view.findViewById(R.id.tv_min);
            tv_date.setText(forecast.date);
            tv_info.setText(forecast.more.info);
            tv_max.setText(forecast.temperature.max+"℃");
            tv_min.setText(forecast.temperature.min+"℃");
            ll_forecast.addView(view);
        }
        tv_aqi.setText(weather.aqi.city.aqi);
        tv_pm25.setText(weather.aqi.city.pm25);
        tv_comfrot.setText("舒适度: "+weather.suggestion.comfort.info);
        tv_wash.setText("洗车建议: "+weather.suggestion.carWash.info);
        tv_sport.setText("运动建议: "+weather.suggestion.sport.info);
        sv_weather.setVisibility(View.VISIBLE);
    }
}
