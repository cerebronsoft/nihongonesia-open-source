package com.introvesia.nihongonesia.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.introvesia.nihongonesia.R;
import com.introvesia.nihongonesia.adapters.PracticeKanjiAdapter;
import com.introvesia.nihongonesia.data.PracticeKanji;
import com.introvesia.nihongonesia.models.PracticeKanjiModel;

import java.util.ArrayList;

/**
 * Created by asus on 29/07/2017.
 */

public class ResultKanjiFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.word_layout, container, false);
        init(view, inflater);
        return view;
    }

    private void init(View view, LayoutInflater inflater) {
        ListView listView = view.findViewById(R.id.listView);
        PracticeKanjiModel practiceKanjiModel = new PracticeKanjiModel();
        ArrayList<PracticeKanji> data = practiceKanjiModel.getAllKanji();
        try {
            PracticeKanjiAdapter customData = new PracticeKanjiAdapter(inflater, data);
            listView.setAdapter(customData);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
