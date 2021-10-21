package dk.au.mad21fall.assignment2.au690736;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import dk.au.mad21fall.assignment2.au690736.model.Movie;
import dk.au.mad21fall.assignment2.au690736.model.MovieAdapter;
import dk.au.mad21fall.assignment2.au690736.model.Search;
import dk.au.mad21fall.assignment2.au690736.model.SearchResult;
import dk.au.mad21fall.assignment2.au690736.service.NotificationService;
import dk.au.mad21fall.assignment2.au690736.viewmodel.ListViewModel;

public class ListActivity extends AppCompatActivity implements MovieAdapter.IMovieItemClickedListener {

    private ListViewModel model;

    RequestQueue queue;
    static final String API_KEY = "25bc54a2";
    String searchBase = "https://www.omdbapi.com/?apikey=" + API_KEY + "&s=";
    String base = "https://www.omdbapi.com/?apikey=" + API_KEY + "&t=";

    CharSequence textSuccessful = "Successfully added";
    CharSequence textExists = "Movie already exists";
    int duration = Toast.LENGTH_SHORT;

    Button btnExit;
    Button btnAdd;
    SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        model = new ViewModelProvider(this).get(ListViewModel.class);

        MovieAdapter adapter = new MovieAdapter(this);
        RecyclerView rcvList = findViewById(R.id.movieList);
        rcvList.setLayoutManager(new LinearLayoutManager(this));
        rcvList.setAdapter(adapter);

        model.getMovieList().observe(this, adapter::updateMovieList);

        search = findViewById(R.id.search);

        btnAdd = findViewById(R.id.add);
        btnAdd.setOnClickListener(v -> addMovie());

        btnExit = findViewById(R.id.exit);
        btnExit.setOnClickListener(v -> exitApp());

        startNotificationService();
    }

    private void addMovie() {
        String query = String.valueOf(search.getQuery());
        sendSearchRequest(searchBase + query);
    }

    private void exitApp() {
        finish();
        System.exit(0);
    }

    @Override
    public void onMovieClicked(Movie movie) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("id", movie.getUid());
        startActivity(intent);
    }

    private void startNotificationService() {
        Intent intent = new Intent(this, NotificationService.class);
        startService(intent);
    }

    private void sendRequest(String url){
        if(queue==null) {
            queue = Volley.newRequestQueue(getApplication().getApplicationContext());
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Log.d(TAG, "onResponse: " + response);
                    parseJson(response);
                }, error -> Log.e(TAG, "That did not work!", error));

        queue.add(stringRequest);
    }

    private void sendSearchRequest(String url){
        if(queue==null) {
            queue = Volley.newRequestQueue(getApplication().getApplicationContext());
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Log.d(TAG, "onResponse: " + response);
                    parseSearchResultsJson(response);
                }, error -> Log.e(TAG, "That did not work!", error));

        queue.add(stringRequest);
    }

    private void parseSearchResultsJson(String json) {
        Gson gson = new GsonBuilder().create();
        Search search = gson.fromJson(json, Search.class);

        List<SearchResult> list = search.getSearch();
        sendRequest(base + list.get(0).getTitle());
    }

    private void parseJson(String json) {
        Gson gson = new GsonBuilder().create();
        Movie movie = gson.fromJson(json, Movie.class);

        // Get the first Genre from the Genre Field and remove the others
        if(movie!=null) {
            String genre = movie.getGenre();
            if(movie.getGenre().contains(",")) {
                genre = genre.substring(0,movie.getGenre().indexOf(","));
                movie.setGenre(genre);
            }
        }

        if (movie != null) {
            if(model.getMovie(movie.getName()) == null) {
                model.addMovie(movie);
                // Toast Code inspired by https://developer.android.com/guide/topics/ui/notifiers/toasts
                Toast toast = Toast.makeText(getApplicationContext(), textSuccessful, duration);
                toast.show();
            } else {
                // Toast Code inspired by https://developer.android.com/guide/topics/ui/notifiers/toasts
                Toast toast = Toast.makeText(getApplicationContext(), textExists, duration);
                toast.show();
            }
        }
    }
}