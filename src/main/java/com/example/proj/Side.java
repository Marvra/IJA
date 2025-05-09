/**
 * @author Martin Vrablec
 * @author Jakub Ramaseuski
 */
package com.example.proj;

/**
 * Enum representing sides of game node
 */
public enum Side {
    EAST,
    SOUTH,
    WEST,
    NORTH;

    /**
     * Converts this enum to a string
     *
     * @return A string representing this enum
     */
    @Override
    public String toString() {
        switch (this) {
            case NORTH:
                return "NORTH";
            case EAST:
                return "EAST";
            case SOUTH:
                return "SOUTH";
            case WEST:
                return "WEST";
            default:
                return " ";
        }
    }

    /**
     * Returns the opposite direction of the current side
     *
     * @return The opposite Side of the current Side
     */
    public Side opposite() {
        switch (this) {
            case NORTH:
                return SOUTH;
            case EAST:
                return WEST;
            case SOUTH:
                return NORTH;
            case WEST:
                return EAST;
            default:
                return null;
        }
    }
}
