package src.service;

import src.data.pojo.Product;
import src.data.repository.ProductRepo;

import java.util.List;

public class ProductService {
    static ProductRepo productRepo = new ProductRepo();

    public ProductService(){
        productRepo.load();
    }

    public void createProduct(Product product) {
        productRepo.create(product);
    }
    public <T> Product retrieveProduct(T input) {
        return productRepo.retrieve(input);
    }
    public List<Product> retrieveProductList() {
        return productRepo.retrieveAll();
    }
}
