package util;

import dao.DatabaseConnection;
import model.criteria.CriteriaItem;
import model.entuty.Customer;
import model.search.Result;
import model.search.ResultItem;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchResult {

    private Result result;

    public SearchResult(ArrayList<Map<String, Object>> arrayListCriteria) {
        result = new Result();
        result.setType("search");

        List<ResultItem> resultItems = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection()) {
            for (Map<String, Object> map : arrayListCriteria) {
                String sql = buildQuery(map);

                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery();

                ResultItem resultItem = new ResultItem();
                CriteriaItem criteria = new CriteriaItem();

                if (map.containsKey("lastName")) {
                    criteria.setLastName((String) map.get("lastName"));
                } else if (map.containsKey("productName") && map.containsKey("minTimes")) {
                    criteria.setProductName((String) map.get("productName"));
                    criteria.setMinTimes((int) map.get("minTimes"));
                } else if (map.containsKey("minExpenses") && map.containsKey("maxExpenses")) {
                    criteria.setMinExpenses((BigDecimal) map.get("minExpenses"));
                    criteria.setMaxExpenses((BigDecimal) map.get("maxExpenses"));
                } else if (map.containsKey("badCustomer")) {
                    criteria.setBadCustomers((int) map.get("badCustomer"));
                }

                resultItem.setCriteria(criteria);

                List<Customer> customers = new ArrayList<>();
                while (resultSet.next()) {
                    Customer customer = new Customer();
                    customer.setId(resultSet.getInt("id"));
                    customer.setFirstName(resultSet.getString("firstName"));
                    customer.setLastName(resultSet.getString("lastName"));
                    customers.add(customer);
                }

                resultItem.setResults(customers);
                resultItems.add(resultItem);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        result.setResults(resultItems);
    }

    public Result getResult() {
        return result;
    }

    private String buildQuery(Map<String, Object> map) {
        if (map.containsKey("lastName")) {
            return "SELECT Customers.id, Customers.lastName, Customers.firstName FROM Customers WHERE Customers.lastName = '" + map.get("lastName") + "'";
        } else if (map.containsKey("productName") && map.containsKey("minTimes")) {
            return "SELECT Customers.id, Customers.firstName, Customers.lastName " +
                    "FROM Customers " +
                    "JOIN Purchases ON Customers.id = Purchases.customerId " +
                    "JOIN Products ON Purchases.productId = Products.id " +
                    "WHERE Products.name = '" + map.get("productName") + "'" +
                    "GROUP BY Customers.id, Customers.firstName, Customers.lastName " +
                    "HAVING COUNT(Purchases.id) >= " + map.get("minTimes");
        } else if (map.containsKey("minExpenses") && map.containsKey("maxExpenses")) {
            return "SELECT Customers.id, Customers.firstName, Customers.lastName " +
                    "FROM Customers " +
                    "JOIN Purchases ON Customers.id = Purchases.id " +
                    "JOIN Products ON Purchases.id = Products.id " +
                    "GROUP BY Customers.id, Customers.firstName, Customers.lastName " +
                    "HAVING SUM(Products.price) >= " + map.get("minExpenses") + " AND SUM(Products.price) <= " + map.get("maxExpenses");
        } else if (map.containsKey("badCustomer")) {
            return "SELECT Customers.id, Customers.firstName, Customers.lastName " +
                    "FROM Customers " +
                    "JOIN Purchases ON Customers.id = Purchases.id " +
                    "GROUP BY Customers.id, Customers.firstName, Customers.lastName " +
                    "ORDER BY COUNT(Purchases.id) " +
                    "LIMIT " + map.get("badCustomer");
        }

        return "";
    }
}
