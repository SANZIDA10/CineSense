package com.example.myapp;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HelloController — compact, with mood icons restored.
 *
 * This file is the same controller as before but now uses setupMoodButton(...) to
 * place an icon (if available) or fallback emoji text on each mood Button.
 */
public class HelloController {

    // Top search / nav
    @FXML private TextField topSearch;

    // Mood chips (note: FXML ids must match)
    @FXML private Button moodHappy, moodSad, moodBored, moodStressed;
    @FXML private Button getStarted, browseMovies;

    // Hero / page
    @FXML private StackPane heroStack;
    @FXML private ImageView heroBg, filmGrain;
    @FXML private ScrollPane pageScroll;
    @FXML private VBox pageContent;

    // Personalized picks row
    @FXML private ScrollPane personalizedScroll;
    @FXML private HBox personalizedRow;
    @FXML private Rectangle personalizedIndicator;
    @FXML private javafx.scene.layout.Pane personalizedIndicatorPane;
    @FXML private Label personalizedTitle;

    // Top picks row
    @FXML private ScrollPane topPicksScroll;
    @FXML private HBox topPicksRow;
    @FXML private Rectangle rowIndicator;
    @FXML private javafx.scene.layout.Pane indicatorPane;
    @FXML private Button topLeft, topRight;

    // Minimal sample data
    private final Map<String, List<String>> moodRecommendations = new HashMap<>();
    private String selectedMood = null;

    @FXML
    public void initialize() {
        // Sample recommendations
        moodRecommendations.put("Happy", List.of("Sunny Days", "Joyride", "Bright Side", "Warm Hearts"));
        moodRecommendations.put("Sad", List.of("After Rain", "A Quiet Year", "Tearfalls", "Solitude"));
        moodRecommendations.put("Bored", List.of("Idle Hours", "Window Watch", "Slow Days", "Blank Scenes"));
        moodRecommendations.put("Stressed", List.of("Edge of Night", "Hunted", "Cold Case", "Silent Vault"));

        // Optional hero / grain images (safe)
        safeSetImage(heroBg, "/com/example/myapp/assets/hero_bg.jpg");
        if (filmGrain != null) safeSetImage(filmGrain, "/com/example/myapp/assets/film_grain.png");

        // --- Restore mood icons / emoji labels ---
        // Try to load an icon from resources; if not found fallback to emoji+text
        setupMoodButton(moodHappy,    "😊  Happy",    "/com/example/myapp/assets/mood_happy.png");
        setupMoodButton(moodSad,      "😢  Sad",      "/com/example/myapp/assets/mood_sad.png");
        setupMoodButton(moodBored,    "😐  Bored",    "/com/example/myapp/assets/mood_bored.png");
        setupMoodButton(moodStressed, "😣  Stressed", "/com/example/myapp/assets/mood_stressed.png");

        // Wire mood buttons
        if (moodHappy != null)    moodHappy.setOnAction(e -> selectMood("Happy"));
        if (moodSad != null)      moodSad.setOnAction(e -> selectMood("Sad"));
        if (moodBored != null)    moodBored.setOnAction(e -> selectMood("Bored"));
        if (moodStressed != null) moodStressed.setOnAction(e -> selectMood("Stressed"));

        if (getStarted != null) getStarted.setOnAction(e -> System.out.println("Get Started"));
        if (browseMovies != null) browseMovies.setOnAction(e -> System.out.println("Browse Movies"));
        if (topSearch != null) topSearch.setOnAction(e -> System.out.println("Search: " + topSearch.getText()));

        // Populate top picks and update indicators
        populateTopPicks();

        // Attach wheel->horizontal only to row content (so outer pageScroll keeps vertical scrolling)
        attachWheelToContent(personalizedScroll);
        attachWheelToContent(topPicksScroll);

        // Indicator updates
        if (personalizedScroll != null) {
            personalizedScroll.viewportBoundsProperty().addListener((o, a, b) -> Platform.runLater(this::updatePersonalizedIndicator));
            personalizedScroll.hvalueProperty().addListener((o, a, b) -> updatePersonalizedIndicatorPosition());
        }
        if (topPicksScroll != null) {
            topPicksScroll.viewportBoundsProperty().addListener((o, a, b) -> Platform.runLater(this::updateTopPicksIndicator));
            topPicksScroll.hvalueProperty().addListener((o, a, b) -> updateTopPicksIndicatorPosition());
        }

        // chevrons
        if (topLeft != null) topLeft.setOnAction(e -> animatedScrollByFraction(topPicksScroll, -0.34));
        if (topRight != null) topRight.setOnAction(e -> animatedScrollByFraction(topPicksScroll, 0.34));

        // initial indicator compute
        Platform.runLater(() -> { updatePersonalizedIndicator(); updateTopPicksIndicator(); });
    }

    // ----------------- FXML handler methods required by hello-view.fxml -----------------

