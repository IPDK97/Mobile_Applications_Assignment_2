package dk.au.mad21fall.assignment2.au690736.model;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.google.common.util.concurrent.ListenableFuture;

import dk.au.mad21fall.assignment2.au690736.database.MovieDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Code inspired by Lab Code --> Room Async
public class Repository {

    private final MovieDatabase db;
    private final ExecutorService executor;
    private final LiveData<List<Movie>> movies;
    private static Repository instance;

    //Singleton pattern to make sure there is only one instance of the Repository in use
    public static Repository getInstance(Application app){
        if(instance==null){
            instance = new Repository(app);
        }
        return instance;
    }

    //constructor - takes Application object for context
    private Repository(Application app){
        db = MovieDatabase.getDatabase(app.getApplicationContext());
        executor = Executors.newSingleThreadExecutor();
        movies = db.movieDAO().getAll();
    }

    //Get LiveData list of movies
    public LiveData<List<Movie>> getMovies(){
        return movies;
    }

    public ListenableFuture<Movie> getMovieAsync(int uid){
        return db.movieDAO().findMovie(uid);
    }

    public ListenableFuture<Movie> getMovieByTitleAsync(String name){
        return db.movieDAO().findMovie(name);
    }

    public void updateMovieAsync(Movie movie){

        executor.execute(() -> db.movieDAO().updateMovie(movie));
    }

    public void addMovieAsync(Movie movie){

        executor.execute(() -> db.movieDAO().addMovie(movie));
    }

    public void deleteMovieAsync(Movie movie) {
        executor.execute(() -> db.movieDAO().delete(movie));
    }

    public ListenableFuture<Integer> getCount() {
        return db.movieDAO().getCount();
    }
}
