package com.scarycat.earth.boosters;

public enum BoosterType {
    NONE(""),
    HAMMER("hammer"),
    BOMB("bomb"),
    SHUFFLE("swap"),
    COLOR_BOMB("color_bomb");

    public final String key;

    BoosterType(String key) {
        this.key = key;
    }
}
