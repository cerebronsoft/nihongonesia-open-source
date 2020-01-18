package com.introvesia.nihongonesia.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.introvesia.nihongonesia.R;
import com.introvesia.nihongonesia.data.PracticeKanji;

import java.util.ArrayList;

/**
 * Created by asus on 29/07/2017.
 */

public class PracticeKanjiAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<PracticeKanji> list;

    public PracticeKanjiAdapter(LayoutInflater inflater, ArrayList<PracticeKanji> list) {
        this.inflater = inflater;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View row;
        if(convertView == null) {
            row = inflater.inflate(R.layout.kanji_row, parent, false);
        } else {
            row = convertView;
        }
        int point = list.get(i).getCorrectCount();
        if (point == 0)
            row.setBackgroundColor(Color.parseColor("#ffdddd"));
        else if (point == PracticeKanji.getMaxCorrectCount())
            row.setBackgroundColor(Color.parseColor("#ddffdd"));
        else
            row.setBackgroundColor(Color.parseColor("#ffffdd"));
        row.setTag(list.get(i).getKanji());
        TextView v = row.findViewById(R.id.kanji);
        v.setText(list.get(i).getKanji());
        v = row.findViewById(R.id.level);
        float percentage = ((float)point / (float)PracticeKanji.getMaxCorrectCount()) * 100;
        if (percentage > 0 && percentage < 100)
            v.setText(list.get(i).getLevel() + " · " + percentage + "%");
        else
            v.setText(list.get(i).getLevel());
        v = row.findViewById(R.id.meaning);
        v.setText(list.get(i).getMeaning());
        return row;
    }
}
