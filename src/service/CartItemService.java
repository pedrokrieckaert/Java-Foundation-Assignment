package src.service;

import src.pojo.CartItem;
import src.data.repository.CartItemRepo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartItemService {
    static CartItemRepo itemRepo = new CartItemRepo();

    //Bean constructor
    public CartItemService() {}

    /**
     * Adds a CartItem object to the repository.
     * Checks to see if this CartItem already exists in the repository.
     * It will add this new CartItem amount field to the existing amount field in the repository.
     * Otherwise, it will create a new entry to the repository for this CartItem.
     * @param item CartItem to be added
     */
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

    /**
     * Creates a deep copy of the repository and stores it into a List
     * @return List of CartItem
     */
    public List<CartItem> getCart() {
        List<CartItem> cart = new ArrayList<>();
        List<Integer> keys = itemRepo.getKeys();

        for (int i : keys) {
            cart.add(getCartItem(i));
        }

        return cart;
    }

    /**
     * Updates the entire CartItem repository with a new List of CartItem.
     * Iterates through the keys of existing CartItem in the repository and CartItem of this new List.
     * If a key matches the CartItem ID field, it will update entry in the repository.
     * If a key cannot find a match in this new List, the entry will be removed from the repository.
     * @param newCart List of CartItem
     */
    public void updateCart(List<CartItem> newCart) {
        List<Integer> keys = itemRepo.getKeys();
        for (int key : keys) {
            boolean keyUpdated = false;
            for (CartItem item : newCart) {
                if (item.getId() == key) {
                    itemRepo.update(key, item);
                    keyUpdated = true;
                }
            }
            if (!keyUpdated) {
                itemRepo.delete(key);
            }
        }
    }

    /**
     * Iterates by keys through the repository and sums the price field value of CartItem.
     * @return BigDecimal sum of price
     */
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

    /**
     * Iterates by keys through the repository and sums the values of the hours field multiplied by the amount field of CartItem.
     * @return int sum of hours
     */
    public int calcTotalHours() {
        int totalHours = 0;
        List<Integer> keys = itemRepo.getKeys();

        for (int i : keys) {
            CartItem item = itemRepo.retrieve(i);
            totalHours += item.getHoursInt() * item.getAmount();
        }

        return totalHours;
    }
}
