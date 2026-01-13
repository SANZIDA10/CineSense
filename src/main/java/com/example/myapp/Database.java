package com.example.myapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Database {

    private static final String DB_URL = "jdbc:sqlite:cinesense.db";

    public static Connection connect() throws Exception {
        return DriverManager.getConnection(DB_URL);
    }

    public static void init() {
        String createMoviesTable = """
            CREATE TABLE IF NOT EXISTS movies (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                genre TEXT,
                mood TEXT,
                description TEXT,
                poster_url TEXT,
                rating TEXT,
                review TEXT,
                imdb_link TEXT
            );
        """;

        String createUserReviewsTable = """
            CREATE TABLE IF NOT EXISTS user_reviews (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                movie_id INTEGER NOT NULL,
                reviewer_name TEXT NOT NULL,
                review_text TEXT NOT NULL,
                review_date TEXT NOT NULL,
                FOREIGN KEY (movie_id) REFERENCES movies(id)
            );
        """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute(createMoviesTable);
            stmt.execute(createUserReviewsTable);
            
            // Migration: Add imdb_link column if it doesn't exist
            try {
                stmt.execute("ALTER TABLE movies ADD COLUMN imdb_link TEXT");
                System.out.println("✅ Successfully added imdb_link column to movies table");
            } catch (Exception e) {
                // Column already exists or other error
                if (e.getMessage() != null && e.getMessage().contains("duplicate column")) {
                    System.out.println("ℹ️ imdb_link column already exists");
                } else {
                    System.err.println("⚠️ Migration note: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
