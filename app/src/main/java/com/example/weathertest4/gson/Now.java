package com.example.weathertest4.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yf on 2017/4/4.
 */

public class Now {
    @SerializedName("cond")
    public More more;
    public class More{
        @SerializedName("txt")
        public String info;
    }
    @SerializedName("tmp")
    public String temperature;
    public Wind wind;
    public class Wind{
        @SerializedName("dir")
        public String windDir;
        @SerializedName("sc")
        public String windClass;
    }
}