    @FXML private void onNavHome()      { System.out.println("Navigate -> Home"); }
    @FXML private void onNavMoodPicks() { System.out.println("Navigate -> Mood Picks"); }
    @FXML private void onNavGenres()    { System.out.println("Navigate -> Genres"); }
    @FXML private void onNavMyList()    { System.out.println("Navigate -> My List"); }
    @FXML private void onNavAbout()     { System.out.println("Navigate -> About"); }

    @FXML private void onMoodHappy()    { selectMood("Happy"); }
    @FXML private void onMoodSad()      { selectMood("Sad"); }
    @FXML private void onMoodBored()    { selectMood("Bored"); }
    @FXML private void onMoodStressed() { selectMood("Stressed"); }

    @FXML private void onGetStarted()   { System.out.println("Get Started clicked"); }
    @FXML private void onBrowseMovies() { System.out.println("Browse Movies clicked"); }

    @FXML private void onSearch() { if (topSearch != null) System.out.println("Search: " + topSearch.getText()); }

    @FXML private void onGenreAction(ActionEvent event) {
        Object src = event.getSource();
        if (src instanceof Button) System.out.println("Genre clicked: " + ((Button) src).getText());
        else System.out.println("Genre clicked");
    }

    // ----------------- small helpers & UI logic -----------------

    private void safeSetImage(ImageView iv, String resource) {
        if (iv == null) return;
        try {
            URL u = getClass().getResource(resource);
            if (u != null) iv.setImage(new Image(u.toExternalForm(), true));
        } catch (Exception ignored) {}
    }

    /**
     * Set icon+text for mood button. If the icon resource is present it will be used;
     * otherwise the provided text (which can include emoji) will be set.
     */
    private void setupMoodButton(Button btn, String fallbackText, String iconResourcePath) {
        if (btn == null) return;
        btn.setText(fallbackText); // fallback label (emoji + word)
        try {
            URL url = getClass().getResource(iconResourcePath);
            if (url != null) {
                Image img = new Image(url.toExternalForm(), true);
                ImageView iv = new ImageView(img);
                iv.setPreserveRatio(true);
                iv.setFitWidth(18);
                iv.setFitHeight(18);
                btn.setGraphic(iv);
                btn.setContentDisplay(ContentDisplay.LEFT);
                btn.setGraphicTextGap(8);
                Tooltip.install(btn, new Tooltip(fallbackText.replaceAll("[^\\p{L}\\p{N}\\s]", "")));
            }
        } catch (Exception ex) {
            System.err.println("Failed to load mood icon: " + iconResourcePath + " -> " + ex.getMessage());
        }
    }

    private void selectMood(String mood) {
        if (mood == null) return;
        if (mood.equals(selectedMood)) { // toggle off
            selectedMood = null;
            if (personalizedRow != null) personalizedRow.getChildren().clear();
            if (personalizedTitle != null) personalizedTitle.setText("Recommended for you — choose a mood above");
            return;
        }
        selectedMood = mood;
        if (personalizedTitle != null) personalizedTitle.setText("Recommended for " + mood);
        populatePersonalizedForMood(mood);
    }

    private void populatePersonalizedForMood(String mood) {
        if (personalizedRow == null) return;
        personalizedRow.getChildren().clear();
        List<String> recs = moodRecommendations.getOrDefault(mood, List.of());
        if (recs.isEmpty()) return;
        for (String t : recs) personalizedRow.getChildren().add(createPosterCard(t));
        Platform.runLater(this::updatePersonalizedIndicator);
    }

    private void populateTopPicks() {
        if (topPicksRow == null) return;
        topPicksRow.getChildren().clear();
        for (int i = 1; i <= 8; i++) topPicksRow.getChildren().add(createPosterCard("Top Pick " + i));
        Platform.runLater(this::updateTopPicksIndicator);
    }

