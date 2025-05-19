package dataModel;

import java.util.Date;

public class Orders {
    private int id;
    private int client_id;
    private int product_id;
    private int quantity;
    private Date order_date;

    public Orders() {
    }

    public Orders(int id, int client_id, int product_id, int quantity, Date order_date) {
        this.id = id;
        this.client_id = client_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.order_date = order_date;
    }

    public Orders(int client_id, int product_id, int quantity, Date order_date) {
        this.client_id = client_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.order_date = order_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClient_id() {
        return client_id;
    }



    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getOrder_date() {
        return order_date;
    }

    public void setOrder_date(Date order_date) {
        this.order_date = order_date;
    }

    @Override
    public String toString() {
        return "Order - id:" + id + ", client id:" + client_id + ", product id:" + product_id + ", quantity:" + quantity
                + ", order date:" + order_date;
    }
}
