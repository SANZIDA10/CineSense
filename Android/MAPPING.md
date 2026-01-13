# Desktop to Android Mapping - CineSense

## Screen Mapping

### 1. Home Screen
**Desktop**: `Home.fxml` + `HomeController.java`
**Android**: `activity_home.xml` + `HomeActivity.java`

**Features Mapped**:
- ✅ Hero heading: "Find the Perfect Movie for Your Mood"
- ✅ Subtitle text
- ✅ Role selection buttons (User/Moderator)
- ✅ Selected role label with color feedback
- ✅ Get Started button
- ✅ Navigation logic to Login (moderator) or UserPage (user)

---

### 2. Login Screen
**Desktop**: `Login.fxml` + `LoginController.java`
**Android**: `activity_login.xml` + `LoginActivity.java`

**Features Mapped**:
- ✅ Email input field
- ✅ Password input field
- ✅ Error label for validation messages
- ✅ Login button with validation
- ✅ Back button
- ✅ Hardcoded credentials: sanzidajerin28@gmail.com / 1122
- ✅ Navigation to Moderator page on success

---

### 3. Moderator Panel
**Desktop**: `Moderator.fxml` + `ModeratorController.java`
**Android**: `activity_moderator.xml` + `ModeratorActivity.java`

**Features Mapped**:
- ✅ Add Movie Form:
  - Title field (required)
  - Genre ComboBox/Spinner (8 genres)
  - Mood ComboBox/Spinner (8 moods)
  - Description TextArea
  - Poster URL field
  - Rating field
  - Review TextArea
  - IMDB Link field
- ✅ Status label for success/error feedback
- ✅ Add Movie button
- ✅ Movies TableView → RecyclerView with columns:
  - Title
  - Genre
  - Mood
  - Description
  - IMDB Link (in Android: shown in details)
  - Delete button
- ✅ Delete confirmation
- ✅ Back and Logout buttons

---

### 4. User Page
**Desktop**: `UserPage.fxml` + `UserPageController.java`
**Android**: `activity_user.xml` + `UserActivity.java`

**Features Mapped**:

#### Search Section
- ✅ Search field
- ✅ Search button
- ✅ Search by title, genre, mood, description
- ✅ Display search results count

#### Mood Selection
- ✅ Dynamic mood buttons (loaded from database)
- ✅ Horizontal scroll
- ✅ Click to filter movies by mood
- ✅ "Showing movies for mood: X" label

#### Recommended Movies
- ✅ Horizontal scrolling movie posters
- ✅ Movie cards with poster + title
- ✅ Click to view details

#### Browse All Movies
- ✅ Movies grouped by genre (Desktop)
- ✅ Grid layout (Android - better for mobile)
- ✅ Movie poster cards
- ✅ Click to view details

#### Movie Details Popup
**Desktop**: `showMovieDetailsPopup()` method
**Android**: `dialog_movie_details.xml`

- ✅ Movie poster (250x370dp)
- ✅ Title
- ✅ Genre
- ✅ Mood
- ✅ Average rating with vote count
- ✅ Description
- ✅ IMDB link button (opens browser)
- ✅ "Show Preview" button
- ✅ Close button

#### Movie Preview Popup
**Desktop**: `showPreviewPopup()` method
**Android**: `dialog_movie_preview.xml`

- ✅ Large poster image
- ✅ Movie title
- ✅ Rating display
- ✅ Genre and Mood
- ✅ **Star Rating System**:
  - 5 clickable stars (☆/★)
  - Hover effect (Android: click effect)
  - Submit rating button
  - Rating feedback message
  - Updates average rating calculation
- ✅ **Official Review Section**:
  - Review title
  - ScrollPane/ScrollView for long reviews
- ✅ **User Reviews Section**:
  - Title: "💬 User Reviews"
  - Write review form:
    - Name field
    - Review text area
    - Submit button
    - Feedback message
  - Display existing reviews:
    - Reviewer name (orange color)
    - Review date
    - Review text
    - Scrollable list
  - Empty state: "No user reviews yet"
