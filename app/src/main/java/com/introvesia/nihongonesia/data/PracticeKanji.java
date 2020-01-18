package com.introvesia.nihongonesia.data;

import android.util.Log;

import com.introvesia.nihongonesia.models.PracticeKanjiModel;

import java.util.ArrayList;

/**
 * Created by asus on 29/07/2017.
 */

public class PracticeKanji {
    private int id;
    private static int currentLevel;
    private String kanji;
    private String level;
    private String onyomi;
    private String kunyomi;
    private String onyomi_romaji;
    private String kunyomi_romaji;
    private String onyomiPart;
    private String kunyomiPart;
    private int correct_count = 0;
    private ArrayList<String> yomikataList;
    private String meaning;
    private String meaningPart;
    private static int totalKanji = 0;
    private static ArrayList<String> method_list;
    private static int max_correct_count = 0;

    public static int getMaxCorrectCount() {
        return max_correct_count;
    }

    public static void setTotalKanji(int totalKanji) {
        PracticeKanji.totalKanji = totalKanji * max_correct_count;
    }

    public static float getPercentage() {
        PracticeKanjiModel practiceKanjiModel = new PracticeKanjiModel();
        int total_correct = practiceKanjiModel.getTotalCorrect(currentLevel);
        if (total_correct == 0)
            return 0;
        float percentage = ((float)total_correct / (float)totalKanji) * 100;
        return (float) (Math.round(percentage * 100.0) / 100.0);
    }

    public static void setCurrentLevel(int currentLevel) {
        PracticeKanji.currentLevel = currentLevel;
    }

    public String getMethod() {
        if (getCorrectCount() >= getMaxCorrectCount())
            return method_list.get(getMaxCorrectCount() - 1);
        return method_list.get(getCorrectCount());
    }

    public static void setMethodList(ArrayList<String> method_list) {
        PracticeKanji.method_list = method_list;
        max_correct_count = method_list.size();
    }

    public String getKanji() {
        return kanji;
    }

    public void setKanji(String kanji) {
        this.kanji = kanji;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getCorrectCount() {
        return correct_count;
    }

    public void setCorrectCount(int correct_count) {
        this.correct_count = correct_count;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getOnyomiRomaji() {
        return onyomi_romaji;
    }

    public void setOnyomiRomaji(String onyomi) {
        this.onyomi_romaji = onyomi;
    }

    public String getKunyomiRomaji() {
        return kunyomi_romaji;
    }

    public void setKunyomiRomaji(String kunyomi) {
        this.kunyomi_romaji = kunyomi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setYomikataList(ArrayList<String> yomikataList) {
        this.yomikataList = yomikataList;
    }

    public void setOnyomi(String onyomi) {
        this.onyomi = onyomi;
    }

    public void setKunyomi(String kunyomi) {
        this.kunyomi = kunyomi;
    }

    public String getOnyomiPart() {
        return onyomiPart;
    }

    public void setOnyomiPart(String onyomiPart) {
        this.onyomiPart = onyomiPart;
    }

    public String getKunyomiPart() {
        return kunyomiPart;
    }

    public void setKunyomiPart(String kunyomiPart) {
        this.kunyomiPart = kunyomiPart;
    }

    public void setMeaningPart(String meaningPart) {
        this.meaningPart = meaningPart;
    }
}
