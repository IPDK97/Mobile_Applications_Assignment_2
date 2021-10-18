package dk.au.mad21fall.assignment2.au690736;

import java.io.Serializable;
public class Movie implements Serializable {

    private String name;
    private final String genre;
    private final int year;
    private final double rating;
    private final String plot;
    private double userRating;
    private String notes;
    private int position;

    public Movie(String name, String genre, int year, double rating, String plot) {
        this.name = name;
        this.genre = genre;
        this.year = year;
        this.rating = rating;
        this.plot = plot;
        this.userRating = 0.0;
        this.notes = "";
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

    public int getPosition() { return position; }

    public void setPosition(int position) { this.position = position; }
}
