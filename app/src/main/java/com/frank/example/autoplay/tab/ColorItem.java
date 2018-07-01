package com.frank.example.autoplay.tab;

import android.support.annotation.ColorInt;

/**
 * Created by frank on 2018/7/1.
 */

public class ColorItem {


    @ColorInt
    private int color1;

    @ColorInt
    private int color2;

    private CharSequence text;


    public int getColor1() {
        return color1;
    }

    public void setColor1(@ColorInt int color1) {
        this.color1 = color1;
    }

    public int getColor2() {
        return color2;
    }

    public void setColor2(@ColorInt int color2) {
        this.color2 = color2;
    }

    public CharSequence getText() {
        return text;
    }

    public void setText(CharSequence text) {
        this.text = text;
    }
}
