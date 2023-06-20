package service;

import pojo.Product;
import repository.ProductRepo;

public class ProductService {
    ProductRepo productRepo;

    public ProductService(ProductRepo repo) {
        this.productRepo = repo;
    }

    public void createProduct(Product product) {
        this.productRepo.createProduct(product);
    }

    /**
     * Retrieve product object
     * @param index int - array index starting at 0
     * @return Product
     */
    public Product retrieveProductById(int index) {
        return this.productRepo.retrieveProductById(index);
    }

    public Product retrieveProductByName(String name) {
        return this.productRepo.retrieveProductByName(name);
    }
}
