package ija.ija2024.homework2.common;

public enum Side {
    EAST,
    SOUTH,
    WEST,
    NORTH;

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

}
