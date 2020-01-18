package com.introvesia.nihongonesia.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.introvesia.nihongonesia.R;
import com.introvesia.nihongonesia.adapters.WordAdapter;
import com.introvesia.nihongonesia.data.Word;
import com.introvesia.nihongonesia.models.WordModel;

import java.util.ArrayList;

/**
 * Created by asus on 01/07/2017.
 */

public class WordFragment extends Fragment {
    private WordModel model = new WordModel();
    private ListView listView;
    private LayoutInflater inflater;
    private String kanji;
    private KotobaFragmentListener listener;

    public void setKanji(String kanji) {
        this.kanji = kanji;
    }

    public interface KotobaFragmentListener {
        void onItemKotobaClickedListener(String valueClicked);
    }

    public void registerForListener(KotobaFragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.word_layout, container, false);
        init(view, inflater);
        return view;
    }

    public void find(String keyword) {
        if (listView == null) return;
        ArrayList<Word> data = model.findKotoba(keyword);
        try {
            WordAdapter customData = new WordAdapter(inflater, data);
            listView.setAdapter(customData);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void populate(ListView listView, LayoutInflater inflater)
    {
        ArrayList<Word> data;
        if (kanji != null)
            data = model.getAllKotoba(kanji);
        else
            data = model.findKotoba("");
        try {
            WordAdapter customData = new WordAdapter(inflater, data);
            listView.setAdapter(customData);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private void init(View view, final LayoutInflater inflater) {
        listView = view.findViewById(R.id.listView);
        this.inflater = inflater;
        populate(listView, inflater);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.onItemKotobaClickedListener(view.getTag() + "");
                } else {
                    throw new IllegalArgumentException("Please pass the listener");
                }
            }
        });
    }
}
