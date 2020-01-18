package com.introvesia.nihongonesia.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.introvesia.nihongonesia.R;
import com.introvesia.nihongonesia.adapters.KanjiAdapter;
import com.introvesia.nihongonesia.data.Kanji;
import com.introvesia.nihongonesia.models.KanjiModel;

import java.util.ArrayList;

/**
 * Created by asus on 01/07/2017.
 */

public class KanjiFragment extends Fragment {
    private KanjiModel kanjiModel = new KanjiModel();
    private KanjiListFragmentListener listener;
    private ListView listView;
    private LayoutInflater inflater;

    public interface KanjiListFragmentListener {
        void onItemKanjiClickedListener(String valueClicked);
    }

    public void registerForListener(KanjiListFragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.word_layout, container, false);
        init(view, inflater);
        return view;
    }

    public void find(int type, String level, String keyword) {
        if (listView == null) return;
        ArrayList<Kanji> data;
        if (type == R.id.opt_all)
            data = kanjiModel.getAllKanjiByAllFilters(level, keyword);
        else if (type == R.id.opt_kunyomi)
            data = kanjiModel.getAllKanjiByKunyomi(level, keyword);
        else if (type == R.id.opt_onkun)
            data = kanjiModel.getAllKanjiByOnyomiKunyomi(level, keyword);
        else if (type == R.id.opt_onyomi)
            data = kanjiModel.getAllKanjiByOnyomi(level, keyword);
        else if (type == R.id.opt_meaning)
            data = kanjiModel.getAllKanjiByMeaning(level, keyword);
        else
            data = kanjiModel.getAllKanjiByAllFilters(level, keyword);
        try {
            KanjiAdapter customData = new KanjiAdapter(inflater, data);
            listView.setAdapter(customData);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void populate(ListView listView, LayoutInflater inflater, String keyword)
    {
        ArrayList<Kanji> data = kanjiModel.getAllKanji();
        try {
            KanjiAdapter customData = new KanjiAdapter(inflater, data);
            listView.setAdapter(customData);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private void init(View view, final LayoutInflater inflater) {
        listView = view.findViewById(R.id.listView);
        this.inflater = inflater;
        populate(listView, inflater, "");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.onItemKanjiClickedListener(view.getTag() + "");
                } else {
                    throw new IllegalArgumentException("Please pass the listener");
                }
            }
        });
    }
}
