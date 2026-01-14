package com.scarycat.earth.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.scarycat.earth.R;

import java.util.List;
import java.util.Objects;

public class LevelAdapter extends BaseAdapter {

    private Context context;
    private List<LevelItem> levels;
    TextView tvlevel,tvStars;

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

        ImageButton btnLevel = convertView.findViewById(R.id.btnLevel);
        TextView tvStars = convertView.findViewById(R.id.tvStars);
        TextView tvLevel = convertView.findViewById(R.id.btnLeveltxt);

        LevelItem item = levels.get(position);

        tvLevel.setText(String.valueOf(item.levelNumber));
        tvStars.setText(getStarsText(item.stars));

        // ----------- SET BACKGROUND -----------
        if (item.unlocked) {
            btnLevel.setBackgroundResource(R.drawable.unlocklevel_btn);
        } else {
            btnLevel.setBackgroundResource(R.drawable.locklevel_btn);
        }

        // ----------- CLICK HANDLING -----------
        convertView.setOnClickListener(v -> {

            if (!item.unlocked) {
                Toast.makeText(context, "Level locked 🔒", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences prefs =
                    context.getSharedPreferences("earth_game_prefs", MODE_PRIVATE);

            int hearts = prefs.getInt("hearts", 0);

            if (hearts <= 0) {
                Toast.makeText(context,
                        "You have no hearts left!", Toast.LENGTH_LONG).show();
                return;
            }

            context.startActivity(
                    LevelRouter.getLevel(context, item.levelNumber)
            );
        });

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