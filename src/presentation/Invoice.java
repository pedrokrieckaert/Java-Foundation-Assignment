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
                + padRight("Total Costs(€)",padSmall)
        );

        for (CartItem item : cart) {
            BigDecimal totalCost = item.getPrice().multiply(BigDecimal.valueOf(item.getAmount()));

            System.out.println(
                    padRight(item.getName(),padLarge)
                            + padRight(item.getPrice().toString(),padSmall)
                            + padRight(String.valueOf(item.getAmount()),padSmall)
                            + padRight(totalCost.toString(),padSmall)
            );
        }
    }

    private static String padRight(String s, int n) {
        String test = String.format("%-" + n + "s", s);
        return test;
    }
}
