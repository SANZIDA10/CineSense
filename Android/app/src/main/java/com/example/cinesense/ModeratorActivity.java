package com.example.cinesense;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinesense.adapters.ModeratorMovieAdapter;
import com.example.cinesense.database.MovieRepository;
import com.example.cinesense.models.Movie;

import java.util.List;

public class ModeratorActivity extends AppCompatActivity {

    private EditText titleField, descriptionField, posterUrlField, ratingField, reviewField, imdbLinkField;
    private Spinner genreSpinner, moodSpinner;
    private RecyclerView moviesRecyclerView;
    private TextView statusLabel;
    private Button addMovieBtn, backBtn, logoutBtn;
    
    private MovieRepository repository;
    private ModeratorMovieAdapter adapter;

    private static final String[] GENRES = {"Action", "Comedy", "Drama", "Horror", "Sci-Fi", "Thriller", "Romance", "Animation"};
    private static final String[] MOODS = {"Happy", "Sad", "Thrilled", "Scared", "Relaxed", "Inspired", "Angry", "Excited"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moderator);

        repository = new MovieRepository(this);
        
        initializeViews();
        setupSpinners();
        setupRecyclerView();
        loadMovies();
    }

    private void initializeViews() {
        titleField = findViewById(R.id.titleField);
        descriptionField = findViewById(R.id.descriptionField);
        posterUrlField = findViewById(R.id.posterUrlField);
        ratingField = findViewById(R.id.ratingField);
        reviewField = findViewById(R.id.reviewField);
        imdbLinkField = findViewById(R.id.imdbLinkField);
        genreSpinner = findViewById(R.id.genreSpinner);
        moodSpinner = findViewById(R.id.moodSpinner);
        moviesRecyclerView = findViewById(R.id.moviesRecyclerView);
        statusLabel = findViewById(R.id.statusLabel);
        addMovieBtn = findViewById(R.id.addMovieBtn);
        backBtn = findViewById(R.id.backBtn);
        logoutBtn = findViewById(R.id.logoutBtn);

        addMovieBtn.setOnClickListener(v -> onAddMovie());
        backBtn.setOnClickListener(v -> finish());
        logoutBtn.setOnClickListener(v -> {
            finish();
        });
    }

    private void setupSpinners() {
        ArrayAdapter<String> genreAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, GENRES);
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreSpinner.setAdapter(genreAdapter);

        ArrayAdapter<String> moodAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, MOODS);
        moodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moodSpinner.setAdapter(moodAdapter);
    }

    private void setupRecyclerView() {
        moviesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ModeratorMovieAdapter(this, movie -> {
            // Show delete confirmation dialog
            new AlertDialog.Builder(this)
                .setTitle("Delete Movie")
                .setMessage("Are you sure you want to delete \"" + movie.getTitle() + "\"?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    repository.deleteMovie(movie.getId());
                    loadMovies();
                    Toast.makeText(this, "Movie deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
        });
        moviesRecyclerView.setAdapter(adapter);
    }

    private void onAddMovie() {
        String title = titleField.getText().toString().trim();
        String genre = genreSpinner.getSelectedItem().toString();
        String mood = moodSpinner.getSelectedItem().toString();
        String description = descriptionField.getText().toString().trim();
        String posterUrl = posterUrlField.getText().toString().trim();
        String rating = ratingField.getText().toString().trim();
        String review = reviewField.getText().toString().trim();
        String imdbLink = imdbLinkField.getText().toString().trim();

        if (title.isEmpty()) {
            statusLabel.setText("❌ Title is required!");
            statusLabel.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            statusLabel.setVisibility(View.VISIBLE);
            return;
        }

        Movie movie = new Movie(0, title, genre, mood, description, posterUrl, rating, review, imdbLink);
        repository.addMovie(movie);

        // Clear fields
        titleField.setText("");
        descriptionField.setText("");
        posterUrlField.setText("");
        ratingField.setText("");
        reviewField.setText("");
        imdbLinkField.setText("");
        genreSpinner.setSelection(0);
        moodSpinner.setSelection(0);

        statusLabel.setText("✅ Movie added successfully!");
        statusLabel.setTextColor(getResources().getColor(android.R.color.holo_green_light));
        statusLabel.setVisibility(View.VISIBLE);

        loadMovies();
    }

    private void loadMovies() {
        List<Movie> movies = repository.getAllMovies();
        adapter.setMovies(movies);
    }
}
