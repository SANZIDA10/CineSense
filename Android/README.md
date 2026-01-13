# CineSense Android App

## Overview
Complete Android replica of the CineSense Desktop Application - a mood-based movie recommendation system.

## Features Implemented

### 1. **Home Screen** (HomeActivity)
- Role selection: User or Moderator
- Get Started button navigates to appropriate screen
- Matches desktop Home.fxml design

### 2. **Login Screen** (LoginActivity)
- Moderator authentication
- Credentials: sanzidajerin28@gmail.com / 1122
- Error handling and validation
- Matches desktop Login.fxml design

### 3. **Moderator Panel** (ModeratorActivity)
- Add new movies with:
  - Title, Genre, Mood, Description
  - Poster URL, Rating, Review, IMDB Link
- View all movies in RecyclerView
- Delete movies with confirmation dialog
- Form validation
- Matches desktop Moderator.fxml design

### 4. **User Screen** (UserActivity)
- Search movies by title, genre, mood, or description
- Browse movies by mood (horizontal scroll)
- Grid view of all movies
- Click on movie to view details
- Movie details dialog with:
  - Poster image
  - Genre, Mood, Rating
  - Description
  - IMDB link (opens in browser)
  - "Show Preview" button
- Movie preview dialog with:
  - Full movie information
  - Star rating system (5 stars)
  - Submit user ratings (updates average)
  - Official review display
  - User reviews section
  - Write and submit reviews
- Matches desktop UserPage.fxml design

## Database Structure

### Movies Table
- id (INTEGER PRIMARY KEY)
- title (TEXT NOT NULL)
- genre (TEXT)
- mood (TEXT)
- description (TEXT)
- poster_url (TEXT)
- rating (TEXT) - Format: "average:count" e.g., "4.5:10"
- review (TEXT)
- imdb_link (TEXT)

### User Reviews Table
- id (INTEGER PRIMARY KEY)
- movie_id (INTEGER FOREIGN KEY)
- reviewer_name (TEXT NOT NULL)
- review_text (TEXT NOT NULL)
- review_date (TEXT NOT NULL)

## Architecture

### Models
- `Movie.java` - Movie data model
- `UserReview.java` - User review data model

### Database
- `DatabaseHelper.java` - SQLite database management
- `MovieRepository.java` - Data access layer with CRUD operations

### Activities
- `HomeActivity.java` - Landing page with role selection
- `LoginActivity.java` - Moderator authentication
- `ModeratorActivity.java` - Movie management panel
- `UserActivity.java` - Main user interface

### Adapters
- `MoodAdapter.java` - Horizontal mood chips
- `MovieAdapter.java` - Movie grid/list display
- `ModeratorMovieAdapter.java` - Moderator movie list with delete
- `UserReviewAdapter.java` - User reviews display

### Layouts
- `activity_home.xml`
- `activity_login.xml`
- `activity_moderator.xml`
- `activity_user.xml`
- `dialog_movie_details.xml`
- `dialog_movie_preview.xml`
- `item_mood.xml`
- `item_movie.xml`
- `item_moderator_movie.xml`
- `item_user_review.xml`

## Dependencies
- AndroidX AppCompat
- Material Design Components
- RecyclerView & CardView
- SQLite Database
- Glide (Image loading)

## Color Scheme
Matches desktop app:
- Primary: #FFB347 (Orange)
- Accent: #FF9800 (Deep Orange)
- Background: Gradient (#1A1A2E → #16213E → #0F3460)
- Text: #FFFFFF (White)
- Secondary Text: #AAAAAA (Gray)
- Success: #4CAF50 (Green)
- Error: #FF4444 (Red)

## Key Features Parity with Desktop

✅ **Complete Feature Parity:**
1. Role-based access (User/Moderator)
2. Moderator authentication
3. Full CRUD operations for movies
4. Mood-based movie filtering
5. Movie search functionality
6. User rating system with vote counting
7. User review system
8. IMDB integration
9. Image loading (posters)
10. SQLite database persistence

## How to Run
1. Open Android Studio
2. Open the `Android` folder
3. Sync Gradle dependencies
4. Run on emulator or physical device (API 24+)

## App Flow
```
HomeActivity (Select Role)
    ├─→ User → UserActivity (Browse & Review Movies)
    └─→ Moderator → LoginActivity → ModeratorActivity (Manage Movies)
```

## Notes
- Minimum SDK: 24 (Android 7.0)
- Target SDK: 36
- Language: Java 11
- Database: SQLite
- Image Loading: Glide library for efficient poster loading
- Internet Permission: Required for loading movie posters and IMDB links
