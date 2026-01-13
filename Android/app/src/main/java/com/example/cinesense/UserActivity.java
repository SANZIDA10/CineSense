package com.example.cinesense;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cinesense.adapters.MoodAdapter;
import com.example.cinesense.adapters.MovieAdapter;
import com.example.cinesense.adapters.UserReviewAdapter;
import com.example.cinesense.database.MovieRepository;
import com.example.cinesense.models.Movie;
import com.example.cinesense.models.UserReview;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserActivity extends AppCompatActivity {

    private EditText searchField;
    private RecyclerView moodRecyclerView, recommendedMoviesRecyclerView, allMoviesRecyclerView;
    private TextView selectedMoodLabel, noMoviesLabel;
    private Button backBtn, searchBtn;
    
    private MovieRepository repository;
    private MoodAdapter moodAdapter;
    private MovieAdapter recommendedAdapter, allMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        repository = new MovieRepository(this);
        
        initializeViews();
        setupMoodRecyclerView();
        setupRecommendedMoviesRecyclerView();
        setupAllMoviesRecyclerView();
        loadAllMovies();
    }

    private void initializeViews() {
        searchField = findViewById(R.id.searchField);
        moodRecyclerView = findViewById(R.id.moodRecyclerView);
        recommendedMoviesRecyclerView = findViewById(R.id.recommendedMoviesRecyclerView);
        allMoviesRecyclerView = findViewById(R.id.allMoviesRecyclerView);
        selectedMoodLabel = findViewById(R.id.selectedMoodLabel);
        noMoviesLabel = findViewById(R.id.noMoviesLabel);
        backBtn = findViewById(R.id.backBtn);
        searchBtn = findViewById(R.id.searchBtn);

        backBtn.setOnClickListener(v -> finish());
        searchBtn.setOnClickListener(v -> onSearch());
    }

    private void setupMoodRecyclerView() {
        moodRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        List<String> moods = repository.getAvailableMoods();
        moodAdapter = new MoodAdapter(moods, mood -> loadMoviesByMood(mood));
        moodRecyclerView.setAdapter(moodAdapter);
    }

    private void setupRecommendedMoviesRecyclerView() {
        recommendedMoviesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recommendedAdapter = new MovieAdapter(movie -> showMovieDetails(movie));
        recommendedMoviesRecyclerView.setAdapter(recommendedAdapter);
    }

    private void setupAllMoviesRecyclerView() {
        allMoviesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        allMoviesAdapter = new MovieAdapter(movie -> showMovieDetails(movie));
        allMoviesRecyclerView.setAdapter(allMoviesAdapter);
    }

    private void loadMoviesByMood(String mood) {
        selectedMoodLabel.setText("Showing movies for mood: " + mood);
        selectedMoodLabel.setVisibility(View.VISIBLE);
        
        List<Movie> movies = repository.getMoviesByMood(mood);
        recommendedAdapter.setMovies(movies);
        
        if (movies.isEmpty()) {
            noMoviesLabel.setText("No movies found for this mood.");
            noMoviesLabel.setVisibility(View.VISIBLE);
        } else {
            noMoviesLabel.setVisibility(View.GONE);
        }
    }

    private void loadAllMovies() {
        List<Movie> movies = repository.getAllMovies();
        allMoviesAdapter.setMovies(movies);
        
        if (movies.isEmpty()) {
            noMoviesLabel.setText("No movies available. Add movies from the moderator panel.");
            noMoviesLabel.setVisibility(View.VISIBLE);
        }
    }

    private void onSearch() {
        String searchTerm = searchField.getText().toString().trim();
        
        if (searchTerm.isEmpty()) {
            loadAllMovies();
            selectedMoodLabel.setVisibility(View.GONE);
            return;
        }
        
        List<Movie> searchResults = repository.searchMovies(searchTerm);
        allMoviesAdapter.setMovies(searchResults);
        
        selectedMoodLabel.setText("Search Results for \"" + searchTerm + "\" (" + searchResults.size() + " found)");
        selectedMoodLabel.setVisibility(View.VISIBLE);
        
        if (searchResults.isEmpty()) {
            noMoviesLabel.setText("No movies found matching \"" + searchTerm + "\"");
            noMoviesLabel.setVisibility(View.VISIBLE);
        } else {
            noMoviesLabel.setVisibility(View.GONE);
        }
    }

    private void showMovieDetails(Movie movie) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_movie_details, null);
        
        ImageView posterImage = dialogView.findViewById(R.id.posterImage);
        TextView titleText = dialogView.findViewById(R.id.titleText);
        TextView genreText = dialogView.findViewById(R.id.genreText);
        TextView moodText = dialogView.findViewById(R.id.moodText);
        TextView ratingText = dialogView.findViewById(R.id.ratingText);
        TextView descriptionText = dialogView.findViewById(R.id.descriptionText);
        Button imdbButton = dialogView.findViewById(R.id.imdbButton);
        Button showPreviewBtn = dialogView.findViewById(R.id.showPreviewBtn);
        
        // Load poster image
        if (!TextUtils.isEmpty(movie.getPosterUrl())) {
            Glide.with(this)
                .load(movie.getPosterUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(posterImage);
        }
        
        titleText.setText(movie.getTitle() != null ? movie.getTitle() : "Untitled");
        genreText.setText("Genre: " + (movie.getGenre() != null ? movie.getGenre() : "-"));
        moodText.setText("Mood: " + (movie.getMood() != null ? movie.getMood() : "-"));
        ratingText.setText("⭐ " + formatRatingForDisplay(movie.getRating()));
        descriptionText.setText(movie.getDescription() != null ? movie.getDescription() : "No description available.");
        
        // IMDB link
        if (!TextUtils.isEmpty(movie.getImdbLink())) {
            imdbButton.setVisibility(View.VISIBLE);
            imdbButton.setOnClickListener(v -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getImdbLink()));
                startActivity(browserIntent);
            });
        } else {
            imdbButton.setVisibility(View.GONE);
        }
        
        AlertDialog detailsDialog = new AlertDialog.Builder(this)
            .setView(dialogView)
            .setNegativeButton("Close", null)
            .create();
        
        showPreviewBtn.setOnClickListener(v -> {
            detailsDialog.dismiss();
            showMoviePreview(movie);
        });
        
        detailsDialog.show();
    }

    private void showMoviePreview(Movie movie) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_movie_preview, null);
        
        ImageView posterImage = dialogView.findViewById(R.id.posterImage);
        TextView titleText = dialogView.findViewById(R.id.titleText);
        TextView genreText = dialogView.findViewById(R.id.genreText);
        TextView moodText = dialogView.findViewById(R.id.moodText);
        TextView ratingText = dialogView.findViewById(R.id.ratingText);
        TextView reviewText = dialogView.findViewById(R.id.reviewText);
        
        // Star rating views
        LinearLayout starsLayout = dialogView.findViewById(R.id.starsLayout);
        TextView ratingFeedback = dialogView.findViewById(R.id.ratingFeedback);
        Button submitRatingBtn = dialogView.findViewById(R.id.submitRatingBtn);
        
        // User review views
        EditText nameField = dialogView.findViewById(R.id.nameField);
        EditText reviewField = dialogView.findViewById(R.id.reviewField);
        Button submitReviewBtn = dialogView.findViewById(R.id.submitReviewBtn);
        TextView reviewFeedback = dialogView.findViewById(R.id.reviewFeedback);
        RecyclerView reviewsRecyclerView = dialogView.findViewById(R.id.reviewsRecyclerView);
        
        // Load poster
        if (!TextUtils.isEmpty(movie.getPosterUrl())) {
            Glide.with(this)
                .load(movie.getPosterUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(posterImage);
        }
        
        titleText.setText(movie.getTitle() != null ? movie.getTitle() : "Untitled");
        genreText.setText("Genre: " + (movie.getGenre() != null ? movie.getGenre() : "-"));
        moodText.setText("Mood: " + (movie.getMood() != null ? movie.getMood() : "-"));
        ratingText.setText("⭐ " + formatRatingForDisplay(movie.getRating()));
        reviewText.setText(movie.getReview() != null ? movie.getReview() : "No official review available.");
        
        // Setup star rating
        final int[] selectedRating = {0};
        setupStarRating(starsLayout, selectedRating);
        
        AlertDialog previewDialog = new AlertDialog.Builder(this)
            .setView(dialogView)
            .setNegativeButton("Close", null)
            .create();
        
        // Submit rating
        submitRatingBtn.setOnClickListener(v -> {
            if (selectedRating[0] > 0) {
                submitUserRating(movie, selectedRating[0]);
                ratingFeedback.setText("✓ Rating added!");
                ratingFeedback.setTextColor(getResources().getColor(android.R.color.holo_green_light));
                
                // Update rating display
                Movie updatedMovie = repository.getAllMovies().stream()
                    .filter(m -> m.getId() == movie.getId())
                    .findFirst()
                    .orElse(movie);
                ratingText.setText("⭐ " + formatRatingForDisplay(updatedMovie.getRating()));
            } else {
                ratingFeedback.setText("Please select a rating!");
                ratingFeedback.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            }
        });
        
        // Setup user reviews
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadUserReviews(movie.getId(), reviewsRecyclerView);
        
        // Submit review
        submitReviewBtn.setOnClickListener(v -> {
            String name = nameField.getText().toString().trim();
            String reviewTextStr = reviewField.getText().toString().trim();
            
            if (name.isEmpty() || reviewTextStr.isEmpty()) {
                reviewFeedback.setText("❌ Please fill in your name and review");
                reviewFeedback.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            } else {
                repository.addUserReview(movie.getId(), name, reviewTextStr);
                reviewFeedback.setText("✓ Review submitted successfully!");
                reviewFeedback.setTextColor(getResources().getColor(android.R.color.holo_green_light));
                
                nameField.setText("");
                reviewField.setText("");
                
                loadUserReviews(movie.getId(), reviewsRecyclerView);
            }
        });
        
        previewDialog.show();
    }

    private void setupStarRating(LinearLayout starsLayout, int[] selectedRating) {
        TextView[] stars = new TextView[5];
        for (int i = 0; i < 5; i++) {
            stars[i] = (TextView) starsLayout.getChildAt(i);
            final int starIndex = i + 1;
            
            stars[i].setOnClickListener(v -> {
                selectedRating[0] = starIndex;
                for (int j = 0; j < 5; j++) {
                    stars[j].setText(j < selectedRating[0] ? "★" : "☆");
                }
            });
        }
    }

    private void loadUserReviews(int movieId, RecyclerView recyclerView) {
        List<UserReview> reviews = repository.getUserReviewsByMovie(movieId);
        UserReviewAdapter reviewAdapter = new UserReviewAdapter(reviews);
        recyclerView.setAdapter(reviewAdapter);
    }

    private void submitUserRating(Movie movie, int userRating) {
        String currentRating = movie.getRating();
        double newAverage;
        int voteCount;

        if (currentRating == null || currentRating.isEmpty() || 
            currentRating.equals("Not rated") || currentRating.equals("Not rated yet")) {
            newAverage = userRating;
            voteCount = 1;
        } else {
            try {
                if (currentRating.contains(":")) {
                    String[] parts = currentRating.split(":");
                    double oldAverage = Double.parseDouble(parts[0]);
                    voteCount = Integer.parseInt(parts[1]);
                    newAverage = ((oldAverage * voteCount) + userRating) / (voteCount + 1);
                    voteCount++;
                } else {
                    String numStr = currentRating.replaceAll("[^0-9.]", "");
                    double oldRating = Double.parseDouble(numStr);
                    newAverage = (oldRating + userRating) / 2.0;
                    voteCount = 2;
                }
            } catch (Exception e) {
                newAverage = userRating;
                voteCount = 1;
            }
        }

        String newRatingStr = String.format("%.1f:%d", newAverage, voteCount);
        movie.setRating(newRatingStr);
        repository.updateRating(movie.getId(), newRatingStr);
        
        // Reload movies to reflect updated rating
        loadAllMovies();
    }

    private String formatRatingForDisplay(String rating) {
        if (rating == null || rating.isEmpty() || rating.equals("Not rated")) {
            return "Not rated yet";
        }

        try {
            if (rating.contains(":")) {
                String[] parts = rating.split(":");
                double avg = Double.parseDouble(parts[0]);
                int count = Integer.parseInt(parts[1]);
                return String.format("%.1f/5 (%d %s)", avg, count, count == 1 ? "vote" : "votes");
            } else {
                return rating;
            }
        } catch (Exception e) {
            return rating;
        }
    }
}
