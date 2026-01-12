package com.scarycat.earth.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.scarycat.earth.R;

import java.util.List;

public class LevelAdapter extends BaseAdapter {

    private Context context;
    private List<LevelItem> levels;

    public LevelAdapter(Context context, List<LevelItem> levels) {
        this.context = context;
        this.levels = levels;
    }

    @Override
    public int getCount() {
        return levels.size();
    }

    @Override
    public Object getItem(int position) {
        return levels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_level, parent, false);
        }

        Button btnLevel = convertView.findViewById(R.id.btnLevel);
        TextView tvStars = convertView.findViewById(R.id.tvStars);

        LevelItem item = levels.get(position);

        btnLevel.setText(String.valueOf(item.levelNumber));
        btnLevel.setEnabled(item.unlocked);

        tvStars.setText(getStarsText(item.stars));


        return convertView;
    }
    private String getStarsText(int stars) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stars; i++) {
            sb.append("⭐");
        }
        return sb.toString();
    }
}