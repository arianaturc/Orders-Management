package dataAccess;

import connection.ConnectionFactory;
import dataModel.Bill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

public class LogDAO extends AbstractDAO<Bill>{

    @Override
    public Bill update(Bill bill) {
        throw new UnsupportedOperationException("Bill can't be updated.");
    }

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



}
