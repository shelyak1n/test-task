package util;

import dao.DatabaseConnection;
import model.ErrorOutput;
import model.entuty.Customer;
import model.statistic.model.CustomerItem;
import model.statistic.model.PurchaseItem;
import model.statistic.model.StatisticsInput;
import model.statistic.model.StatisticsResult;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StatisticsOutput {

    private StatisticsResult result;

    public StatisticsOutput(StatisticsInput statisticsInput) {

        result = new StatisticsResult();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String startDate = simpleDateFormat.format(statisticsInput.getStartDate());
        String endDate = simpleDateFormat.format(statisticsInput.getEndDate());

        result.setType("stat");
        result.setTotalDays(countWeekdays(statisticsInput.getStartDate(), statisticsInput.getEndDate()));
        result.setTotalExpenses(BigDecimal.valueOf(0));
        result.setAvgExpenses(BigDecimal.valueOf(0));

        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "WITH CustomerTotalExpenses AS (" +
                    "SELECT " +
                    "c.id AS customer_id, " +
                    "c.firstName, " +
                    "c.lastName, " +
                    "SUM(pr.price) AS totalExpenses " +
                    "FROM " +
                    "purchases p " +
                    "JOIN " +
                    "products pr ON p.productid = pr.id " +
                    "JOIN " +
                    "customers c ON p.customerid = c.id " +
                    "WHERE " +
                    "p.purchaseDate BETWEEN '" + startDate + "' AND '" + endDate + "' " +
                    "AND EXTRACT(DOW FROM p.purchaseDate) NOT IN (0, 6) " +
                    "GROUP BY " +
                    "c.id, c.firstName, c.lastName " +
                    "), " +
                    "ProductTotalExpenses AS ( " +
                    "SELECT " +
                    "c.id AS customer_id, " +
                    "pr.name AS productName, " +
                    "SUM(pr.price) AS totalExpensesForProduct " +
                    "FROM " +
                    "purchases p " +
                    "JOIN " +
                    "products pr ON p.productid = pr.id " +
                    "JOIN " +
                    "customers c ON p.customerid = c.id " +
                    "WHERE " +
                    "p.purchaseDate BETWEEN '" + startDate + "' AND '" + endDate + "' " +
                    "AND EXTRACT(DOW FROM p.purchaseDate) NOT IN (0, 6) " +
                    "GROUP BY " +
                    "c.id, pr.name " +
                    ") " +
                    "SELECT " +
                    "c.firstName, " +
                    "c.lastName, " +
                    "c.totalExpenses AS totalCustomerExpenses, " +
                    "pt.productName, " +
                    "pt.totalExpensesForProduct " +
                    "FROM " +
                    "CustomerTotalExpenses c " +
                    "JOIN " +
                    "ProductTotalExpenses pt ON c.customer_id = pt.customer_id " +
                    "ORDER BY " +
                    "c.totalExpenses DESC, pt.totalExpensesForProduct DESC;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<CustomerItem> customersItem = new ArrayList<>();
            while (resultSet.next()) {

                CustomerItem customerItem = new CustomerItem();
                Customer customer = new Customer();

                customer.setFirstName(resultSet.getString("firstName"));
                customer.setLastName(resultSet.getString("lastName"));
                customerItem.setCustomer(customer);

                PurchaseItem purchaseItem = new PurchaseItem();
                purchaseItem.setName(resultSet.getString("productName"));
                purchaseItem.setTotalExpensesForProduct(resultSet.getBigDecimal("totalExpensesForProduct"));

                result.setTotalExpenses(result.getTotalExpenses().add(purchaseItem.getTotalExpensesForProduct()));

                customerItem.setPurchases(new ArrayList<>());
                customerItem.getPurchases().add(purchaseItem);

                boolean customerExists = false;
                for (CustomerItem existingCustomer : customersItem) {
                    if (existingCustomer.getCustomer().getFirstName().equals(customer.getFirstName()) &&
                            existingCustomer.getCustomer().getLastName().equals(customer.getLastName())) {

                        existingCustomer.getPurchases().addAll(customerItem.getPurchases());

                        customerExists = true;
                        break;
                    }
                }
                if (!customerExists) {
                    customersItem.add(customerItem);
                }
            }

            result.setCustomers(customersItem);

            result.setAvgExpenses(result.getTotalExpenses().divide(BigDecimal.valueOf(result.getCustomers().size()), MathContext.DECIMAL32));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public StatisticsResult getStatisticsResult() {
        return result;
    }

    public static int countWeekdays(Date startDate, Date endDate) {
        int weekdays = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        while (!calendar.getTime().after(endDate)) {
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                weekdays++;
            }

            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return weekdays;
    }
}
