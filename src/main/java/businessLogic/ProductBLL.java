package businessLogic;

import businessLogic.validators.Validator;
import dataAccess.ProductDAO;
import dataModel.Product;
import java.util.ArrayList;
import java.util.List;

/**
 * Business Logic Layer class for handling product operations.
 * Supports CRUD operations and basic validation.
 */
public class ProductBLL {
    private final List<Validator<Product>> validators = new ArrayList<>();
    private final ProductDAO productDAO = new ProductDAO();

    public ProductBLL() {

    }
    /**
     * Finds a product by its ID.
     *
     * @param id the product ID
     * @return the found product
     * @throws RuntimeException if no product is found
     */
    public Product findProductById(int id) {
        Product product = productDAO.findById(id);
        if(product != null) {
            return product;
        } else throw new RuntimeException("Product with the given id was not found");
    }

    /**
     * Retrieves all products from the database.
     *
     * @return a list of all products
     */
    public List<Product> findAllProducts() {
        return productDAO.findAll();
    }

    /**
     * Inserts a new product after validation.
     *
     * @param product the product to insert
     * @return the inserted product
     */
    public Product insertProduct(Product product) {
        validators.forEach(validator -> validator.validate(product));
        return productDAO.insert(product);
    }

    /**
     * Updates an existing product.
     *
     * @param product the product to update
     */
    public void updateProduct(Product product) {
        productDAO.update(product);
    }


    /**
     * Deletes a product by ID, after checking it exists.
     *
     * @param productId the ID of the product to delete
     * @throws IllegalArgumentException if the product does not exist
     */
    public void deleteProduct(int productId) {
        Product existingProduct = productDAO.findById(productId);
        if (existingProduct == null) {
            throw new IllegalArgumentException("Product with id " + productId + " does not exist.");
        }

        productDAO.delete(productId);
    }

}
