package com.example.myapp;

public class Movie {

    private int id;
    private String title;
    private String genre;
    private String mood;
    private String description;
    private String posterUrl;
    private String rating;
    private String review;
    private String imdbLink;

    public Movie(int id, String title, String genre, String mood,
                 String description, String posterUrl,
                 String rating, String review, String imdbLink) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.mood = mood;
        this.description = description;
        this.posterUrl = posterUrl;
        this.rating = rating;
        this.review = review;
        this.imdbLink = imdbLink;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public String getMood() { return mood; }
    public String getDescription() { return description; }
    public String getPosterUrl() { return posterUrl; }
    public String getRating() { return rating; }
    public String getReview() { return review; }
    public String getImdbLink() { return imdbLink; }

    public void setRating(String rating) { this.rating = rating; }
    public void setReview(String review) { this.review = review; }
}
