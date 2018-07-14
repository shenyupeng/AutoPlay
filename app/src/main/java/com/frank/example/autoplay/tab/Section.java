package com.frank.example.autoplay.tab;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by frank on 2018/7/13.
 */

public class Section {
    private String title;
    private List<ColorItem> colorItems;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ColorItem> getColorItems() {
        if (colorItems == null) {
            colorItems = new ArrayList<>();
        }
        return colorItems;
    }

    public void setColorItems(List<ColorItem> colorItems) {
        this.colorItems = colorItems;
    }
}
