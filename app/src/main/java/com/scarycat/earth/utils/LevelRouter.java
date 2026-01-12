package com.scarycat.earth.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.scarycat.earth.levels.*;
import com.scarycat.earth.Meanu;
import com.scarycat.earth.Meanu;

public class LevelRouter {

    public static Intent getNextLevel(Context context, int currentLevel) {
        Log.d("APP_LOG", "Running: " + currentLevel );

        currentLevel +=1;
        Log.d("APP_LOG", "Next: " + currentLevel );
        switch (currentLevel) {
            case 2: return new Intent(context, Level2.class);
            case 3: return new Intent(context, Level3.class);
            case 4: return new Intent(context, Level4.class);
            case 5: return new Intent(context, Level5.class);
            case 6: return new Intent(context, Level6.class);
            case 7: return new Intent(context, Level7.class);
            case 8: return new Intent(context, Level8.class);
            case 9: return new Intent(context, Level9.class);
            case 10: return new Intent(context, Level10.class);
            default:
                return new Intent(context, Meanu.class); // no more levels
        }
    }
    public static Intent getLevel(Context context, int level) {
        switch (level) {
            case 1: return new Intent(context, Level1.class);
            case 2: return new Intent(context, Level2.class);
            case 3: return new Intent(context, Level3.class);
            case 4: return new Intent(context, Level4.class);
            case 5: return new Intent(context, Level5.class);
            case 6: return new Intent(context, Level6.class);
            case 7: return new Intent(context, Level7.class);
            case 8: return new Intent(context, Level8.class);
            case 9: return new Intent(context, Level9.class);
            case 10: return new Intent(context, Level10.class);
            default:
                return new Intent(context, Meanu.class);
        }
    }
    
}
