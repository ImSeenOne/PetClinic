package org.springframework.samples.petclinic.product;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface ProductRepository extends Repository<Product, Integer> {
    @Query("SELECT DISTINCT product FROM Product product WHERE product.name LIKE :name%")
    @Transactional(readOnly = true)
    Collection<Product> findByName(@Param("name") String name);

    @Query("SELECT product FROM Product product WHERE product.id = :id")
    @Transactional(readOnly = true)
    Product findById(@Param("id") Integer id);

    void delete(Product product);

    void save(Product product);
}
