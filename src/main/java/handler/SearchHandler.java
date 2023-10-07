package handler;

import dao.DatabaseConnection;
import model.criteria.Criteria;
import model.criteria.CriteriaItem;
import model.entuty.Customer;
import model.search.Result;
import model.search.ResultItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SearchHandler {
    public Result performSearch(Criteria criteria) {
        Result result = new Result();
        result.setType("search");
        List<ResultItem> resultItems = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection()) {
            for (CriteriaItem criteriaItem : criteria.getCriterias()) {
                String sql = buildQuery(criteriaItem);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery();
                ResultItem resultItem = new ResultItem();
                resultItem.setCriteria(criteriaItem);
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
            e.printStackTrace();
        }

        result.setResults(resultItems);
        return result;
    }

    private String buildQuery(CriteriaItem criteriaItem) {
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT Customers.id, Customers.firstName, Customers.lastName FROM Customers WHERE"
        );

        if (criteriaItem.getLastName() != null) {
            queryBuilder.append(" Customers.lastName = '")
                    .append(criteriaItem.getLastName())
                    .append("'");
        }

        if (criteriaItem.getProductName() != null && criteriaItem.getMinTimes() != null) {
            if (criteriaItem.getLastName() != null) {
                queryBuilder.append(" AND");
            }
            queryBuilder.append(" EXISTS (SELECT 1 FROM Purchases JOIN Products ON Purchases.productId = Products.id WHERE Customers.id = Purchases.customerId AND Products.name = '")
                    .append(criteriaItem.getProductName())
                    .append("' GROUP BY Customers.id HAVING COUNT(Purchases.id) >= ")
                    .append(criteriaItem.getMinTimes())
                    .append(")");
        }

        if (criteriaItem.getMinExpenses() != null && criteriaItem.getMaxExpenses() != null) {
            if (criteriaItem.getLastName() != null || (criteriaItem.getProductName() != null && criteriaItem.getMinTimes() != null)) {
                queryBuilder.append(" AND");
            }
            queryBuilder.append(" EXISTS (SELECT 1 FROM Purchases JOIN Products ON Purchases.productId = Products.id WHERE Customers.id = Purchases.customerId GROUP BY Customers.id HAVING SUM(Products.price) BETWEEN ")
                    .append(criteriaItem.getMinExpenses())
                    .append(" AND ")
                    .append(criteriaItem.getMaxExpenses())
                    .append(")");
        }

        if (criteriaItem.getBadCustomers() != null) {
            if (criteriaItem.getLastName() != null || (criteriaItem.getProductName() != null && criteriaItem.getMinTimes() != null) || (criteriaItem.getMinExpenses() != null && criteriaItem.getMaxExpenses() != null)) {
                queryBuilder.append(" AND");
            }
            queryBuilder.append(" EXISTS (SELECT 1 FROM Purchases WHERE Customers.id = Purchases.customerId GROUP BY Customers.id HAVING COUNT(Purchases.id) = (SELECT MIN(purchase_count) FROM (SELECT COUNT(Purchases.id) AS purchase_count FROM Purchases GROUP BY Purchases.customerId) AS counts) LIMIT ")
                    .append(criteriaItem.getBadCustomers())
                    .append(")");
        }

        return queryBuilder.toString();
    }
}

