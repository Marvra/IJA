/**
 * @author Martin Vrablec
 * @author Jakub Ramaseuski
 * 
 *  class representing a game node of a game 
 *  
 */
package com.example.proj;

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

    /**
     * Constructor for gamenode class
     *
     * @param type type of game node
     * @param position position of game node
     * @param sides sides of game node
     */
    public GameNode(Type type, Position position, Side ... sides) {
        this.type = type;
        this.position = position;

        if (sides != null) {
            this.sides = new HashSet<>(Arrays.asList(sides));
        } else {
            this.sides = new HashSet<>();
        }
    }

    /**
     * get position of game node
     * 
     * @return position of game node
     */

    public Position getPosition() {
        return this.position;
    }

    /**
     * get light of game node
     * 
     * @return true if game node is light
     */
    public boolean light() {
        return this.light;
    }

    /**
     * turns the game node to the right 90 degrees (NORTH -> EAST -> etc.)
     */
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

    /**
     * set type of game node
     * 
     * @param t type to set
     */
    public void setType(Type t) {
        this.type = t;
    }

    /**
     * set sides of game node 
     * 
     * @param s sides to set
     */
    public void setSides(Side ... s) {
        this.sides = new HashSet<>(Arrays.asList(s));
    }


    /**
     * set light of game node
     * 
     * @param l light to set
     * @param notify true if u want to notify observers
     */
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

    /**
     * check if game node is empty
     * 
     * @return true if game node is empty
     */
    public boolean isEmpty() {
        return this.type == Type.EMPTY ? true : false;
    }

    /**
     * check if game node is link
     * 
     * @return true if game node is link
     */
    public boolean isLink() {
        return this.type == Type.LINK ? true : false;
    }

    /**
     * check if game node is bulb
     * 
     * @return true if game node is bulb
     */
    public boolean isBulb() {
        return this.type == Type.BULB ? true : false;
    }

    /**
     * check if game node is power
     * 
     * @return true if game node is power
     */
    public boolean isPower() {
        return this.type == Type.POWER ? true : false;
    }

    /**
     * check if game node contains connector
     * 
     * @param s side to check
     * @return true if game node contains connector
     */
    public boolean containsConnector(Side s) {

        if (this.sides == null) {
            return false;
        }
        return this.sides.contains(s);
    }

    /**
     * check if game node contains EAST
     * 
     * @return true if game node contains EAST
     */
    public boolean east() {
        return containsConnector(Side.EAST);
    }

    /**
     * check if game node contains WEST
     * 
     * @return true if game node contains WEST
     */
    public boolean west() {
        return containsConnector(Side.WEST);
    }

    /**
     * check if game node contains SOUTH
     * 
     * @return true if game node contains SOUTH
     */
    public boolean south() {
        return containsConnector(Side.SOUTH);
    }

    /**
     * check if game node contains NORTH
     * 
     * @return true if game node contains NORTH
     */
    public boolean north() {
        return containsConnector(Side.NORTH);
    }

    /**
     * Overrides equals method
     *
     * @return true if the game node is equal
     */
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
