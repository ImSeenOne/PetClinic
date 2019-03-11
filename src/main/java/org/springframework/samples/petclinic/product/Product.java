package org.springframework.samples.petclinic.product;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "products")
public class Product {

    @Column(name = "name")
    @NotEmpty
    private String name;

    @Column(name = "price")
    @NotEmpty
    private String price;

    @Column(name = "description")
    @NotEmpty
    private String description;

    @Column(name = "stock")
    @NotEmpty
    private Integer stock;

    @Override
    public String toString() {
        return "Product{" +
            "name='" + name + '\'' +
            ", price='" + price + '\'' +
            ", description='" + description + '\'' +
            ", stock=" + stock +
            '}';
    }

    public String getName() {
        return name;
    }

    public Product setName(String name) {
        this.name = name;
        return this;
    }

    public String getPrice() {
        return price;
    }

    public Product setPrice(String price) {
        this.price = price;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Product setDescription(String description) {
        this.description = description;
        return this;
    }

    public Integer getStock() {
        return stock;
    }

    public Product setStock(Integer stock) {
        this.stock = stock;
        return this;
    }
}
