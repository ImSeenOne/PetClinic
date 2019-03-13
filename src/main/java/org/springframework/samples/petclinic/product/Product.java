package org.springframework.samples.petclinic.product;


import org.springframework.samples.petclinic.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "products")
public class Product extends BaseEntity {

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
    private String stock;

    @Column(name = "imagepath")
    private String imagePath;

    @Override
    public String toString() {
        return "Product{" +
            "name='" + name + '\'' +
            ", price='" + price + '\'' +
            ", description='" + description + '\'' +
            ", stock='" + stock + '\'' +
            ", imagePath='" + imagePath + '\'' +
            '}';
    }

    public String getImagePath() {
        return imagePath;
    }

    public Product setImagePath(String imagePath) {
        this.imagePath = imagePath;
        return this;
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

    public String getStock() {
        return stock;
    }

    public Product setStock(String stock) {
        this.stock = stock;
        return this;
    }
}
