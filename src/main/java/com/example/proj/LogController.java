/**
 * @author: Martin Vrablec
 * 
 * controller for the log menu and log mode of board
 * allows to load the game from the log file
 * and create the logged game and its traversions
 */
package com.example.proj;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.example.proj.game.Game;
import com.example.proj.Position;
import javafx.stage.Stage;

public class LogController {
    @FXML
    private ListView<String> logFiles;
    @FXML
    private Button backToMenuBtn;

    private Game logGame;

    private String logFileName;
    private List<String> log;

    /**
     * initialize controller and gets list of log files
     * from the resources/log
     */
    public void initialize() {
        logFiles.getItems().clear();
        Path dirPath = Paths.get("lib/resources/log");
        try (Stream<Path> paths = Files.list(dirPath)) {
            paths.filter(path -> path.toString().endsWith(".txt"))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .forEach(logName -> logFiles.getItems().add(logName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * methid to go back to the main menu
     *
     * @param event event that triggered the method
     */
    @FXML
    public void backToMenu(ActionEvent event) {
        MainMenuController.changeScreen(event, "main_menu.fxml");
    }

    /**
     * start the selected game from the log file
     * loads the game from the log file and creates a new board
     *
     * @param event event that triggered the method
     */
    public void startSelectedGame(ActionEvent event) {
        if (logFiles.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        Game game = handleLogSelected();

        try {
            FXMLLoader loader = new FXMLLoader(MainMenuController.class.getResource("board.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            BoardController boardController = loader.getController();
            boardController.logMode(log);
            boardController.createBoard(game);

            stage.sizeToScene();
            stage.show();

        } catch (IOException e) {
            System.out.println("Error loading board.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Handle log file selection and creates a game from the log file
     * calls the stringToGameNode method to create the game nodes
     *
     * @return Game object created from the log file
     */

    @FXML
    private Game handleLogSelected() {
        String selectedFile = logFiles.getSelectionModel().getSelectedItem();

        if (selectedFile == null) {
            return null;
        }

        Path filePath = Paths.get("lib/resources/log", selectedFile);

        if (!Files.exists(filePath)) {
            return null;
        }

        List<String> allLines;
        try {
            allLines = Files.readAllLines(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        if (allLines.isEmpty()) {
            return null;
        }

        int[] dimensions = getBoardDimensionsFromLog(allLines.get(0).trim());
        logGame = Game.create(dimensions[0], dimensions[1]);

        boolean inBoardSection = false;
        boolean boardSectionEnded = false;
        log = new ArrayList<>();

        for (String line : allLines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.contains("START BOARD CREATION")) {
                inBoardSection = true;
                continue;
            }

            if (line.contains("END BOARD CREATION")) {
                inBoardSection = false;
                boardSectionEnded = true;
                continue;
            }

            if (inBoardSection) {
                stringToGameNode(line, logGame, false);
            } else if (boardSectionEnded) {
                log.add(line);
            }
        }

        return logGame;
    }



    /**
     * convers string of game node to aPosition object and creates corresponding node in game
     * Uses regex to parse string for needed information about gameNode in log
     * string format is  example : "{L[2@3][SOUTH,NORTH]}"
     *
     * @param line string representation of the game node
     * @param game Game object where the node will be created
     * @return position object representing the coordinates of the node
     */
    static public Position stringToGameNode(String line, Game game, boolean uglyStop) {
        // {L[2@3][SOUTH,NORTH]}
        Pattern pattern = Pattern.compile("\\{([LPEB])\\[(\\d+)@(\\d+)\\]\\[([A-Z, ]*)\\]\\}");
        Matcher matcher = pattern.matcher(line);

        if (matcher.matches()) {

            String typeStr = matcher.group(1);

            int x = Integer.parseInt(matcher.group(2));
            int y = Integer.parseInt(matcher.group(3));
            Position position = new Position(x, y);

            if(uglyStop) return position; // STOP IN BOARDGAME SHOT IDK CHANGE THIS

            String sidesStr = matcher.group(4);

            if (sidesStr.isEmpty()) {
                return null;
            } else {
                String[] sideStrings = sidesStr.split(",");
                Side[] sides = new Side[sideStrings.length];

                for (int i = 0; i < sideStrings.length; i++) {
                    sides[i] = stringToSide(sideStrings[i]);
                }

                switch (typeStr) {
                    case "L":
                        game.createLinkNode(position, sides);
                        System.out.println(game.node(position).toString());
                        return position;
                    case "P":
                        game.createPowerNode(position, sides);
                        System.out.println(game.node(position).toString());
                        return position;
                    case "E":
                        return position;
                    case "B":
                        game.createBulbNode(position,sides);
                        System.out.println(game.node(position).toString());
                        return position;
                    default:
                        throw new IllegalArgumentException("Unknown node type: " + typeStr);
                }

            }
        } else {
            throw new IllegalArgumentException("Invalid game node string format: " + line);
        }
    }

    /**
     * Get  board dimensions from the log line
     * Format of board dimensions : "BOARD DIMENSIONS : [rows@cols]"
     *
     * @param logLine the log line containing the board dimensions
     * @return array with the board dimensions [rows, cols]
     */
    public static int[] getBoardDimensionsFromLog(String logLine) {
        // board dimensions
        Pattern pattern = Pattern.compile("BOARD DIMENSIONS : \\[(\\d+)@(\\d+)\\]");
        Matcher matcher = pattern.matcher(logLine);

        if (matcher.find()) {
            int rows = Integer.parseInt(matcher.group(1));
            int cols = Integer.parseInt(matcher.group(2));
            return new int[]{rows, cols};
        } else {
            throw new IllegalArgumentException(logLine);
        }
    }



    static public Side stringToSide(String sideStr) {
        switch (sideStr.trim()) {
            case "NORTH": return Side.NORTH;
            case "SOUTH": return Side.SOUTH;
            case "EAST": return Side.EAST;
            case "WEST": return Side.WEST;
            default:
                throw new IllegalArgumentException(sideStr);
        }
    }
}