package com.example.proj;

import ija.ija2024.homework2.game.DifficultyLevels.EasyDifficulty;
import ija.ija2024.homework2.game.DifficultyLevels.GeneralDifficulty;
import ija.ija2024.homework2.game.GameGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import ija.ija2024.homework2.game.DifficultyLevels.GameDifficulty;
import ija.ija2024.homework2.game.Game;

import java.io.IOException;

public class MainMenuController {

    @FXML
    private Button startBtn;
    private Button difficultyBtn;

//    private Scene scene;
//    private Stage stage;
//    private Parent root;

    public static FXMLLoader changeScreen(ActionEvent event, String fxmlName) {
        Scene scene;
        Stage stage;
        Parent root;

        try {
            FXMLLoader loader = new FXMLLoader(MainMenuController.class.getResource(fxmlName));
            root = loader.load();
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            return loader;
        } catch (IOException e ) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void startGame(ActionEvent event){
        changeScreen(event, "game_mode_selection.fxml");
    }

    public void chooseDifficulty(ActionEvent event){
        BoardController boardController = changeScreen(event, "board.fxml").getController();
        GameGenerator generator = new GameGenerator(8, 8, GeneralDifficulty.medium);
        Game game = generator.generate();
        boardController.createBoard(game);
    }

    public void logGameScreen(ActionEvent event){
        changeScreen(event, "log_game_selection.fxml");
    }
}