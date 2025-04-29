package com.example.proj;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    @FXML
    private Button startBtn;
    private Button difficultyBtn;

    private Scene scene;
    private Stage stage;
    private Parent root;

    private void changeScreen(ActionEvent event, String fxmlName){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlName));
            root = loader.load();

            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e ) {
            System.out.println(e.getMessage());
        }
    }

    public void startGame(ActionEvent event){
        changeScreen(event, "game_mode_selection.fxml");
    }

    public void chooseDifficulty(ActionEvent event){
        changeScreen(event, "board.fxml");
    }
}