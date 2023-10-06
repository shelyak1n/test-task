package model.statistic.model;

import java.math.BigDecimal;

public class PurchaseItem {
    private String name;
    private BigDecimal expenses;

    public String getName() {
        return name;
    }

    public void setName(String prductName) {
        this.name = prductName;
    }

    public BigDecimal getTotalExpensesForProduct() {
        return expenses;
    }

    public void setTotalExpensesForProduct(BigDecimal totalExpensesForProduct) {
        this.expenses = totalExpensesForProduct;
    }
}

