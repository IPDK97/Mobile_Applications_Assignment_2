package dk.au.mad21fall.assignment2.au690736.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

import dk.au.mad21fall.assignment2.au690736.model.Movie;
import dk.au.mad21fall.assignment2.au690736.model.Repository;

// Use AndroidViewModel to get the Application object
public class ListViewModel extends AndroidViewModel {
    Repository repository;

    public ListViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance(application);
    }

    public LiveData<List<Movie>> getMovieList() {
        return repository.getMovies();
    }

    public void addMovie(Movie movie) {
        repository.addMovieAsync(movie);
    }

    public Movie getMovie(String name) {
        try {
            return repository.getMovieByTitleAsync(name).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
