package com.gsx.xmweather.gson;

/**
 * @author Administrator
 * @version $Rev
 * @time 2017/1/25 9:31
 * @des ${TODO}
 * @updateAuthor $Author
 * @updateDate $Date
 * @updateDes ${TODO}
 */

public class AQI {

    public AQICity city;
    public class AQICity{
        public String aqi;
        public String pm25;
    }
}
