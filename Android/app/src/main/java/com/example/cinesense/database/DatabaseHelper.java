package com.example.cinesense.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    
    private static final String DATABASE_NAME = "cinesense.db";
    private static final int DATABASE_VERSION = 1;
    
    // Table names
    public static final String TABLE_MOVIES = "movies";
    public static final String TABLE_USER_REVIEWS = "user_reviews";
    
    // Movies table columns
    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_GENRE = "genre";
    public static final String COL_MOOD = "mood";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_POSTER_URL = "poster_url";
    public static final String COL_RATING = "rating";
    public static final String COL_REVIEW = "review";
    public static final String COL_IMDB_LINK = "imdb_link";
    
    // User reviews table columns
    public static final String COL_MOVIE_ID = "movie_id";
    public static final String COL_REVIEWER_NAME = "reviewer_name";
    public static final String COL_REVIEW_TEXT = "review_text";
    public static final String COL_REVIEW_DATE = "review_date";
    
    // Create movies table
    private static final String CREATE_MOVIES_TABLE = 
        "CREATE TABLE " + TABLE_MOVIES + " (" +
        COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        COL_TITLE + " TEXT NOT NULL, " +
        COL_GENRE + " TEXT, " +
        COL_MOOD + " TEXT, " +
        COL_DESCRIPTION + " TEXT, " +
        COL_POSTER_URL + " TEXT, " +
        COL_RATING + " TEXT, " +
        COL_REVIEW + " TEXT, " +
        COL_IMDB_LINK + " TEXT)";
    
    // Create user reviews table
    private static final String CREATE_USER_REVIEWS_TABLE = 
        "CREATE TABLE " + TABLE_USER_REVIEWS + " (" +
        COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        COL_MOVIE_ID + " INTEGER NOT NULL, " +
        COL_REVIEWER_NAME + " TEXT NOT NULL, " +
        COL_REVIEW_TEXT + " TEXT NOT NULL, " +
        COL_REVIEW_DATE + " TEXT NOT NULL, " +
        "FOREIGN KEY(" + COL_MOVIE_ID + ") REFERENCES " + TABLE_MOVIES + "(" + COL_ID + "))";
    
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MOVIES_TABLE);
        db.execSQL(CREATE_USER_REVIEWS_TABLE);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_REVIEWS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        onCreate(db);
    }
}