- ✅ Close button

#### Rating Calculation
**Desktop**: `submitUserRating()` method
**Android**: `submitUserRating()` method in UserActivity

- ✅ Parse existing rating format: "average:count"
- ✅ Calculate new average: ((oldAvg * count) + newRating) / (count + 1)
- ✅ Store as "average:count" e.g., "4.5:10"
- ✅ Display as "4.5/5 (10 votes)"
- ✅ Handle first rating case
- ✅ Update movie in database
- ✅ Refresh display

#### Review System
**Desktop**: `MovieRepository.addUserReview()` + `getUserReviewsByMovie()`
**Android**: Same methods in MovieRepository.java

- ✅ Store: movie_id, reviewer_name, review_text, review_date
- ✅ Auto-generate date: "yyyy-MM-dd HH:mm"
- ✅ Foreign key relationship
- ✅ Order by newest first (DESC)

---

## Database Schema Comparison

### Desktop (SQLite via JDBC)
```sql
CREATE TABLE movies (
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

CREATE TABLE user_reviews (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    movie_id INTEGER NOT NULL,
    reviewer_name TEXT NOT NULL,
    review_text TEXT NOT NULL,
    review_date TEXT NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movies(id)
);
```

### Android (SQLite via SQLiteOpenHelper)
**Identical schema** - Same table structure, same columns, same data types

---

## UI/UX Elements Comparison

### Desktop (JavaFX)
- FXML layouts
- CSS styling (Style.CSS)
- Scene transitions
- TableView
- ComboBox
- TextArea
- ScrollPane
- VBox, HBox, StackPane layouts
- Pop-up Stages (Modality.APPLICATION_MODAL)

### Android (XML + Material Design)
- XML layouts
- Theme styling (themes.xml)
- Intent navigation
- RecyclerView
- Spinner
- EditText (multiline)
- ScrollView
- LinearLayout, GridLayout
- AlertDialog

### Color Scheme (Identical)
- Primary: #FFB347 (Orange)
- Accent: #FF9800
- Background: Gradient (#1A1A2E → #16213E → #0F3460)
- Text: #FFFFFF
- Success: #4CAF50
- Error: #FF4444
- Gold: #FFD700 (stars)

---

## Functionality Checklist

### User Features
- [x] Browse all movies
- [x] Filter by mood
- [x] Search movies
- [x] View movie details
- [x] Rate movies (1-5 stars)
- [x] View average ratings with vote count
- [x] Write reviews
- [x] View other user reviews
- [x] Open IMDB links

### Moderator Features
- [x] Login authentication
- [x] Add new movies
- [x] View all movies
- [x] Delete movies
- [x] Form validation
- [x] Success/error feedback

### Database Features
- [x] SQLite persistence
- [x] CRUD operations
- [x] Foreign key relationships
- [x] Auto-increment IDs
- [x] Date/time tracking

### Navigation
- [x] Home → User/Moderator selection
- [x] Moderator → Login required
- [x] User → Direct access
- [x] Back button navigation
- [x] Logout functionality

---

## Implementation Notes

1. **Image Loading**: Desktop uses `Image` class, Android uses Glide library
2. **Layouts**: Desktop uses FXML declarative layouts, Android uses XML layouts
3. **Lists**: Desktop uses `ObservableList`, Android uses `List<T>` with adapter pattern
4. **Navigation**: Desktop uses `FXMLLoader` + Scene, Android uses `Intent`
5. **Database**: Desktop uses JDBC Connection, Android uses SQLiteOpenHelper
6. **Dialogs**: Desktop uses Stage with Modality, Android uses AlertDialog
7. **Threading**: Both handle UI updates on main thread

---

## Complete Feature Parity ✓

The Android app is a **100% complete replica** of the desktop application with:
- ✅ All screens implemented
- ✅ All features functional
- ✅ Identical database schema
- ✅ Same business logic
- ✅ Matching UI/UX design
- ✅ Equal functionality
- ✅ Complete data persistence
