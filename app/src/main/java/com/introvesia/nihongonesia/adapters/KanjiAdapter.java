package com.introvesia.nihongonesia.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.introvesia.nihongonesia.R;
import com.introvesia.nihongonesia.data.Kanji;

import java.util.ArrayList;

/**
 * Created by asus on 01/07/2017.
 */

public class KanjiAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<Kanji> list;

    public KanjiAdapter(LayoutInflater inflater, ArrayList<Kanji> list) {
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
        row.setTag(list.get(i).getKanji());
        TextView v = row.findViewById(R.id.kanji);
        v.setText(list.get(i).getKanji());
        v = row.findViewById(R.id.level);
        v.setText(list.get(i).getLevel());
        v = row.findViewById(R.id.meaning);
        v.setText(list.get(i).getMeaning());
        return row;
    }
}
