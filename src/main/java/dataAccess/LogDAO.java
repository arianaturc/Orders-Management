package dataAccess;

import connection.ConnectionFactory;
import dataModel.Bill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * DAO for Bill (log) entries. Overrides insert and disallows updates.
 */
public class LogDAO extends AbstractDAO<Bill>{

    /**
     * Prevents updating a bill entry.
     * @throws UnsupportedOperationException Always.
     */
    @Override
    public void update(Bill bill) {
        throw new UnsupportedOperationException("Bill can't be updated.");
    }

    /**
     * Inserts a bill (log) entry into the database.
     * @param bill The bill to insert.
     * @return The inserted bill.
     */
    @Override
    public Bill insert(Bill bill) {
        Connection connection = null;
        PreparedStatement statement = null;
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO log (order_id, client_name, product_name, quantity, order_date, price) VALUES (?, ?, ?, ?, ?, ?)");

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query.toString());

            statement.setInt(1, bill.getOrder_id());
            statement.setString(2, bill.getClient_name());
            statement.setString(3, bill.getProduct_name());
            statement.setInt(4, bill.getQuantity());
            statement.setDate(5, new java.sql.Date(bill.getOrder_date().getTime()));
            statement.setDouble(6, bill.getPrice());

            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "BillDAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return bill;
    }

    /**
     * Retrieves all bill entries from the log table.
     * @return A list of all bills.
     */
    @Override
    public List<Bill> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM " + "log";

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            return createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Maps the result set rows to Bill objects.
     * @param resultSet The result set from a query.
     * @return List of Bill objects.
     */
    @Override
    protected List<Bill> createObjects(ResultSet resultSet) {
        List<Bill> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int order_id = resultSet.getInt("order_id");
                String client_name = resultSet.getString("client_name");
                String product_name = resultSet.getString("product_name");
                int quantity = resultSet.getInt("quantity");
                java.sql.Date order_date = resultSet.getDate("order_date");
                double price = resultSet.getDouble("price");

                Bill bill = new Bill(order_id, client_name, product_name, quantity, order_date, price);
                list.add(bill);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "LogDAO:createObjects " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

}
