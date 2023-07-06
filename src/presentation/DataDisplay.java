package src.presentation;

import src.data.pojo.Product;

import java.util.List;

public abstract class DataDisplay {

    static void displayProducts(List<Product> products) {
        for (Product product : products) {
            System.out.println(product.toString());
        }
    }

    static void displayHours() {

    }

    static void displayCart() {

    }
}
