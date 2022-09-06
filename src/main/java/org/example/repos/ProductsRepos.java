package org.example.repos;

import org.example.entities.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepos extends JpaRepository<Products, Integer> {
    Products findByProductName(String productName);
}
