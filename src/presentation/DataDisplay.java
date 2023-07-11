package src.presentation;

import src.data.pojo.CartItem;
import src.data.pojo.OpeningHours;
import src.data.pojo.Product;

import java.math.BigDecimal;
import java.util.List;

import static src.presentation.StringPrintFormats.*;

public abstract class DataDisplay {

    static void displayProducts(List<Product> products) {
        System.out.println(
                padRight("ID: ", 6) +
                padRight("Photo Type:", PAD_LARGE) +
                padRight("Price(€):", PAD_SMALL) +
                padRight("Production Time(h):", PAD_SMALL)
        );

        for (Product product : products) {
            System.out.println(
                padRight("[" + product.getId() + "]", 6) +
                padRight(product.getName(), PAD_LARGE) +
                padRight(padLeft(product.getPrice().toString(), 8), PAD_SMALL) +
                padRight(padLeft(product.getHours(),18), PAD_SMALL)
            );
        }
    }

    static void displayHours(List<OpeningHours> hours) {
        System.out.println(
                padRight("Day:", PAD_SMALL) +
                padRight("Opening time:", PAD_SMALL) +
                padRight("Closing time:", PAD_SMALL)
        );

        for (OpeningHours hour : hours) {
            System.out.println(
                    padRight(hour.getDay(), PAD_SMALL) +
                    padRight(padLeft(hour.getOpenHour(), 12), PAD_SMALL) +
                    padRight(padLeft(hour.getCloseHour(),12), PAD_SMALL)
            );
        }
    }

    static void displayCart(List<CartItem> cart, String totalPrice) {
        System.out.println(
                padRight("ID: ", 6)
                        + padRight("Photo Type:", PAD_LARGE)
                        + padRight("Price(€):", PAD_SMALL)
                        + padRight("Amount:", PAD_SMALL)
                        + "Total Costs(€):"
        );

        for (int i = 0; i < cart.size(); i++) {

            CartItem item = cart.get(i);

            BigDecimal totalItemCost = item.getPrice().multiply(BigDecimal.valueOf(item.getAmount()));

            System.out.println(
                    padRight("[" + (i + 1) + "]", 6)
                            + padRight(item.getName(), PAD_LARGE)
                            + padRight(padLeft(item.getPrice().toString(),8), PAD_SMALL)
                            + padRight(padLeft(String.valueOf(item.getAmount()), 6), PAD_SMALL)
                            + padRight(padLeft(totalItemCost.toString(),7), PAD_SMALL)
            );
        }

        System.out.println("\n" + padRight("Total Costs:", 66) + padLeft(totalPrice,7));
    }
}
