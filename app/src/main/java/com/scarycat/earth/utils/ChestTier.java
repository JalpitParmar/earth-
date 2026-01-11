package com.scarycat.earth.utils;

public enum ChestTier {

    BRONZE(1),
    SILVER(2),
    GOLD(3),
    EPIC(4),
    MYTHIC(5);

    public final int multiplier;

    ChestTier(int multiplier) {
        this.multiplier = multiplier;
    }

    public ChestTier next() {
        switch (this) {
            case BRONZE: return SILVER;
            case SILVER: return GOLD;
            case GOLD: return EPIC;
            case EPIC: return MYTHIC;
            case MYTHIC: return BRONZE;
            default: return BRONZE;
        }
    }
}
