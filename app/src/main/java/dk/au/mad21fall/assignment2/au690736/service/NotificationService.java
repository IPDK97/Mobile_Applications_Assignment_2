package dk.au.mad21fall.assignment2.au690736.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleService;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dk.au.mad21fall.assignment2.au690736.R;
import dk.au.mad21fall.assignment2.au690736.model.Movie;
import dk.au.mad21fall.assignment2.au690736.model.Repository;

// Foreground Service for Notifications
// Code inspired by Services Lab
public class NotificationService extends LifecycleService {

    private static final String TAG = "NotificationService";
    private List<Movie> movies;
    private final Random random = new Random();

    public static final String SERVICE_CHANNEL = "serviceChannel";
    public static final int NOTIFICATION_ID = 42;

    static final String API_KEY = "25bc54a2";
    static final String[] initialState = {"The Terminator", "The Shawshank Redemption", "Hackers", "Cube", "The Ring", "Django Unchained", "Casablanca", "Hercules in New York", "The Matrix", "WarGames"};

    String base = "https://www.omdbapi.com/?apikey=" + API_KEY + "&t=";

    ExecutorService execService;
    RequestQueue queue;
    Repository repository
            ;
    boolean started = false;
    int count;

    @Override
    public void onCreate() {
        super.onCreate();

        repository = Repository.getInstance(getApplication());
        repository.getMovies().observe(this, movies -> this.movies = movies);

        // Initialize List with Default Movies if Database is empty
        try {
            int itemsCount = repository.getCount().get();
            if(itemsCount == 0) {
                initializeList();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        count = 0;
    }

    // onStartCommand called when an Activity starts the Service with Intent through calling startService(...)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(SERVICE_CHANNEL, "Notification Service", NotificationManager.IMPORTANCE_LOW);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

        initiateBackgroundTasks();

        // Returning START_STICKY will make the Service restart again if it gets killed
        return START_STICKY;
    }

    // Initiate Background Tasks
    private void initiateBackgroundTasks() {
        if(!started) {
            started = true;
            createNotificationRecursive();
        }
    }

    private void createNotificationRecursive() {

        if(execService == null) {
            execService = Executors.newSingleThreadExecutor();
        }

        execService.submit(() -> {
            createNotification();
            count++;
            Log.d(TAG, "Count: " + count);
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                Log.e(TAG, "run: ERROR", e);
            }
            // Recursive -> If started still true, call self again
            if(started) {
                createNotificationRecursive();
            }
        });
    }

    private void createNotification() {
        Movie movieRecommendation = getRecommendation();
        if(movieRecommendation != null) {
            Notification notification = new NotificationCompat.Builder(this, SERVICE_CHANNEL)
                    .setContentTitle("Consider Watching")
                    .setContentText(movieRecommendation.getName() + " - IMDB Rating: " + movieRecommendation.getRating() )
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .build();

            startForeground(NOTIFICATION_ID, notification);
        }
    }

    @Override
    public IBinder onBind(@NonNull Intent intent) {
        super.onBind(intent);
        return null;
    }

    @Override
    public void onDestroy() {
        started = false;
        super.onDestroy();
    }

    // Get Data from API
    private void initializeList() {
        for (String s : initialState) {
            sendRequest(base + s);
        }
    }

    private void sendRequest(String url){
        if(queue==null) {
            queue = Volley.newRequestQueue(this);
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Log.d(TAG, "onResponse: " + response);
                    parseJson(response);
                }, error -> Log.e(TAG, "That did not work!", error));

        queue.add(stringRequest);
    }

    private void parseJson(String json){
        Gson gson = new GsonBuilder().create();
        Movie movie =  gson.fromJson(json, Movie.class);

        // Get the first Genre from the Genre Field and remove the others
        if(movie!=null){
            String genre = movie.getGenre();
            if(movie.getGenre().contains(",")) {
                genre = genre.substring(0,movie.getGenre().indexOf(","));
                movie.setGenre(genre);
            }
            repository.addMovieAsync(movie);
        }
    }

    // Get Random Movie from MovieList
    private Movie getRecommendation() {
        if (movies != null) {
            int id = random.nextInt(movies.size());
            return movies.get(id);
        }
        return null;
    }
}
