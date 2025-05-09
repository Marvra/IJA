/**
 * @author Martin Vrablec
 *  * @author Jakub Ramaseuski
 */
package com.example.proj;

import java.util.Objects;

/**
 * Class representing a position on the game board
 * position is row and column
 */
public class Position {
    int row;
    int col;

    /**
     * Constructor for Position class
     *
     * @param row  the row of the position
     * @param col  the column of the position
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Gets row of the position
     *
     * @param row  the row of the position
     * @param col  the column of the position
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Gets column of the position
     *
     * @param row  the row of the position
     * @param col  the column of the position
     */
    public int getCol() {
        return this.col;
    }

    /**

     * Overrides equals method
     *
     * @return true if the position is eqqual
     */
    @Override
    public boolean equals(Object o) {
        
        if (o == this) {
            return true;
        }

        if (!(o instanceof Position)) {
            return false;
        }

        Position p = (Position) o;

        return this.row == p.row && this.col == p.col;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.row, this.col);
    }
    @Override
    public String toString() {
        return "(" + this.row + ", " + this.col + ")";
    }
}
