package org.example.repos;

import org.example.entities.Customers;
import org.example.entities.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomersRepos extends JpaRepository<Customers, Integer> {
    List<Customers> findByLastName(String lastName);
}
