package dk.au.mad21fall.assignment2.au690736.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.google.common.util.concurrent.ListenableFuture;
import dk.au.mad21fall.assignment2.au690736.model.Movie;

import java.util.List;

// Code inspired by Lab Code --> Room Async
@Dao
public interface MovieDAO {

    @Query("SELECT * FROM movie")
    LiveData<List<Movie>> getAll();

    @Query("SELECT * FROM movie WHERE uid LIKE :uid")
    ListenableFuture<Movie> findMovie(int uid);

    @Query("SELECT * FROM movie WHERE name LIKE :name LIMIT 1")
    ListenableFuture<Movie> findMovie(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addMovie(Movie movie);

    @Update
    void updateMovie(Movie movie);

    @Delete
    void delete(Movie movie);

    @Query("SELECT COUNT(name) FROM movie")
    ListenableFuture<Integer> getCount();
}