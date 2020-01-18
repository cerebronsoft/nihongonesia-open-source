package com.introvesia.nihongonesia.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.introvesia.nihongonesia.data.Kanji;
import com.introvesia.nihongonesia.data.PracticeKanji;
import com.introvesia.nihongonesia.lib.JapaneseUtils;
import com.introvesia.nihongonesia.lib.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by asus on 27/07/2017.
 */

public class PracticeKanjiModel extends Model {
    public static final String TABLE_NAME = "tb_practice_kanji";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_IS_CORRECT = "is_correct";

    @Override
    protected String tableName() {
        return TABLE_NAME;
    }

    public boolean isExists(String kanji) {
        return super.isExists(COLUMN_ID, kanji);
    }

    public boolean save(String id, int incr) {
        if (!super.isExists(COLUMN_ID, id)) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, id);
            int value = incr;
            if (value < 0)
                value = 0;
            if (value > PracticeKanji.getMaxCorrectCount())
                value = PracticeKanji.getMaxCorrectCount();
            values.put(COLUMN_IS_CORRECT, value);
            return super.insert(values) > 0;
        } else {
            Cursor res = super.getData(COLUMN_ID, id);
            ContentValues values = new ContentValues();
            int new_value = Integer.parseInt(getStringValue(res, COLUMN_IS_CORRECT)) + incr;
            if (new_value < 0)
                new_value = 0;
            if (new_value > PracticeKanji.getMaxCorrectCount())
                new_value = PracticeKanji.getMaxCorrectCount();
            values.put(COLUMN_IS_CORRECT, new_value);
            return super.update(COLUMN_ID, id, values) > 0;
        }
    }

    public static ArrayList<String> listString(String str) {
        ArrayList<String> list = new ArrayList<>();
        String[] parts = str.split(",");
        for (String part : parts) {
            list.add(JapaneseUtils.convertKana(part));
        }
        return list;
    }

    public static String getRandomItem(String str) {
        String[] parts = str.split(",");
        if (parts.length == 0)
            return str;
        ArrayList<String> filtered = new ArrayList<>();
        String x = "";
        for (String part :
                parts) {
            part = part.trim();
            if (!part.equals("") && !part.contains("-")) {
                filtered.add(part);
                x += part + " ";
            }
        }
        if (filtered.size() == 0)
            return str;
        Random random = new Random();
        int min = 0;
        int max = filtered.size() - 1;
        int idx = random.nextInt(max - min + 1) + min;
        return filtered.get(idx);
    }

    public static String convertToRomaji(String str) {
        String output = "";
        String[] parts = str.split(",");
        int j = 0;
        for (String part : parts) {
            part = JapaneseUtils.convertToRomaji(part);
            output += part;
            if (j < parts.length - 1)
                output += ", ";
            j++;
        }
        return output;
    }

    public int getTotalCorrect(int level) {
        String join = "INNER JOIN " + KanjiModel.TABLE_NAME + " AS k ON pk.id = k.id";
        String criteria = "k.practice_level = '" + level + "'";
        return super.sumJoin(COLUMN_IS_CORRECT, "pk", join, criteria);
    }

    public int getTotal(String level) {
        String join = "INNER JOIN " + KanjiModel.TABLE_NAME + " AS k ON pk.id = k.id";
        String criteria;
        if (level.equals("N5"))
            criteria = "k.level = 'N5' AND ";
        else if (level.equals("N4"))
            criteria = "(k.level = 'N5' OR k.level = 'N4') AND ";
        else if (level.equals("N3"))
            criteria = "(k.level = 'N5' OR k.level = 'N4' OR k.level = 'N3') AND ";
        else if (level.equals("N2"))
            criteria = "(k.level = 'N5' OR k.level = 'N4' OR k.level = 'N3' OR k.level = 'N2') AND ";
        else
            criteria = "(k.level = 'N5' OR k.level = 'N4' OR k.level = 'N3' OR k.level = 'N2' OR k.level = 'N1') AND ";
        criteria += "pk.is_correct = '" + PracticeKanji.getMaxCorrectCount() + "'";
        return super.getCountOfRowsJoin("pk.*", "pk", join, criteria);
    }

    public boolean reset() {
        return super.delete() > 0;
    }

    private String capitalizeString(String str) {
        if (str == "") return str;
        String[] strArray = str.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String s : strArray) {
            if (s.length() == 0) break;
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            builder.append(cap + " ");
        }
        return builder.toString();
    }

    public ArrayList<PracticeKanji> getAllKanji() {
        String join = "INNER JOIN " + KanjiModel.TABLE_NAME + " AS k ON pk.id = k.id";
        Cursor res = super.getAllJoin("k.*, pk.is_correct", "pk", join, "1 ORDER BY pk.is_correct, k.level, k.id DESC LIMIT 50");
        ArrayList<PracticeKanji> list = new ArrayList<>();

        while (res.isAfterLast() == false) {
            PracticeKanji item = new PracticeKanji();
            item.setKanji(getStringValue(res, KanjiModel.COLUMN_KANJI));
            item.setLevel(getStringValue(res, KanjiModel.COLUMN_LEVEL));
            item.setCorrectCount(getIntegerValue(res, COLUMN_IS_CORRECT));
            item.setMeaning(capitalizeString(getStringValue(res, KanjiModel.COLUMN_MEANING)));
            list.add(item);
            res.moveToNext();
        }
        return list;
    }
}
