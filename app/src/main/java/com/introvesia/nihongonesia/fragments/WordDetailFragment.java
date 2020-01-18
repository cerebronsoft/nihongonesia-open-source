package com.introvesia.nihongonesia.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.introvesia.nihongonesia.R;
import com.introvesia.nihongonesia.data.Kanji;
import com.introvesia.nihongonesia.data.Word;
import com.introvesia.nihongonesia.data.WordTag;
import com.introvesia.nihongonesia.layouts.TagLayout;
import com.introvesia.nihongonesia.lib.JapaneseUtils;
import com.introvesia.nihongonesia.models.KanjiModel;
import com.introvesia.nihongonesia.models.WordModel;
import com.introvesia.nihongonesia.models.WordTagModel;

import java.util.ArrayList;

/**
 * Created by asus on 17/07/2017.
 */

public class WordDetailFragment extends Fragment {
    private WordModel wordModel = new WordModel();
    private KotobaDetailFragmentListener listener;
    private String id;
    private String kanji;
    private Word wordData;

    public void setId(String id) {
        this.id = id;
    }

    public void registerForListener(KotobaDetailFragmentListener listener) {
        this.listener = listener;
    }

    public interface KotobaDetailFragmentListener {
        void onItemKanjiClickedListener(String valueClicked);
        void onItemTagClickedListener(String valueClicked);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.word_detail_layout, container, false);
        init(view, inflater);
        return view;
    }

    public void findKotoba(View view) {
        wordData = wordModel.getKotoba(id);

        TextView kanjiText = view.findViewById(R.id.kanji);
        kanjiText.setText(wordData.getKanji());
        kanji = wordData.getKanji();

        TextView romajiText = view.findViewById(R.id.romaji);
        romajiText.setText(wordData.getRomaji());

        TextView meaningText = view.findViewById(R.id.meaning);
        meaningText.setText(wordData.getMeaning());
    }



    public void populateKanji(View view, LayoutInflater inflater) {
        LinearLayout listView = view.findViewById(R.id.kanji_list);

        // Kanji
        ArrayList<String> kanji_list = JapaneseUtils.getAllKanji(kanji);
        if (kanji_list.size() == 0) {
            TextView kosaKataLabel = view.findViewById(R.id.kanji_list_label);
            kosaKataLabel.setVisibility(View.GONE);
        } else {
            KanjiModel kanjiModel = new KanjiModel();
            ArrayList<String> listed_kanji = new ArrayList<>();
            for (String kanji_char : kanji_list) {
                if (listed_kanji.contains(kanji_char))
                    continue;
                listed_kanji.add(kanji_char);

                Kanji item = kanjiModel.getKanji(kanji_char);
                if (item == null) continue;

                View row = inflater.inflate(R.layout.kanji_row_custom, null);
                row.setTag(item.getKanji());
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.onItemKanjiClickedListener(view.getTag() + "");
                        } else {
                            throw new IllegalArgumentException("Please pass the listener");
                        }
                    }
                });

                TextView bunpoText = row.findViewById(R.id.kanji);
                bunpoText.setText(item.getKanji());

                TextView romajiText = row.findViewById(R.id.level);
                romajiText.setText(item.getLevel());

                TextView meaningText = row.findViewById(R.id.meaning);
                meaningText.setText(item.getMeaning());
                listView.addView(row);
            }
        }

        // Label
        TagLayout tagLayout = view.findViewById(R.id.tag_list);
        String tag_str = wordData.getTag();
        if (!tag_str.equals("")) {
            String[] tags = tag_str.split(", ");
            for (final String tag : tags) {
                WordTagModel ktm = new WordTagModel();
                WordTag tagData = ktm.getTag(tag);
                if (tagData == null)
                    continue;

                View tagView = inflater.inflate(R.layout.tags, null, false);
                TextView tagTextView = tagView.findViewById(R.id.tagTextView);
                tagTextView.setText(tagData.getName());
                tagTextView.setTag(tag);
                tagTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.onItemTagClickedListener(tag);
                        } else {
                            throw new IllegalArgumentException("Please pass the listener");
                        }
                    }
                });
                tagLayout.addView(tagView);
            }
        } else {
            TextView label = view.findViewById(R.id.tag_label);
            label.setVisibility(View.GONE);
        }
    }

    private void init(View view, final LayoutInflater inflater) {
        findKotoba(view);
        populateKanji(view, inflater);
    }
}
