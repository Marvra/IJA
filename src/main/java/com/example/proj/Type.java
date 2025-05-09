/**
 * @author Martin Vrablec
 * @author Jakub Ramaseuski
 */
package com.example.proj;

/**
 * Enum representing the type of game node.
 */
public enum Type {
    BULB,
    LINK,
    POWER,
    EMPTY;

    /**
     * Converts this enum to a string
     *
     * @return string representing this enum
     */
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