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
import java.util.NoSuchElementException;

public class OrderBLL {
    private final OrderDAO orderDAO = new OrderDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final ClientDAO clientDAO = new ClientDAO();
    private final LogDAO logDAO = new LogDAO();

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
}
