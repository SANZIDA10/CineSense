package com.example.cinesense.models;

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

    public Movie() {
    }

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
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getMood() { return mood; }
    public void setMood(String mood) { this.mood = mood; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }

    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }

    public String getReview() { return review; }
    public void setReview(String review) { this.review = review; }

    public String getImdbLink() { return imdbLink; }
    public void setImdbLink(String imdbLink) { this.imdbLink = imdbLink; }
}
