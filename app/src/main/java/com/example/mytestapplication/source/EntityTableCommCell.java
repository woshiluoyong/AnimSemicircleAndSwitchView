package com.example.mytestapplication.source;
import android.graphics.Color;

/**
 * Created by Administrator on 2016-12-05.
 */
public class EntityTableCommCell {
    private String str1 = "",str2 = "";
    private int showColorResId;

    public EntityTableCommCell() {
        this.str1 = "";
        this.str2 = "";
        this.showColorResId = Color.BLACK;
    }

    public EntityTableCommCell(String str1) {
        this.str1 = str1;
        this.showColorResId = Color.BLACK;
    }

    public EntityTableCommCell(String str1, String str2) {
        this.str1 = str1;
        this.str2 = str2;
        this.showColorResId = Color.BLACK;
    }

    public String getShowStr() {
        return str1+"\n"+str2;
    }

    public String getStr1() {
        return str1;
    }

    public String getStr2() {
        return str2;
    }

    public int getShowColorResId() {
        return showColorResId;
    }

    public void setShowColorResId(int showColorResId) {
        this.showColorResId = showColorResId;
    }
}
