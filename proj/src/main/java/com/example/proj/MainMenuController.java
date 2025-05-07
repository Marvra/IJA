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

    /**
     * Used to change the screen when a button is clicked (event).
     * It loads the fxml file and sets the scene to the stage and shows it.
     * 
     * @param fxmlName name of the fxml file to load
     * @param event event that triggered the method
     * @return FXMLLoader object
     */

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


    /**
     * Used to start the game when the start button is clicked.
     * it changes the screen using changeScreen method.
     * 
     * @param event event that triggered the method
     */

    public void startGame(ActionEvent event){
        changeScreen(event, "game_mode_selection.fxml");
    }

    /**
     * Used to choose the difficulty of the game when the button is clicked.
     * It changes the screen using changeScreen method and creates a new game with the chosen difficulty.
     * 
     * @param event event that triggered the method
     * @param difficulty difficulty level of the game
     * @param dimensions dimensions of the game board
     */
    private void chooseDifficulty(ActionEvent event, GeneralDifficulty difficulty, int dimensions){
        BoardController boardController = changeScreen(event, "board.fxml").getController();
        GameGenerator generator = new GameGenerator(dimensions, dimensions, difficulty);
        Game game = generator.generate();
        boardController.setTimedMode(true, 5); // urob selectable button
        boardController.createBoard(game);
    }

    /**
     * Method used to choose easy difficulty when the button is clicked.
     * 
     * @param event event that triggered the method
     */
    public void easyDifficulty(ActionEvent event){chooseDifficulty(event, GeneralDifficulty.easy, 6);}

    /**
     * Method used to choose medium difficulty when the button is clicked.
     * 
     * @param event event that triggered the method
     */
    public void mediumDifficulty(ActionEvent event){chooseDifficulty(event, GeneralDifficulty.medium, 8);}

    /**
     * Method used to choose hard difficulty when the button is clicked.
     * 
     * @param event event that triggered the method
     */
    public void hardDifficulty(ActionEvent event){chooseDifficulty(event, GeneralDifficulty.hard, 12);}

    /**
     * Method used to choose custom difficulty when the button is clicked.
     * 
     * @param event event that triggered the method
     */
    public void logGameScreen(ActionEvent event){
        changeScreen(event, "log_game_selection.fxml");
    }
}