package dk.au.mad21fall.assignment2.au690736.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import dk.au.mad21fall.assignment2.au690736.model.Movie;


// Code inspired by Lab Code --> Room Async
@Database(entities = {Movie.class}, version = 4)
public abstract class MovieDatabase extends RoomDatabase {

    public abstract MovieDAO movieDAO();
    private static MovieDatabase instance;

    //singleton pattern used, for lazy loading + single instance since db object is expensive
    public static MovieDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (MovieDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            MovieDatabase.class, "movie_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }
}
