package com.example.myapp;

import javafx.fxml.FXML;

public class MainPageController {

    @FXML
    private void onNavHome() {
        System.out.println("Home clicked");
    }

    @FXML
    private void onNavMoodPicks() {
        System.out.println("Mood Picks clicked");
    }

    @FXML
    private void onNavGenres() {
        System.out.println("Genres clicked");
    }

    @FXML
    private void onNavMyList() {
        System.out.println("My List clicked");
    }

    @FXML
    private void onNavAbout() {
        System.out.println("About clicked");
    }

    @FXML
    private void onGenreAction() {
        System.out.println("Genre clicked");
    }
}
