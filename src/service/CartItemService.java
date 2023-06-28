package src.service;

import src.data.pojo.CartItem;
import src.data.repository.CartItemRepo;

import java.math.BigDecimal;
import java.util.List;

public class CartItemService {
    static CartItemRepo itemRepo = new CartItemRepo();

    public CartItemService() {}

    public void addCartItem(CartItem item) {this.itemRepo.create(item);}

    public CartItem getCartItem(int id) {return this.itemRepo.retrieve(id);}

    public BigDecimal calcTotalPrice() {
        BigDecimal totalPrice = new BigDecimal(0);
        List<Integer> keys = itemRepo.getKeys();

        for (int i : keys) {
            CartItem item = itemRepo.retrieve(i);
            totalPrice = totalPrice
                    .add(item
                            .getPrice()
                            .multiply(BigDecimal
                                    .valueOf(item
                                            .getAmount())));
        }

        return totalPrice;
    }

    public int calcTotalHours() {
        int totalHours = 0;
        List<Integer> keys = itemRepo.getKeys();

        for (int i : keys) {
            CartItem item = itemRepo.retrieve(i);
            totalHours +=
                    Integer.parseInt(item
                            .getHours()
                            .substring(0,2))
                    * item
                    .getAmount();
        }

        return totalHours;
    }
}
