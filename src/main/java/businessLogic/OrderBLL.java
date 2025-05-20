package businessLogic;

import dataAccess.ClientDAO;
import dataAccess.LogDAO;
import dataAccess.OrderDAO;
import dataAccess.ProductDAO;
import dataModel.Bill;
import dataModel.Client;
import dataModel.Orders;
import dataModel.Product;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Business Logic Layer class for handling order operations.
 * Validates stock and client existence before placing orders and logs the transaction.
 */
public class OrderBLL {
    private final OrderDAO orderDAO = new OrderDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final ClientDAO clientDAO = new ClientDAO();
    private final LogDAO logDAO = new LogDAO();

    public OrderBLL(){

    }

    /**
     * Places a new order for a client and product, validating stock and client data.
     *
     * @param client_id the ID of the client
     * @param product_id the ID of the product
     * @param quantity the quantity ordered
     * @return the created order
     * @throws NoSuchElementException if the product or client does not exist
     * @throws RuntimeException if insufficient stock is available
     */
    public Orders placeOrder(int client_id, int product_id, int quantity) {
        Product product = productDAO.findById(product_id);
        if(product == null) {
            throw new NoSuchElementException("Product not found");
        }
        if(product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        Client client = clientDAO.findById(client_id);
        if(client == null) {
            throw new NoSuchElementException("Client not found");
        }
        product.setStock(product.getStock() - quantity);
        productDAO.update(product);

        Orders order = new Orders(client_id, product_id, quantity, new Date());
        orderDAO.insert(order);

        Orders addedOrder = orderDAO.getLastInserted();
        double totalPrice = product.getPrice() * quantity;

        Bill bill = new Bill(addedOrder.getId(), client.getName(), product.getProduct_name(), quantity, addedOrder.getOrder_date(), totalPrice);

        logDAO.insert(bill);

        return addedOrder;
    }

    /**
     * Retrieves all placed orders.
     *
     * @return a list of all orders
     */
    public List<Orders> findAllOrders() {
        return orderDAO.findAll();
    }
}
