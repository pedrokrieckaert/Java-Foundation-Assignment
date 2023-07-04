package src.presentation;

import src.data.pojo.CartItem;

import java.math.BigDecimal;
import java.util.List;

import static src.presentation.InvoiceSpacingEnum.*;

public abstract class Invoice {

    public static void printCart(List<CartItem> cart) {
        final int padSmall = 15;
        final int padLarge = 30;

        System.out.println(padRight("Photo Type",padLarge)
                + padRight("Price(€)",padSmall)
                + padRight("Amount",padSmall)
                + "Total Costs(€)"
        );

        for (CartItem item : cart) {
            BigDecimal totalItemCost = item.getPrice().multiply(BigDecimal.valueOf(item.getAmount()));

            System.out.println(
                    padRight(item.getName(),padLarge)
                            + padRight(padLeft(item.getPrice().toString(),8),padSmall)
                            + padRight(padLeft(String.valueOf(item.getAmount()), 6),padSmall)
                            + padRight(padLeft(totalItemCost.toString(),7),padSmall)
            );
        }

        System.out.println("\n" + padRight("Total Costs", 60) + padLeft("250",7));
    }

    private static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    private static  String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }
}
