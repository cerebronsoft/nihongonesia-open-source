package com.introvesia.nihongonesia.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.introvesia.nihongonesia.R;
import com.introvesia.nihongonesia.data.Kanji;
import com.introvesia.nihongonesia.data.Word;
import com.introvesia.nihongonesia.layouts.TagLayout;
import com.introvesia.nihongonesia.models.KanjiModel;
import com.introvesia.nihongonesia.models.WordModel;

import java.util.ArrayList;

/**
 * Created by asus on 02/07/2017.
 */

public class KanjiDetailFragment extends Fragment {
    public static final String TAG_NAME = "Nihongonesia";
    private KanjiModel kanjiModel = new KanjiModel();
    private WordModel wordModel = new WordModel();
    private View kanji_view;
    private LayoutInflater kanji_inflater;
    private String kanji;
    private KanjiDetailFragmentListener listener;

    public interface KanjiDetailFragmentListener {
        void onItemKanjiYomiClickedListener(String valueClicked);
        void onItemKotobaClickedListener(String valueClicked);
    }

    public void setKanji(String kanji) {
        this.kanji = kanji;
    }

    public void registerForListener(KanjiDetailFragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.kanji_detail_layout, container, false);
        init(view, inflater);
        return view;
    }

    public void findKanji() {
        Kanji kanjiData = kanjiModel.getKanji(kanji);

        TextView kanjiText = kanji_view.findViewById(R.id.kanji);
        kanjiText.setText(kanjiData.getKanji());

        TextView meaningText = kanji_view.findViewById(R.id.meaning);
        meaningText.setText(kanjiData.getMeaning());

        TagLayout onyomiLayout = kanji_view.findViewById(R.id.onyomi);
        String onyomi = kanjiData.getOnyomi();
        if (!onyomi.isEmpty()) {
            String[] onyomiParts = onyomi.split(",");
            for (final String onyomiPart : onyomiParts) {
                View tagView = kanji_inflater.inflate(R.layout.tags, null, false);
                TextView tagTextView = tagView.findViewById(R.id.tagTextView);
                tagTextView.setText(onyomiPart);
                tagTextView.setTag(onyomiPart);
                tagTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showYomikataMenu(view);
                    }
                });
                onyomiLayout.addView(tagView);
            }
        } else {
            View tagView = kanji_inflater.inflate(R.layout.tags, null, false);
            TextView tagTextView = tagView.findViewById(R.id.tagTextView);
            tagTextView.setText("Tidak ada");
            tagTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemKanjiYomiClickedListener("");
                    } else {
                        throw new IllegalArgumentException("Please pass the listener");
                    }
                }
            });
            onyomiLayout.addView(tagView);
        }

        TagLayout kunyomiLayout = kanji_view.findViewById(R.id.kunyomi);
        String kunyomi = kanjiData.getKunyomi();
        if (!kunyomi.isEmpty()) {
            String[] kunyomiParts = kunyomi.split(",");
            for (final String kunyomiPart : kunyomiParts) {
                if (kunyomiPart.contains("-"))
                    continue;
                View tagView = kanji_inflater.inflate(R.layout.tags, null, false);
                TextView tagTextView = tagView.findViewById(R.id.tagTextView);
                tagTextView.setText(kunyomiPart);
                tagTextView.setTag(kunyomiPart);
                tagTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showYomikataMenu(view);
                    }
                });
                kunyomiLayout.addView(tagView);
            }
        } else {
            View tagView = kanji_inflater.inflate(R.layout.tags, null, false);
            TextView tagTextView = tagView.findViewById(R.id.tagTextView);
            tagTextView.setText("Tidak ada");
            tagTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemKanjiYomiClickedListener("");
                    } else {
                        throw new IllegalArgumentException("Please pass the listener");
                    }
                }
            });
            kunyomiLayout.addView(tagView);
        }
    }

    private void showYomikataMenu(final View view) {
        PopupMenu menu = new PopupMenu(getContext(), view);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (listener != null) {
                    switch (item.getItemId()) {
                        case R.id.opt_kanji_list:
                            listener.onItemKanjiYomiClickedListener(view.getTag() + "");
                            break;
                        case R.id.opt_kotoba_list:
                            populateKotobaByYomikata(view.getTag() + "");
                            break;
                    }
                } else {
                    throw new IllegalArgumentException("Please pass the listener");
                }
                return true;
            }
        });
        menu.inflate(R.menu.kanji_reading_menu);
        menu.show();
    }

    private void populateKotobaByYomikata(String yomikata) {
        LinearLayout listView = kanji_view.findViewById(R.id.kotoba_container);
        listView.removeAllViews();
        ArrayList<Word> data = wordModel.getAllKotobaByYomikata(kanji, yomikata.replace(".", ""));
        if (data.size() == 0) {
            TextView kosaKataLabel = kanji_view.findViewById(R.id.kosaKataLabel);
            kosaKataLabel.setVisibility(View.GONE);
        }
        for (Word item : data){
            View row = kanji_inflater.inflate(R.layout.custom_list_row, null);
            row.setTag(item.getId());
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemKotobaClickedListener(view.getTag() + "");
                    } else {
                        throw new IllegalArgumentException("Please pass the listener");
                    }
                }
            });

            TextView bunpoText = row.findViewById(R.id.bunpo);
            bunpoText.setText(item.getKanji());

            TextView romajiText = row.findViewById(R.id.romaji);
            romajiText.setText(item.getRomaji());

            TextView meaningText = row.findViewById(R.id.meaning);
            meaningText.setText(item.getMeaning());
            listView.addView(row);
        }
    }

    public void populateKotoba() {
        LinearLayout listView = kanji_view.findViewById(R.id.kotoba_container);
        listView.removeAllViews();
        ArrayList<Word> data = wordModel.getAllKotoba(kanji);
        if (data.size() == 0) {
            TextView kosaKataLabel = kanji_view.findViewById(R.id.kosaKataLabel);
            kosaKataLabel.setVisibility(View.GONE);
        }
        for (Word item : data){
            View row = kanji_inflater.inflate(R.layout.custom_list_row, null);
            row.setTag(item.getId());
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemKotobaClickedListener(view.getTag() + "");
                    } else {
                        throw new IllegalArgumentException("Please pass the listener");
                    }
                }
            });

            TextView bunpoText = row.findViewById(R.id.bunpo);
            bunpoText.setText(item.getKanji());

            TextView romajiText = row.findViewById(R.id.romaji);
            romajiText.setText(item.getRomaji());

            TextView meaningText = row.findViewById(R.id.meaning);
            meaningText.setText(item.getMeaning());
            listView.addView(row);
        }
    }

    private void init(View view, final LayoutInflater inflater) {
        this.kanji_inflater = inflater;
        this.kanji_view = view;
        findKanji();
        populateKotoba();
    }
}