    private VBox createPosterCard(String title) {
        ImageView iv = new ImageView();
        iv.setFitWidth(160);
        iv.setFitHeight(230);
        iv.setPreserveRatio(true);
        iv.setOpacity(0);

        // load placeholder (safe)
        safeSetImage(iv, "/com/example/myapp/assets/poster_placeholder.png");

        // fade-in when loaded
        if (iv.getImage() != null) {
            iv.getImage().progressProperty().addListener((obs, o, n) -> {
                if (n != null && n.doubleValue() >= 1.0) {
                    Platform.runLater(() -> {
                        FadeTransition ft = new FadeTransition(Duration.millis(360), iv);
                        ft.setFromValue(0);
                        ft.setToValue(1);
                        ft.play();
                    });
                }
            });
        } else {
            iv.setOpacity(1);
        }

        Label lbl = new Label(title);
        Button add = new Button("Add");
        add.setOnAction(e -> System.out.println("Added " + title));

        VBox card = new VBox(6, iv, lbl, add);
        card.getStyleClass().add("movie-card");

        // hover scale
        card.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(140), card); st.setToX(1.03); st.setToY(1.03); st.play();
        });
        card.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(140), card); st.setToX(1); st.setToY(1); st.play();
        });

        // click -> simple modal
        card.setOnMouseClicked(e -> openDetailsModal(title, iv.getImage()));

        return card;
    }

    private void openDetailsModal(String title, Image poster) {
        VBox root = new VBox(10);
        root.setStyle("-fx-padding:18; -fx-background-color:#111;");
        Label t = new Label(title);
        t.setStyle("-fx-text-fill:white; -fx-font-weight:700;");
        ImageView iv = new ImageView();
        if (poster != null) { iv.setImage(poster); iv.setFitWidth(300); iv.setPreserveRatio(true); }
        Button close = new Button("Close"); close.setOnAction(e -> ((Stage)close.getScene().getWindow()).close());
        root.getChildren().addAll(t, iv, close);

        Stage s = new Stage(StageStyle.UTILITY);
        s.initModality(Modality.APPLICATION_MODAL);
        s.setScene(new Scene(root));
        s.setTitle(title);
        s.showAndWait();
    }

    // Attach wheel->horizontal to content node of the given scroll pane so
    // vertical wheel events still scroll pageScroll when pointer is elsewhere.
    private void attachWheelToContent(ScrollPane sp) {
        if (sp == null) return;
        var attach = new Runnable() {
            private boolean attached = false;
            @Override public void run() {
                if (attached) return;
                if (sp.getContent() == null) return;
                sp.getContent().addEventFilter(ScrollEvent.SCROLL, ev -> {
                    // convert mostly-vertical wheel into horizontal scroll on the row
                    if (Math.abs(ev.getDeltaY()) > Math.abs(ev.getDeltaX())) {
                        double sensitivity = 0.009;
                        double delta = -ev.getDeltaY() * sensitivity;
                        sp.setHvalue(clamp(sp.getHvalue() + delta, 0, 1));
                        ev.consume();
                    }
                });
                attached = true;
            }
        };
        if (sp.getContent() != null) attach.run();
        else sp.contentProperty().addListener((o, oldC, newC) -> { if (newC != null) Platform.runLater(attach); });
    }

    // --- indicators for personalized row ---
    private void updatePersonalizedIndicator() {
        if (personalizedScroll == null || personalizedIndicator == null || personalizedIndicatorPane == null) return;
        Bounds v = personalizedScroll.getViewportBounds();
        var c = personalizedScroll.getContent() == null ? null : personalizedScroll.getContent().getLayoutBounds();
        if (c == null) { personalizedIndicator.setVisible(false); return; }
        double vw = v.getWidth(), cw = c.getWidth();
        if (cw <= vw || vw <= 0) { personalizedIndicator.setVisible(false); return; }
        personalizedIndicator.setVisible(true);
        double w = Math.max(24, vw * (vw/cw));
        personalizedIndicator.setWidth(w);
        personalizedIndicatorPane.setPrefWidth(vw);
        updatePersonalizedIndicatorPosition();
    }
    private void updatePersonalizedIndicatorPosition() {
        if (personalizedScroll == null || personalizedIndicator == null) return;
        var c = personalizedScroll.getContent() == null ? null : personalizedScroll.getContent().getLayoutBounds();
        if (c == null) return;
        double vw = personalizedScroll.getViewportBounds().getWidth(), cw = c.getWidth();
        if (cw <= vw || vw <= 0) return;
        double x = personalizedScroll.getHvalue() * (vw - personalizedIndicator.getWidth());
        personalizedIndicator.setLayoutX(Math.max(0, x));
    }

    // --- indicators for top picks row ---
    private void updateTopPicksIndicator() {
        if (topPicksScroll == null || rowIndicator == null || indicatorPane == null) return;
        Bounds v = topPicksScroll.getViewportBounds();
        var c = topPicksScroll.getContent() == null ? null : topPicksScroll.getContent().getLayoutBounds();
        if (c == null) { rowIndicator.setVisible(false); return; }
        double vw = v.getWidth(), cw = c.getWidth();
        if (cw <= vw || vw <= 0) { rowIndicator.setVisible(false); return; }
        rowIndicator.setVisible(true);
        double w = Math.max(24, vw * (vw/cw));
        rowIndicator.setWidth(w);
        indicatorPane.setPrefWidth(vw);
        updateTopPicksIndicatorPosition();
    }
    private void updateTopPicksIndicatorPosition() {
        if (topPicksScroll == null || rowIndicator == null) return;
        var c = topPicksScroll.getContent() == null ? null : topPicksScroll.getContent().getLayoutBounds();
        if (c == null) return;
        double vw = topPicksScroll.getViewportBounds().getWidth(), cw = c.getWidth();
        if (cw <= vw || vw <= 0) return;
        double x = topPicksScroll.getHvalue() * (vw - rowIndicator.getWidth());
        rowIndicator.setLayoutX(Math.max(0, x));
    }

    // small animation helper
    private void animatedScrollByFraction(ScrollPane sp, double fraction) {
        if (sp == null) return;
        double target = clamp(sp.getHvalue() + fraction, 0, 1);
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(320), new KeyValue(sp.hvalueProperty(), target)));
        tl.play();
    }

    private double clamp(double v, double a, double b) { return Math.max(a, Math.min(b, v)); }
}