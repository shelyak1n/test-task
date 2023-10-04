package model.entuty;

import java.time.LocalDate;

public class Purchase {
    private int id;
    private Customer customer;
    private Product product;
    private LocalDate purchaseDate;

    public Product getProduct() {
        return product;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }
}
