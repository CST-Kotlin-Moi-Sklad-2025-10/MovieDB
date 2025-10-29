# Movie Database - Compose Multiplatform Sample

This is a Compose Multiplatform sample application demonstrating Room database with complex relations.

## Database Schema

### Entities

1. **Director** (`directors` table)
  - `id` (Primary Key, auto-generated)
  - `name` (String)
  - `birthDate` (LocalDate - stored as ISO-8601 String)

2. **Movie** (`movies` table)
  - `id` (Primary Key, auto-generated)
  - `title` (String)
  - `releaseYear` (Int)
  - `directorId` (Foreign Key → Director)

3. **Actor** (`actors` table)
  - `id` (Primary Key, auto-generated)
  - `name` (String)
  - `birthYear` (Int)

4. **Cast** (`cast` table) - Junction table for Many-to-Many
  - `movieId` (Foreign Key → Movie, part of composite primary key)
  - `actorId` (Foreign Key → Actor, part of composite primary key)

5. **Review** (`reviews` table)
  - `id` (Primary Key, auto-generated)
  - `movieId` (Foreign Key → Movie)
  - `rating` (Int, 1-5 stars)
  - `comment` (String)

## Database Relations

### Director ↔ Movie: One-to-Many
- One director can direct multiple movies
- Each movie has exactly one director
- Implemented via foreign key `directorId` in Movie table

### Movie ↔ Actor: Many-to-Many
- One movie can have multiple actors
- One actor can appear in multiple movies
- Implemented via junction table `Cast` with composite primary key

### Movie ↔ Review: One-to-Many
- One movie can have multiple reviews
- Each review belongs to exactly one movie
- Implemented via foreign key `movieId` in Review table

## Architecture

### Data Layer
- **Entity classes** (`data/entity/`): Room entities representing database tables
- **DAO interfaces** (`data/dao/`): Data Access Objects for database operations
- **Relation classes** (`data/relations/`): Data classes for nested query results
- **Database** (`MovieDatabase.kt`): Main Room database configuration
- **Repository** (`MovieRepository.kt`): Business logic and data access

### Platform-Specific
- **DatabaseBuilder**: Expect/actual pattern for platform-specific database initialization
  - Android: Uses application context
  - iOS: Uses NSHomeDirectory

### UI Layer
- **MovieViewModel**: ViewModel managing app state
- **App.kt**: Main UI with expandable movie cards

## Sample Data

The database is pre-populated with:
- 3 directors (Christopher Nolan, Steven Spielberg, Quentin Tarantino)
- 8 movies
- 11 actors
- Movie-Actor relationships
- 15 reviews

## Features

- **Loading State**: Shows progress indicator while loading data
- **Movie List**: Displays all movies with title, year, and director
- **Expandable Cards**: Tap any movie to see full details
- **Cast Information**: View all actors in the movie
- **Reviews**: See user reviews with star ratings

## Building

The project uses:
- Kotlin 2.2.20
- Room 2.7.0-alpha12
- Compose Multiplatform 1.9.0
- KSP for Room code generation

Run on Android:
```bash
./gradlew :composeApp:installDebug
```

Build for iOS (requires macOS with Xcode):
```bash
./gradlew :composeApp:iosSimulatorArm64Build
```

## File Structure

```
composeApp/src/
├── commonMain/kotlin/io/github/ajiekcx/moviedb/
│   ├── data/
│   │   ├── entity/          # Room entities
│   │   ├── dao/             # Data access objects
│   │   ├── relations/       # Relation data classes
│   │   ├── repository/      # Repository layer
│   │   ├── MovieDatabase.kt
│   │   └── DatabaseBuilder.kt (expect)
│   ├── ui/
│   │   └── MovieViewModel.kt
│   └── App.kt
├── androidMain/kotlin/io/github/ajiekcx/moviedb/
│   ├── data/
│   │   └── DatabaseBuilder.android.kt (actual)
│   └── MainActivity.kt
└── iosMain/kotlin/io/github/ajiekcx/moviedb/
    └── data/
        └── DatabaseBuilder.ios.kt (actual)
```

