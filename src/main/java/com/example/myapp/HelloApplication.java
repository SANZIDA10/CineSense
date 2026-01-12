package com.example.myapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        URL fxml = HelloApplication.class.getResource("/com/example/myapp/hello-view.fxml");
        if (fxml == null) throw new RuntimeException("Cannot find hello-view.fxml");
        FXMLLoader loader = new FXMLLoader(fxml);
        Scene scene = new Scene(loader.load());
        URL css = HelloApplication.class.getResource("/com/example/myapp/style.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());
        stage.setTitle("CineSense");
        stage.setWidth(1280);
        stage.setHeight(900);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) { launch(); }
}