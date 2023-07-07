package src.presentation;

import src.data.pojo.CartItem;
import src.data.pojo.Order;

import java.math.BigDecimal;
import java.util.List;

import static src.presentation.StringPrintFormats.*;

public abstract class Invoice {
    static void printUserData(){
        System.out.println( "\n" +
                padRight("Invoice of Photoshop order:", PAD_LARGE) + "35510" + "\n\n" +
                padRight("Customer:", PAD_LARGE) + "Shop assistant:" + "\n" +
                padRight("Theo Jansen", PAD_LARGE) + "Japie Bonestakie" + "\n" + //Users' name + Shop assistant name
                padRight("Alblasserstraat 23", PAD_LARGE) + "145" + "\n" + //Address + Shop assistant ID
                "3454 KR\n" + //Postcode
                "Alkmaar\n" + //City
                "t.jansen@gmail.nl\n" + //Email
                "06-45867474\n" //Phone number
        );

    }
    static void printPickUp(Order order) {
        System.out.println(
                "Order Specifications:" + "\n" +
                padRight("Order Number", PAD_XL) + "35510" + "\n" +
                padRight("Production time in working hours", PAD_XL) + order.getTotalHours() + ":00" + "\n" +
                padRight("Order Date", PAD_XL) + order.getOrderDate() + "\n" +
                padRight("You can pick up your order after", PAD_XL) +
                        order.getPickUpDay() + " " +
                        order.getPickUpDate() + " " +
                        order.getPickUpTime() + "\n"
        );
    }
    static void printCart(List<CartItem> cart, String totalPrice) {
        System.out.println(padRight("Photo Type:", PAD_LARGE)
                + padRight("Price(€):", PAD_SMALL)
                + padRight("Amount:", PAD_SMALL)
                + "Total Costs(€):"
        );

        for (CartItem item : cart) {
            BigDecimal totalItemCost = item.getPrice().multiply(BigDecimal.valueOf(item.getAmount()));

            System.out.println(
                    padRight(item.getName(), PAD_LARGE)
                            + padRight(padLeft(item.getPrice().toString(),8), PAD_SMALL)
                            + padRight(padLeft(String.valueOf(item.getAmount()), 6), PAD_SMALL)
                            + padRight(padLeft(totalItemCost.toString(),7), PAD_SMALL)
            );
        }

        System.out.println("\n" + padRight("Total Costs:", 60) + padLeft(totalPrice,7));
    }
}
