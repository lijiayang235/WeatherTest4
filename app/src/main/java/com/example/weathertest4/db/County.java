package com.example.weathertest4.db;

import org.litepal.crud.DataSupport;

/**
 * Created by yf on 2017/3/29.
 */

public class County extends DataSupport{
    private String countyCode;
    private String countyName;
    private String weatherId;
    private String cityCode;

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public String getCountyName() {
        return countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }
}
