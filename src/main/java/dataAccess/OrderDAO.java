package dataAccess;

import connection.ConnectionFactory;
import dataModel.Orders;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;

public class OrderDAO extends AbstractDAO<Orders> {

    public Orders getLastInserted() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        String query = "SELECT * FROM " + type.getSimpleName().toLowerCase() + " ORDER BY id DESC LIMIT 1";

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            List<Orders> results = createObjects(resultSet);
            if (!results.isEmpty()) {
                return results.get(0);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "OrderDAO:getLastInserted " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return null;
    }


}
