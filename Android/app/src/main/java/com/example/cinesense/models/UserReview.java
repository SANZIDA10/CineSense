package com.example.cinesense.models;

public class UserReview {
    private int id;
    private int movieId;
    private String reviewerName;
    private String reviewText;
    private String reviewDate;

    public UserReview() {
    }

    public UserReview(int id, int movieId, String reviewerName, String reviewText, String reviewDate) {
        this.id = id;
        this.movieId = movieId;
        this.reviewerName = reviewerName;
        this.reviewText = reviewText;
        this.reviewDate = reviewDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }

    public String getReviewerName() { return reviewerName; }
    public void setReviewerName(String reviewerName) { this.reviewerName = reviewerName; }

    public String getReviewText() { return reviewText; }
    public void setReviewText(String reviewText) { this.reviewText = reviewText; }

    public String getReviewDate() { return reviewDate; }
    public void setReviewDate(String reviewDate) { this.reviewDate = reviewDate; }
}
