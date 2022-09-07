package org.example.conrtollers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entities.Customers;
import org.example.entities.Products;
import org.example.entities.Purchases;
import org.example.repos.CustomersRepos;
import org.example.repos.ProductsRepos;
import org.example.repos.PurchasesRepos;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SearchController {

    private final CustomersRepos customersRepos;
    private final ProductsRepos productsRepos;
    private final PurchasesRepos purchasesRepos;

    public SearchController(CustomersRepos customersRepos, ProductsRepos productsRepos, PurchasesRepos purchasesRepos) {
        this.customersRepos = customersRepos;
        this.productsRepos = productsRepos;
        this.purchasesRepos = purchasesRepos;
    }

    public void run(String... args) throws IOException {
        try (FileReader reader = new FileReader(args[1])) {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
            JSONArray jsonArray = (JSONArray) jsonObject.get("criteria");

            List<Object> result = new LinkedList<>();

            for (Object o : jsonArray) {
                JSONObject innerObj = (JSONObject) o;
                Set keys = innerObj.keySet();

                for (Object key : keys) {
                    String k = key.toString();

                    if (Objects.equals(k, "lastName")) {
                        Map<String, Object> mapResult = saveByLastName(innerObj);
                        result.add(mapResult);
                    } else if (Objects.equals(k, "productName")) {
                        Map<String, Object> mapResult = saveByProductNameEndCount(innerObj);
                        result.add(mapResult);
                        break;
                    } else if (Objects.equals(k, "minExpenses")) {
                        Map<String, Object> mapResult = saveFromRange(innerObj);
                        result.add(mapResult);
                        break;
                    } else if (Objects.equals(k, "badCustomers")) {
                        Map<String, Object> mapResult = saveBadCustomers(innerObj);
                        result.add(mapResult);
                    }
                }
            }

            Map<String, Object> title = new LinkedHashMap<>();
            title.put("type", "search");
            title.put("result", result);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new FileWriter((args[2] != null)? args[2]: "output.json"), title);
        } catch (FileNotFoundException e) {
            Map<String, String> error = new LinkedHashMap<>();
            error.put("type", "error");
            error.put("message", "Файл не найден");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new FileWriter((args[2] != null)? args[2]: "output.json"), error);
        } catch (ParseException e) {
            Map<String, String> error = new LinkedHashMap<>();
            error.put("type", "error");
            error.put("message", "Неправильный формат файла");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new FileWriter((args[2] != null)? args[2]: "output.json"), error);
        }
    }

    private Map<String, Object> saveByLastName(JSONObject innerObj) throws IOException {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("criteria", innerObj);
        List<Customers> customersList = customersRepos.findByLastName(innerObj.get("lastName").toString());
        if (customersList == null) {
            map.put("result", "");
        } else {
            map.put("result", customersList);
        }
        return map;
    }

    private Map<String, Object> saveByProductNameEndCount(JSONObject innerObj) throws IOException {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("criteria", innerObj);

        Products product = productsRepos.findByProductName(innerObj.get("productName").toString());

        if (product == null) {
            result.put("result", "");
        } else {
            List<Purchases> purchasesList = purchasesRepos.findByProduct(product);

            if (purchasesList == null) {
                result.put("result", "");
            } else {
                Map<Customers, List<Purchases>> map = purchasesList.stream()
                        .collect(Collectors.groupingBy(Purchases::getCustomer));

                int count = Integer.parseInt(innerObj.get("minTimes").toString());

                List<Customers> customersList = map.entrySet().parallelStream()
                        .filter(e -> e.getValue().size() >= count)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

                result.put("result", customersList);
            }
        }
        return result;
    }

    private Map<String, Object> saveFromRange(JSONObject innerObj) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("criteria", innerObj);

        List<Purchases> purchasesList = purchasesRepos.findAll();

        if (purchasesList == null) {
            map.put("result", "");
        } else {
            Map<Customers, List<Products>> mapCustomers = purchasesList.stream()
                    .collect(Collectors.groupingBy(Purchases::getCustomer,
                            Collectors.mapping(Purchases::getProduct, Collectors.toList())));

            List<Customers> customersList = mapCustomers.entrySet().parallelStream()
                    .filter(e -> e.getValue().stream()
                            .mapToDouble(Products::getPrice)
                            .sum() >= Integer.parseInt(innerObj.get("minExpenses").toString()) &&
                            e.getValue().stream()
                                    .mapToDouble(Products::getPrice)
                                    .sum() <= Integer.parseInt(innerObj.get("maxExpenses").toString()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            map.put("result", customersList);
        }
        return map;
    }

    private Map<String, Object> saveBadCustomers(JSONObject innerObj) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("criteria", innerObj);

        List<Purchases> purchases = purchasesRepos.findAll();

        if (purchases == null) {
            map.put("result", "");
        } else {
            Map<Customers, List<Products>> mapCustomers = purchases.stream()
                    .collect(Collectors.groupingBy(Purchases::getCustomer,
                            Collectors.mapping(Purchases::getProduct, Collectors.toList())));

            List<Customers> customersList = mapCustomers.entrySet().parallelStream()
                    .sorted(Comparator.comparing(e -> e.getValue().stream()
                            .mapToDouble(Products::getPrice).sum()))
                    .map(Map.Entry::getKey)
                    .limit(Integer.parseInt(innerObj.get("badCustomers").toString()))
                    .collect(Collectors.toList());

            map.put("result", customersList);
        }
        return map;
    }
}
