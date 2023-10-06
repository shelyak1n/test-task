package model.statistic.model;

import model.entuty.Customer;

import java.util.List;

public class CustomerItem {
    private Customer customer;
    private List<PurchaseItem> purchases;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<PurchaseItem> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<PurchaseItem> purchases) {
        this.purchases = purchases;
    }
}
