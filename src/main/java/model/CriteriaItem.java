package model;

import java.math.BigDecimal;

public class CriteriaItem {
    private String lastName;
    private String productName;
    private Integer minTimes;
    private BigDecimal minExpenses;
    private BigDecimal maxExpenses;
    private Integer badCustomers;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getMinTimes() {
        return minTimes;
    }

    public void setMinTimes(int minTimes) {
        this.minTimes = minTimes;
    }

    public BigDecimal getMinExpenses() {
        return minExpenses;
    }

    public void setMinExpenses(BigDecimal minExpenses) {
        this.minExpenses = minExpenses;
    }

    public BigDecimal getMaxExpenses() {
        return maxExpenses;
    }

    public void setMaxExpenses(BigDecimal maxExpenses) {
        this.maxExpenses = maxExpenses;
    }

    public Integer getBadCustomers() {
        return badCustomers;
    }

    public void setBadCustomers(int badCustomers) {
        this.badCustomers = badCustomers;
    }
}
