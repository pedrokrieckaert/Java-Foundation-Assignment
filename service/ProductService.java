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

    public Product retrieveProduct(int index) {
        return this.productRepo.retrieveProduct(index);
    }
}
