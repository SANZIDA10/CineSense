package com.example.myapp;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UserPageController {

    // Mood selection section
    @FXML private HBox moodButtonsBox;

    // Recommended movies section
    @FXML private Label selectedMoodLabel;
    @FXML private HBox moviesBox;

    // Preview section
    @FXML private VBox previewSection;
    @FXML private StackPane posterPreview;
    @FXML private Label previewTitleLabel;
    @FXML private Label previewGenreLabel;
    @FXML private Label previewMoodLabel;
    @FXML private Label previewRatingLabel;
    @FXML private Label previewReviewLabel;
    @FXML private Button previewToggleBtn;

    // Browse all movies section
    @FXML private VBox allMoviesSection;

    private Movie selectedMovie;

    @FXML
    public void initialize() {
        loadMoodButtons();
        loadAllMovies();
    }

    // Load all movies grouped by genre in Browse All Movies section
    private void loadAllMovies() {
        allMoviesSection.getChildren().clear();

        ObservableList<Movie> allMovies = MovieRepository.getAllMovies();
        if (allMovies.isEmpty()) {
            Label emptyLabel = new Label("No movies available. Add movies from the moderator panel.");
            emptyLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.6); -fx-font-size: 14px;");
            allMoviesSection.getChildren().add(emptyLabel);
            return;
        }

        // Group movies by genre
        java.util.Map<String, java.util.List<Movie>> moviesByGenre = new java.util.HashMap<>();
        for (Movie movie : allMovies) {
            String genre = movie.getGenre() != null ? movie.getGenre() : "Other";
            moviesByGenre.computeIfAbsent(genre, k -> new java.util.ArrayList<>()).add(movie);
        }

        // Create a section for each genre
        for (java.util.Map.Entry<String, java.util.List<Movie>> entry : moviesByGenre.entrySet()) {
            String genre = entry.getKey();
            java.util.List<Movie> movies = entry.getValue();

            VBox genreSection = new VBox(12);
            genreSection.setStyle("-fx-padding: 12; -fx-background-color: rgba(255,255,255,0.02); -fx-background-radius: 8;");

            // Genre title
            Label genreTitle = new Label(genre);
            genreTitle.setStyle("-fx-text-fill: #ffb347; -fx-font-size: 18px; -fx-font-weight: bold;");

            // Movies horizontal scroll
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setFitToHeight(true);
            scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

            HBox moviesRow = new HBox(16);
            moviesRow.setStyle("-fx-padding: 8;");

            for (Movie movie : movies) {
                VBox movieCard = createMoviePosterCard(movie);
                moviesRow.getChildren().add(movieCard);
            }

            scrollPane.setContent(moviesRow);
            genreSection.getChildren().addAll(genreTitle, scrollPane);
            allMoviesSection.getChildren().add(genreSection);
        }
    }

    // Dynamically create buttons for each mood
    private void loadMoodButtons() {
        ObservableList<String> moods = MovieRepository.getAvailableMoods();
        for (String mood : moods) {
            Button btn = new Button(mood);
            btn.getStyleClass().add("mood-chip");
            btn.setOnAction(e -> loadMoviesByMood(mood));
            moodButtonsBox.getChildren().add(btn);
        }
    }

    // Load recommended movies for selected mood
    private void loadMoviesByMood(String mood) {
        selectedMoodLabel.setText("Showing movies for mood: " + mood);
        moviesBox.getChildren().clear();

        ObservableList<Movie> movies = MovieRepository.getMoviesByMood(mood);
        if (movies.isEmpty()) {
            moviesBox.getChildren().add(new Label("No movies found for this mood."));
            return;
        }

        for (Movie movie : movies) {
            VBox movieCard = createMoviePosterCard(movie);
            moviesBox.getChildren().add(movieCard);
        }
    }

    // Create a movie poster card with image and title
    private VBox createMoviePosterCard(Movie movie) {
        VBox card = new VBox(8);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-cursor: hand; -fx-padding: 8; -fx-background-color: rgba(255,255,255,0.05); " +
                "-fx-background-radius: 8; -fx-border-color: rgba(255,179,71,0.2); -fx-border-radius: 8;");

        // Poster image
        StackPane posterPane = new StackPane();
        posterPane.setStyle("-fx-background-color: rgba(0,0,0,0.3); -fx-background-radius: 6;");
        posterPane.setMinSize(150, 220);
        posterPane.setMaxSize(150, 220);

        if (movie.getPosterUrl() != null && !movie.getPosterUrl().isEmpty()) {
            try {
                Image posterImage = new Image(movie.getPosterUrl(), 150, 220, false, true, true);
                ImageView posterView = new ImageView(posterImage);
                posterView.setFitWidth(150);
                posterView.setFitHeight(220);
                posterView.setPreserveRatio(true);
                posterPane.getChildren().add(posterView);
            } catch (Exception ex) {
                Label noImage = new Label("No Image");
                noImage.setStyle("-fx-text-fill: rgba(255,255,255,0.4);");
                posterPane.getChildren().add(noImage);
            }
        } else {
            Label noImage = new Label("No Poster");
            noImage.setStyle("-fx-text-fill: rgba(255,255,255,0.4);");
            posterPane.getChildren().add(noImage);
        }

        // Movie title
        Label titleLabel = new Label(movie.getTitle());
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 12px;");
        titleLabel.setMaxWidth(150);
        titleLabel.setWrapText(true);
        titleLabel.setAlignment(Pos.CENTER);

        card.getChildren().addAll(posterPane, titleLabel);

        // Click to show details popup
        card.setOnMouseClicked(e -> showMovieDetailsPopup(movie));

        // Hover effect
        card.setOnMouseEntered(e -> card.setStyle("-fx-cursor: hand; -fx-padding: 8; -fx-background-color: rgba(255,179,71,0.15); " +
                "-fx-background-radius: 8; -fx-border-color: rgba(255,179,71,0.5); -fx-border-radius: 8;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-cursor: hand; -fx-padding: 8; -fx-background-color: rgba(255,255,255,0.05); " +
                "-fx-background-radius: 8; -fx-border-color: rgba(255,179,71,0.2); -fx-border-radius: 8;"));

        return card;
    }

    // Show movie details in a popup dialog
    private void showMovieDetailsPopup(Movie movie) {
        selectedMovie = movie;

        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle(movie.getTitle());

        VBox content = new VBox(20);
        content.setAlignment(Pos.TOP_CENTER);
        content.setStyle("-fx-padding: 30; -fx-background-color: linear-gradient(to bottom, #1a1a2e, #16213e);");

        // Title
        Label titleLabel = new Label(safe(movie.getTitle(), "Untitled"));
        titleLabel.setStyle("-fx-text-fill: #ffb347; -fx-font-size: 24px; -fx-font-weight: bold;");

        // Details box
        VBox detailsBox = new VBox(12);
        detailsBox.setStyle("-fx-padding: 20; -fx-background-color: rgba(255,255,255,0.05); -fx-background-radius: 8;");

        detailsBox.getChildren().addAll(
                createDetailRow("Genre:", safe(movie.getGenre(), "-")),
                createDetailRow("Mood:", safe(movie.getMood(), "-")),
                createDetailRow("Average Rating:", formatRatingForDisplay(movie.getRating())),
                createDetailRow("Description:", safe(movie.getDescription(), "No description available."))
        );

        // IMDB Link (if available)
        if (movie.getImdbLink() != null && !movie.getImdbLink().trim().isEmpty()) {
            javafx.scene.control.Hyperlink imdbLink = new javafx.scene.control.Hyperlink("🎬 View on IMDB");
            imdbLink.setStyle("-fx-text-fill: #ffb347; -fx-font-size: 14px; -fx-font-weight: bold; -fx-underline: true;");
            imdbLink.setOnAction(e -> {
                try {
                    java.awt.Desktop.getDesktop().browse(new java.net.URI(movie.getImdbLink()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            VBox linkBox = new VBox(imdbLink);
            linkBox.setAlignment(Pos.CENTER);
            linkBox.setStyle("-fx-padding: 10 0;");
            content.getChildren().add(linkBox);
        }

        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button showPreviewBtn = new Button("Show Preview");
        showPreviewBtn.setStyle("-fx-background-color: linear-gradient(#ffb347, #ff9800); -fx-text-fill: black; " +
                "-fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 6;");
        showPreviewBtn.setOnAction(e -> {
            popup.close();
            showPreviewPopup(movie);
        });

        Button closeBtn = new Button("Close");
        closeBtn.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 6;");
        closeBtn.setOnAction(e -> popup.close());

        buttonBox.getChildren().addAll(showPreviewBtn, closeBtn);

        content.getChildren().addAll(titleLabel, detailsBox, buttonBox);

        Scene scene = new Scene(content, 550, 400);
        popup.setScene(scene);
        popup.show();
    }

    // Show preview in a popup dialog with poster and review
    private void showPreviewPopup(Movie movie) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle(movie.getTitle() + " - Preview");

        HBox content = new HBox(25);
        content.setAlignment(Pos.TOP_CENTER);
        content.setStyle("-fx-padding: 30; -fx-background-color: linear-gradient(to bottom, #1a1a2e, #16213e);");

        // Left: Poster
        VBox posterBox = new VBox(10);
        posterBox.setAlignment(Pos.TOP_CENTER);

        StackPane posterPane = new StackPane();
        posterPane.setStyle("-fx-background-color: rgba(0,0,0,0.3); -fx-background-radius: 8;");
        posterPane.setMinSize(250, 370);
        posterPane.setMaxSize(250, 370);

        if (movie.getPosterUrl() != null && !movie.getPosterUrl().isEmpty()) {
            try {
                Image posterImage = new Image(movie.getPosterUrl(), 250, 370, false, true, true);
                ImageView posterView = new ImageView(posterImage);
                posterView.setFitWidth(250);
                posterView.setFitHeight(370);
                posterView.setPreserveRatio(true);
                posterPane.getChildren().add(posterView);
            } catch (Exception ex) {
                Label noImage = new Label("No Poster Available");
                noImage.setStyle("-fx-text-fill: rgba(255,255,255,0.5);");
                posterPane.getChildren().add(noImage);
            }
        } else {
            Label noImage = new Label("No Poster");
            noImage.setStyle("-fx-text-fill: rgba(255,255,255,0.5);");
            posterPane.getChildren().add(noImage);
        }

        posterBox.getChildren().add(posterPane);

        // Create rating label early so it can be referenced in rating box
        final Label ratingLabel = new Label("⭐ " + formatRatingForDisplay(movie.getRating()));
        ratingLabel.setStyle("-fx-text-fill: #ffd700; -fx-font-size: 16px; -fx-font-weight: bold;");

        // Rating section below poster
        VBox ratingBox = new VBox(10);
        ratingBox.setAlignment(Pos.CENTER);
        ratingBox.setStyle("-fx-padding: 15; -fx-background-color: rgba(255,179,71,0.1); -fx-background-radius: 8;");

        Label ratingTitle = new Label("Rate this movie:");
        ratingTitle.setStyle("-fx-text-fill: #ffb347; -fx-font-weight: bold; -fx-font-size: 13px;");

        HBox starsBox = new HBox(6);
        starsBox.setAlignment(Pos.CENTER);

        Label[] stars = new Label[5];
        final int[] selectedRating = {0};

        for (int i = 0; i < 5; i++) {
            final int starIndex = i + 1;
            stars[i] = new Label("☆");
            stars[i].setStyle("-fx-font-size: 24px; -fx-text-fill: #ffd700; -fx-cursor: hand;");

            // Hover effect
            stars[i].setOnMouseEntered(e2 -> {
                for (int j = 0; j < starIndex; j++) {
                    stars[j].setText("★");
                }
            });

            stars[i].setOnMouseExited(e2 -> {
                for (int j = 0; j < 5; j++) {
                    stars[j].setText(j < selectedRating[0] ? "★" : "☆");
                }
            });

            // Click to select rating
            stars[i].setOnMouseClicked(e2 -> {
                selectedRating[0] = starIndex;
                for (int j = 0; j < 5; j++) {
                    stars[j].setText(j < selectedRating[0] ? "★" : "☆");
                }
            });

            starsBox.getChildren().add(stars[i]);
        }

        Label ratingFeedback = new Label("");
        ratingFeedback.setStyle("-fx-text-fill: #4caf50; -fx-font-size: 10px; -fx-font-weight: bold;");
        ratingFeedback.setWrapText(true);
        ratingFeedback.setMaxWidth(240);
        ratingFeedback.setAlignment(Pos.CENTER);

        Button submitRatingBtn = new Button("Submit Rating");
        submitRatingBtn.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-padding: 6 14; -fx-background-radius: 6; -fx-font-size: 12px;");
        submitRatingBtn.setOnAction(e2 -> {
            if (selectedRating[0] > 0) {
                submitUserRating(movie, selectedRating[0]);
                ratingFeedback.setText("✓ Rating added!");

                // Update the rating display
                String updatedRating = MovieRepository.getAllMovies().stream()
                        .filter(m -> m.getId() == movie.getId())
                        .findFirst()
                        .map(Movie::getRating)
                        .orElse(movie.getRating());
                ratingLabel.setText("⭐ " + formatRatingForDisplay(updatedRating));

                // Refresh the movie list to show updated rating
                loadAllMovies();
            } else {
                ratingFeedback.setText("Please select a rating!");
                ratingFeedback.setStyle("-fx-text-fill: #ff4444; -fx-font-size: 10px; -fx-font-weight: bold;");
            }
        });

        ratingBox.getChildren().addAll(ratingTitle, starsBox, submitRatingBtn, ratingFeedback);
        posterBox.getChildren().add(ratingBox);

        // Right: Info
        VBox infoBox = new VBox(15);
        infoBox.setMaxWidth(400);

        Label titleLabel = new Label(safe(movie.getTitle(), "Untitled"));
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
        titleLabel.setWrapText(true);

        // ratingLabel already created earlier before rating box

        VBox metaBox = new VBox(8);
        metaBox.getChildren().addAll(
                createDetailRow("Genre:", safe(movie.getGenre(), "-")),
                createDetailRow("Mood:", safe(movie.getMood(), "-"))
        );

        Label reviewTitle = new Label("📝 Review");
        reviewTitle.setStyle("-fx-text-fill: #ffb347; -fx-font-size: 16px; -fx-font-weight: bold;");

        ScrollPane reviewScroll = new ScrollPane();
        reviewScroll.setFitToWidth(true);
        reviewScroll.setPrefHeight(120);
        reviewScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        Label reviewLabel = new Label(safe(movie.getReview(), "No official review available."));
        reviewLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.85); -fx-font-size: 13px;");
        reviewLabel.setWrapText(true);
        reviewLabel.setMaxWidth(380);

        reviewScroll.setContent(reviewLabel);

        // User Reviews Section
        VBox userReviewsSection = new VBox(12);
        userReviewsSection.setStyle("-fx-padding: 15; -fx-background-color: rgba(255,179,71,0.08); -fx-background-radius: 8;");

        Label userReviewsTitle = new Label("💬 User Reviews");
        userReviewsTitle.setStyle("-fx-text-fill: #ffb347; -fx-font-size: 15px; -fx-font-weight: bold;");

        // Write Review Section
        VBox writeReviewBox = new VBox(8);
        writeReviewBox.setStyle("-fx-padding: 10; -fx-background-color: rgba(0,0,0,0.2); -fx-background-radius: 6;");

        Label writeReviewLabel = new Label("Write your review:");
        writeReviewLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold;");

        javafx.scene.control.TextField nameField = new javafx.scene.control.TextField();
        nameField.setPromptText("Your name");
        nameField.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-text-fill: white; -fx-prompt-text-fill: rgba(255,255,255,0.5); -fx-padding: 5;");
        nameField.setMaxWidth(200);

        javafx.scene.control.TextArea reviewTextArea = new javafx.scene.control.TextArea();
        reviewTextArea.setPromptText("Share your thoughts about this movie...");
        reviewTextArea.setWrapText(true);
        reviewTextArea.setPrefRowCount(2);
        reviewTextArea.setStyle("-fx-control-inner-background: rgba(255,255,255,0.1); -fx-text-fill: white; -fx-prompt-text-fill: rgba(255,255,255,0.5);");

        Label reviewFeedback = new Label("");
        reviewFeedback.setStyle("-fx-text-fill: #4caf50; -fx-font-size: 11px; -fx-font-weight: bold;");

        Button submitReviewBtn = new Button("Submit Review");
        submitReviewBtn.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 6 14; -fx-background-radius: 5;");

        writeReviewBox.getChildren().addAll(writeReviewLabel, nameField, reviewTextArea, submitReviewBtn, reviewFeedback);

        // Display existing reviews
        ScrollPane reviewsListScroll = new ScrollPane();
        reviewsListScroll.setFitToWidth(true);
        reviewsListScroll.setPrefHeight(180);
        reviewsListScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        VBox reviewsList = new VBox(10);
        reviewsList.setStyle("-fx-padding: 10;");

        javafx.collections.ObservableList<UserReview> userReviews = MovieRepository.getUserReviewsByMovie(movie.getId());

        if (userReviews.isEmpty()) {
            Label noReviews = new Label("No user reviews yet. Be the first to review!");
            noReviews.setStyle("-fx-text-fill: rgba(255,255,255,0.5); -fx-font-size: 12px; -fx-font-style: italic;");
            reviewsList.getChildren().add(noReviews);
        } else {
            for (UserReview ur : userReviews) {
                VBox reviewItem = new VBox(5);
                reviewItem.setStyle("-fx-padding: 10; -fx-background-color: rgba(0,0,0,0.3); -fx-background-radius: 5;");

                HBox reviewHeader = new HBox(10);
                Label reviewerName = new Label(ur.getReviewerName());
                reviewerName.setStyle("-fx-text-fill: #ffb347; -fx-font-weight: bold; -fx-font-size: 12px;");

                Label reviewDate = new Label(ur.getReviewDate());
                reviewDate.setStyle("-fx-text-fill: rgba(255,255,255,0.5); -fx-font-size: 10px;");

                reviewHeader.getChildren().addAll(reviewerName, reviewDate);

                Label reviewText = new Label(ur.getReviewText());
                reviewText.setStyle("-fx-text-fill: rgba(255,255,255,0.9); -fx-font-size: 11px;");
                reviewText.setWrapText(true);
                reviewText.setMaxWidth(380);

                reviewItem.getChildren().addAll(reviewHeader, reviewText);
                reviewsList.getChildren().add(reviewItem);
            }
        }

        reviewsListScroll.setContent(reviewsList);

        // Submit review action
        submitReviewBtn.setOnAction(ev -> {
            String name = nameField.getText().trim();
            String reviewText = reviewTextArea.getText().trim();

            if (name.isEmpty() || reviewText.isEmpty()) {
                reviewFeedback.setText("❌ Please fill in your name and review");
                reviewFeedback.setStyle("-fx-text-fill: #ff4444; -fx-font-size: 11px; -fx-font-weight: bold;");
            } else {
                MovieRepository.addUserReview(movie.getId(), name, reviewText);
                reviewFeedback.setText("✓ Review submitted successfully!");
                reviewFeedback.setStyle("-fx-text-fill: #4caf50; -fx-font-size: 11px; -fx-font-weight: bold;");

                // Clear fields
                nameField.clear();
                reviewTextArea.clear();

                // Refresh reviews list
                reviewsList.getChildren().clear();
                javafx.collections.ObservableList<UserReview> updatedReviews = MovieRepository.getUserReviewsByMovie(movie.getId());

                if (updatedReviews.isEmpty()) {
                    Label noReviews = new Label("No user reviews yet. Be the first to review!");
                    noReviews.setStyle("-fx-text-fill: rgba(255,255,255,0.5); -fx-font-size: 12px; -fx-font-style: italic;");
                    reviewsList.getChildren().add(noReviews);
                } else {
                    for (UserReview ur : updatedReviews) {
                        VBox reviewItem = new VBox(5);
                        reviewItem.setStyle("-fx-padding: 10; -fx-background-color: rgba(0,0,0,0.3); -fx-background-radius: 5;");

                        HBox reviewHeader = new HBox(10);
                        Label reviewerName = new Label(ur.getReviewerName());
                        reviewerName.setStyle("-fx-text-fill: #ffb347; -fx-font-weight: bold; -fx-font-size: 12px;");

                        Label reviewDate = new Label(ur.getReviewDate());
                        reviewDate.setStyle("-fx-text-fill: rgba(255,255,255,0.5); -fx-font-size: 10px;");

                        reviewHeader.getChildren().addAll(reviewerName, reviewDate);

                        Label reviewText2 = new Label(ur.getReviewText());
                        reviewText2.setStyle("-fx-text-fill: rgba(255,255,255,0.9); -fx-font-size: 11px;");
                        reviewText2.setWrapText(true);
                        reviewText2.setMaxWidth(380);

                        reviewItem.getChildren().addAll(reviewHeader, reviewText2);
                        reviewsList.getChildren().add(reviewItem);
                    }
                }
            }
        });

        userReviewsSection.getChildren().addAll(userReviewsTitle, writeReviewBox, reviewsListScroll);

        Button closeBtn = new Button("Close");
        closeBtn.setStyle("-fx-background-color: linear-gradient(#ffb347, #ff9800); -fx-text-fill: black; " +
                "-fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 6;");
        closeBtn.setOnAction(e -> popup.close());

        infoBox.getChildren().addAll(titleLabel, ratingLabel, metaBox, reviewTitle, reviewScroll, userReviewsSection, closeBtn);

        content.getChildren().addAll(posterBox, infoBox);

        Scene scene = new Scene(content, 750, 800);
        popup.setScene(scene);
        popup.show();
    }

    // Helper method to create detail rows
    private HBox createDetailRow(String label, String value) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        Label labelText = new Label(label);
        labelText.setStyle("-fx-text-fill: #ffb347; -fx-font-weight: bold;");

        Label valueText = new Label(value);
        valueText.setStyle("-fx-text-fill: white;");
        valueText.setWrapText(true);
        valueText.setMaxWidth(350);

        row.getChildren().addAll(labelText, valueText);
        return row;
    }

    private String safe(String value, String fallback) {
        return (value == null || value.isBlank()) ? fallback : value;
    }

    // Calculate and update average rating
    private void submitUserRating(Movie movie, int userRating) {
        String currentRating = movie.getRating();
        double newAverage;
        int voteCount;

        if (currentRating == null || currentRating.isBlank() || currentRating.equals("Not rated") || currentRating.equals("Not rated yet")) {
            // First rating
            newAverage = userRating;
            voteCount = 1;
        } else {
            // Parse existing rating format: "4.5:10" (average:count) or "4.5/5" or "4.5/5 (10 votes)"
            try {
                if (currentRating.contains(":")) {
                    // Format: "4.5:10"
                    String[] parts = currentRating.split(":");
                    double oldAverage = Double.parseDouble(parts[0]);
                    voteCount = Integer.parseInt(parts[1]);
                    newAverage = ((oldAverage * voteCount) + userRating) / (voteCount + 1);
                    voteCount++;
                } else if (currentRating.contains("(")) {
                    // Format: "4.5/5 (10 votes)"
                    String avgPart = currentRating.substring(0, currentRating.indexOf("/"));
                    String countPart = currentRating.substring(currentRating.indexOf("(") + 1, currentRating.indexOf(" votes"));
                    double oldAverage = Double.parseDouble(avgPart);
                    voteCount = Integer.parseInt(countPart);
                    newAverage = ((oldAverage * voteCount) + userRating) / (voteCount + 1);
                    voteCount++;
                } else {
                    // Format: "4.5/5" or just "4.5" - treat as single rating
                    String numStr = currentRating.replaceAll("[^0-9.]", "");
                    double oldRating = Double.parseDouble(numStr);
                    newAverage = (oldRating + userRating) / 2.0;
                    voteCount = 2;
                }
            } catch (Exception e) {
                // If parsing fails, treat as first rating
                newAverage = userRating;
                voteCount = 1;
            }
        }

        // Store in "average:count" format
        String newRatingStr = String.format("%.1f:%d", newAverage, voteCount);
        movie.setRating(newRatingStr);
        MovieRepository.updateRating(movie.getId(), newRatingStr);
    }

    // Format rating for display
    private String formatRatingForDisplay(String rating) {
        if (rating == null || rating.isBlank() || rating.equals("Not rated")) {
            return "Not rated yet";
        }

        try {
            if (rating.contains(":")) {
                // Format: "4.5:10" -> "4.5/5 (10 votes)"
                String[] parts = rating.split(":");
                double avg = Double.parseDouble(parts[0]);
                int count = Integer.parseInt(parts[1]);
                return String.format("%.1f/5 (%d %s)", avg, count, count == 1 ? "vote" : "votes");
            } else {
                // Already formatted or simple rating
                return rating;
            }
        } catch (Exception e) {
            return rating;
        }
    }

    // Toggle preview section visibility
    @FXML
    private void onTogglePreview() {
        if (selectedMovie != null) {
            showPreviewPopup(selectedMovie);
        } else {
            // Show message if no movie is selected
            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.setTitle("No Movie Selected");

            VBox content = new VBox(20);
            content.setAlignment(Pos.CENTER);
            content.setStyle("-fx-padding: 40; -fx-background-color: linear-gradient(to bottom, #1a1a2e, #16213e);");

            Label message = new Label("Please select a movie first!");
            message.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

            Button closeBtn = new Button("OK");
            closeBtn.setStyle("-fx-background-color: linear-gradient(#ffb347, #ff9800); -fx-text-fill: black; " +
                    "-fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 6;");
            closeBtn.setOnAction(e -> popup.close());

            content.getChildren().addAll(message, closeBtn);

            Scene scene = new Scene(content, 300, 150);
            popup.setScene(scene);
            popup.show();
        }
    }

    // Back button handler
    @FXML
    private void onBack() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("Home.fxml")
            );
            javafx.stage.Stage stage = (javafx.stage.Stage) previewToggleBtn.getScene().getWindow();
            javafx.scene.Scene scene = new javafx.scene.Scene(loader.load(), stage.getWidth(), stage.getHeight());
            scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());

            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
