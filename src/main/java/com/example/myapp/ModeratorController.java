package com.example.myapp;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;

public class ModeratorController {

    @FXML
    private TextField titleField;

    @FXML
    private ComboBox<String> genreCombo;

    @FXML
    private ComboBox<String> moodCombo;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField posterUrlField;

    @FXML
    private TextField ratingField;

    @FXML
    private TextArea reviewField;

    @FXML
    private TableView<Movie> moviesTable;

    @FXML
    private Label statusLabel;

    private static final String[] GENRES = {"Action", "Comedy", "Drama", "Horror", "Sci-Fi", "Thriller", "Romance", "Animation"};
    private static final String[] MOODS = {"Happy", "Sad", "Thrilled", "Scared", "Relaxed", "Inspired", "Angry"};

    @FXML
    public void initialize() {
        // Initialize combo boxes
        genreCombo.setItems(FXCollections.observableArrayList(GENRES));
        moodCombo.setItems(FXCollections.observableArrayList(MOODS));

        // Setup table columns
        setupTableColumns();

        // Load movies
        loadMovies();
    }

    private void setupTableColumns() {
        // Title column
        TableColumn<Movie, String> titleColumn = (TableColumn<Movie, String>) moviesTable.getColumns().get(0);
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        // Genre column
        TableColumn<Movie, String> genreColumn = (TableColumn<Movie, String>) moviesTable.getColumns().get(1);
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));

        // Mood column
        TableColumn<Movie, String> moodColumn = (TableColumn<Movie, String>) moviesTable.getColumns().get(2);
        moodColumn.setCellValueFactory(new PropertyValueFactory<>("mood"));

        // Description column
        TableColumn<Movie, String> descriptionColumn = (TableColumn<Movie, String>) moviesTable.getColumns().get(3);
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Action column
        TableColumn<Movie, Movie> actionColumn = (TableColumn<Movie, Movie>) moviesTable.getColumns().get(4);
        actionColumn.setCellFactory(col -> new TableCell<Movie, Movie>() {
            private final Button deleteBtn = new Button("Delete");

            {
                deleteBtn.setStyle("-fx-font-size:11; -fx-padding:5 10;");
                deleteBtn.setOnAction(event -> {
                    Movie movie = getTableView().getItems().get(getIndex());
                    MovieRepository.deleteMovie(movie.getId());
                    loadMovies();
                });
            }

            @Override
            protected void updateItem(Movie item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteBtn);
            }
        });
    }

    private void loadMovies() {
        moviesTable.setItems(MovieRepository.getAllMovies());
    }

    @FXML
    private void onAddMovie(ActionEvent event) {
        String title = titleField.getText().trim();
        String genre = genreCombo.getValue();
        String mood = moodCombo.getValue();
        String description = descriptionField.getText().trim();
        String posterUrl = posterUrlField.getText().trim();
        String rating = ratingField.getText().trim();
        String review = reviewField.getText().trim();

        if (title.isEmpty() || genre == null || mood == null) {
            statusLabel.setText("❌ Title, Genre, and Mood are required!");
            statusLabel.setStyle("-fx-text-fill:#ff4444; -fx-font-weight: bold;");
            return;
        }

        Movie movie = new Movie(0, title, genre, mood, description, posterUrl, rating, review);
        MovieRepository.addMovie(movie);

        // Clear fields
        titleField.clear();
        genreCombo.setValue(null);
        moodCombo.setValue(null);
        descriptionField.clear();
        posterUrlField.clear();
        ratingField.clear();
        reviewField.clear();

        // Show success message
        statusLabel.setText("✅ Movie added successfully!");
        statusLabel.setStyle("-fx-text-fill:#4caf50; -fx-font-weight: bold;");

        // Reload table
        loadMovies();
    }

    @FXML    private void onBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("Home.fxml"));
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(loader.load(), stage.getWidth(), stage.getHeight());
            scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML    private void onLogout(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
        
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(loader.load(), stage.getWidth(), stage.getHeight());
        scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
