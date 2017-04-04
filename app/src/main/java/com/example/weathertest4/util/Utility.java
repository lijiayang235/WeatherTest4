package com.example.weathertest4.util;

import com.example.weathertest4.db.City;
import com.example.weathertest4.db.County;
import com.example.weathertest4.db.Province;
import com.example.weathertest4.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yf on 2017/3/29.
 */

public class Utility {
    public static boolean handProvince(String reponseText){
        try {
            JSONArray jsonArray=new JSONArray(reponseText);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                Province province=new Province();
                province.setProvinceCode(jsonObject.getString("id"));
                province.setProvinceName(jsonObject.getString("name"));
                province.save();
            }
            return true;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean handCity(String reponseText,String provinceCode){
        try {
            JSONArray jsonArray=new JSONArray(reponseText);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                City city=new City();
                city.setCityCode(jsonObject.getString("id"));
                city.setCityName(jsonObject.getString("name"));
                city.setProvinceCode(provinceCode);
                city.save();
            }
            return true;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean handCouty(String reponseText,String cityCode){
        try {
            JSONArray jsonArray=new JSONArray(reponseText);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                County county=new County();
                county.setCountyCode(jsonObject.getString("id"));
                county.setCountyName(jsonObject.getString("name"));
                county.setWeatherId(jsonObject.getString("weather_id"));
                county.setCityCode(cityCode);
                county.save();
            }
            return true;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static Weather handWeather(String responsText)  {
        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(responsText);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather");
            JSONObject jsonObject1=jsonArray.getJSONObject(0);
            return new Gson().fromJson(jsonObject1.toString(),Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
     return  null;

    }
}
