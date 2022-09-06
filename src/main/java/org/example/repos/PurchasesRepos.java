package org.example.repos;

import org.example.entities.Products;
import org.example.entities.Purchases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchasesRepos extends JpaRepository<Purchases, Integer> {
    List<Purchases> findByProduct(Products product);
}
