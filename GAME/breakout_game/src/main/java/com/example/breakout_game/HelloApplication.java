package com.example.breakout_game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        GameCanvas gameCanvas = new GameCanvas();
        gameCanvas.loadLevel();

        GridPane pane = new GridPane();
        pane.add(gameCanvas, 0, 0);
        stage.setScene(new Scene(pane));


        gameCanvas.initialize();
        gameCanvas.draw();
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}