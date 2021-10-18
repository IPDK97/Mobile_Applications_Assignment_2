package dk.au.mad21fall.assignment2.au690736;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements MovieAdapter.IMovieItemClickedListener {

    private ArrayList<Movie> movies;
    private MovieAdapter adapter;
    private MovieListViewModel model;

    Button btnExit;

    // Code inspired by Example Code from the labs
    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            if(result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                assert data != null;
                Movie movie = (Movie) data.getSerializableExtra("result");
                movies = model.getMovieList().getValue();
                if(movie != null && movies != null) {
                    movies.set(movie.getPosition(), movie);
                    adapter.updateMovieList(movies);
                    model.setMovieList(movies);
                }
            }
        }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        adapter = new MovieAdapter(this);
        RecyclerView rcvList = findViewById(R.id.movieList);
        rcvList.setLayoutManager(new LinearLayoutManager(this));
        rcvList.setAdapter(adapter);

        createMovieList();

        if(model == null) {
            model = new ViewModelProvider(this).get(MovieListViewModel.class);
        }

        if(model.getMovieList() == null) {
            model.setMovieList(movies);
        }

        model.getMovieList().observe(this, movieList -> adapter.updateMovieList(movieList));

        btnExit = findViewById(R.id.exit);
        btnExit.setOnClickListener(v -> exitApp());
    }

    //Import Data from CSV file
    private void createMovieList() {
        movies = new ArrayList<>();
        try {
            // CSV read inspired by https://www.youtube.com/watch?v=i-TqNzUryn8
            InputStream inputStream = getResources().openRawResource(R.raw.movie_data);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            int i = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip first line
                if(i == 0) {
                    i = 1;
                    continue;
                }
                String[] records = line.split(",");
                movies.add(new Movie(records[0], records[1], Integer.parseInt(records[2]), Double.parseDouble(records[3]), records[4]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exitApp() {
        finish();
        System.exit(0);
    }

    @Override
    public void onMovieClicked(int index) {
        Movie data = model.getMovieList().getValue().get(index);
        data.setPosition(index);
        movies.set(index, data);
        Intent intent = new Intent(this,DetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("serializable", movies.get(index));
        intent.putExtras(bundle);
        launcher.launch(intent);
    }
}