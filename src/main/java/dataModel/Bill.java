package dataModel;

import java.util.Date;

public final class Bill {

    private final int order_id;
    private final String client_name;
    private final String product_name;
    private final int quantity;
    private final Date order_date;
    private final double price;

    public Bill(int order_id, String client_name, String product_name, int quantity, Date order_date, double price) {
        this.order_id = order_id;
        this.client_name = client_name;
        this.product_name = product_name;
        this.quantity = quantity;
        this.order_date = order_date;
        this.price = price;
    }


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
