package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.conrtollers.SearchController;
import org.example.conrtollers.StatController;
import org.example.repos.CustomersRepos;
import org.example.repos.ProductsRepos;
import org.example.repos.PurchasesRepos;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.FileWriter;
import java.util.LinkedHashMap;
import java.util.Map;
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
                StatController stat = new StatController(purchasesRepos);
                stat.run(args);
            } else {
                Map<String, String> error = new LinkedHashMap<>();
                error.put("type", "error");
                error.put("message", "Команда не определена");
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writeValue(new FileWriter((args[2] != null)? args[2]: "output.json"), error);
            }
        };
    }
}