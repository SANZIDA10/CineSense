package com.example.cinesense.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.cinesense.models.Movie;
import com.example.cinesense.models.UserReview;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MovieRepository {
    
    private DatabaseHelper dbHelper;
    
    public MovieRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }
    
    // Add a movie
    public long addMovie(Movie movie) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(DatabaseHelper.COL_TITLE, movie.getTitle());
        values.put(DatabaseHelper.COL_GENRE, movie.getGenre());
        values.put(DatabaseHelper.COL_MOOD, movie.getMood());
        values.put(DatabaseHelper.COL_DESCRIPTION, movie.getDescription());
        values.put(DatabaseHelper.COL_POSTER_URL, movie.getPosterUrl());
        values.put(DatabaseHelper.COL_RATING, movie.getRating());
        values.put(DatabaseHelper.COL_REVIEW, movie.getReview());
        values.put(DatabaseHelper.COL_IMDB_LINK, movie.getImdbLink());
        
        long id = db.insert(DatabaseHelper.TABLE_MOVIES, null, values);
        db.close();
        return id;
    }
    
    // Get all movies
    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        Cursor cursor = db.query(DatabaseHelper.TABLE_MOVIES, null, null, null, null, null, null);
        
        if (cursor.moveToFirst()) {
            do {
                movies.add(cursorToMovie(cursor));
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return movies;
    }
    
    // Get available moods
    public List<String> getAvailableMoods() {
        List<String> moods = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        Cursor cursor = db.query(true, DatabaseHelper.TABLE_MOVIES, 
                new String[]{DatabaseHelper.COL_MOOD}, 
                null, null, null, null, null, null);
        
        if (cursor.moveToFirst()) {
            do {
                String mood = cursor.getString(0);
                if (mood != null && !mood.isEmpty()) {
                    moods.add(mood);
                }
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return moods;
    }
    
    // Get movies by mood
    public List<Movie> getMoviesByMood(String mood) {
        List<Movie> movies = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        Cursor cursor = db.query(DatabaseHelper.TABLE_MOVIES, null, 
                DatabaseHelper.COL_MOOD + "=?", new String[]{mood}, 
                null, null, null);
        
        if (cursor.moveToFirst()) {
            do {
                movies.add(cursorToMovie(cursor));
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return movies;
    }
    
    // Search movies
    public List<Movie> searchMovies(String searchTerm) {
        List<Movie> movies = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        String selection = DatabaseHelper.COL_TITLE + " LIKE ? OR " +
                          DatabaseHelper.COL_GENRE + " LIKE ? OR " +
                          DatabaseHelper.COL_MOOD + " LIKE ? OR " +
                          DatabaseHelper.COL_DESCRIPTION + " LIKE ?";
        
        String searchPattern = "%" + searchTerm + "%";
        String[] selectionArgs = {searchPattern, searchPattern, searchPattern, searchPattern};
        
        Cursor cursor = db.query(DatabaseHelper.TABLE_MOVIES, null, selection, selectionArgs, 
                null, null, null);
        
        if (cursor.moveToFirst()) {
            do {
                movies.add(cursorToMovie(cursor));
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return movies;
    }
    
    // Update movie rating
    public void updateRating(int id, String rating) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_RATING, rating);
        
        db.update(DatabaseHelper.TABLE_MOVIES, values, 
                DatabaseHelper.COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }
    
    // Delete movie
    public void deleteMovie(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        // Delete associated reviews first
        db.delete(DatabaseHelper.TABLE_USER_REVIEWS, 
                DatabaseHelper.COL_MOVIE_ID + "=?", new String[]{String.valueOf(id)});
        
        // Delete the movie
        db.delete(DatabaseHelper.TABLE_MOVIES, 
                DatabaseHelper.COL_ID + "=?", new String[]{String.valueOf(id)});
        
        db.close();
    }
    
    // Add user review
    public long addUserReview(int movieId, String reviewerName, String reviewText) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        
        values.put(DatabaseHelper.COL_MOVIE_ID, movieId);
        values.put(DatabaseHelper.COL_REVIEWER_NAME, reviewerName);
        values.put(DatabaseHelper.COL_REVIEW_TEXT, reviewText);
        values.put(DatabaseHelper.COL_REVIEW_DATE, currentDate);
        
        long id = db.insert(DatabaseHelper.TABLE_USER_REVIEWS, null, values);
        db.close();
        return id;
    }
    
    // Get user reviews by movie
    public List<UserReview> getUserReviewsByMovie(int movieId) {
        List<UserReview> reviews = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        Cursor cursor = db.query(DatabaseHelper.TABLE_USER_REVIEWS, null, 
                DatabaseHelper.COL_MOVIE_ID + "=?", new String[]{String.valueOf(movieId)}, 
                null, null, DatabaseHelper.COL_ID + " DESC");
        
        if (cursor.moveToFirst()) {
            do {
                reviews.add(cursorToUserReview(cursor));
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return reviews;
    }
    
    // Helper method to convert cursor to Movie
    private Movie cursorToMovie(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TITLE));
        String genre = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_GENRE));
        String mood = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MOOD));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DESCRIPTION));
        String posterUrl = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_POSTER_URL));
        String rating = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_RATING));
        String review = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_REVIEW));
        String imdbLink = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_IMDB_LINK));
        
        return new Movie(id, title, genre, mood, description, posterUrl, rating, review, imdbLink);
    }
    
    // Helper method to convert cursor to UserReview
    private UserReview cursorToUserReview(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID));
        int movieId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_MOVIE_ID));
        String reviewerName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_REVIEWER_NAME));
        String reviewText = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_REVIEW_TEXT));
        String reviewDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_REVIEW_DATE));
        
        return new UserReview(id, movieId, reviewerName, reviewText, reviewDate);
    }
}
