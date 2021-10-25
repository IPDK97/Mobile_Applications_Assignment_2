package dk.au.mad21fall.assignment2.au690736;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import dk.au.mad21fall.assignment2.au690736.model.Movie;
import dk.au.mad21fall.assignment2.au690736.viewmodel.EditViewModel;

public class EditActivity extends AppCompatActivity {

    private Movie movie;
    private EditViewModel model;

    ImageView imgGenre;
    TextView txtName;
    TextView txtRating;
    TextView txtNotes;
    Button btnSave;
    Button btnCancel;
    SeekBar skbRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        model = new ViewModelProvider(this).get(EditViewModel.class);
        model.setMovie(getIntent().getIntExtra("id", 0));

        movie = model.getMovie();
        createEditPage();
    }

    private void createEditPage() {
        imgGenre = findViewById(R.id.imageGenre);
        txtName = findViewById(R.id.nameMovie);
        txtRating = findViewById(R.id.ratingMovie);
        txtNotes = findViewById(R.id.editNotes);
        skbRating = findViewById(R.id.seekBar);
        btnSave = findViewById(R.id.save);
        btnCancel = findViewById(R.id.cancel);

        txtName.setText(movie.getName());
        txtRating.setText(String.valueOf(movie.getUserRating()));
        txtNotes.setText(movie.getNotes());
        skbRating.setProgress((int) (movie.getUserRating() * 10));

        skbRating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txtRating.setText(String.valueOf((double) i /10));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Glide.with(imgGenre.getContext()).load(movie.getPoster()).into(imgGenre);

        btnSave.setOnClickListener(v -> saveData());
        btnCancel.setOnClickListener(v -> cancel());
    }

    private void cancel() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void saveData() {
        movie.setNotes(txtNotes.getText().toString());
        movie.setUserRating(Double.parseDouble(txtRating.getText().toString()));
        model.updateMovie(movie);
        finish();
    }
}