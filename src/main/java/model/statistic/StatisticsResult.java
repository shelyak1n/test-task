package model.statistic;

import java.math.BigDecimal;
import java.util.List;

public class StatisticsResult {
    private String type;
    private int totalDays;
    private List<CustomerItem> customers;
    private BigDecimal totalExpenses;
    private BigDecimal avgExpenses;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }

    public List<CustomerItem> getCustomers() {
        return customers;
    }

    public void setCustomers(List<CustomerItem> customers) {
        this.customers = customers;
    }

    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(BigDecimal totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public BigDecimal getAvgExpenses() {
        return avgExpenses;
    }

    public void setAvgExpenses(BigDecimal avgExpenses) {
        this.avgExpenses = avgExpenses;
    }
}
