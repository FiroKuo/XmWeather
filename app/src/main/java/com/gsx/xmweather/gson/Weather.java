package com.gsx.xmweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Administrator
 * @version $Rev
 * @time 2017/1/25 9:45
 * @des ${TODO}
 * @updateAuthor $Author
 * @updateDate $Date
 * @updateDes ${TODO}
 */

public class Weather {

    public String status;
    public Basic basic;
    public AQI aqi;
    public Now now;
    public Suggestion suggestion;
    @SerializedName("daily_forecast")
    public List<Forecast> forecasts;
}
