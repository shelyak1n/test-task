package model;

import java.math.BigDecimal;
import java.util.List;

public class Criteria {
    private List<CriteriaItem> criterias;

    public List<CriteriaItem> getCriterias() {
        return criterias;
    }

    public void setCriterias(List<CriteriaItem> criterias) {
        this.criterias = criterias;
    }
}
