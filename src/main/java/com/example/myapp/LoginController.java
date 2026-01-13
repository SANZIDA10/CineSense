package com.example.myapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private static final String VALID_EMAIL = "sanzidajerin28@gmail.com";
    private static final String VALID_PASSWORD = "1122";

    @FXML
    private void onLogin(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("❌ Please fill in all fields");
            errorLabel.setStyle("-fx-text-fill: #ff4444; -fx-font-size: 13px;");
            return;
        }

        if (email.equals(VALID_EMAIL) && password.equals(VALID_PASSWORD)) {
            // Login successful - navigate to Moderator page
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("Moderator.fxml"));
                
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(loader.load(), stage.getWidth(), stage.getHeight());
                scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                errorLabel.setText("❌ Error loading moderator page");
                errorLabel.setStyle("-fx-text-fill: #ff4444;");
            }
        } else {
            errorLabel.setText("❌ Invalid email or password");
            errorLabel.setStyle("-fx-text-fill: #ff4444; -fx-font-size: 13px;");
        }
    }

    @FXML
    private void onBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("Home.fxml"));
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(loader.load(), stage.getWidth(), stage.getHeight());
            scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
