package com.introvesia.nihongonesia.models;

import android.database.Cursor;

import com.introvesia.nihongonesia.data.Word;
import com.introvesia.nihongonesia.lib.JapaneseCharacter;
import com.introvesia.nihongonesia.lib.JapaneseUtils;
import com.introvesia.nihongonesia.lib.Model;

import java.util.ArrayList;

/**
 * Created by asus on 01/07/2017.
 */

public class WordModel extends Model {
    public static final String TABLE_NAME = "tb_kotoba";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_KANJI = "kanji";
    public static final String COLUMN_KANA = "kana";
    public static final String COLUMN_TAG = "tag";
    public static final String COLUMN_ROMAJI = "romaji";
    public static final String COLUMN_MEANING = "meaning";

    @Override
    protected String tableName() {
        return TABLE_NAME;
    }

    public ArrayList<Word> getAllKotoba(String keyword) {
        String criteria = "kanji LIKE '%" + keyword + "%' "+
                "ORDER BY romaji LIMIT 1000";
        Cursor res = super.getAll(criteria);
        ArrayList<Word> list = new ArrayList<Word>();

        while (res.isAfterLast() == false) {
            Word item = new Word();
            item.setId(getIntegerValue(res, COLUMN_ID));
            item.setKanji(getStringValue(res, COLUMN_KANJI));
            item.setKana(getStringValue(res, COLUMN_KANA));
            item.setRomaji(getStringValue(res, COLUMN_ROMAJI));
            item.setMeaning(capitalizeString(getStringValue(res, COLUMN_MEANING)));
            list.add(item);
            res.moveToNext();
        }
        return list;
    }

    public ArrayList<Word> getAllKotobaByYomikata(String kanji, String yomikata) {
        char firstChar = yomikata.charAt(0);
        if (JapaneseCharacter.isKatakana(firstChar)) {
            yomikata = JapaneseUtils.convertKana(yomikata);
        }
        String criteria = "kanji LIKE '%" + kanji + "%' AND kana LIKE '%" + yomikata + "%' "+
                "ORDER BY romaji LIMIT 2";
        Cursor res = super.getAll(criteria);
        ArrayList<Word> list = new ArrayList<Word>();

        while (res.isAfterLast() == false) {
            Word item = new Word();
            item.setId(getIntegerValue(res, COLUMN_ID));
            item.setKanji(getStringValue(res, COLUMN_KANJI));
            item.setKana(getStringValue(res, COLUMN_KANA));
            item.setRomaji(getStringValue(res, COLUMN_ROMAJI));
            item.setMeaning(capitalizeString(getStringValue(res, COLUMN_MEANING)));
            list.add(item);
            res.moveToNext();
        }
        return list;
    }

    public ArrayList<Word> findKotoba(String keyword) {
        String criteria;
        if (keyword != "" && keyword.charAt(0) == '+') {
            keyword = keyword.replace("+", "");
            criteria = "tag LIKE '%" + keyword + "%' ";
        } else {
            criteria = "romaji LIKE '%" + keyword + "%' OR meaning LIKE '%" + keyword + "%' ";
        }
        criteria += "ORDER BY romaji LIMIT 1000";
        Cursor res = super.getAll(criteria);
        ArrayList<Word> list = new ArrayList<Word>();

        while (res.isAfterLast() == false) {
            Word item = new Word();
            item.setId(getIntegerValue(res, COLUMN_ID));
            item.setKanji(getStringValue(res, COLUMN_KANJI));
            item.setKana(getStringValue(res, COLUMN_KANA));
            item.setRomaji(getStringValue(res, COLUMN_ROMAJI));
            item.setMeaning(capitalizeString(getStringValue(res, COLUMN_MEANING)));
            list.add(item);
            res.moveToNext();
        }
        return list;
    }

    private String capitalizeString(String str)
    {
        String[] strArray = str.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String s : strArray) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            builder.append(cap + " ");
        }
        return builder.toString();
    }

    public Word getKotoba(String id) {
        Cursor res = super.getData(COLUMN_ID, id);
        Word item = new Word();
        item.setId(getIntegerValue(res, COLUMN_ID));
        item.setKanji(getStringValue(res, COLUMN_KANJI));
        item.setRomaji(getStringValue(res, COLUMN_ROMAJI));
        item.setTag(getStringValue(res, COLUMN_TAG));
        item.setMeaning(getStringValue(res, COLUMN_MEANING));
        return item;
    }

    public Word getKotobaByKanji(String kanji) {
        Cursor res = super.getData(COLUMN_KANJI, kanji);
        if (res != null) {
            Word item = new Word();
            item.setId(getIntegerValue(res, COLUMN_ID));
            item.setKanji(getStringValue(res, COLUMN_KANJI));
            item.setKana(getStringValue(res, COLUMN_KANA));
            item.setRomaji(getStringValue(res, COLUMN_ROMAJI));
            item.setTag(getStringValue(res, COLUMN_TAG));
            item.setMeaning(getStringValue(res, COLUMN_MEANING));
            return item;
        }
        return null;
    }

    public Word getRandomKotoba(String kanji, String yomikata) {
        yomikata = yomikata.replace(".", "");
        if (JapaneseUtils.isKatakana(yomikata))
            yomikata = JapaneseUtils.convertKana(yomikata);
        String criteria = "kanji LIKE '%" + kanji + "%' AND kana LIKE '%" + yomikata + "%'";
        Cursor res = super.getData(criteria + " ORDER BY RANDOM() LIMIT 1");
        Word item = new Word();
        if (res != null) {
            item.setId(getIntegerValue(res, COLUMN_ID));
            item.setKanji(getStringValue(res, COLUMN_KANJI));
            item.setKana(getStringValue(res, COLUMN_KANA));
            item.setRomaji(getStringValue(res, COLUMN_ROMAJI));
            item.setTag(getStringValue(res, COLUMN_TAG));
            item.setMeaning(getStringValue(res, COLUMN_MEANING));
            return item;
        }
        return null;
    }
}
