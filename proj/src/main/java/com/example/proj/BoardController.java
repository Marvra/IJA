package com.example.proj;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import javafx.scene.input.MouseEvent;

public class BoardController {
    private int imageWidth = 50;
    private int imageHeight = 50;

    @FXML
    GridPane gridBoard;

    private ImageView[][] boardTitles;

    public void createBoard(int size) {
        // generate map [][] gamenode
        boardTitles = new ImageView[size][size];

        Image image = new Image(getClass().getResourceAsStream("l_wire.png"), imageHeight, imageWidth, false, false);

        ColorAdjust darkenTitle =  new ColorAdjust();
        darkenTitle.setBrightness(-0.5);

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {

                // getGamenodeTypeAt(row, col)
                // function(setWireImage)

                ImageView title = new ImageView(image);

                boardTitles[row][col] = title;
                gridBoard.add(title, col, row);

                title.setPickOnBounds(true);
                title.setOnMouseEntered(enterHoverEvent -> title.setEffect(darkenTitle));
                title.setOnMouseExited(exitHoverEvent -> title.setEffect(null));
                title.setOnMouseClicked(mouseClickedEvent -> printClickedTitle(title));
            }
        }
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
