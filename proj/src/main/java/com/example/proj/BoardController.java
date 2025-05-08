package com.example.proj;

import ija.ija2024.homework2.common.Position;
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
import ija.ija2024.homework2.game.Game;
import ija.ija2024.homework2.common.GameNode;
import ija.ija2024.homework2.common.Type;
import ija.ija2024.homework2.common.Side;
import ija.ija2024.tool.common.Observable.Observer;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashSet;

public class BoardController implements Observer {

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
    @FXML
    Button backToMenuBtn;
    @FXML
    Button helpBtn;
    @FXML
    Button saveBtn;

    // TIME (vlastny conroller treba)
    private Timeline timer;
    private int timeRemaining;
    private boolean timedMode = false;
    private int startTime = 0;
    @FXML
    private Label timerLabel;
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

    private Game createdGame;

    private ImageView[][] boardTitles;

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

    @FXML
    public void onHelpClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("help_window.fxml"));
            Parent root = loader.load();

            helpWindowController = loader.getController();

            helpWindowController.initHelpBoard(createdGame, copyGame, gridBoard, boardTitles);

            helpWindowStage = new Stage();
            helpWindowStage.setTitle("Help");
            helpWindowStage.setScene(new Scene(root));
            helpWindowStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void update(Observable o) {
        if (!(o instanceof GameNode))
            return;
        GameNode node = (GameNode) o;
        Position pos = node.getPosition();
        int row = pos.getRow() - 1;
        int col = pos.getCol() - 1;

        ImageView view = boardTitles[row][col];
        if (node.light()) {
            Glow glow = new Glow(1.0);
            view.setEffect(glow);
        } else {
            view.setEffect(null);
        }

        if(helpWindowController != null) {

            helpWindowController.updateGame(node);
        } else {
            System.out.println("SEM NULL");

        }



        System.out.println("WAS UPDATED");

    }

    public void createBoard(Game game) {

        if (game == null)
            return;

        boardTitles = new ImageView[game.rows()][game.cols()];

        ColorAdjust litTitle = new ColorAdjust();
        litTitle.setBrightness(1.0);

        logOutput = new StringBuilder();
        logOutput.append("BOARD DIMENSIONS : " + "[" + game.rows() + "@" + game.cols() + "]").append("\n");
        logOutput.append("START BOARD CREATION").append("\n");

        for (int row = 0; row < game.rows(); row++) {
            for (int col = 0; col < game.cols(); col++) {

                // SKIP EMPTY
                Position position = new Position(row + 1, col + 1);
                GameNode node = game.node(new Position(position.getRow(), position.getCol()));

                if (node == null)
                    continue;

                Image image = selectCorrectImageTitle(node);

                ImageView title = new ImageView(image);

                int numberOfRotations = numberOfRotations(node.sides.size(), node);
                title.setRotate((title.getRotate() + 90 * numberOfRotations) % 360);

                boardTitles[row][col] = title;
                gridBoard.add(title, col, row);

                if (node.isEmpty())
                    continue; // SKIP EMPTY
                node.addObserver(this);


                title.setPickOnBounds(true);
                title.setOnMouseClicked(mouseClickedEvent -> printClickedTitle(title, node));
                System.out.println(node.toString()); // PRINT ONLY NOT EMPTY
                logOutput.append(node).append("\n"); // instead of sys.out etc node.toString()
            }
        }
        logOutput.append("END BOARD CREATION").append("\n");
        this.createdGame = game;
        if(game == null) {
            System.out.println("HOVNO GENEROVANE ");
            return;
        }
        copyGame = copyByValue(game);

        if (timedMode) {
            startTimer(startTime);
        }
    }

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


    // NOT IDEAL FIX LATER
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

    // TIME

    public void setTimedMode(boolean enabled, int seconds) {
        this.timedMode = enabled;
        this.startTime = seconds;
    }

    public void startTimer(int seconds) {
        timeRemaining = seconds;
        timerLabel.setText("Time: " + timeRemaining);

        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeRemaining--;
            timerLabel.setText("Time: " + timeRemaining);

            if (timeRemaining <= 0) {
                timer.stop();
                gridBoard.setDisable(true);
                System.out.println("Game Over: Time's up!");
            }
        }));

        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }
    // TIME


    private void endGame() {
        gridBoard.setDisable(true);

        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setContentText("every bulb is lit");
        alert.showAndWait();
    }
    // NOT IDEAL FIX LATER

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
            base = base + "_" + count + ".png"; // napr.: L_2,P_3 etc.
        }
        return new Image(getClass().getResourceAsStream(base), imageHeight, imageWidth, false, false);
    }


    // TO GAME PROBABLY
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

    // TO GAME PROBABLY
    static public boolean sameSides(List<Side> a, List<Side> b) {
        return new HashSet<>(a).equals(new HashSet<>(b));
    }

    // TO GAME PROBABLY
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

    private void printClickedTitle(ImageView title, GameNode node) {

        node.turn();
        System.out.println(node.toString());

        title.setRotate((title.getRotate() + 90) % 360);

        if(helpWindowController != null) {
            helpWindowController.updateGame(node);
        }

        logOutput.append(node.toString()).append("\n");

        if (allBulbsAreLit()) {
            endGame();
        }
    }

    @FXML
    public void saveCUrrentGame() {
        saveToLogFile();
    }

    // POZOR TU SI TO PREPISUJES TEDA SI PREPISUJES VZDY LOGS
    // MOZNO NIE TU ZROVNA ALE PROSTE ABY SI SI ULOZIL #. LOG MUSIS KLIKNU 3.KRAT SAVE
    // A TIE PREDTYM SA TI PREPISU ABY SA VYTOVRIL DALSI LOG
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
            File logFile = new File("src/main/resources/log", logname );
            FileWriter fileWriter = new FileWriter(logFile, false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(String.valueOf(logOutput));
            bufferedWriter.close();
            System.out.println("SAVED TO FILE");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


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
        printClickedTitle(boardTitles[position.getRow() - 1][position.getCol() - 1], createdGame.node(position));
    }

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
}
