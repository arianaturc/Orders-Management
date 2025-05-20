package dataModel;

/**
 * Class representing a product.
 */
public class Product {
    private int id;
    private String product_name;
    private double price;
    private int stock;

    public Product() {
    }

    public Product(int id, String product_name, double price, int stock) {
        this.id = id;
        this.product_name = product_name;
        this.price = price;
        this.stock = stock;
    }

    public Product(String product_name, double price, int stock) {
        this.product_name = product_name;
        this.price = price;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Product - id:" + id + ", product name:" + product_name + ", price:" + price + ", stock:" + stock;
    }
}
