package com.example.weathertest4.db;

import org.litepal.crud.DataSupport;

/**
 * Created by yf on 2017/3/29.
 */

public class Province extends DataSupport {
    private String provinceCode;
    private String provinceName;

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }
}
