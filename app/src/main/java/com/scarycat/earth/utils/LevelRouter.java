package com.scarycat.earth.utils;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.scarycat.earth.levels.*;
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
            case 11: return new Intent(context, Level11.class);
            case 12: return new Intent(context, Level12.class);
            case 13: return new Intent(context, Level13.class);
            case 14: return new Intent(context, Level14.class);
            case 15: return new Intent(context, Level15.class);
            case 16: return new Intent(context, Level16.class);
            case 17: return new Intent(context, Level17.class);
            case 18: return new Intent(context, Level18.class);
            case 19: return new Intent(context, Level19.class);
            case 20: return new Intent(context, Level20.class);
            case 21: return new Intent(context, Level21.class);
            case 22: return new Intent(context, Level22.class);
            case 23: return new Intent(context, Level23.class);
            case 24: return new Intent(context, Level24.class);
            case 25: return new Intent(context, Level25.class);
            case 26: return new Intent(context, Level26.class);
            case 27: return new Intent(context, Level27.class);
            case 28: return new Intent(context, Level28.class);
            case 29: return new Intent(context, Level29.class);
            case 30: return new Intent(context, Level30.class);
            case 31: return new Intent(context, Level31.class);
            case 32: return new Intent(context, Level32.class);
            case 33: return new Intent(context, Level33.class);
            case 34: return new Intent(context, Level34.class);
            case 35: return new Intent(context, Level35.class);
            case 36: return new Intent(context, Level36.class);
            case 37: return new Intent(context, Level37.class);
            case 38: return new Intent(context, Level38.class);
            case 39: return new Intent(context, Level39.class);
            case 40: return new Intent(context, Level40.class);
            case 41: return new Intent(context, Level41.class);
            case 42: return new Intent(context, Level42.class);
            case 43: return new Intent(context, Level43.class);
            case 44: return new Intent(context, Level44.class);
            case 45: return new Intent(context, Level45.class);
            case 46: return new Intent(context, Level46.class);
            case 47: return new Intent(context, Level47.class);
            case 48: return new Intent(context, Level48.class);
            case 49: return new Intent(context, Level49.class);
            case 50: return new Intent(context, Level50.class);
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
            case 11: return new Intent(context, Level11.class);
            case 12: return new Intent(context, Level12.class);
            case 13: return new Intent(context, Level13.class);
            case 14: return new Intent(context, Level14.class);
            case 15: return new Intent(context, Level15.class);
            case 16: return new Intent(context, Level16.class);
            case 17: return new Intent(context, Level17.class);
            case 18: return new Intent(context, Level18.class);
            case 19: return new Intent(context, Level19.class);
            case 20: return new Intent(context, Level20.class);
            case 21: return new Intent(context, Level21.class);
            case 22: return new Intent(context, Level22.class);
            case 23: return new Intent(context, Level23.class);
            case 24: return new Intent(context, Level24.class);
            case 25: return new Intent(context, Level25.class);
            case 26: return new Intent(context, Level26.class);
            case 27: return new Intent(context, Level27.class);
            case 28: return new Intent(context, Level28.class);
            case 29: return new Intent(context, Level29.class);
            case 30: return new Intent(context, Level30.class);
            case 31: return new Intent(context, Level31.class);
            case 32: return new Intent(context, Level32.class);
            case 33: return new Intent(context, Level33.class);
            case 34: return new Intent(context, Level34.class);
            case 35: return new Intent(context, Level35.class);
            case 36: return new Intent(context, Level36.class);
            case 37: return new Intent(context, Level37.class);
            case 38: return new Intent(context, Level38.class);
            case 39: return new Intent(context, Level39.class);
            case 40: return new Intent(context, Level40.class);
            case 41: return new Intent(context, Level41.class);
            case 42: return new Intent(context, Level42.class);
            case 43: return new Intent(context, Level43.class);
            case 44: return new Intent(context, Level44.class);
            case 45: return new Intent(context, Level45.class);
            case 46: return new Intent(context, Level46.class);
            case 47: return new Intent(context, Level47.class);
            case 48: return new Intent(context, Level48.class);
            case 49: return new Intent(context, Level49.class);
            case 50: return new Intent(context, Level50.class);

            default:
                return new Intent(context, Meanu.class);
        }
    }
    
}
