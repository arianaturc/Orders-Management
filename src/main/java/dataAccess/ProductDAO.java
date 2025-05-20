package dataAccess;

import connection.ConnectionFactory;
import dataModel.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 * DAO class for the Product entity. Provides additional deletion logic
 * to remove associated bills and orders before deleting the product.
 */
public class ProductDAO extends AbstractDAO<Product>{

    /**
     * Deletes a product and all associated orders and bill entries.
     * @param id The ID of the product to delete.
     */
    public void delete(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionFactory.getConnection();
            connection.setAutoCommit(false);


            String deleteBills = "DELETE FROM log WHERE order_id IN (SELECT id FROM orders WHERE product_id = ?)";
            try (PreparedStatement billStmt = connection.prepareStatement(deleteBills)) {
                billStmt.setInt(1, id);
                billStmt.executeUpdate();
            }


            String deleteOrders = "DELETE FROM orders WHERE product_id = ?";
            try (PreparedStatement orderStmt = connection.prepareStatement(deleteOrders)) {
                orderStmt.setInt(1, id);
                orderStmt.executeUpdate();
            }


            String deleteProduct = "DELETE FROM product WHERE id = ?";
            statement = connection.prepareStatement(deleteProduct);
            statement.setInt(1, id);
            statement.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            LOGGER.log(Level.WARNING, "ProductDAO:delete " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

}
