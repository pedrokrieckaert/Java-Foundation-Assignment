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

    public Product retrieveProductById(int index) {
        return this.productRepo.retrieveProductById(index);
    }

    public Product retrieveProductByName(String name) {
        return this.productRepo.retrieveProductByName(name);
    }
}
