package com.introvesia.nihongonesia.lib;

import android.util.Log;

import com.introvesia.nihongonesia.data.JapaneseTextStructure;

import java.util.ArrayList;

/**
 * Created by asus on 06/07/2017.
 */

public class JapaneseUtils {
    public static boolean isKatakana(String input) {
        if (input == null || input.length() == 0) return false;
        return JapaneseCharacter.isKatakana(input.charAt(0));
    }

    public static String convertKana(String input) {
        if (input == null || input.length() == 0) return "";

        StringBuilder out = new StringBuilder();
        char ch = input.charAt(0);

        if (JapaneseCharacter.isHiragana(ch)) { // convert to hiragana to katakana
            for (int i = 0; i < input.length(); i++) {
                out.append(JapaneseCharacter.toKatakana(input.charAt(i)));
            }
        } else if (JapaneseCharacter.isKatakana(ch)) { // convert to katakana to hiragana
            for (int i = 0; i < input.length(); i++) {
                out.append(JapaneseCharacter.toHiragana(input.charAt(i)));
            }
        } else { // do nothing if neither
            return input;
        }

        return out.toString();
    }

    public static String convertToRomaji(String input) {
        if (input == null || input.length() == 0) return "";

        StringBuilder out = new StringBuilder();
        char ch = input.charAt(0);
        int length = input.length();

        /*
        130 small katakana tsu
        162 small katakana ya
        164 small katakana yu
        166 small katakana yo
        34 small hiraga tsu
        66  small hiragana ya
        68  small hiragana yu
        70  small hiragana yo */
        for (int i = 0; i < length; i++) {
            char item = input.charAt(i);
            String romaji = JapaneseCharacter.toRomaji(item);
            if (i > 0) {
                if ((romaji.equals("ya") || romaji.equals("yu") || romaji.equals("yo"))) {
                    String prev_romaji = JapaneseCharacter.toRomaji(input.charAt(i - 1));
                    if (prev_romaji == "n")
                        romaji = "\'" + romaji;
                }
            } else if (i < length - 1) {
                String next_romaji = JapaneseCharacter.toRomaji(input.charAt(i + 1));
                if (romaji.equals("n")) {
                    if (next_romaji.charAt(0) == 'b' || next_romaji.charAt(0) == 'p')
                        romaji = "m";
                    else if (JapaneseCharacter.isVowel(next_romaji.charAt(0)))
                        romaji += "'";
                }
            }

            if (i < length - 1) {
                int index = JapaneseCharacter.getRomajiIndex(input.charAt(i));
                if (JapaneseCharacter.isKatakana(item)) {
                    int next_index = JapaneseCharacter.getRomajiIndex(input.charAt(i + 1));
                    if (index == 130) {
                        romaji = JapaneseCharacter.toRomaji(input.charAt(i + 1)).charAt(0) + "";
                    } else if (next_index == 162 || next_index == 164 || next_index == 166) {
                        romaji = romaji.substring(0, romaji.length() - 1);
                        if (!romaji.equals("sh") && !romaji.equals("ch") && !romaji.equals("j"))
                            romaji += "y";
                    }
                } else if (JapaneseCharacter.isHiragana(item)) {
                    int next_index = JapaneseCharacter.getRomajiIndex(input.charAt(i + 1));
                    if (index == 34) {
                        romaji = JapaneseCharacter.toRomaji(input.charAt(i + 1)).charAt(0) + "";
                    } else if (next_index == 66 || next_index == 68 || next_index == 70) {
                        romaji = romaji.substring(0, romaji.length() - 1);
                        if (!romaji.equals("sh") && !romaji.equals("ch") && !romaji.equals("j"))
                            romaji += "y";
                    }
                }
            }
            out.append(romaji);
        }

        return out.toString();
    }

    public static ArrayList<String> getAllKanji(String input) {
        ArrayList<String> kanji = new ArrayList<>();
        if (input == null || input.length() == 0) return kanji;

        for (int i = 0; i < input.length(); i++) {
            if (JapaneseCharacter.isKanji(input.charAt(i)))
                kanji.add(input.charAt(i) + "");
        }

        return kanji;
    }

