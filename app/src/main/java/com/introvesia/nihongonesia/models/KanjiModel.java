package com.introvesia.nihongonesia.models;

import android.database.Cursor;

import com.introvesia.nihongonesia.data.Kanji;
import com.introvesia.nihongonesia.data.PracticeKanji;
import com.introvesia.nihongonesia.lib.JapaneseCharacter;
import com.introvesia.nihongonesia.lib.JapaneseUtils;
import com.introvesia.nihongonesia.lib.Model;

import java.util.ArrayList;

/**
 * Created by asus on 01/07/2017.
 */

public class KanjiModel extends Model {
    public static final String TABLE_NAME = "tb_kanji";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_KANJI = "kanji";
    public static final String COLUMN_ONYOMI = "onyomi";
    public static final String COLUMN_KUNYOMI = "kunyomi";
    public static final String COLUMN_MEANING = "meaning";
    public static final String COLUMN_LEVEL = "level";
    private Object randomKanji;

    @Override
    protected String tableName() {
        return TABLE_NAME;
    }

    public Kanji getKanji(String kanji) {
        Cursor res = super.getData(COLUMN_KANJI, kanji);
        Kanji item = new Kanji();
        if (res != null) {
            item.setId(getIntegerValue(res, COLUMN_ID));
            item.setKanji(getStringValue(res, COLUMN_KANJI));
            item.setOnyomi(getStringValue(res, COLUMN_ONYOMI));
            item.setKunyomi(getStringValue(res, COLUMN_KUNYOMI));
            item.setLevel(getStringValue(res, COLUMN_LEVEL));
            item.setMeaning(capitalizeString(getStringValue(res, COLUMN_MEANING)));
            return item;
        }
        return null;
    }

    public Kanji getYomikata(String kanji) {
        Cursor res = super.getDataBySql("SELECT onyomi, kunyomi FROM " + TABLE_NAME + " WHERE kanji = '" + kanji + "'");
        Kanji item = new Kanji();
        if (res != null) {
            item.setOnyomi(getStringValue(res, COLUMN_ONYOMI));
            item.setKunyomi(getStringValue(res, COLUMN_KUNYOMI));
            return item;
        }
        return null;
    }

    public ArrayList<Kanji> getAllKanji() {
        String criteria = "1 ORDER BY level DESC LIMIT 500";
        Cursor res = super.getAll(criteria);
        ArrayList<Kanji> list = new ArrayList<Kanji>();

        while (res.isAfterLast() == false) {
            Kanji item = new Kanji();
            item.setId(getIntegerValue(res, COLUMN_ID));
            item.setKanji(getStringValue(res, COLUMN_KANJI));
            item.setOnyomi(getStringValue(res, COLUMN_ONYOMI));
            item.setKunyomi(getStringValue(res, COLUMN_KUNYOMI));
            item.setLevel(getStringValue(res, COLUMN_LEVEL));
            item.setMeaning(capitalizeString(getStringValue(res, COLUMN_MEANING)));
            list.add(item);
            res.moveToNext();
        }
        return list;
    }

    public ArrayList<Kanji> getAllKanji(String[] kanjis) {
        String criteria = "";
        if (kanjis.length > 0) {
            int i = 0;
            for (String kanji : kanjis) {
                criteria += "kanji = '" + kanji + "'";
                if (i < kanjis.length - 1) {
                    criteria += " OR ";
                }
                i++;
            }
        } else
            criteria += "1";
        criteria += " ORDER BY level DESC LIMIT 500";
        Cursor res = super.getAll(criteria);
        ArrayList<Kanji> list = new ArrayList<Kanji>();

        while (res.isAfterLast() == false) {
            Kanji item = new Kanji();
            item.setId(getIntegerValue(res, COLUMN_ID));
            item.setKanji(getStringValue(res, COLUMN_KANJI));
            item.setOnyomi(getStringValue(res, COLUMN_ONYOMI));
            item.setKunyomi(getStringValue(res, COLUMN_KUNYOMI));
            item.setLevel(getStringValue(res, COLUMN_LEVEL));
            item.setMeaning(capitalizeString(getStringValue(res, COLUMN_MEANING)));
            list.add(item);
            res.moveToNext();
        }
        return list;
    }

