package ija.ija2024.homework2.game;

import ija.ija2024.homework2.common.GameNode;
import ija.ija2024.homework2.common.Position;
import ija.ija2024.homework2.common.Side;
import ija.ija2024.homework2.common.Type;
import ija.ija2024.tool.common.ToolEnvironment;
import ija.ija2024.tool.common.ToolField;
import ija.ija2024.tool.common.Observable;
import ija.ija2024.tool.common.Observable.Observer;
import ija.ija2024.tool.view.FieldView;

public class Game implements ToolEnvironment, Observer {

    int rows;
    int cols;
    GameNode[][] nodes;
    boolean hasPower = false;
    public int powerRow = -1;
    public int powerCol = -1;
    private boolean notifyRun = false;

    @Override
    public ToolField fieldAt(int row, int col) {

        return checkParams(row,col) ? this.nodes[row][col] : null;
    }

    public Game(int rows, int cols) { 
        this.rows = rows;
        this.cols = cols;
    }

    public static Game create (int rows, int cols) {

        if (rows < 1 || cols < 1) {
            throw new IllegalArgumentException();
        }

        Game game = new Game(rows, cols);

        // +1 because [1,1] is top left
        game.nodes = new GameNode[rows+1][cols+1];

        for(int i = 1; i <= rows; i++) {
            for(int j = 1; j <= cols; j++) {
                GameNode node = new GameNode(Type.EMPTY, new Position(i, j), new Side[0]);

                game.nodes[i][j] = node;
            }
        }

        return game;
    }

    @Override
    public void update(Observable o) {
        if (notifyRun) return; 
        
        notifyRun = true;

        GameNode node = (GameNode) o;

        resetLights();
        traverseConnected(powerRow, powerCol, this.nodes[powerRow][powerCol], true);

        notifyRun = false;
    }

    public void resetLights() {
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= cols; j++) {
                if (this.nodes[i][j].type == Type.EMPTY || this.nodes[i][j].type == Type.POWER) {
                    continue;
                }

                this.nodes[i][j].setLight(false, true);
            }
        }
    }

    public void init() {

        if(powerRow == -1 && powerCol == -1) {
            return;
        }

        this.nodes[powerRow][powerCol].setLight(true, false);

        traverseConnected(powerRow, powerCol, this.nodes[powerRow][powerCol], false);
    }

    public void traverseConnected(int row, int col, GameNode node, boolean notify) {
        for(Side s : node.sides) {
            switch (s) {
                case NORTH:
                    traverseNodes(row-1, col, s, notify);
                    break;
                case SOUTH:
                    traverseNodes(row+1, col, s, notify);
                    break;
                case WEST:
                    traverseNodes(row, col-1, s, notify);
                    break;
                case EAST:
                    traverseNodes(row, col+1, s, notify);
                    break;
            }
        }
    }

    public void traverseNodes(int row, int col, Side sideFromConnected, boolean notify)
    {
        GameNode currNode = this.nodes[row][col];
        if(currNode.isEmpty() || currNode.light()) {
            return;
        }

        switch (sideFromConnected) {
            case NORTH:
                if(!currNode.containsConnector(Side.SOUTH)) return;
                break;
            case SOUTH:
                if(!currNode.containsConnector(Side.NORTH)) return;
                break;
            case WEST:
                if(!currNode.containsConnector(Side.EAST)) return;
                break;
            case EAST:
                if(!currNode.containsConnector(Side.WEST)) return;
                break;
        }

        currNode.setLight(true, notify);

        if(currNode.type == Type.BULB) {
            return;
        }

        traverseConnected(row, col, currNode, notify);

        return;
    }


    public GameNode createNodeBase(Position p, Type t, Side... sides) {

        this.nodes[p.getRow()][p.getCol()].setType(t);
        this.nodes[p.getRow()][p.getCol()].setSides(sides);
        this.nodes[p.getRow()][p.getCol()].addObserver(this);
        return this.nodes[p.getRow()][p.getCol()];
    }

    public GameNode createLinkNode(Position p, Side... sides) {

        return checkNode(p, Type.LINK, sides) ? createNodeBase(p, Type.LINK, sides) : null;
    }

    public GameNode createPowerNode(Position p, Side... sides) {
        this.powerRow = p.getRow();
        this.powerCol = p.getCol();
        return checkNode(p, Type.POWER, sides) ? createNodeBase(p, Type.POWER, sides) : null;
    }

    public GameNode createBulbNode(Position p, Side... sides) {

        return checkNode(p, Type.BULB, sides) ? createNodeBase(p, Type.BULB, sides) : null;
    }

    public int rows() {
        return this.rows;
    }

    public int cols() {
        return this.cols;
    }

    public GameNode node(Position p) {
        return checkParams(p.getRow(), p.getCol()) ? this.nodes[p.getRow()][p.getCol()] : null;
    }

    public boolean checkParams(int row, int col) {
        if (row < 1 || row > this.rows || col < 1 || col > this.cols) {
            return false;
        }
        return true;
    }

    public boolean checkSides(int row, int col, Side... sides) {
        for (Side s : sides) {
            if ((s == Side.NORTH && row == 1) || (s == Side.SOUTH && row == this.rows) || (s == Side.WEST && col == 1) || (s == Side.EAST && col == this.cols)) {
                return false;
            }
        }
        return true;
    }

    public boolean checkNode(Position p, Type t, Side... sides) {

        // basic check for every node 
        if (checkParams(p.getRow(), p.getCol()) == false) {
            return false;
        }

        if (checkSides(p.getRow(), p.getCol(), sides) == false) {
            return false;
        } 

        // checking links
        if((sides.length < 2 || sides.length > 4) && t == Type.LINK) {
            return false;
        } else if (sides.length <= 0 || sides.length > 4) {
            return false;
        }

        // power check
        if (t == Type.POWER) {
            if(hasPower == false)
            {
                this.hasPower = true;
            } else {
                return false;
            }
        }

        return true;
    }
}
