package com.example.proj;

import com.sun.tools.javac.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.File;
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
import ija.ija2024.homework2.common.GameNode;
import ija.ija2024.homework2.common.Position;
import ija.ija2024.homework2.common.Side;
import ija.ija2024.homework2.common.Type;
import ija.ija2024.homework2.game.Game;
import org.ietf.jgss.GSSManager;

public class LogController {
    @FXML
    private ListView<String> logFiles;
    private Button logSelectedButton;

    private String logFileName;
    private List<String> log;

    public void initialize() {
        Path dirPath = Paths.get("src/main/resources/log");

        try (Stream<Path> paths = Files.list(dirPath)) {
            paths.filter(path -> path.toString().endsWith(".txt"))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .forEach(logName -> logFiles.getItems().add(logName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Game game = Game.create(4,4);
    }

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

        URL resource = getClass().getResource("/log/" + selectedFile);
        if (resource == null) {
            System.out.println("File not found in resources: " + selectedFile);
            return null;
        }

        Path filePath;
        try {
            filePath = Paths.get(resource.toURI());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Game game = Game.create(4, 4); // or dynamic

        boolean inBoardSection = false;
        boolean boardSectionEnded = false;
        log = new ArrayList<>();

        try (Stream<String> lines = Files.lines(filePath)) {
            for (String line : lines.collect(Collectors.toList())) {
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
                    stringToGameNode(line, game, false);
                } else if (boardSectionEnded) {
                    log.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Game successfully created. Log size: " + log.size());
        return game;
    }



    static public Position stringToGameNode(String line, Game game, boolean uglyStop) {
        // {L[2@3][SOUTH,NORTH]}
        Pattern pattern = Pattern.compile("\\{([LPEB])\\[(\\d+)@(\\d+)\\]\\[([A-Z,]*)\\]\\}");
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

                // Convert string sides to Side enum values
                for (int i = 0; i < sideStrings.length; i++) {
                    sides[i] = stringToSide(sideStrings[i]);
                }

                // Create nodes based on type
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

    static public Side stringToSide(String sideStr) {
        switch (sideStr) {
            case "NORTH": return Side.NORTH;
            case "SOUTH": return Side.SOUTH;
            case "EAST": return Side.EAST;
            case "WEST": return Side.WEST;
            default:
                throw new IllegalArgumentException("Invalid side: " + sideStr);
        }
    }
}