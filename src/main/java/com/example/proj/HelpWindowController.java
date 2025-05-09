package com.example.proj;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.example.proj.game.Game;

public class HelpWindowController {

    @FXML
    private GridPane gridHelpBoard;

    private ImageView[][] boardTitles;

    private Game currentGame;
    private Game originalGame;

    /**
     * Initializes the help board with the original game state.
     *
     * @param game          The current game instance.
     * @param original      The original game instance.
     * @param originalGrid  The grid pane of the original game.
     * @param originalBoardTitles The image views of the original board titles.
     */
    public void initHelpBoard(Game game, Game original, GridPane originalGrid, ImageView[][] originalBoardTitles) {
        currentGame = game;
        originalGame = original;
        boardTitles = originalBoardTitles;

        gridHelpBoard.getChildren().clear();

        for (Node node : originalGrid.getChildren()) {
            if (node instanceof ImageView originalImageView) {
                Integer row = GridPane.getRowIndex(originalImageView);
                Integer col = GridPane.getColumnIndex(originalImageView);
                row = (row == null) ? 0 : row;
                col = (col == null) ? 0 : col;

                Position pos = new Position(row + 1, col + 1);
                GameNode currentNode = currentGame.node(pos);
                GameNode originalNode = originalGame.node(pos);

                StackPane tile = createTile(row, col, currentNode, originalNode);
                gridHelpBoard.add(tile, col, row);
            }
        }

    }

    /**
     * Updates the help board with the current game state.
     *
     * @param node The current game node to be updated.
     */
    public void updateGame(GameNode node) {
        if (node == null || gridHelpBoard == null) {
            System.out.println("OUTTTTT");
            return;
        }

        Position pos = node.getPosition();
        int row = pos.getRow() - 1;
        int col = pos.getCol() - 1;

        // Remove lebo visual srandy
        gridHelpBoard.getChildren().removeIf(child -> {
            Integer childRow = GridPane.getRowIndex(child);
            Integer childCol = GridPane.getColumnIndex(child);
            childRow = (childRow == null) ? 0 : childRow;
            childCol = (childCol == null) ? 0 : childCol;
            return childRow == row && childCol == col;
        });

        GameNode originalNode = originalGame.node(pos);
        StackPane tile = createTile(row, col, node, originalNode);
        gridHelpBoard.add(tile, col, row);
    }

    /**
     * Creates a tile for the help board.
     *
     * @param row          The row index of the tile.
     * @param col          The column index of the tile.
     * @param currentNode  The current game node.
     * @param originalNode The original game node.
     * @return A StackPane containing the tile and its label.
     */
    private StackPane createTile(int row, int col, GameNode currentNode, GameNode originalNode) {
        ImageView originalImageView = boardTitles[row][col];
        ImageView cloned = new ImageView(originalImageView.getImage());

        cloned.setRotate(originalImageView.getRotate());
        cloned.setFitWidth(originalImageView.getFitWidth());
        cloned.setFitHeight(originalImageView.getFitHeight());
        cloned.setEffect(originalImageView.getEffect());
        cloned.setPreserveRatio(true);

        StackPane stack = new StackPane(cloned);

        if (currentNode != null && originalNode != null && !currentNode.isEmpty()) {
            int rotations = rotationsToMatchOriginal(
                    new ArrayList<>(currentNode.sides),
                    new ArrayList<>(originalNode.sides)
            );

            Label label = new Label(String.valueOf(rotations));
            label.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            label.setTextFill(Color.RED);
            StackPane.setAlignment(label, Pos.BOTTOM_RIGHT);
            stack.getChildren().add(label);
        }

        stack.setMouseTransparent(true);
        return stack;
    }

    /**
     * Calculates the number of rotations needed to match the original game state.
     *
     * @param current The current sides of the game node.
     * @param target  The target sides of the original game node.
     * @return The number of rotations needed to match the original game state.
     */
    public static int rotationsToMatchOriginal(List<Side> current, List<Side> target) {
        List<Side> targetCopy = new ArrayList<>(target);
        targetCopy.sort(Comparator.naturalOrder());
        // HASHHHHEESTET
        for (int i = 0; i < 4; i++) {
            List<Side> currentCopy = new ArrayList<>(current);
            currentCopy.sort(Comparator.naturalOrder());

            if (currentCopy.equals(targetCopy)) {
                return i;
            }

            BoardController.rotateRight(current);
        }

        return -1;
    }


}