package com.aigoshin.weather.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponseModel {

    @JsonProperty("now")
    private String now;

    @JsonProperty("now_dt")
    private String nowDt;

    @JsonProperty("fact")
    private FactObjectModel factObjectModel;

    public String getNow() {
        return now;
    }

    public void setNow(String now) {
        this.now = now;
    }

    public String getNowDt() {
        return nowDt;
    }

    public void setNowDt(String nowDt) {
        this.nowDt = nowDt;
    }

    public FactObjectModel getFactObjectModel() {
        return factObjectModel;
    }

    public void setFactObjectModel(FactObjectModel factObjectModel) {
        this.factObjectModel = factObjectModel;
    }
}
