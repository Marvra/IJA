package com.example.proj;

import ija.ija2024.homework2.common.Position;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import ija.ija2024.homework2.game.Game;
import ija.ija2024.homework2.common.GameNode;
import ija.ija2024.homework2.common.Type;
import ija.ija2024.homework2.common.Side;
import ija.ija2024.homework2.game.DifficultyLevels.GameDifficulty;


import javafx.scene.input.MouseEvent;

public class BoardController {
    private int imageWidth = 50;
    private int imageHeight = 50;

    @FXML
    GridPane gridBoard;

    private ImageView[][] boardTitles;

    public void createBoard(int size) {
        // generate map [][] gamenode
        GameDifficulty gameDifficulty = new GameDifficulty();
        Game game = gameDifficulty.generate();

        if(game == null) return;

        boardTitles = new ImageView[size][size];


        ColorAdjust darkenTitle =  new ColorAdjust();
        darkenTitle.setBrightness(-0.5);

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
                title.setOnMouseClicked(mouseClickedEvent -> printClickedTitle(title));
            }
        }
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

        System.out.println(base);

        return new Image(getClass().getResourceAsStream(base), imageHeight, imageWidth, false, false);
    }


    private void printClickedTitle (ImageView title) {

        Integer hoveredRow = GridPane.getRowIndex(title);
        Integer hoveredCol = GridPane.getColumnIndex(title);

        int rowIdx = hoveredRow != null ? hoveredRow : 0;
        int colIdx = hoveredCol != null ? hoveredCol : 0;

        System.out.println("clicked title on [row, col] : [" + rowIdx + ", " + colIdx + "]");

        title.setRotate((title.getRotate() + 90) % 360);
    }
}
