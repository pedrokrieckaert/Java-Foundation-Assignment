package repository;

import java.util.ArrayList;

import pojo.Product;

public class ProductRepo {
    //Store products in array list, the product id is intrsinic to the index
    private ArrayList<Product> datastore = new ArrayList<>();

    public ProductRepo(Product product) {
        this.datastore.add(new Product(product));
    }

    public Product retrieveProduct(int index) {
        return new Product(this.datastore.get(index));
    }
}
