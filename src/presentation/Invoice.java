package src.presentation;

import src.data.pojo.CartItem;

import java.math.BigDecimal;
import java.util.List;

import static src.presentation.InvoiceSpacingEnum.*;

public abstract class Invoice {

    public static void printCart(List<CartItem> cart) {
        System.out.println("Photo Type" + FOUR.spaces
                + "Price" + FOUR.spaces
                + "Amount" + FOUR.spaces
                + "Total Costs"
        );

        for (CartItem item : cart) {
            System.out.println(
                    item.getName() + FOUR.spaces
                            + item.getPrice() + FOUR.spaces
                            + item.getAmount() + FOUR.spaces
                            + (item.getPrice().multiply(BigDecimal.valueOf(item.getAmount())))
            );
        }
    }
}
