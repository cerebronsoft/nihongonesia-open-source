package com.introvesia.nihongonesia.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.introvesia.nihongonesia.R;
import com.introvesia.nihongonesia.data.Word;

import java.util.ArrayList;

/**
 * Created by asus on 01/07/2017.
 */

public class WordAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<Word> list;

    public WordAdapter(LayoutInflater inflater, ArrayList<Word> list) {
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
            row = inflater.inflate(R.layout.list_view_row, parent, false);
        } else {
            row = convertView;
        }
        row.setTag(list.get(i).getId());
        TextView v = row.findViewById(R.id.bunpo);
        v.setText(list.get(i).getKanji());
        v = row.findViewById(R.id.romaji);
        v.setText(list.get(i).getRomaji());
        v = row.findViewById(R.id.meaning);
        v.setText(list.get(i).getMeaning());
        return row;
    }
}
