package com.example.mytestapplication.source;

import java.io.Serializable;

public class BeanResponseBase implements Serializable{
    private int status;
    private String info;

    public int getStatus() {
        return status;
    }

    public String getInfo() {
        return info;
    }
}
