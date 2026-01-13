package com.example.myapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Database.init(); // create DB & table
        Scene scene = new Scene(
            FXMLLoader.load(getClass().getResource("Moderator.fxml"))
        );
        stage.setTitle("CineSense");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

