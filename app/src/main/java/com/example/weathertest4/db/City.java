package com.example.weathertest4.db;

import org.litepal.crud.DataSupport;

/**
 * Created by yf on 2017/3/29.
 */

public class City extends DataSupport {
    private String cityCode;
    private String cityName;
    public String provinceCode;

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public String getCityName() {
        return cityName;
    }
}
