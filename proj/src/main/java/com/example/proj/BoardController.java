package com.example.proj;

import ija.ija2024.homework2.common.Position;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import ija.ija2024.homework2.game.Game;
import ija.ija2024.homework2.common.GameNode;
import ija.ija2024.homework2.common.Type;
import ija.ija2024.homework2.common.Side;


import javafx.scene.input.MouseEvent;

import java.util.List;

public class BoardController {
    private int imageWidth = 50;
    private int imageHeight = 50;

    @FXML
    GridPane gridBoard;
    @FXML
    Button nextMoveBtn;
    @FXML
    Button prevMoveBtn;
    @FXML
    Button playLogBtn;

    private Game createdGame;
    private List<String> logData;
    private int logLine = 0;


    private ImageView[][] boardTitles;

    public void createBoard(Game game) {

        if(game == null) return;

        boardTitles = new ImageView[game.rows()][game.cols()];


        ColorAdjust darkenTitle =  new ColorAdjust();
        darkenTitle.setBrightness(-0.5);

        System.out.println("START BOARD CREATION");

        for (int row = 0; row < 4; row++) { // POZOR 4 LEBO BASIC GENERATE JE TERAZ NA 4X4 POLI
            for (int col = 0; col < 4; col++) { // POZOR 4 LEBO BASIC GENERATE JE TERAZ NA 4X4 POLI

                // SKIP EMPTY

                GameNode node = game.node(new Position(row+1,col+1));


                Image image = selectCorrectImageTitle(node);

                ImageView title = new ImageView(image);

                boardTitles[row][col] = title;
                gridBoard.add(title, col, row);

                if (node.isEmpty()) continue; // SKIP EMPTY

                title.setPickOnBounds(true);
                title.setOnMouseEntered(enterHoverEvent -> title.setEffect(darkenTitle));
                title.setOnMouseExited(exitHoverEvent -> title.setEffect(null));
                title.setOnMouseClicked(mouseClickedEvent -> printClickedTitle(title, node));
                System.out.println(node.toString()); // PRINT ONLY NOT EMPTY
            }
        }
        createdGame = game;
        System.out.println("END BOARD CREATION");
    }

    private Image selectCorrectImageTitle(GameNode node) {

        Side[] sides = node.sides;
        Type type = node.type;
        String base = type.toString(); // B L P

        if (type == Type.EMPTY) {
            base = base + ".png"; // B.png
            return new Image(getClass().getResourceAsStream(base), imageHeight, imageWidth, false, false);
        }

        if (type == Type.BULB) {
            base = base + ".png"; // B.png
            return new Image(getClass().getResourceAsStream(base), imageHeight, imageWidth, false, false);
        }
        //System.out.println(base);

        int count = sides.length;

        if(count == 2) {
            if (node.north() && node.south() || node.west() && node.east()) { // ROVNA NODE
                base = base + "_" + count + ".png";
            } else { // L NODE
                base = base + "L_" + count + ".png";
            }
        } else {
            base = base + "_" + count + ".png"; // napr.: L_2,P_3 etc.
        }

        return new Image(getClass().getResourceAsStream(base), imageHeight, imageWidth, false, false);
    }


    private void printClickedTitle (ImageView title, GameNode node) {

        Integer hoveredRow = GridPane.getRowIndex(title);
        Integer hoveredCol = GridPane.getColumnIndex(title);

        int rowIdx = hoveredRow != null ? hoveredRow : 0;
        int colIdx = hoveredCol != null ? hoveredCol : 0;
        node.turn();
        System.out.println(node.toString());

        title.setRotate((title.getRotate() + 90) % 360);
    }

    public void logMode(List<String> log) {
        logData = log;
        ColorAdjust darkenBoard =  new ColorAdjust();
        darkenBoard.setBrightness(-0.5);
        gridBoard.setEffect(darkenBoard);
        //gridBoard.setDisable(true);

        nextMoveBtn.setDisable(false);
        playLogBtn.setDisable(false);

        nextMoveBtn.setOpacity(1);
        prevMoveBtn.setOpacity(0.5);
        playLogBtn.setOpacity(1);
    }

    public void nextMove(ActionEvent event) {
        if (logLine < logData.size()) {
            String lineToUpdate = logData.get(logLine);
            updateGame(lineToUpdate);
            logLine++;

            prevMoveBtn.setDisable(false);
            prevMoveBtn.setOpacity(1.0);

            if (logLine == logData.size()) {
                nextMoveBtn.setDisable(true);
                nextMoveBtn.setOpacity(0.5);
            }
        }
    }

    public void prevMove(ActionEvent event) {
        if (logLine > 0) {
            logLine--;
            String lineToUpdate = logData.get(logLine);
            updateGame(lineToUpdate);

            nextMoveBtn.setDisable(false);
            nextMoveBtn.setOpacity(1.0);

            if (logLine == 0) {
                prevMoveBtn.setDisable(true);
                prevMoveBtn.setOpacity(0.5);
            }
        }
    }


    private void updateGame(String line) {
        Position position = LogController.stringToGameNode(line, createdGame, true);
        printClickedTitle(boardTitles[position.getRow()-1][position.getCol()-1], createdGame.node(position));

    }

    public void playMode(ActionEvent event) {
        // Disable log control buttons
        nextMoveBtn.setDisable(true);
        prevMoveBtn.setDisable(true);
        playLogBtn.setDisable(true);

        nextMoveBtn.setOpacity(0);
        prevMoveBtn.setOpacity(0);
        playLogBtn.setOpacity(0);

        // Remove board darkening to show it’s interactive again
        gridBoard.setEffect(null);

        // Prevent further log interaction
        logData = null;
        logLine = 0;

        // Allow user to click nodes freely
//        for (int row = 0; row < boardTitles.length; row++) {
//            for (int col = 0; col < boardTitles[row].length; col++) {
//                ImageView title = boardTitles[row][col];
//                Position pos = new Position(row + 1, col + 1);
//                GameNode node = createdGame.node(pos);
//
//                if (node == null || node.isEmpty()) continue;
//
//                // Make tiles interactive again
//                title.setOnMouseClicked(mouseEvent -> printClickedTitle(title, node));
//            }
//        }

        System.out.println("Switched to play mode. Log replay is now disabled.");
    }

}
