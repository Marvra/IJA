package com.example.proj;

import ija.ija2024.tool.common.AbstractObservableField;
import ija.ija2024.tool.common.ToolField;

import java.util.Arrays;

public class GameNode extends AbstractObservableField implements ToolField {

    public Side[] sides;

    public Type type;
    Position position;
    boolean light = false;

    public boolean notifyView = false;

    public GameNode(Type type, Position position, Side ... sides) {
        this.type = type;
        this.position = position;

        if (sides != null) {
            this.sides = new Side[sides.length];
            this.sides = sides;
        } else {
            this.sides = new Side[0];
        }
    }


    public Position getPosition() {
        return this.position;
    }

    public boolean light() {
        return this.light;
    }

    public void turn() {
        for (int i = 0; i < this.sides.length; i++) {
            switch (this.sides[i]) {
                case NORTH:
                    this.sides[i] = Side.EAST;
                    break;
                case EAST:
                    this.sides[i] = Side.SOUTH;
                    break;
                case SOUTH:
                    this.sides[i] = Side.WEST;
                    break;
                case WEST:
                    this.sides[i] = Side.NORTH;
                    break;
                default:
                    break;
            }   
        }
        notifyObservers();
    }

    public void setType(Type t) {
        this.type = t;
    }

    public void setSides(Side ... s) {
        this.sides = s;
    }
    

    public void setLight(boolean l, boolean notify) {
        // run for init
        if (!notify) {
            this.light = l;
            return;
        }
        
        // run only for observer notify
        if (this.light != l) {
            this.light = l;
            notifyObservers();
        }
    }
    
    public boolean isEmpty() {
        return this.type == Type.EMPTY ? true : false;
    }

    public boolean isLink() {
        return this.type == Type.LINK ? true : false;
    }

    public boolean isBulb() {
        return this.type == Type.BULB ? true : false;
    }

    public boolean isPower() {
        return this.type == Type.POWER ? true : false;
    }

    public boolean containsConnector(Side s) {

        if (this.sides == null) {
            return false;
        }

        for (Side side : this.sides) {
            if(side == s) return true;
        }
        return false;
    }
    public boolean east() {
        return containsConnector(Side.EAST);
    }

    public boolean west() {
        return containsConnector(Side.WEST);
    }

    public boolean south() {
        return containsConnector(Side.SOUTH);
    }

    public boolean north() {
        return containsConnector(Side.NORTH);
    }

    @Override
    public String toString() {

        String sidesString = "";
        int sidesLength = this.sides.length;
        Arrays.sort(this.sides);

        for (int i = 0; i < sidesLength; i++) {
            sidesString += this.sides[i].toString();
            sidesString += i != this.sides.length-1 ? "," : "";
        }

        return "{" + this.type.toString() + "[" + this.position.getRow() + "@" + this.position.getCol() + "][" + sidesString + "]}";
    }

}
