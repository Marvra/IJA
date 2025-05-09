package com.example.proj;

import com.sun.tools.javac.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

import com.example.proj.DifficultyLevels.EasyDifficulty;
import com.example.proj.DifficultyLevels.GeneralDifficulty;
import com.example.proj.game.Game;
import com.example.proj.game.GameGenerator;

public class MainMenuController {

    private static boolean startWithTimer = false;
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
     * Used to choose the difficulty of the game when the button is clicked.
     * It changes the screen using changeScreen method and creates a new game with the chosen difficulty.
     *
     * @param event event that triggered the method
     * @param difficulty difficulty level of the game
     * @param dimensions dimensions of the game board
     */
    private void chooseDifficulty(ActionEvent event, GeneralDifficulty difficulty, int dimensions) {
        FXMLLoader loader = new FXMLLoader(MainMenuController.class.getResource("board.fxml"));

        try {
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            BoardController boardController = loader.getController();
            GameGenerator generator = new GameGenerator(dimensions, dimensions, difficulty);
            Game game = null;

            if (difficulty == GeneralDifficulty.easy) { // no time for proper fix
                while (true) {
                    try {
                        game = generator.generate();
                        break;
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("regeneration");
                    }
                }
            } else {
                game = generator.generate();
            }


            int time = -1;
            if (startWithTimer) {
                if (difficulty == GeneralDifficulty.easy) time = 30;
                else if (difficulty == GeneralDifficulty.medium) time = 60;
                else time = 120;

                boardController.setTimedMode(true, time);
            }
            boardController.createBoard(game);

            stage.sizeToScene();
            stage.show();

        } catch (IOException e) {
            System.out.println("Error loading board.fxml: " + e.getMessage());
        }
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
     * Method to start a Classic Mode Game
     *
     * @param event event that triggered the method
     */
    public void classicMode(ActionEvent event){
        startWithTimer =false;
        changeScreen(event, "game_mode_selection.fxml");}

    /**
     * Method to start a Timeout Mode Game
     *
     * @param event event that triggered the method
     */
    public void timerMode(ActionEvent event){
        startWithTimer = true;
        changeScreen(event, "game_mode_selection.fxml");}

    public void backToMainMenu(ActionEvent event){changeScreen(event, "main_menu.fxml");}

    /**
     * Method used to choose custom difficulty when the button is clicked.
     *
     * @param event event that triggered the method
     */
    public void logGameScreen(ActionEvent event){changeScreen(event, "log_game_selection.fxml");}
}