package com.gsx.xmweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

public class SettingActivity extends AppCompatActivity {

    private RelativeLayout rl_city_manager;
    private RelativeLayout rl_auto_ref;
    private CheckBox mCheckBox;
    private Button bt_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT>=21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.parseColor("#3F51B5"));
        }
        setContentView(R.layout.activity_setting);

        rl_auto_ref= (RelativeLayout) findViewById(R.id.rl_auto_refresh);
        rl_city_manager= (RelativeLayout) findViewById(R.id.rl_city_manager);
        mCheckBox= (CheckBox) findViewById(R.id.checkbox);
        bt_back= (Button) findViewById(R.id.bt_back);

        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isChecked = sp.getBoolean("isChecked", false);
        mCheckBox.setChecked(isChecked);
        rl_auto_ref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = mCheckBox.isChecked();
                mCheckBox.setChecked(!checked);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("isChecked",!checked);
                editor.apply();
            }
        });
        rl_city_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(SettingActivity.this,CityManagerActivity.class);
                startActivity(intent);
                finish();
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
