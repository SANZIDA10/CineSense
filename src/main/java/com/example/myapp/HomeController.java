package com.example.myapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;

public class HomeController {

    @FXML private Button userBtn;
    @FXML private Button moderatorBtn;
    @FXML private Label selectedRoleLabel;

    private String selectedRole = null;

    @FXML
    public void initialize() {
        // Set initial border styles for buttons
        setButtonBorderStyle(userBtn);
        setButtonBorderStyle(moderatorBtn);
    }

    @FXML
    private void onSelectUser(ActionEvent event) {
        selectedRole = "user";
        selectedRoleLabel.setText("✓ User role selected");
        selectedRoleLabel.setStyle("-fx-text-fill:#4caf50;");
        updateButtonStyles();
    }

    @FXML
    private void onSelectModerator(ActionEvent event) {
        selectedRole = "moderator";
        selectedRoleLabel.setText("✓ Moderator role selected");
        selectedRoleLabel.setStyle("-fx-text-fill:#4caf50;");
        updateButtonStyles();
    }

    @FXML
    private void onGetStarted(ActionEvent event) throws IOException {
        if (selectedRole == null) {
            selectedRoleLabel.setText("Please select a role first!");
            selectedRoleLabel.setStyle("-fx-text-fill:#ff4444;");
            return;
        }

        // Determine which FXML to load - moderator goes to login first
        String fxmlFile = selectedRole.equals("moderator") ? "Login.fxml" : "UserPage.fxml";

        // Load the FXML
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxmlFile));
        
        // Get current stage and preserve its size
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(loader.load(), stage.getWidth(), stage.getHeight());
        scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());

        // Set new scene
        stage.setScene(scene);
        stage.show();
    }

    private void setButtonBorderStyle(Button button) {
        button.setStyle("-fx-background-color: transparent; " +
                "-fx-border-color: #ffb347; " +
                "-fx-border-width: 2; " +
                "-fx-text-fill: #ffb347; " +
                "-fx-font-weight: 800; " +
                "-fx-padding: 10 20; " +
                "-fx-background-radius: 8; " +
                "-fx-border-radius: 8;");
    }

    private void setButtonSelectedStyle(Button button) {
        button.setStyle("-fx-background-color: linear-gradient(#ffb347, #ff9800); " +
                "-fx-text-fill: black; " +
                "-fx-font-weight: 800; " +
                "-fx-padding: 12 22; " +
                "-fx-background-radius: 8; " +
                "-fx-border-width: 0;");
    }

    private void updateButtonStyles() {
        if ("user".equals(selectedRole)) {
            setButtonSelectedStyle(userBtn);
            setButtonBorderStyle(moderatorBtn);
        } else if ("moderator".equals(selectedRole)) {
            setButtonSelectedStyle(moderatorBtn);
            setButtonBorderStyle(userBtn);
        }
    }
}
