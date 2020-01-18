package com.introvesia.nihongonesia.data;

/**
 * Created by asus on 30/07/2017.
 */

public class JapaneseTextStructure {
    private String text;
    private StructureType type;
    private String meaning;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setType(String type) {
        if (type == "kanji")
            this.type = StructureType.KANJI;
        else if (type == "hiragana")
            this.type = StructureType.HIRAGANA;
        else if (type == "katakana")
            this.type = StructureType.KATAKANA;
        else if (type == "romaji")
            this.type = StructureType.ROMAJI;
        else
            this.type = StructureType.UNKNOWN;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public enum StructureType{
        KANJI,
        HIRAGANA,
        KATAKANA,
        ROMAJI,
        UNKNOWN
    }
}
