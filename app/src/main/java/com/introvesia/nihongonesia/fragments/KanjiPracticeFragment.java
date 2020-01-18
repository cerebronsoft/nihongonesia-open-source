package com.introvesia.nihongonesia.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.introvesia.nihongonesia.R;
import com.introvesia.nihongonesia.data.Word;
import com.introvesia.nihongonesia.data.PracticeKanji;
import com.introvesia.nihongonesia.lib.JapaneseUtils;
import com.introvesia.nihongonesia.lib.NavigationManager;
import com.introvesia.nihongonesia.models.KanjiModel;
import com.introvesia.nihongonesia.models.WordModel;
import com.introvesia.nihongonesia.models.PracticeKanjiModel;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by asus on 27/07/2017.
 */

public class KanjiPracticeFragment extends Fragment {
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button_next;
    private Button correct_button;
    private ArrayList<PracticeKanji> options;
    private TextView message;
    private String correct_answer_id;
    private View base_view;
    private int level = 1;
    private TextView level_info;
    private PracticeKanjiModel practiceKanjiModel = new PracticeKanjiModel();
    private TextView clueText;
    private TextView clueAnswerText;
    private TextView clueMeaningText;
    private String correct_clue_answer;
    private String correct_clue_meaning;
    private boolean has_clue = false;
    private final int default_kanji_size = 100;
    private final int default_correct_clue_size = 15;
    private final int default_button_size = 15;
    private TextView kanjiText;
    private TextView questionText;
    private ArrayList<String> loaded_method_list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.practice_kanji_layout, container, false);
        init(view, inflater);
        return view;
    }

    private void init(View view, LayoutInflater inflater) {
        base_view = view;
        options = new ArrayList<>();
        message = view.findViewById(R.id.message);
        clueText = view.findViewById(R.id.clue);
        clueAnswerText = view.findViewById(R.id.clue_answer);
        clueMeaningText = view.findViewById(R.id.clue_meaning);
        kanjiText = view.findViewById(R.id.kanji);
        questionText = view.findViewById(R.id.question);

        final KanjiModel kanjiModel = new KanjiModel();
        level_info = view.findViewById(R.id.level_info);
        ArrayList<String> method_list = new ArrayList<>();
        method_list.add("kunyomi");
        method_list.add("onyomi");
        method_list.add("meaning");
        method_list.add("kanji");
        PracticeKanji.setMethodList(method_list);
        PracticeKanji.setTotalKanji(kanjiModel.getTotalKanjiByLevel(level));
        PracticeKanji.setCurrentLevel(level);

        View.OnClickListener check_answer = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = view.getTag() + "";
                ((Button)view).setTextColor(Color.WHITE);
                if (correct_answer_id.equals(id)) {
                    practiceKanjiModel.save(correct_answer_id, 1);
                    view.setBackgroundColor(Color.parseColor("#006600"));
                    message.setText("Jawaban kamu benar!");
                    message.setTextColor(Color.parseColor("#006600"));
                    level_info.setText("Level " + level + " ( " + PracticeKanji.getPercentage() + "% )");
                } else {
                    practiceKanjiModel.save(correct_answer_id, -1);
                    view.setBackgroundColor(Color.parseColor("#990000"));
                    message.setText("Jawaban kamu salah!");
                    message.setTextColor(Color.parseColor("#990000"));
                    correct_button.setBackgroundColor(Color.parseColor("#006600"));
                    correct_button.setTextColor(Color.WHITE);
                }
                clueAnswerText.setText(correct_clue_answer);
                clueMeaningText.setText(correct_clue_meaning);
                setButtonDisabled();
            }
        };

        button1 = view.findViewById(R.id.button1);
        button2 = view.findViewById(R.id.button2);
        button3 = view.findViewById(R.id.button3);
        button4 = view.findViewById(R.id.button4);

        button_next = view.findViewById(R.id.next_btn);

        button1.setBackgroundResource(android.R.drawable.btn_default);
        button2.setBackgroundResource(android.R.drawable.btn_default);
        button3.setBackgroundResource(android.R.drawable.btn_default);
        button4.setBackgroundResource(android.R.drawable.btn_default);

        button1.setOnClickListener(check_answer);
        button2.setOnClickListener(check_answer);
        button3.setOnClickListener(check_answer);
        button4.setOnClickListener(check_answer);

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonEnabled();
                loadData(base_view);
            }
        });

        Button button_result = view.findViewById(R.id.result_btn);
        button_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResultKanjiFragment frg = new ResultKanjiFragment();
                NavigationManager.push(frg);
            }
        });

        loadData(view);
    }

    private void loadData(View view) {
        options.clear();
        loaded_method_list.clear();
        float percetage = PracticeKanji.getPercentage();
        level_info.setText("Level " + level + " ( " + percetage + "% )");
        setButtonEnabled();

        if (percetage == 100) {
            level++;
            KanjiModel kanjiModel = new KanjiModel();
            PracticeKanji.setTotalKanji(kanjiModel.getTotalKanjiByLevel(level));
            PracticeKanji.setCurrentLevel(level);
            loadData(base_view);
        } else {
            // Correct answer
            KanjiModel kanjiModel = new KanjiModel();
            String criteria_level = "k.practice_level = '" + level + "'";
            String criteria;
            PracticeKanji kanji = kanjiModel.getRandomKanjiFiltered(criteria_level);

            if (kanji == null) return;

            String method = kanji.getMethod();
            String question = "";
            if (method.equals("onyomi"))
                question = "On'yomi dari kanji ini yang mana";
            else if (method.equals("kunyomi"))
                question = "Kun'yomi dari kanji ini yang mana";
            else if (method.equals("meaning"))
                question = "Arti dari kanji ini yang mana";
            else if (method.equals("kanji"))
                question = "Kanji dari on'yomi ini yang mana";
            questionText.setText(question + "?");
            options.add(kanji);
            correct_answer_id = kanji.getId() + "";
            addToExceptionList(method, kanji);
            // Another answer
            criteria_level = "practice_level <= '" + level + "'";
            for (int i = 0; i < 3; i++) {
                KanjiModel other_kanji_model = new KanjiModel();
                String criteria_method = "";
                int j = 0;
                for (String loaded_id : loaded_method_list) {
                    if (!loaded_id.equals(""))
                        criteria_method += method + " NOT LIKE '%" + loaded_id + "%'";
                    else
                        criteria_method += method + " != '" + loaded_id + "'";
                    if (j < loaded_method_list.size() - 1)
                        criteria_method += " AND ";
                    j++;
                }
                criteria = criteria_level + " AND id != '" + correct_answer_id + "' AND " + criteria_method;
                PracticeKanji other_kanji = other_kanji_model.getRandomKanji(criteria);
                addToExceptionList(method, other_kanji);
                options.add(other_kanji);
            }

            // Question

            if (options.size() == 4) {
                Collections.shuffle(options);
                int correct_button_id = -1;
                for (int i = 0; i < options.size(); i++) {
                    String id = options.get(i).getId() + "";
                    if (correct_answer_id.equals(id))
                        correct_button_id = i;
                }
                if (correct_button_id == 0)
                    correct_button = button1;
                else if (correct_button_id == 1)
                    correct_button = button2;
                else if (correct_button_id == 2)
                    correct_button = button3;
                else if (correct_button_id == 3)
                    correct_button = button4;

                button1.setTag(options.get(0).getId());
                button2.setTag(options.get(1).getId());
                button3.setTag(options.get(2).getId());
                button4.setTag(options.get(3).getId());
            }

            if (kanji.getMethod() == "onyomi")
                methodOnyomi(kanji);
            else if (kanji.getMethod() == "kunyomi")
                methodKunyomi(kanji);
            else if (kanji.getMethod() == "kanji")
                methodKanji(kanji);
            else if (kanji.getMethod() == "meaning")
                methodMeaning(kanji);
        }
    }

    private void addToExceptionList(String method, PracticeKanji kanji) {
        if (method.equals("onyomi"))
            loaded_method_list.add(kanji.getOnyomiPart());
        else if (method.equals("kunyomi"))
            loaded_method_list.add(kanji.getKunyomiPart());
        else if (method.equals("kanji"))
            loaded_method_list.add(kanji.getKanji());
        else if (method.equals("meaning"))
            loaded_method_list.add(kanji.getMeaning());
    }

    private void methodMeaning(PracticeKanji kanji) {
        kanjiText.setText(kanji.getKanji());
        kanjiText.setTextSize(default_kanji_size);
        clueText.setVisibility(View.GONE);
        button1.setText(options.get(0).getMeaning());
        button2.setText(options.get(1).getMeaning());
        button3.setText(options.get(2).getMeaning());
        button4.setText(options.get(3).getMeaning());
        button1.setTextSize(default_button_size);
        button2.setTextSize(default_button_size);
        button3.setTextSize(default_button_size);
        button4.setTextSize(default_button_size);
        clueAnswerText.setTextSize(default_correct_clue_size);
    }

    private void methodKunyomi(PracticeKanji kanji) {
        kanjiText.setText(kanji.getKanji());
        // Clue
        Word clueWord = getClueKotoba(kanji.getKanji(), kanji.getKunyomiPart());
        if (clueWord != null) {
            has_clue = true;
            correct_clue_answer = JapaneseUtils.convertToRomaji(clueWord.getKana());
            correct_clue_meaning = clueWord.getMeaning();
            clueText.setText(clueWord.getKanji());
        } else {
            has_clue = false;
            clueText.setText("");
        }
        if (!has_clue)
            clueText.setVisibility(View.GONE);
        else
            clueText.setVisibility(View.VISIBLE);
        button1.setText(options.get(0).getKunyomiRomaji());
        button2.setText(options.get(1).getKunyomiRomaji());
        button3.setText(options.get(2).getKunyomiRomaji());
        button4.setText(options.get(3).getKunyomiRomaji());

        // Text size
        kanjiText.setTextSize(default_kanji_size);
        button1.setTextSize(default_button_size);
        button2.setTextSize(default_button_size);
        button3.setTextSize(default_button_size);
        button4.setTextSize(default_button_size);
        clueAnswerText.setTextSize(default_correct_clue_size);
    }

    private Word getClueKotoba(String kanji, String onyomi) {
        WordModel wordModel = new WordModel();
        return wordModel.getRandomKotoba(kanji, onyomi);
    }

    private void methodOnyomi(PracticeKanji kanji) {
        kanjiText.setText(kanji.getKanji());
        // Clue
        Word clueWord = getClueKotoba(kanji.getKanji(), kanji.getOnyomiPart());
        if (clueWord != null) {
            has_clue = true;
            correct_clue_answer = JapaneseUtils.convertToRomaji(clueWord.getKana());
            correct_clue_meaning = clueWord.getMeaning();
            clueText.setText(clueWord.getKanji());
        } else {
            has_clue = false;
            clueText.setText("");
        }
        if (!has_clue)
            clueText.setVisibility(View.GONE);
        else
            clueText.setVisibility(View.VISIBLE);
        button1.setText(options.get(0).getOnyomiRomaji());
        button2.setText(options.get(1).getOnyomiRomaji());
        button3.setText(options.get(2).getOnyomiRomaji());
        button4.setText(options.get(3).getOnyomiRomaji());

        // Text size
        kanjiText.setTextSize(default_kanji_size);
        button1.setTextSize(default_button_size);
        button2.setTextSize(default_button_size);
        button3.setTextSize(default_button_size);
        button4.setTextSize(default_button_size);
        clueAnswerText.setTextSize(default_correct_clue_size);
    }

    private void methodKanji(PracticeKanji kanji) {
        Word clueWord = getClueKotoba(kanji.getKanji(), kanji.getOnyomiPart());
        if (clueWord != null) {
            has_clue = true;
            correct_clue_answer = clueWord.getKanji();
            correct_clue_meaning = clueWord.getMeaning();
            clueText.setText(JapaneseUtils.convertToRomaji(clueWord.getKana()));
        } else {
            has_clue = false;
            clueText.setText("");
        }
        if (!has_clue)
            clueText.setVisibility(View.GONE);
        else
            clueText.setVisibility(View.VISIBLE);
        kanjiText.setText(kanji.getOnyomiRomaji().toUpperCase());
        kanjiText.setTextSize(50);
        button1.setText(options.get(0).getKanji());
        button2.setText(options.get(1).getKanji());
        button3.setText(options.get(2).getKanji());
        button4.setText(options.get(3).getKanji());
        button1.setTextSize(30);
        button2.setTextSize(30);
        button3.setTextSize(30);
        button4.setTextSize(30);
        clueAnswerText.setTextSize(20);
    }

    private void setButtonEnabled() {
        button1.setEnabled(true);
        button2.setEnabled(true);
        button3.setEnabled(true);
        button4.setEnabled(true);
        button1.setTextColor(Color.BLACK);
        button2.setTextColor(Color.BLACK);
        button3.setTextColor(Color.BLACK);
        button4.setTextColor(Color.BLACK);
        button1.setBackgroundResource(android.R.drawable.btn_default);
        button2.setBackgroundResource(android.R.drawable.btn_default);
        button3.setBackgroundResource(android.R.drawable.btn_default);
        button4.setBackgroundResource(android.R.drawable.btn_default);
        button_next.setEnabled(false);
        correct_clue_answer = "";
        correct_clue_meaning = "";
        has_clue = false;

        message.setText("");
        clueText.setText("");
        clueAnswerText.setText("");
        clueMeaningText.setText("");

        message.setVisibility(View.GONE);
        clueMeaningText.setVisibility(View.GONE);
        clueAnswerText.setVisibility(View.GONE);
    }

    private void setButtonDisabled() {
        button1.setEnabled(false);
        button2.setEnabled(false);
        button3.setEnabled(false);
        button4.setEnabled(false);
        button_next.setEnabled(true);
        message.setVisibility(View.VISIBLE);
        if (has_clue) {
            clueText.setVisibility(View.VISIBLE);
            clueMeaningText.setVisibility(View.VISIBLE);
            clueAnswerText.setVisibility(View.VISIBLE);
        }
    }
}