    public ArrayList<Kanji> getAllKanjiByKunyomi(String keyword) {
        String criteria = "";
        if (keyword != "") {
            char firstChar = keyword.charAt(0);
            if (JapaneseCharacter.isRomaji(firstChar)) {
                String hiragana = JapaneseUtils.convertToHiragana(keyword);
                criteria += "kunyomi LIKE '%" + hiragana + "%' ";
            } else {
                criteria += "kunyomi LIKE '%" + keyword + "%' ";
            }
        } else {
            criteria += "1 ";
        }
        criteria += "ORDER BY level DESC LIMIT 500";
        Cursor res = super.getAll(criteria);
        ArrayList<Kanji> list = new ArrayList<Kanji>();

        while (res.isAfterLast() == false) {
            Kanji item = new Kanji();
            item.setId(getIntegerValue(res, COLUMN_ID));
            item.setKanji(getStringValue(res, COLUMN_KANJI));
            item.setOnyomi(getStringValue(res, COLUMN_ONYOMI));
            item.setKunyomi(getStringValue(res, COLUMN_KUNYOMI));
            item.setLevel(getStringValue(res, COLUMN_LEVEL));
            item.setMeaning(capitalizeString(getStringValue(res, COLUMN_MEANING)));
            list.add(item);
            res.moveToNext();
        }
        return list;
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

    public ArrayList<Kanji> getAllKanjiByExactYomikata(String keyword) {
        String criteria = "";
        criteria += "(onyomi LIKE '" + keyword + "' OR ";
        criteria += "onyomi LIKE '" + keyword + ",%' OR ";
        criteria += "onyomi LIKE '%," + keyword + ",%' OR ";
        criteria += "onyomi LIKE '%," + keyword + "') OR ";
        criteria += "(kunyomi LIKE '" + keyword + "' OR ";
        criteria += "kunyomi LIKE '" + keyword + ",%' OR ";
        criteria += "kunyomi LIKE '%," + keyword + ",%' OR ";
        criteria += "kunyomi LIKE '%," + keyword + "') ";
        criteria += "ORDER BY level DESC LIMIT 500";
        Cursor res = super.getAll(criteria);
        ArrayList<Kanji> list = new ArrayList<Kanji>();

        while (res.isAfterLast() == false) {
            Kanji item = new Kanji();
            item.setId(getIntegerValue(res, COLUMN_ID));
            item.setKanji(getStringValue(res, COLUMN_KANJI));
            item.setOnyomi(getStringValue(res, COLUMN_ONYOMI));
            item.setKunyomi(getStringValue(res, COLUMN_KUNYOMI));
            item.setLevel(getStringValue(res, COLUMN_LEVEL));
            item.setMeaning(capitalizeString(getStringValue(res, COLUMN_MEANING)));
            list.add(item);
            res.moveToNext();
        }
        return list;
    }

    public PracticeKanji getRandomKanji(String criteria) {
        Cursor res = super.getData(criteria + " ORDER BY RANDOM() LIMIT 1");
        PracticeKanji item = new PracticeKanji();
        if (res != null) {
            item.setId(getIntegerValue(res, COLUMN_ID));
            item.setKanji(getStringValue(res, COLUMN_KANJI));
            item.setOnyomi(getStringValue(res, KanjiModel.COLUMN_ONYOMI));
            item.setKunyomi(getStringValue(res, KanjiModel.COLUMN_KUNYOMI));
            // Onyomi
            String onyomi = getStringValue(res, KanjiModel.COLUMN_ONYOMI);
            onyomi = PracticeKanjiModel.getRandomItem(onyomi);
            item.setOnyomiPart(onyomi);
            if (!onyomi.isEmpty())
                item.setOnyomiRomaji(PracticeKanjiModel.convertToRomaji(onyomi));
            else
                item.setOnyomiRomaji("Tidak ada");
            // Kunyomi
            String kunyomi = getStringValue(res, KanjiModel.COLUMN_KUNYOMI);
            kunyomi = PracticeKanjiModel.getRandomItem(kunyomi);
            item.setKunyomiPart(kunyomi);
            if (!kunyomi.isEmpty())
                item.setKunyomiRomaji(PracticeKanjiModel.convertToRomaji(kunyomi.replace(".", "")));
            else
                item.setKunyomiRomaji("Tidak ada");
            item.setLevel(getStringValue(res, COLUMN_LEVEL));
            item.setYomikataList(PracticeKanjiModel.listString(item.getOnyomiRomaji()));
            // Meaning
            String meaning = getStringValue(res, KanjiModel.COLUMN_MEANING);
            meaning = PracticeKanjiModel.getRandomItem(meaning);
            item.setMeaning(capitalizeString(meaning));
            return item;
        }
        return null;
    }

    public PracticeKanji getRandomKanjiFiltered(String criteria) {
        String join = "LEFT JOIN " + PracticeKanjiModel.TABLE_NAME + " AS pk ON pk.id = k.id";
        Cursor res = super.getDataJoin("k.*, pk.is_correct, pk.id AS loaded_kanji", "k", join, criteria + " " +
                "GROUP BY k.kanji " +
                "HAVING loaded_kanji IS NULL OR pk.is_correct < " + PracticeKanji.getMaxCorrectCount() + " " +
                "ORDER BY RANDOM() LIMIT 1");
        PracticeKanji item = new PracticeKanji();
        if (res != null) {
            item.setId(getIntegerValue(res, KanjiModel.COLUMN_ID));
            item.setKanji(getStringValue(res, KanjiModel.COLUMN_KANJI));
            item.setOnyomi(getStringValue(res, KanjiModel.COLUMN_ONYOMI));
            item.setKunyomi(getStringValue(res, KanjiModel.COLUMN_KUNYOMI));
            // Onyomi
            String onyomi = getStringValue(res, KanjiModel.COLUMN_ONYOMI);
            onyomi = PracticeKanjiModel.getRandomItem(onyomi);
            item.setOnyomiPart(onyomi);
            if (!onyomi.isEmpty())
                item.setOnyomiRomaji(PracticeKanjiModel.convertToRomaji(onyomi));
            else
                item.setOnyomiRomaji("Tidak ada");
            // Kunyomi
            String kunyomi = getStringValue(res, KanjiModel.COLUMN_KUNYOMI);
            kunyomi = PracticeKanjiModel.getRandomItem(kunyomi);
            item.setKunyomiPart(kunyomi);
            if (!kunyomi.equals(""))
                item.setKunyomiRomaji(PracticeKanjiModel.convertToRomaji(kunyomi.replace(".", "")));
            else
                item.setKunyomiRomaji("Tidak ada");
            item.setLevel(getStringValue(res, KanjiModel.COLUMN_LEVEL));
            item.setCorrectCount(getIntegerValue(res, PracticeKanjiModel.COLUMN_IS_CORRECT));
            item.setYomikataList(PracticeKanjiModel.listString(item.getOnyomiRomaji()));
            // Meaning
            String meaning = getStringValue(res, KanjiModel.COLUMN_MEANING);
            meaning = PracticeKanjiModel.getRandomItem(meaning);
            item.setMeaning(capitalizeString(meaning));
            return item;
        }
        return null;
    }

    public int getTotalKanjiByLevel(int level) {
        String criteria;
//        if (level.equals("N5"))
//            criteria = "level = 'N5'";
//        else if (level.equals("N4"))
//            criteria = "level = 'N5' OR level = 'N4'";
//        else if (level.equals("N3"))
//            criteria = "level = 'N5' OR level = 'N4' OR level = 'N3'";
//        else if (level.equals("N2"))
//            criteria = "level = 'N5' OR level = 'N4' OR level = 'N3' OR level = 'N2'";
//        else
//            criteria = "level = 'N5' OR level = 'N4' OR level = 'N3' OR level = 'N2' OR level = 'N1'";
        return super.getCountOfRows("practice_level = '" + level + "'");
    }

    public ArrayList<Kanji> getAllKanjiByAllFilters(String keyword) {
        return getAllKanjiByAllFilters("", keyword);
    }

    public ArrayList<Kanji> getAllKanjiByAllFilters(String level, String keyword) {
        String criteria = "";
        if (keyword != "") {
            char firstChar = keyword.charAt(0);
            // Kunyomi
            if (JapaneseCharacter.isRomaji(firstChar)) {
                String hiragana = JapaneseUtils.convertToHiragana(keyword);
                criteria += "(kunyomi LIKE '%" + hiragana + "%' OR ";
            } else {
                criteria += "(kunyomi LIKE '%" + keyword + "%' OR ";
            }
            // Onyomi
            if (JapaneseCharacter.isRomaji(firstChar)) {
                String hiragana = JapaneseUtils.convertToFullWidthKatakana(keyword);
                criteria += "onyomi LIKE '%" + hiragana + "%' OR ";
            } else {
                criteria += "onyomi LIKE '%" + keyword + "%' OR ";
            }
            // Meaning
            criteria += "meaning LIKE '%" + keyword + "%') ";
        } else {
            criteria += "1 ";
        }
        if (!level.equals(""))
            criteria += " AND level = '" + level + "' ";
        criteria += "ORDER BY level DESC LIMIT 500";
        Cursor res = super.getAll(criteria);
        ArrayList<Kanji> list = new ArrayList<>();

        while (res.isAfterLast() == false) {
            Kanji item = new Kanji();
            item.setId(getIntegerValue(res, COLUMN_ID));
            item.setKanji(getStringValue(res, COLUMN_KANJI));
            item.setOnyomi(getStringValue(res, COLUMN_ONYOMI));
            item.setKunyomi(getStringValue(res, COLUMN_KUNYOMI));
            item.setLevel(getStringValue(res, COLUMN_LEVEL));
            item.setMeaning(capitalizeString(getStringValue(res, COLUMN_MEANING)));
            list.add(item);
            res.moveToNext();
        }
        return list;
    }

    public ArrayList<Kanji> getAllKanjiByMeaning(String keyword) {
        return getAllKanjiByMeaning("", keyword);
    }

    public ArrayList<Kanji> getAllKanjiByMeaning(String level, String keyword) {
        String criteria = "meaning LIKE '%" + keyword + "%' ";
        if (!level.equals(""))
            criteria += " AND level = '" + level + "' ";
        criteria += "ORDER BY level DESC LIMIT 500";
        Cursor res = super.getAll(criteria);
        ArrayList<Kanji> list = new ArrayList<Kanji>();

        while (res.isAfterLast() == false) {
            Kanji item = new Kanji();
            item.setId(getIntegerValue(res, COLUMN_ID));
            item.setKanji(getStringValue(res, COLUMN_KANJI));
            item.setOnyomi(getStringValue(res, COLUMN_ONYOMI));
            item.setKunyomi(getStringValue(res, COLUMN_KUNYOMI));
            item.setLevel(getStringValue(res, COLUMN_LEVEL));
            item.setMeaning(capitalizeString(getStringValue(res, COLUMN_MEANING)));
            list.add(item);
            res.moveToNext();
        }
        return list;
    }

    public ArrayList<Kanji> getAllKanjiByOnyomi(String keyword) {
        return getAllKanjiByOnyomi("", keyword);
    }

    public ArrayList<Kanji> getAllKanjiByOnyomi(String level, String keyword) {
        String criteria = "";
        if (keyword != "") {
            char firstChar = keyword.charAt(0);
            if (JapaneseCharacter.isRomaji(firstChar)) {
                String hiragana = JapaneseUtils.convertToFullWidthKatakana(keyword);
                criteria += "onyomi LIKE '%" + hiragana + "%' ";
            } else {
                criteria += "onyomi LIKE '%" + keyword + "%' ";
            }
        } else {
            criteria += "1 ";
        }
        if (!level.equals(""))
            criteria += " AND level = '" + level + "' ";
        criteria += "ORDER BY level DESC LIMIT 500";
        Cursor res = super.getAll(criteria);
        ArrayList<Kanji> list = new ArrayList<Kanji>();

        while (res.isAfterLast() == false) {
            Kanji item = new Kanji();
            item.setId(getIntegerValue(res, COLUMN_ID));
            item.setKanji(getStringValue(res, COLUMN_KANJI));
            item.setOnyomi(getStringValue(res, COLUMN_ONYOMI));
            item.setKunyomi(getStringValue(res, COLUMN_KUNYOMI));
            item.setLevel(getStringValue(res, COLUMN_LEVEL));
            item.setMeaning(capitalizeString(getStringValue(res, COLUMN_MEANING)));
            list.add(item);
            res.moveToNext();
        }
        return list;
    }

    public ArrayList<Kanji> getAllKanjiByOnyomiKunyomi(String keyword) {
        return getAllKanjiByOnyomiKunyomi("", keyword);
    }

    public ArrayList<Kanji> getAllKanjiByOnyomiKunyomi(String level, String keyword) {
        String criteria = "";
        if (keyword != "") {
            char firstChar = keyword.charAt(0);
            // Kunyomi
            if (JapaneseCharacter.isRomaji(firstChar)) {
                String hiragana = JapaneseUtils.convertToHiragana(keyword);
                criteria += "(kunyomi LIKE '%" + hiragana + "%' OR ";
            } else {
                criteria += "(kunyomi LIKE '%" + keyword + "%' OR ";
            }
            // Onyomi
            if (JapaneseCharacter.isRomaji(firstChar)) {
                String hiragana = JapaneseUtils.convertToFullWidthKatakana(keyword);
                criteria += "onyomi LIKE '%" + hiragana + "%') ";
            } else {
                criteria += "onyomi LIKE '%" + keyword + "%') ";
            }
        } else {
            criteria += "1 ";
        }
        if (!level.equals(""))
            criteria += " AND level = '" + level + "' ";
        criteria += "ORDER BY level DESC LIMIT 500";
        Cursor res = super.getAll(criteria);
        ArrayList<Kanji> list = new ArrayList<>();

        while (res.isAfterLast() == false) {
            Kanji item = new Kanji();
            item.setId(getIntegerValue(res, COLUMN_ID));
            item.setKanji(getStringValue(res, COLUMN_KANJI));
            item.setOnyomi(getStringValue(res, COLUMN_ONYOMI));
            item.setKunyomi(getStringValue(res, COLUMN_KUNYOMI));
            item.setLevel(getStringValue(res, COLUMN_LEVEL));
            item.setMeaning(capitalizeString(getStringValue(res, COLUMN_MEANING)));
            list.add(item);
            res.moveToNext();
        }
        return list;
    }

    public ArrayList<Kanji> getAllKanjiByKunyomi(String level, String keyword) {
        return null;
    }
}
