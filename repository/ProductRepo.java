package repository;

import java.util.ArrayList;

import pojo.Product;

public class ProductRepo {
    //Store products in array list, the product id is intrsinic to the index
    private ArrayList<Product> datastore = new ArrayList<>();

    public void createProduct(Product product) {
        this.datastore.add(product.clone());
    }

    public Product retrieveProductById(int index) {
        return this.datastore.get(index).clone();
    }

    public Product retrieveProductByName(String name) {
        for (Product product : this.datastore) {
            if (product.getName().equalsIgnoreCase(name)) {
                return product.clone();
            }
        }
        return null;
    }
}
