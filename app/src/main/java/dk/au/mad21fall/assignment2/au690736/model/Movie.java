package dk.au.mad21fall.assignment2.au690736.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

// Code inspired by Lab Code --> Room Async
@Entity
public class Movie implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @SerializedName("Title")
    @Expose
    private String name;

    @SerializedName("Genre")
    @Expose
    private String genre;

    @SerializedName("Year")
    @Expose
    private final int year;

    @SerializedName("imdbRating")
    @Expose
    private final double rating;

    @SerializedName("Plot")
    @Expose
    private final String plot;

    private double userRating;
    private String notes;

    public Movie(String name, String genre, int year, double rating, String plot) {
        this.name = name;
        this.genre = genre;
        this.year = year;
        this.rating = rating;
        this.plot = plot;
        this.userRating = 0.0;
        this.notes = "";
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getYear() {
        return year;
    }

    public double getRating() {
        return rating;
    }

    public String getPlot() {
        return plot;
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
