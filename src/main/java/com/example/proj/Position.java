package com.example.proj;

import java.util.Objects;

public class Position {
    int row;
    int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

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
