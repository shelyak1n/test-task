package util;

import model.criteria.Criteria;
import model.criteria.CriteriaItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchCriteria {
    private ArrayList<Map<String, Object>> arrayListCriteria = new ArrayList<>();
    public SearchCriteria(Criteria criteria) {

        for (CriteriaItem criteriaItem : criteria.getCriterias()) {
            if (criteriaItem.getLastName() != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("lastName", criteriaItem.getLastName());
                arrayListCriteria.add(map);
            }
            if (criteriaItem.getProductName() != null && criteriaItem.getMinTimes() != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("productName", criteriaItem.getProductName());
                map.put("minTimes", criteriaItem.getMinTimes());
                arrayListCriteria.add(map);
            }
            if (criteriaItem.getMinExpenses() != null && criteriaItem.getMinExpenses() != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("minExpenses", criteriaItem.getMinExpenses());
                map.put("maxExpenses", criteriaItem.getMaxExpenses());
                arrayListCriteria.add(map);
            }
            if (criteriaItem.getBadCustomers() != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("badCustomer", criteriaItem.getBadCustomers());
                arrayListCriteria.add(map);
            }
        }
    }
    public ArrayList<Map<String, Object>> arrayListCriteria() {
        return arrayListCriteria;
    }
}

