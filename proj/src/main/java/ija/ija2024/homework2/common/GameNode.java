package ija.ija2024.homework2.common;

import ija.ija2024.tool.common.AbstractObservableField;
import ija.ija2024.tool.common.ToolField;
import java.util.Arrays;
import java.util.HashSet;

public class GameNode extends AbstractObservableField implements ToolField {

    public HashSet<Side> sides;

    public Type type;
    Position position;
    boolean light = false;

    public boolean notifyView = false;

    public GameNode(Type type, Position position, Side ... sides) {
        this.type = type;
        this.position = position;

        if (sides != null) {
            this.sides = new HashSet<>(Arrays.asList(sides));
        } else {
            this.sides = new HashSet<>();
        }
    }


    public Position getPosition() {
        return this.position;
    }

    public boolean light() {
        return this.light;
    }

    public void turn() {
        HashSet<Side> newSides = new HashSet<>();
        for (Side side : this.sides) {
            switch (side) {
                case NORTH:
                    newSides.add(Side.EAST);
                    break;
                case EAST:
                    newSides.add(Side.SOUTH);
                    break;
                case SOUTH:
                    newSides.add(Side.WEST);
                    break;
                case WEST:
                    newSides.add(Side.NORTH);
                    break;
                default:
                    break;
            }
        }
        this.sides = newSides;
        notifyObservers();
    }

    public void setType(Type t) {
        this.type = t;
    }

    public void setSides(Side ... s) {
        this.sides = new HashSet<>(Arrays.asList(s));
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
        return this.sides.contains(s);
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
        StringBuilder sidesStringBuilder = new StringBuilder();
        for (Side s : this.sides) {
            sidesStringBuilder.append(s.toString()).append(",");
        }

        // Remove the trailing comma if sides is not empty
        if (!this.sides.isEmpty()) {
            sidesStringBuilder.setLength(sidesStringBuilder.length() - 1);
        }

        return "{" + this.type.toString() + "[" + this.position.getRow() + "@" + this.position.getCol() + "]" + this.sides.toString() + "}";
    }

}
