package com.example.proj;

public enum Type {
    BULB,
    LINK,
    POWER,
    EMPTY;

    @Override
    public String toString() {
        switch (this) {
            case BULB:
                return "B";
            case LINK:
                return "L";
            case POWER:
                return "P";
            case EMPTY:
                return "E";
            default:
                return " ";
        }
    }

}