    public static String convertToHiragana(String input) {
        if (input == null || input.length() == 0) return "";

        StringBuilder out = new StringBuilder();

        String fonem = "";
        boolean foundConsonant = false;
        int length = input.length();
        for (int i = 0; i < length; i++) {
            fonem += input.charAt(i);
            if (!foundConsonant) {
                if (!JapaneseCharacter.isVowel(input.charAt(i))) {
                    foundConsonant = true;
                    if (input.charAt(i) == 'n') {
                        i++;
                        out.append(JapaneseCharacter.fromRomajiToHiragana("n", 0));
                        foundConsonant = false;
                        fonem = "";
                    } else if (input.charAt(i) == 's' && i < length - 2 && input.charAt(i + 1) == 'h') {
                        i += 2;
                        out.append(JapaneseCharacter.fromRomajiToHiragana("shi", 0));
                        if (input.charAt(i) != 'i')
                            out.append(JapaneseCharacter.fromRomajiToHiragana("y" + input.charAt(i), -1));
                        foundConsonant = false;
                        fonem = "";
                    } else if (input.charAt(i) == 'c' && i < length - 2 && input.charAt(i + 1) == 'h') {
                        i += 2;
                        out.append(JapaneseCharacter.fromRomajiToHiragana("chi", 0));
                        if (input.charAt(i) != 'i')
                            out.append(JapaneseCharacter.fromRomajiToHiragana("y" + input.charAt(i), -1));
                        foundConsonant = false;
                        fonem = "";
                    } else if (input.charAt(i) == 'j' && i < length - 1) {
                        i++;
                        out.append(JapaneseCharacter.fromRomajiToHiragana("ji", 0));
                        if (input.charAt(i) != 'i')
                            out.append(JapaneseCharacter.fromRomajiToHiragana("y" + input.charAt(i), -1));
                        foundConsonant = false;
                        fonem = "";
                    } else if (i < length - 2 && input.charAt(i + 1) == 'y') {
                        out.append(JapaneseCharacter.fromRomajiToHiragana(input.charAt(i) + "i", 0));
                        i += 2;
                        if (input.charAt(i) != 'i')
                            out.append(JapaneseCharacter.fromRomajiToHiragana("y" + input.charAt(i), -1));
                        foundConsonant = false;
                        fonem = "";
                    }
                } else {
                    out.append(JapaneseCharacter.fromRomajiToHiragana(fonem, 1));
                    fonem = "";
                }
            } else {
                foundConsonant = false;
                out.append(JapaneseCharacter.fromRomajiToHiragana(fonem, 0));
                fonem = "";
            }
        }

        return out.toString();
    }

    public static String convertToFullWidthKatakana(String input) {
        if (input == null || input.length() == 0) return "";

        StringBuilder out = new StringBuilder();
        char ch = input.charAt(0);

        String fonem = "";
        boolean foundConsonant = false;
        int length = input.length();
        for (int i = 0; i < length; i++) {
            fonem += input.charAt(i);
            if (!foundConsonant) {
                if (!JapaneseCharacter.isVowel(input.charAt(i))) {
                    foundConsonant = true;
                    if (input.charAt(i) == 'n') {
                        i++;
                        out.append(JapaneseCharacter.fromRomajiToFullWidthKatakana("n", 0));
                        foundConsonant = false;
                        fonem = "";
                    } else if (input.charAt(i) == 's' && i < length - 2 && input.charAt(i + 1) == 'h') {
                        i += 2;
                        out.append(JapaneseCharacter.fromRomajiToFullWidthKatakana("shi", 0));
                        if (input.charAt(i) != 'i')
                            out.append(JapaneseCharacter.fromRomajiToFullWidthKatakana("y" + input.charAt(i), -1));
                        foundConsonant = false;
                        fonem = "";
                    } else if (input.charAt(i) == 'c' && i < length - 2 && input.charAt(i + 1) == 'h') {
                        i += 2;
                        out.append(JapaneseCharacter.fromRomajiToFullWidthKatakana("chi", 0));
                        if (input.charAt(i) != 'i')
                            out.append(JapaneseCharacter.fromRomajiToFullWidthKatakana("y" + input.charAt(i), -1));
                        foundConsonant = false;
                        fonem = "";
                    } else if (input.charAt(i) == 'j' && i < length - 1) {
                        i++;
                        out.append(JapaneseCharacter.fromRomajiToFullWidthKatakana("ji", 0));
                        if (input.charAt(i) != 'i')
                            out.append(JapaneseCharacter.fromRomajiToFullWidthKatakana("y" + input.charAt(i), -1));
                        foundConsonant = false;
                        fonem = "";
                    } else if (i < length - 2 && input.charAt(i + 1) == 'y') {
                        out.append(JapaneseCharacter.fromRomajiToFullWidthKatakana(input.charAt(i) + "i", 0));
                        i += 2;
                        if (input.charAt(i) != 'i')
                            out.append(JapaneseCharacter.fromRomajiToFullWidthKatakana("y" + input.charAt(i), -1));
                        foundConsonant = false;
                        fonem = "";
                    }
                } else {
                    out.append(JapaneseCharacter.fromRomajiToFullWidthKatakana(fonem, 1));
                    fonem = "";
                }
            } else {
                foundConsonant = false;
                out.append(JapaneseCharacter.fromRomajiToFullWidthKatakana(fonem, 0));
                fonem = "";
            }
        }

        return out.toString();
    }
}
