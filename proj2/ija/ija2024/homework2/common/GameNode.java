package ija.ija2024.homework2.common;

public class GameNode {

    Type type;
    Position position;
    Side[] sides;

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
        return false;
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
    }

    public void setType(Type t) {
        this.type = t;
    }

    public void setSides(Side ... s) {
        this.sides = s;
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

    public String toString() {

        String sidesString = "";
        int sidesLength = this.sides.length;

        for (int i = 0; i < sidesLength; i++) {
            sidesString += this.sides[i].toString();
            sidesString += i != this.sides.length-1 ? "," : "";
        }

        return "{" + this.type.toString() + "[" + this.position.getRow() + "@" + this.position.getCol() + "][" + sidesString + "]}";
    }

}
