package dk.au.mad21fall.assignment2.au690736;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class MovieListViewModel extends ViewModel {
    MutableLiveData<ArrayList<Movie>> movieList;

    public LiveData<ArrayList<Movie>> getMovieList() {
        return movieList;
    }

    public void setMovieList(ArrayList<Movie> movieListObject) {
        movieList = new MutableLiveData<>();
        movieList.setValue(movieListObject);
    }
}
