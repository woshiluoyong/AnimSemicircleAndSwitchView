package com.example.mytestapplication.source;

import java.util.List;

public class EntityWeiXinUserInfo {
    private String openid;
    private String nickname;
    private int sex;
    private String province;
    private String city;
    private String country;
    private String headimgurl;
    private List<Privilege> privilege ;
    private String unionid;

    public String getOpenid() {
        return openid;
    }

    public String getNickname() {
        return nickname;
    }

    public int getSex() {
        return sex;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public List<Privilege> getPrivilege() {
        return privilege;
    }

    public String getUnionid() {
        return unionid;
    }

    public class Privilege {
        private String privilege;

        public String getPrivilege() {
            return privilege;
        }
    }
}