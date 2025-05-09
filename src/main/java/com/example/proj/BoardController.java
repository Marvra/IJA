
package com.example.proj;

import ija.ija2024.tool.common.Observable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import ija.ija2024.tool.common.Observable.Observer;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javafx.util.Duration;

import java.util.*;

import com.example.proj.game.Game;

public class BoardController implements Observer {

    private int imageWidth = 50;
    private int imageHeight = 50;
    private Game createdGame;
    private ImageView[][] boardTiles;


    // TIME
    private Timeline timer;
    private int timeRemaining;
    private boolean timedMode = false;
    private int startTime = 0;
    // TIME

    // HELP
    private HelpWindowController helpWindowController;
    private Stage helpWindowStage;
    private Game copyGame;
    // HELP

    //LOG
    private List<String> logData;
    private int logLine = 0;
    private StringBuilder logOutput;
    //LOG

    @FXML
    private GridPane gridBoard;
    @FXML
    private Button nextMoveBtn;
    @FXML
    private Button prevMoveBtn;
    @FXML
    private Button playLogBtn;
    @FXML
    private Button backToMenuBtn;
    @FXML
    private Button helpBtn;
    @FXML
    private Button saveBtn;
    @FXML
    private Label uWonLabel;
    @FXML
    private Label timerLabel;


    // --- GAME CREATION ---

    /**
     * Creates the game board based on the given game.
     * It initializes the board tiles and sets up the grid.
     * It also randomizes the board rotation and sets up the timer if needed.
     *
     * @param game The game object containing the board.
     */
    public void createBoard(Game game) {

        if (game == null)
            return;
        copyGame = copyByValue(game);
        randomizeBoardRotation(game);
        boardTiles = new ImageView[game.rows()][game.cols()];

        ColorAdjust litTile = new ColorAdjust();
        litTile.setBrightness(1.0);

        logOutput = new StringBuilder();
        logOutput.append("BOARD DIMENSIONS : " + "[" + game.rows() + "@" + game.cols() + "]").append("\n");
        logOutput.append("START BOARD CREATION").append("\n");

        for (int row = 0; row < game.rows(); row++) {
            for (int col = 0; col < game.cols(); col++) {

                Position position = new Position(row + 1, col + 1);
                GameNode node = game.node(new Position(position.getRow(), position.getCol()));

                if (node == null)
                    continue;

                Image image = selectCorrectImageTitle(node);

                ImageView tile = new ImageView(image);

                int numberOfRotations = numberOfRotations(node.sides.size(), node);
                tile.setRotate((tile.getRotate() + 90 * numberOfRotations) % 360);

                boardTiles[row][col] = tile;
                gridBoard.add(tile, col, row);

                StackPane tileContainer = new StackPane(tile);
                tileContainer.getStyleClass().add("board-tile");

                gridBoard.add(tileContainer, col, row);


                if (node.isEmpty())
                    continue; // SKIP EMPTY
                node.addObserver(this);


                tile.setPickOnBounds(true);
                tile.setOnMouseClicked(mouseClickedEvent -> printClickedTile(tile, node));
                System.out.println(node.toString()); // PRINT ONLY NOT EMPTY
                logOutput.append(node).append("\n"); // instead of sys.out etc node.toString()
            }
        }

        logOutput.append("END BOARD CREATION").append("\n");
        this.createdGame = game;
        createdGame.init();
        update(createdGame.node( new Position(createdGame.powerRow, createdGame.powerCol)));
        createdGame.update(createdGame.node( new Position(createdGame.powerRow, createdGame.powerCol)));
        if(game == null) {
            return;
        }
        if (timedMode) {
            startTimer(startTime);
        }
    }

    /**
     * Randomizes the rotation of the game board.
     * It iterates through each node and randomly rotates it 0 to 3 times.
     *
     * @param game The game object containing the board.
     */
    private void randomizeBoardRotation(Game game) {
        Random rand = new Random();
        for (int row = 1; row <= game.rows(); row++) {
            for (int col = 1; col <= game.cols(); col++) {
                Position pos = new Position(row, col);
                GameNode node = game.node(pos);

                if (node == null || node.isEmpty()) {
                    continue;
                }
                int rotations;
                if (node.isBulb()) {
                    rotations = rand.nextInt(3) + 1;
                } else {
                    rotations = rand.nextInt(4);
                }
                for (int i = 0; i < rotations; i++) {
                    node.turn();
                }
            }
        }
    }


    /**
     * Checks if all bulbs are lit in the game.
     *
     * @return true if all bulbs are lit, false otherwise.
     */
    private boolean allBulbsAreLit() {
        for (int row = 0; row < createdGame.rows(); row++) {
            for (int col = 0; col < createdGame.cols(); col++) {
                GameNode node = createdGame.node(new Position(row + 1, col + 1));
                if (node != null && node.type == Type.BULB && !node.light()) {
                    return false;
                }
            }
        }
        if (timer != null) {
            timer.stop();
        }
        return true;
    }

    /**
     * Ends the game and shows a message.
     */
    private void endGame() {
        gridBoard.setDisable(true);
        uWonLabel.setText("U Won!");
    }


