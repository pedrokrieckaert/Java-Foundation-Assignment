package src.presentation;

import src.data.pojo.Product;

import java.util.List;

import static src.presentation.StringPrintFormats.*;

public abstract class DataDisplay {

    static void displayProducts(List<Product> products) {
        System.out.println(
                padRight("ID: ", 6) +
                padRight("Photo Type:", padLarge) +
                padRight("Price(€):", padSmall) +
                padRight("Production Time(h):", padSmall)
        );

        for (Product product : products) {
            System.out.println(
                padRight("[" + product.getId() + "]", 6) +
                padRight(product.getName(), padLarge) +
                padRight(padLeft(product.getPrice().toString(), 8), padSmall) +
                padRight(padLeft(product.getHours(),18), padSmall)
            );
        }
    }

    static void displayHours() {

    }

    static void displayCart() {

    }
}
