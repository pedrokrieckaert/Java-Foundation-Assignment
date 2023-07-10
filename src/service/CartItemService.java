package src.service;

import src.data.pojo.CartItem;
import src.data.repository.CartItemRepo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartItemService {
    static CartItemRepo itemRepo = new CartItemRepo();

    public CartItemService() {}

    public void addCartItem(CartItem item) {
        try {
            if (itemRepo.retrieve(item.getId()) == null){
                itemRepo.create(item);
            } else {
                CartItem temp = itemRepo.retrieve(item.getId());

                temp.setAmount(temp.getAmount() + item.getAmount());

                itemRepo.update(item.getId(), temp);
            }
        } catch (NullPointerException e) {
            itemRepo.create(item);
        }
    }

    public CartItem getCartItem(int id) {
        return itemRepo.retrieve(id);
    }

    public List<CartItem> getCart() {
        List<CartItem> cart = new ArrayList<>();
        List<Integer> keys = itemRepo.getKeys();

        for (int i : keys) {
            cart.add(getCartItem(i));
        }

        return cart;
    }

    public void updateCart(List<CartItem> newCart) {
        for (CartItem item : newCart) {
            itemRepo.update(item.getId(), item.clone());
        }
    }

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
