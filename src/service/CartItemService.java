package src.service;

import src.data.pojo.CartItem;
import src.data.repository.CartItemRepo;

import java.math.BigDecimal;
import java.util.List;

public class CartItemService {
    CartItemRepo itemRepo = new CartItemRepo();

    public CartItemService() {}

    public void addCartItem(CartItem item) {this.itemRepo.create(item);}

    public CartItem getCartItem(int id) {return this.itemRepo.retrieve(id);}

    public BigDecimal calcTotalPrice() {
        BigDecimal totalPrice = new BigDecimal(0);
        List<Integer> keys = itemRepo.getKeys();

        for (int i : keys) {
            totalPrice = totalPrice
                    .add(itemRepo
                            .retrieve(i)
                            .getPrice()
                            .multiply(BigDecimal
                                    .valueOf(itemRepo
                                            .retrieve(i)
                                            .getAmount())));
        }

        return totalPrice;
    }
}
