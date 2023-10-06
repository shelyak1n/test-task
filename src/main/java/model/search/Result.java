package model.search;

import java.util.List;

public class Result {
    private String type;
    private List<ResultItem> results;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ResultItem> getResults() {
        return results;
    }

    public void setResults(List<ResultItem> results) {
        this.results = results;
    }
}
