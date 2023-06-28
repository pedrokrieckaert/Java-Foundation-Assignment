package src.service;

import src.data.pojo.Product;
import src.data.repository.ProductRepo;

public class ProductService {
    ProductRepo productRepo = new ProductRepo();

    public ProductService(){
        productRepo.load();
    }

    public void createProduct(Product product) {
        this.productRepo.create(product);
    }

    /**
     * Retrieve product object
     * @param index int - array index starting at 0
     * @return Product
     */
    public Product retrieveProductById(int index) {
        return this.productRepo.retrieveById(index);
    }

    public Product retrieveProductByName(String name) {
        return this.productRepo.retrieveByName(name);
    }
}
