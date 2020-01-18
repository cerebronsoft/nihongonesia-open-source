package com.introvesia.nihongonesia.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
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
 * Created by asus on 04/07/2017.
 */

public class KanjiReadingFilterFragment extends Fragment {
    private String yomi;
    private KanjiModel kanjiModel = new KanjiModel();
    private KanjiYomiFilterFragmentListener listener;

    public interface KanjiYomiFilterFragmentListener {
        void onItemYomiKanjiClickedListener(String valueClicked);
    }

    public void registerForListener(KanjiYomiFilterFragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.word_layout, container, false);
        init(view, inflater);
        return view;
    }

    public void populate(ListView listView, LayoutInflater inflater)
    {
        ArrayList<Kanji> data = kanjiModel.getAllKanjiByExactYomikata(yomi);
        try {
            KanjiAdapter customData = new KanjiAdapter(inflater, data);
            listView.setAdapter(customData);
        }catch(Exception ex) {
            Log.v(getClass().getName(), ex.getMessage());
        }
    }

    private void init(View view, LayoutInflater inflater) {
        ListView listView = view.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.onItemYomiKanjiClickedListener(view.getTag() + "");
                } else {
                    throw new IllegalArgumentException("Please pass the listener");
                }
            }
        });
        populate(listView, inflater);
    }

    public void setYomi(String yomi) {
        this.yomi = yomi;
    }
}
