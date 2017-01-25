package com.gsx.xmweather.util;

import com.gsx.xmweather.db.City;
import com.gsx.xmweather.db.County;
import com.gsx.xmweather.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Administrator
 * @version $Rev
 * @time 2017/1/24 16:02
 * @des ${TODO}
 * @updateAuthor $Author
 * @updateDate $Date
 * @updateDes ${TODO}
 */

public class Utility {

    public static boolean handleProvinceResponse(String data){

        if (!data.isEmpty()) {
            try {
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {

                    Province province = new Province();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    province.setProvinceName(jsonObject.getString("name"));
                    province.setProvinceCode(jsonObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleCityResponse(String data,int provinceId){

        if (!data.isEmpty()){

            try {
                JSONArray jsonArray=new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    City city=new City();
                    city.setCityName(jsonObject.getString("name"));
                    city.setCityCode(jsonObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    public static boolean handleCountyResponse(String data,int cityId){

        if (!data.isEmpty()) {
            try {
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    County county=new County();
                    county.setCountyName(jsonObject.getString("name"));
                    county.setWeatherId(jsonObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
