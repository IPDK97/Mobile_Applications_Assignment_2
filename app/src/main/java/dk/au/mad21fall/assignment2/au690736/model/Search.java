package dk.au.mad21fall.assignment2.au690736.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// Generated with jsonschema2pojo.org
public class Search {

    @SerializedName("Search")
    @Expose
    private List<SearchResult> search = null;

    public List<SearchResult> getSearch() {
        return search;
    }

    public void setSearch(List<SearchResult> search) {
        this.search = search;
    }
}


