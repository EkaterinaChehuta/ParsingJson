package org.example.conrtollers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entities.Customers;
import org.example.entities.Products;
import org.example.entities.Purchases;
import org.example.repos.PurchasesRepos;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class StatController {
    private final PurchasesRepos purchasesRepos;

    public StatController(PurchasesRepos purchasesRepos) {
        this.purchasesRepos = purchasesRepos;
    }

    public void run(String... args) throws IOException {
        try (FileReader reader = new FileReader(args[1])) {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = format.parse(jsonObject.get("startDate").toString());
            Date endDate = format.parse(jsonObject.get("endDate").toString());

            Map<String, Object> result = new LinkedHashMap<>();

            List<Object> customers = saveStat(startDate, endDate);

            double totalExpenses = 0;
            double avgExpenses = 0;

            result.put("type", "stat");
            result.put("totalDays", getCountDays(startDate, endDate));
            result.put("customers", (customers == null) ? "" : customers);
            result.put("totalExpenses", totalExpenses);
            result.put("avgExpenses", avgExpenses);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new FileWriter((args[2] != null) ? args[2] : "output.json"), result);
        } catch (FileNotFoundException e) {
            Map<String, String> error = new LinkedHashMap<>();
            error.put("type", "error");
            error.put("message", "Файл не найден");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new FileWriter((args[2] != null) ? args[2] : "output.json"), error);
        } catch (ParseException e) {
            Map<String, String> error = new LinkedHashMap<>();
            error.put("type", "error");
            error.put("message", "Неправильный формат файла");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new FileWriter((args[2] != null) ? args[2] : "output.json"), error);
        } catch (java.text.ParseException e) {
            Map<String, String> error = new LinkedHashMap<>();
            error.put("type", "error");
            error.put("message", "Неправильный формат даты");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new FileWriter((args[2] != null) ? args[2] : "output.json"), error);
        }
    }

    private int getCountDays(Date startDate, Date endDate) {
        int days = (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24)) + 1;

        LocalDate start = convertToLocalDateViaInstant(startDate);
        LocalDate end = convertToLocalDateViaInstant(endDate);

        while (start.isBefore(end)) {
            if (DayOfWeek.SATURDAY.equals(start.getDayOfWeek())
                    || DayOfWeek.SUNDAY.equals(start.getDayOfWeek())) {
                days--;
            }
            start = start.plusDays(1);
        }

        return days;
    }

    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private List<Object> saveStat(Date startDate, Date endDate) {
        List<Purchases> purchasesList = purchasesRepos.findByDateBetween(startDate, endDate);

        if (purchasesList == null) {
            return null;
        } else {
            purchasesList = purchasesList.stream()
                    .filter(e ->
                            !DayOfWeek.SATURDAY.equals(convertToLocalDateViaInstant(e.getDate()).getDayOfWeek()) &&
                                    !DayOfWeek.SUNDAY.equals(convertToLocalDateViaInstant(e.getDate()).getDayOfWeek()))
                    .collect(Collectors.toList());

            Map<Customers, List<Purchases>> map = purchasesList.stream()
                    .collect(Collectors.groupingBy(Purchases::getCustomer));

            List<Object> result = new LinkedList<>();

            for (Map.Entry<Customers, List<Purchases>> items : map.entrySet()) {
                result.add(addCustomers(items));
            }

            return result;
        }
    }

    private Map<String, Object> addCustomers(Map.Entry<Customers, List<Purchases>> productsList) {
        String name = productsList.getKey().getFirstName().concat(" ")
                .concat(productsList.getKey().getLastName());

        Map<Products, List<Purchases>> products = productsList.getValue().stream()
                .collect(Collectors.groupingBy(Purchases::getProduct));

        List<Object> purchases = new ArrayList<>();

        double totalExpenses = 0;

        for (Map.Entry<Products, List<Purchases>> product : products.entrySet()) {
            Map<String, Object> productMap = new LinkedHashMap<>();

            String productName = product.getKey().getProductName();
            double expenses = product.getValue().stream().mapToDouble(e -> e.getProduct().getPrice()).sum();

            totalExpenses += expenses;

            productMap.put("productName", productName);
            productMap.put("expenses", expenses);
            purchases.add(productMap);
        }

        Map<String, Object> customers = new LinkedHashMap<>();
        customers.put("name", name);
        customers.put("purchases", purchases);
        customers.put("totalExpenses", totalExpenses);

        return customers;
    }
}
