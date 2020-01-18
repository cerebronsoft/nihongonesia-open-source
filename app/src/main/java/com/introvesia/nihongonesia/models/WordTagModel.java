package com.introvesia.nihongonesia.models;

import android.database.Cursor;

import com.introvesia.nihongonesia.data.WordTag;
import com.introvesia.nihongonesia.lib.Model;

import java.util.ArrayList;

/**
 * Created by asus on 25/07/2017.
 */

public class WordTagModel extends Model {
    public static final String TABLE_NAME = "tb_kotoba_tag";
    public static final String COLUMN_TAG = "tag";
    public static final String COLUMN_NAME = "name";

    @Override
    protected String tableName() {
        return TABLE_NAME;
    }

    public ArrayList<WordTag> getAllTag() {
        String criteria = "1 ORDER BY tag LIMIT 1000";
        Cursor res = super.getAll(criteria);
        ArrayList<WordTag> list = new ArrayList<WordTag>();

        int i = 0;
        while (res.isAfterLast() == false) {
            WordTag item = new WordTag();
            item.setId(i);
            item.setTag(getStringValue(res, COLUMN_TAG));
            item.setName(getStringValue(res, COLUMN_NAME));
            list.add(item);
            res.moveToNext();
            i++;
        }
        return list;
    }

    public WordTag getTag(String tag) {
        Cursor res = super.getData(COLUMN_TAG, tag);
        WordTag item = new WordTag();
        if (res != null) {
            item.setTag(getStringValue(res, COLUMN_TAG));
            item.setName(getStringValue(res, COLUMN_NAME));
            return item;
        }
        return null;
    }
}
