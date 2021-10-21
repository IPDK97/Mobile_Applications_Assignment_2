package dk.au.mad21fall.assignment2.au690736.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// Generated with jsonschema2pojo.org
public class SearchResult {
    @SerializedName("Title")
    @Expose
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
