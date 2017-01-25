package com.gsx.xmweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * @author Administrator
 * @version $Rev
 * @time 2017/1/25 9:29
 * @des ${TODO}
 * @updateAuthor $Author
 * @updateDate $Date
 * @updateDes ${TODO}
 */

public class Basic {

    @SerializedName("city")
    public String cityName;
    @SerializedName("id")
    public String weatherId;
    public Update update;
    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }
}
