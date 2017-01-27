package com.gsx.xmweather;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.gsx.xmweather.util.ListDataIO;
import com.gsx.xmweather.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class CityManagerActivity extends AppCompatActivity {

    private static final String TAG = "CityManagerActivity";
    public static List<String> cities = new ArrayList<>();
    private ListView lv_city;
    private ImageButton ib_add;
    private TextView tv_title_name;
    private Button bt_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manager);

        lv_city = (ListView) findViewById(R.id.lv_city);
        ib_add = (ImageButton) findViewById(R.id.ib_add_city);
        bt_back = (Button) findViewById(R.id.bt_back);
        tv_title_name = (TextView) findViewById(R.id.tv_title_name);

        tv_title_name.setText("所有城市");
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ib_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CityManagerActivity.this, AddCityActivity.class);
                startActivity(intent);
            }
        });


        String data = ListDataIO.readData(this);
        LogUtil.e(TAG,"Filedata:"+data);
        if (data != null) {
            cities.clear();
            String[] spData = data.split(",");
            for (int i = 0; i < spData.length; i++) {
                cities.add(spData[i].trim());
            }
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cities);
        lv_city.setAdapter(adapter);

        lv_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cityName = cities.get(position);
                String weatherId = PreferenceManager.getDefaultSharedPreferences(CityManagerActivity.this).getString(cityName, null);
                if (weatherId != null) {
                    Intent intent = new Intent(CityManagerActivity.this, WeatherActivity.class);
                    intent.putExtra("weather_id", weatherId);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
