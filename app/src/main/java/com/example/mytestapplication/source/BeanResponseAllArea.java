package com.example.mytestapplication.source;

import java.util.List;

public class BeanResponseAllArea extends BeanResponseBase {
    private List<AreaBean> data;

    public List<AreaBean> getData() {
        return data;
    }

    public class AreaBean{
        private long areaID;
        private String area;

        public long getAreaID() {
            return areaID;
        }

        public String getArea() {
            return area;
        }
    }
}