    /**
     * Goes back to the main menu when the button is clicked.
     *
     * @param event The event that triggered the method.
     */
    public void backToMenu(ActionEvent event) {
        if (timer != null) {
            timer.stop();
            timer = null;
        }

        if (helpWindowStage != null) {
            helpWindowStage.close();
            helpWindowStage = null;
        }

        MainMenuController.changeScreen(event, "game_mode_selection.fxml");
    }

    /**
     * Updates the game board based on the observable pattern.
     * It checks if the observable is an instance of GameNode and updates the corresponding ImageView.
     * It also updates the help window if it is open.
     *
     * @param o The observable object that triggered the update.
     */
    @Override
    public void update(Observable o) {
        if (!(o instanceof GameNode))
            return;
        GameNode node = (GameNode) o;
        Position pos = node.getPosition();
        int row = pos.getRow() - 1;
        int col = pos.getCol() - 1;

        ImageView view = boardTiles[row][col];
        if (node.light()) {
            Glow glow = new Glow(1.0);
            view.setEffect(glow);
        } else {
            view.setEffect(null);
        }

        if(helpWindowController != null) {

            helpWindowController.updateGame(node);
        }

    }

    // --- GAME CREATION ---

    // --- HELP WINDOW HELPER ---

    /**
     * Initializes the board with the given game.
     * It sets up the grid and creates the board based on the game.
     *
     * @param event The game object containing the board.
     */
    @FXML
    public void onHelpClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("help_window.fxml"));
            Parent root = loader.load();

            helpWindowController = loader.getController();

            helpWindowController.initHelpBoard(createdGame, copyGame, gridBoard, boardTiles);

            helpWindowStage = new Stage();
            helpWindowStage.setTitle("Help");
            helpWindowStage.setScene(new Scene(root));
            helpWindowStage.sizeToScene();
            helpWindowStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a copy of the original game by value.
     * It creates a new game with the same dimensions and copies the nodes.
     *
     * @param original The original game to be copied.
     * @return A new game that is a copy of the original.
     */
    public Game copyByValue(Game original) {
        Game copy = Game.create(original.rows(), original.cols());

        for (int row = 1; row <= original.rows(); row++) {
            for (int col = 1; col <= original.cols(); col++) {
                Position pos = new Position(row, col);
                GameNode node = original.node(pos);
                if (node == null) {
                    continue;
                }
                copy.createNodeBase(pos, node.type, node.sides.toArray(new Side[0]));

            }
        }

        return copy;
    }
    // --- HELP WINDOW HELPER ---

    // --- TIME MODE HELPER ---
    /**
     * Sets the timed mode for the game.
     *
     * @param enabled Whether the timed mode is enabled or not.
     * @param seconds The number of seconds for the timer.
     */
    public void setTimedMode(boolean enabled, int seconds) {
        this.timedMode = enabled;
        this.startTime = seconds;
    }

    /**
     * Starts the timer with the given number of seconds.
     * It updates the timer label every second and stops when time is up.
     *
     * @param seconds The number of seconds to start the timer with.
     */
    public void startTimer(int seconds) {
        timeRemaining = seconds;
        timerLabel.setText("Time: " + timeRemaining);

        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeRemaining--;
            timerLabel.setText("Time: " + timeRemaining);

            if (timeRemaining <= 0) {
                timer.stop();
                gridBoard.setDisable(true);
                timerLabel.setText("Game Over !");
                timerLabel.setStyle("-fx-text-fill: red; -fx-font-size: 20px;");
            }
        }));

        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }
    // --- TIME MODE HELPER ---


    // --- CORRECT IMAGE SELECTION ---
    /**
     * Selects the correct image for the given GameNode based on its type and sides.
     *
     * @param node The GameNode to select the image for.
     * @return The selected Image.
     */
    private Image selectCorrectImageTitle(GameNode node) {
        HashSet<Side> sides = node.sides;
        Type type = node.type;
        String base = type.toString(); // B L P

        if (type == Type.EMPTY) {
            base = base + ".png"; // E.png
            return new Image(getClass().getResourceAsStream(base), imageHeight, imageWidth, false, false);
        }

        if (type == Type.BULB) {
            base = base + ".png"; // B.png
            return new Image(getClass().getResourceAsStream(base), imageHeight, imageWidth, false, false);
        }

        int count = sides.size();

        if (count == 2) {
            if (node.north() && node.south() || node.west() && node.east()) { // ROVNA NODE
                base = base + "_" + count + ".png";
            } else { // L NODE
                base = base + "L_" + count + ".png";
            }
        } else {
            base = base + "_" + count + ".png"; // napr: L_2,P_3 etc.
        }
        return new Image(getClass().getResourceAsStream(base), imageHeight, imageWidth, false, false);
    }

    /**
     * Counts the number of rotations needed to match the sides of the given GameNode.
     *
     * @param current The current sides of the GameNode.
     * @param target The target sides to match.
     * @return The number of rotations needed.
     */
    static public int countRotationsToMatch(List<Side> current, List<Side> target) {
        int count = 0;
        while (!sameSides(current, target)) {
            count++;
            rotateRight(target);
            if (count > 3)
                break;
        }
        return  count;
    }

    /**
     * Counts the number of rotations needed to match the sides of the given GameNode.
     *
     * @param numberOfSides The number of sides of the GameNode.
     * @param node The GameNode to check.
     * @return The number of rotations needed.
     */
    private int numberOfRotations(int numberOfSides, GameNode node) {
        List<Side> sidesPic;

        if (numberOfSides == 1) { // B, P_1
            sidesPic = Arrays.asList(Side.EAST);
        } else if (numberOfSides == 2) {

            if ((node.north() && node.south()) || (node.east() && node.west())) {
                sidesPic = Arrays.asList(Side.EAST, Side.WEST);
            } else {
                sidesPic = Arrays.asList(Side.EAST, Side.SOUTH);
            }

        } else if (numberOfSides == 3) {
            sidesPic = Arrays.asList(Side.EAST, Side.WEST, Side.SOUTH);
        } else {
            return 0;
        }

        List<Side> origSides = new ArrayList<>(node.sides);
        return countRotationsToMatch(origSides, sidesPic);
    }

    static public boolean sameSides(List<Side> a, List<Side> b) {
        return new HashSet<>(a).equals(new HashSet<>(b));
    }

    static public void rotateRight(List<Side> sides) {
        for (int i = 0; i < sides.size(); i++) {
            sides.set(i, switch (sides.get(i)) {
                case NORTH -> Side.EAST;
                case EAST -> Side.SOUTH;
                case SOUTH -> Side.WEST;
                case WEST -> Side.NORTH;
            });
        }
    }

    /**
     * Prints the clicked tile  to log and updates the game node.
     *
     * @param tile The ImageView representing the clicked tile.
     * @param node The GameNode associated with the clicked tile.
     */
    private void printClickedTile(ImageView tile, GameNode node) {

        node.turn();

        tile.setRotate((tile.getRotate() + 90) % 360);

        if(helpWindowController != null) {
            helpWindowController.updateGame(node);
        }

        logOutput.append(node.toString()).append("\n");

        if (allBulbsAreLit()) {
            endGame();
        }
    }
    // --- CORRECT IMAGE SELECTION ---

    //  --- LOG HELPER ---
    @FXML
    public void saveCUrrentGame() {
        saveToLogFile();
    }

    /**
     * Saves the current game to a log file.
     * creates  new file with the name based of the current time and difficulty level.
     * log is saved in the lib.
     */
    private void saveToLogFile() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String difficlty = "";

        switch (createdGame.cols()){
            case 6:
                difficlty = "easy";
                break;
            case 8:
                difficlty = "medium";
                break;
            case 12:
                difficlty = "hard";
                break;
        }

        String logname = "game_log_"+ difficlty + "_" + timestamp + ".txt";
        try {
            File logFile = new File("lib/resources/log/", logname );
            FileWriter fileWriter = new FileWriter(logFile, false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(String.valueOf(logOutput));
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Used to set the log mode.
     * set the log data and disables the grid board.
     * enables the next, prev, play buttons.
     *
     * @param log list of strings representing the log
     */
    public void logMode(List<String> log) {
        this.logData = log;
        this.logLine = 0;

        ColorAdjust darkenBoard = new ColorAdjust();
        darkenBoard.setBrightness(-0.5);
        gridBoard.setEffect(darkenBoard);
        gridBoard.setDisable(true);

        nextMoveBtn.setDisable(false);
        playLogBtn.setDisable(false);

        nextMoveBtn.setOpacity(1);
        prevMoveBtn.setOpacity(0.5);
        playLogBtn.setOpacity(1);
    }

    /**
     * Used to go to next move in log (next line in current log).
     * Updates game with the next line from log.
     *
     * @param event event that triggered method
     */
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

    /**
     * Used to go back to previous move in log (previous line in current log).
     * updates game with the previous line from log.
     *
     * @param event event that triggered method
     */
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

    /**
     * Updates the game with the given line from the log.
     * ceates a new Position object and updates the game node.
     * Used in log mode in prev and next move
     *
     * @param line line from log file
     */
    private void updateGame(String line) {
        Position position = LogController.stringToGameNode(line, createdGame, true);
        printClickedTile(boardTiles[position.getRow() - 1][position.getCol() - 1], createdGame.node(position));
    }

    /**
     * Used to play the log when the button is clicked.
     * sets log buttons back to being disabled
     *
     * @param event event that triggered method
     */
    public void playMode(ActionEvent event) {
        nextMoveBtn.setDisable(true);
        prevMoveBtn.setDisable(true);
        playLogBtn.setDisable(true);

        nextMoveBtn.setOpacity(0);
        prevMoveBtn.setOpacity(0);
        playLogBtn.setOpacity(0);

        gridBoard.setDisable(false);
        gridBoard.setEffect(null);

        logData = null;
        logLine = 0;
    }

    //  --- LOG HELPER ---
}
