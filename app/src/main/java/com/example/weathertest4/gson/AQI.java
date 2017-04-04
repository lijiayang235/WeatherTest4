package com.example.weathertest4.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yf on 2017/4/4.
 */

public class AQI {
    @SerializedName("city")
    public AQICity city;
    public class AQICity{
        public String aqi;
        public String pm25;
        @SerializedName("qlty")
        public String qulity;
    }
}
