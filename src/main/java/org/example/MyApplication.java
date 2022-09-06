package org.example;

import org.example.conrtollers.SearchController;
import org.example.repos.CustomersRepos;
import org.example.repos.ProductsRepos;
import org.example.repos.PurchasesRepos;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Objects;

@SpringBootApplication
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }

    @Bean
    public CommandLineRunner CommandLineRunnerBean(CustomersRepos customersRepos,
                                                   ProductsRepos productsRepos,
                                                   PurchasesRepos purchasesRepos) {
        return (args) -> {
            if (Objects.equals(args[0], "search")) {
                SearchController search = new SearchController(customersRepos, productsRepos, purchasesRepos);
                search.run(args);
            } else if (Objects.equals(args[0], "stat")) {

            }
        };
    }
}