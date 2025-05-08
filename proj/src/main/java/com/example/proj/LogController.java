package com.example.proj;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

public class LogController {
    @FXML
    private ListView<String> logFiles;
    @FXML
    private Button backToMenuBtn;

    private Game logGame;

    private String logFileName;
    private List<String> log;

    public void initialize() {
        logFiles.getItems().clear();
        Path dirPath = Paths.get("src/main/resources/log");
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
     * Used to go back to the main menu when the back button is clicked.
     * 
     * @param event event that triggered the method
     */
    @FXML
    public void backToMenu(ActionEvent event) {
        MainMenuController.changeScreen(event, "game_mode_selection.fxml");
    }

    /**
     * 
     * 
     * @param event event that triggered the method
     */
    public void startSelectedGame(ActionEvent event) {
        Game game = handleLogSelected();

        if(logFiles.getSelectionModel().getSelectedItem() == null) {
            System.out.println("No file selected.");
            return;
        }
        BoardController boardController = MainMenuController.changeScreen(event, "board.fxml").getController();
        boardController.logMode(log);
        boardController.createBoard(game);
    }

    @FXML
    private Game handleLogSelected() {
        String selectedFile = logFiles.getSelectionModel().getSelectedItem();
        if (selectedFile == null) {
            System.out.println("No file selected.");
            return null;
        }

        Path filePath = Paths.get("src/main/resources/log", selectedFile);

        if (!Files.exists(filePath)) {
            System.out.println("File not found: " + filePath.toAbsolutePath());
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
            throw new RuntimeException("Log file is empty.");
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

        System.out.println("Game successfully created. Log size: " + log.size());
        return logGame;
    }



    /**
     * Converts a string of a game node to a Position object and creates the corresponding node in the game.
     * Uses regex to parse the string for needed information about give string of node.
     * string format is "{L[2@3][SOUTH,NORTH]}".
     *
     * @param line string representation of the game node.
     * @param game Game object where the node will be created.
     * @return Position object representing the coordinates of the node.
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
     * This method extracts the board dimensions from a log line.
     * It assumes the log line has the format: "BOARD DIMENSIONS : [rows@cols]"
     *
     * @param logLine the log line containing the board dimensions
     * @return an array with the board dimensions [rows, cols]
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
            throw new IllegalArgumentException("Invalid log format for board dimensions: " + logLine);
        }
    }



    // DO SIDE
    static public Side stringToSide(String sideStr) {
        switch (sideStr.trim()) {
            case "NORTH": return Side.NORTH;
            case "SOUTH": return Side.SOUTH;
            case "EAST": return Side.EAST;
            case "WEST": return Side.WEST;
            default:
                throw new IllegalArgumentException("Invalid side: " + sideStr);
        }
    }
}