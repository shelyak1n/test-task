package handler;

import dao.DatabaseConnection;
import model.entuty.Customer;
import model.statistic.CustomerItem;
import model.statistic.PurchaseItem;
import model.statistic.StatisticsInput;
import model.statistic.StatisticsResult;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class StatisticsHandler {
    public StatisticsResult performStatistics(StatisticsInput input) {
        StatisticsResult result = new StatisticsResult();
        result.setType("stat");
        result.setTotalDays(countWeekdays(input.getStartDate(), input.getEndDate()));

        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = buildQuery(input);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            preparedStatement.setDate(1, java.sql.Date.valueOf(dateFormat.format(input.getStartDate().getTime())));
            preparedStatement.setDate(2, java.sql.Date.valueOf(dateFormat.format(input.getEndDate().getTime())));
            preparedStatement.setDate(3, java.sql.Date.valueOf(dateFormat.format(input.getStartDate().getTime())));
            preparedStatement.setDate(4, java.sql.Date.valueOf(dateFormat.format(input.getEndDate().getTime())));

            result.setType("stat");
            result.setTotalDays(countWeekdays(input.getStartDate(), input.getEndDate()));
            result.setTotalExpenses(BigDecimal.valueOf(0));
            result.setAvgExpenses(BigDecimal.valueOf(0));

            ResultSet resultSet = preparedStatement.executeQuery();

            List<CustomerItem> customersItem = new ArrayList<>();
            while (resultSet.next()) {

                CustomerItem customerItem = new CustomerItem();
                Customer customer = new Customer();

                customer.setId(resultSet.getInt("customer_id"));
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
            if (!result.getCustomers().isEmpty()) {
                result.setAvgExpenses(result.getTotalExpenses()
                        .divide(BigDecimal.valueOf(result.getCustomers().size()), 2, RoundingMode.HALF_UP));
            } else {
                result.setAvgExpenses(BigDecimal.ZERO);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private int countWeekdays(Date startDate, Date endDate) {
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

    private String buildQuery(StatisticsInput input) {
        StringBuilder queryBuilder = new StringBuilder("WITH CustomerTotalExpenses AS (");
        queryBuilder.append("SELECT c.id AS customer_id, c.firstName, c.lastName, SUM(pr.price) AS totalExpenses")
                .append(" FROM purchases p")
                .append(" JOIN products pr ON p.productid = pr.id")
                .append(" JOIN customers c ON p.customerid = c.id")
                .append(" WHERE p.purchaseDate BETWEEN ?  AND ?")
                .append(" AND EXTRACT(DOW FROM p.purchaseDate) NOT IN (0, 6)")
                .append(" GROUP BY c.id, c.firstName, c.lastName),")
                .append("ProductTotalExpenses AS (")
                .append("SELECT c.id AS customer_id, pr.name AS productName, SUM(pr.price) AS totalExpensesForProduct")
                .append(" FROM purchases p")
                .append(" JOIN products pr ON p.productid = pr.id")
                .append(" JOIN customers c ON p.customerid = c.id")
                .append(" WHERE p.purchaseDate BETWEEN ? AND ?")
                .append(" AND EXTRACT(DOW FROM p.purchaseDate) NOT IN (0, 6)")
                .append(" GROUP BY c.id, pr.name)")
                .append(" SELECT c.customer_id AS customer_id, c.firstName, c.lastName, c.totalExpenses AS totalCustomerExpenses, pt.productName, pt.totalExpensesForProduct")
                .append(" FROM CustomerTotalExpenses c")
                .append(" JOIN ProductTotalExpenses pt ON c.customer_id = pt.customer_id")
                .append(" ORDER BY c.totalExpenses DESC, pt.totalExpensesForProduct DESC;");

        return queryBuilder.toString();
    }
}

