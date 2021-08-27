package com.example.mytestapplication.source;

import java.util.List;

public class BeanResponseAllCity extends BeanResponseBase {
    private List<CityBean> data;

    public List<CityBean> getData() {
        return data;
    }

    public class CityBean{
        private long cityID;
        private String city;

        public long getCityID() {
            return cityID;
        }

        public String getCity() {
            return city;
        }
    }
}
