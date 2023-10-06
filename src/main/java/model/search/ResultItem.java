package model.search;

import model.criteria.CriteriaItem;
import model.entuty.Customer;

import java.util.List;

public class ResultItem {
    private CriteriaItem criteria;
    private List<Customer> results;

    public CriteriaItem getCriteria() {
        return criteria;
    }

    public void setCriteria(CriteriaItem criteria) {
        this.criteria = criteria;
    }

    public List<Customer> getResults() {
        return results;
    }

    public void setResults(List<Customer> results) {
        this.results = results;
    }
}