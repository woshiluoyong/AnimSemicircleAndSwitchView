package com.example.mytestapplication.source.beans;

import java.io.Serializable;

public class TaglayoutBean implements Serializable {
    String text;
    boolean isChecked;
    boolean isClickable;

    public boolean isClickable() {
        return isClickable;
    }

    public void setClickable(boolean clickable) {
        isClickable = clickable;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
