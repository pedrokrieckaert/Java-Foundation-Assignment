package src.pojo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private String orderDate;
    public static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("EEEE dd-MM-yyyy HH:mm");
    private String pickUpDate;
    private int totalPrice;
    private int totalHours;
    private String pickUpTime;
    private String pickUpDay;
    private List<CartItem> cart;

    public Order(int totalPrice, int totalHours, List<CartItem> cart) {
        this.setTotalPrice(totalPrice);
        this.setTotalHours(totalHours);
        this.setCart(cart);
        this.setOrderDate();
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
        if (this.cart == null) {
            return new ArrayList<>();
        } else {
            return cart;
        }
    }

    //Create deep copy
    public void setCart(List<CartItem> source) {
        List<CartItem> cart = new ArrayList<>();

        for (int i = 0; i < source.size(); i++) {
            cart.add(source.get(i).clone());
        }
        this.cart = cart;
    }

    public String getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(String pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public String getPickUpDay() {
        return pickUpDay;
    }

    public void setPickUpDay(String pickUpDay) {
        this.pickUpDay = pickUpDay;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate() {
        LocalDateTime now = LocalDateTime.now();
        this.orderDate = this.DTF.format(now);
    }

    public String getPickUpDate() {
        return pickUpDate;
    }

    public void setPickUpDate(String pickUpDate) {
        this.pickUpDate = pickUpDate;
    }

    public String pickUpDataToString() {
        return this.pickUpDay + " " + this.pickUpDate + " " + this.pickUpTime;
    }
}