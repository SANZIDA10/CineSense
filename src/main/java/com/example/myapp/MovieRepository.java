package com.example.myapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class MovieRepository {

    public static void addMovie(Movie m) {
        String sql = """
            INSERT INTO movies(title,genre,mood,description,poster_url,rating,review)
            VALUES(?,?,?,?,?,?,?)
        """;

        try (Connection c = Database.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, m.getTitle());
            ps.setString(2, m.getGenre());
            ps.setString(3, m.getMood());
            ps.setString(4, m.getDescription());
            ps.setString(5, m.getPosterUrl());
            ps.setString(6, m.getRating());
            ps.setString(7, m.getReview());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<Movie> getAllMovies() {
        ObservableList<Movie> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM movies";

        try (Connection c = Database.connect();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ObservableList<String> getAvailableMoods() {
        ObservableList<String> list = FXCollections.observableArrayList();
        try (Connection c = Database.connect();
             ResultSet rs = c.createStatement()
                     .executeQuery("SELECT DISTINCT mood FROM movies")) {
            while (rs.next()) list.add(rs.getString(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ObservableList<Movie> getMoviesByMood(String mood) {
        ObservableList<Movie> list = FXCollections.observableArrayList();
        try (Connection c = Database.connect();
             PreparedStatement ps =
                     c.prepareStatement("SELECT * FROM movies WHERE mood=?")) {

            ps.setString(1, mood);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void updateRating(int id, String rating) {
        update("UPDATE movies SET rating=? WHERE id=?", rating, id);
    }

    public static void updateReview(int id, String review) {
        update("UPDATE movies SET review=? WHERE id=?", review, id);
    }

    public static void deleteMovie(int id) {
        String sql = "DELETE FROM movies WHERE id=?";
        try (Connection c = Database.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void update(String sql, String value, int id) {
        try (Connection c = Database.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, value);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Movie map(ResultSet rs) throws SQLException {
        return new Movie(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("genre"),
                rs.getString("mood"),
                rs.getString("description"),
                rs.getString("poster_url"),
                rs.getString("rating"),
                rs.getString("review")
        );
    }

    // User Reviews Methods
    public static void addUserReview(int movieId, String reviewerName, String reviewText) {
        String sql = "INSERT INTO user_reviews(movie_id, reviewer_name, review_text, review_date) VALUES(?,?,?,?)";

        try (Connection c = Database.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, movieId);
            ps.setString(2, reviewerName);
            ps.setString(3, reviewText);
            ps.setString(4, java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static javafx.collections.ObservableList<UserReview> getUserReviewsByMovie(int movieId) {
        javafx.collections.ObservableList<UserReview> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM user_reviews WHERE movie_id=? ORDER BY id DESC";

        try (Connection c = Database.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, movieId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new UserReview(
                        rs.getInt("id"),
                        rs.getInt("movie_id"),
                        rs.getString("reviewer_name"),
                        rs.getString("review_text"),
                        rs.getString("review_date")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
