package src.data.pojo;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private int totalPrice;
    private int totalHours;
    private List<CartItem> cart;

    public Order(int totalPrice, int totalHours, List<CartItem> cart) {
        this.setTotalPrice(totalPrice);
        this.setTotalHours(totalHours);
        this.setCart(cart);
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(int totalHours) {
        this.totalHours = totalHours;
    }

    public List<CartItem> getCart() {
        return cart;
    }

    //Create deep copy
    public void setCart(List<CartItem> source) {
        List<CartItem> cart = new ArrayList<>();

        for (int i = 0; i < source.size(); i++) {
            cart.add(source.get(i).clone());
        }
        this.cart = cart;
    }
}
