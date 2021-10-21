package dk.au.mad21fall.assignment2.au690736;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import dk.au.mad21fall.assignment2.au690736.model.Movie;
import dk.au.mad21fall.assignment2.au690736.viewmodel.DetailsViewModel;

public class DetailsActivity extends AppCompatActivity {

    private Movie movie;
    private DetailsViewModel model;
    CharSequence text = "Successfully deleted";
    int duration = Toast.LENGTH_SHORT;

    ImageView imgGenre;
    TextView txtName;
    TextView txtYear;
    TextView txtGenre;
    TextView txtPlot;
    TextView txtImdbRating;
    TextView txtUserRating;
    TextView txtUserNotes;
    Button btnRate;
    Button btnBack;
    Button btnDelete;

    // Code inspired by Example Code from the labs
    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> finish()
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        model = new ViewModelProvider(this).get(DetailsViewModel.class);
        model.setMovie(getIntent().getIntExtra("id", 0));

        movie = model.getMovie();
        initializeViews();

        btnRate = findViewById(R.id.rate);
        btnRate.setOnClickListener(v -> openEditActivity());

        btnBack = findViewById(R.id.back);
        btnBack.setOnClickListener(v -> finish());

        btnDelete = findViewById(R.id.delete);
        btnDelete.setOnClickListener(v -> deleteMovie());
    }

    private void initializeViews() {
        imgGenre = findViewById(R.id.genreImage);
        txtName = findViewById(R.id.movieTitle);
        txtYear = findViewById(R.id.year);
        txtGenre = findViewById(R.id.genre);
        txtPlot = findViewById(R.id.plot);
        txtImdbRating = findViewById(R.id.imdbRating);
        txtUserRating = findViewById(R.id.userRating);
        txtUserNotes = findViewById(R.id.userNotes);

        txtName.setText(movie.getName());
        txtYear.setText(String.valueOf(movie.getYear()));
        txtGenre.setText(movie.getGenre());
        txtPlot.setText(movie.getPlot());
        txtImdbRating.setText(String.valueOf(movie.getRating()));
        txtUserRating.setText(String.valueOf(movie.getUserRating()));
        txtUserNotes.setText(movie.getNotes());

        switch (movie.getGenre()) {
            case "Action":
                imgGenre.setImageResource(R.drawable.ic_action);
                break;
            case "Comedy":
                imgGenre.setImageResource(R.drawable.ic_comedy);
                break;
            case "Drama":
                imgGenre.setImageResource(R.drawable.ic_drama);
                break;
            case "Horror":
                imgGenre.setImageResource(R.drawable.ic_horror);
                break;
            case "Romance":
                imgGenre.setImageResource(R.drawable.ic_romance);
                break;
            case "Western":
                imgGenre.setImageResource(R.drawable.ic_western);
                break;
            case "Adventure":
                imgGenre.setImageResource(R.drawable.ic_adventure);
                break;
            default:
                imgGenre.setImageResource(R.drawable.ic_resource_default);
                break;
        }
    }

    private void openEditActivity() {
        Intent intent = new Intent(this,EditActivity.class);
        intent.putExtra("id", movie.getUid());
        launcher.launch(intent);
    }

    private void deleteMovie() {
        model.deleteMovie(movie);
        // Toast Code inspired by https://developer.android.com/guide/topics/ui/notifiers/toasts
        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
        toast.show();
        finish();
    }
}