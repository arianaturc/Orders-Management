package dataModel;

import java.util.Date;

/**
 * Immutable record representing a Bill.
 */
public record Bill(int order_id, String client_name, String product_name, int quantity, Date order_date, double price) {

    public int getOrder_id() {
        return order_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public String getProduct_name() {
        return product_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public Date getOrder_date() {
        return order_date;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Bill - id:" + order_id + ", name:" + client_name + ", product name:" + product_name
                + ", quantity:" + quantity + ", order date:" + order_date + ", price:" + price;
    }
}