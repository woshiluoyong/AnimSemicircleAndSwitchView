package com.example.mytestapplication.source;

import java.util.List;

public class BeanResponseAllProvince extends BeanResponseBase {
    private List<ProvinceBean> data;

    public List<ProvinceBean> getData() {
        return data;
    }

    public class ProvinceBean{
        private long provinceID;
        private String province;

        public ProvinceBean(long provinceID, String province) {
            this.provinceID = provinceID;
            this.province = province;
        }

        public long getProvinceID() {
            return provinceID;
        }

        public String getProvince() {
            return province;
        }
    }
}
