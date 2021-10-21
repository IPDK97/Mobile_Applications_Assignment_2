package dk.au.mad21fall.assignment2.au690736.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

import dk.au.mad21fall.assignment2.au690736.model.Movie;
import dk.au.mad21fall.assignment2.au690736.model.Repository;

public class EditViewModel extends AndroidViewModel {
    Repository repository;
    private ListenableFuture<Movie> movie;

    public EditViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance(application);
    }

    public void setMovie(int uid) {
        this.movie = repository.getMovieAsync(uid);
    }

    public Movie getMovie() {
        try {
            return this.movie.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateMovie(Movie movie) {
        repository.updateMovieAsync(movie);
    }
}
