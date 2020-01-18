package com.introvesia.nihongonesia.data;

import android.util.Log;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by asus on 25/07/2017.
 */

public class WordTag {
    private int id;
    private String tag;
    private String name;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
