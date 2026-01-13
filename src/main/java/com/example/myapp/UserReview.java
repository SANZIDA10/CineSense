package com.example.myapp;

public class UserReview {
    private int id;
    private int movieId;
    private String reviewerName;
    private String reviewText;
    private String reviewDate;

    public UserReview(int id, int movieId, String reviewerName, String reviewText, String reviewDate) {
        this.id = id;
        this.movieId = movieId;
        this.reviewerName = reviewerName;
        this.reviewText = reviewText;
        this.reviewDate = reviewDate;
    }

    public int getId() { return id; }
    public int getMovieId() { return movieId; }
    public String getReviewerName() { return reviewerName; }
    public String getReviewText() { return reviewText; }
    public String getReviewDate() { return reviewDate; }
}
