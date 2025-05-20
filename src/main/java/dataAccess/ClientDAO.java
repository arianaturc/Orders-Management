package dataAccess;

import connection.ConnectionFactory;
import dataModel.Client;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 * DAO class for the Client entity. Provides additional delete operation
 * that handles cascade deletions from related tables.
 */
public class ClientDAO extends AbstractDAO<Client> {

    /**
     * Deletes a client and all associated orders and bills.
     * @param id The ID of the client to delete.
     */
    public void delete(int id) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            connection.setAutoCommit(false);

            String deleteBills = "DELETE FROM log WHERE order_id IN (SELECT id FROM orders WHERE client_id = ?)";
            try (PreparedStatement billStmt = connection.prepareStatement(deleteBills)) {
                billStmt.setInt(1, id);
                billStmt.executeUpdate();
            }

            String deleteOrders = "DELETE FROM orders WHERE client_id = ?";
            try (PreparedStatement orderStmt = connection.prepareStatement(deleteOrders)) {
                orderStmt.setInt(1, id);
                orderStmt.executeUpdate();
            }

            String deleteClient = "DELETE FROM client WHERE id = ?";
            try (PreparedStatement clientStmt = connection.prepareStatement(deleteClient)) {
                clientStmt.setInt(1, id);
                clientStmt.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ClientDAO:delete " + e.getMessage());
        }
    }
}

