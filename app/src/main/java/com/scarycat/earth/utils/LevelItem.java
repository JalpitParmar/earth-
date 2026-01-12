package com.scarycat.earth.utils;

public class LevelItem {
    public int levelNumber;
    public boolean unlocked;
    public int stars;

    public LevelItem(int levelNumber, boolean unlocked, int stars) {
        this.levelNumber = levelNumber;
        this.unlocked = unlocked;
        this.stars = stars;
    }
}