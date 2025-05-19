package businessLogic;

import businessLogic.validators.Validator;
import dataAccess.ProductDAO;
import dataModel.Product;
import java.util.ArrayList;
import java.util.List;

public class ProductBLL {
    private final List<Validator<Product>> validators = new ArrayList<>();
    private final ProductDAO productDAO = new ProductDAO();

    public Product findProductById(int id) {
        Product product = productDAO.findById(id);
        if(product != null) {
            return product;
        } else throw new RuntimeException("Product with the given id was not found");
    }

    public List<Product> findAllProducts() {
        return productDAO.findAll();
    }

    public Product insertProduct(Product product) {
        validators.forEach(validator -> validator.validate(product));
        return productDAO.insert(product);
    }

    public void updateProduct(Product product) {
        productDAO.update(product);
    }

    public void deleteProduct(int productId) {
        Product existingProduct = productDAO.findById(productId);
        if (existingProduct == null) {
            throw new IllegalArgumentException("Product with id " + productId + " does not exist.");
        }

        productDAO.delete(productId);
    }

}
