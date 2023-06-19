package repository;

import java.util.ArrayList;

import pojo.Product;

public class ProductRepo {
    //Store products in array list, the product id is intrsinic to the index
    private ArrayList<Product> datastore = new ArrayList<>();

    public void createProduct(Product product) {
        this.datastore.add(product.clone());
    }

    public Product retrieveProduct(int index) {
        return this.datastore.get(index).clone();
    }
}
