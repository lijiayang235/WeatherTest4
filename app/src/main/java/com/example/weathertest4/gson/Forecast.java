package com.example.weathertest4.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yf on 2017/4/4.
 */

public class Forecast {
    @SerializedName("cond")
    public More more;
    public class More{
        @SerializedName("txt_d")
        public String forecastInfo;
    }
    @SerializedName("date")
    public String forecastDate;
    @SerializedName("tmp")
    public Temperature temperature;
    public class Temperature{
        public String min;
        public String max;
    }
}
